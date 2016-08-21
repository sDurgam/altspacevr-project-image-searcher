package testsample.altvr.com.testsample.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.fragments.PhotosFragment;
import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.util.ItemImageTransformation;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoVo;

public class ItemsListAdapter extends ItemsBaseAdapter
{
    private List<PhotoVo> mItems;
    private List<String> mImageIdsList;  //list of images in DB

    public ItemsListAdapter(List<PhotoVo> items, ItemListener listener, int imageWidth, Context context)
    {
        super(listener, imageWidth, context);
        log = new LogUtil(ItemsBaseAdapter.class);
        mItems = new ArrayList<>();
        mItems.addAll(items);
        mImageIdsList = getPhotosFromDB();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
      	/*
         * YOUR CODE HERE
         *
         * For Part 1a, you should get the proper PhotoVo instance from the mItems collection,
         * image, text, etc, into the ViewHolder (which will be an ItemViewHolder.)
         *
         * For part 1b, you should attach a click listener to the save label so users can save
         * or delete photos from their local db.
         */
        final ItemViewHolder itemholder = (ItemViewHolder) holder;
        PhotoVo photo = mItems.get(position);
        itemholder.itemName.setText(photo.tags);
        itemholder.itemImage.setTag(photo.id);
        if(mImageIdsList.contains(photo.id))
        {
            itemholder.saveText.setText(mSavedText);
        }
        else
        {
            itemholder.saveText.setText(mSaveText);
        }
        itemholder.saveText.setTag(itemholder);
        Picasso.with(mContext).load(photo.webformatURL)
                .placeholder(android.R.drawable.progress_horizontal)
                .transform(new ItemImageTransformation(mImageWidth))
                .into(itemholder.itemImage, new Callback()
                {
                    @Override
                    public void onSuccess() {
                        LogUtil.log("Success loading the image");
                        itemholder.saveText.setVisibility(View.VISIBLE); //show save option after the image is loaded
                    }

                    @Override
                    public void onError()
                    {
                        LogUtil.log("Error loading the image");
                    }
                });
            itemholder.saveText.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(view.getTag() instanceof  ItemViewHolder)
                    {
                        mListener.itemClicked((ItemViewHolder) view.getTag(), position);    //set a click listener for save buton
                    }
                }
            });
    }


    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    //Method to update recycler view data items
    public void swap(List<PhotoVo> photoVoList)
    {
        if(mItems != null)
        {
            mItems.clear();
        }
        mItems.addAll(photoVoList);
        notifyDataSetChanged();
    }

    public void updateDBPhotosList()
    {
        mImageIdsList = getPhotosFromDB();
        notifyDataSetChanged();
    }

    private List<String> getPhotosFromDB()
    {
        return mDbUtil.getImagesId();
    }

}
