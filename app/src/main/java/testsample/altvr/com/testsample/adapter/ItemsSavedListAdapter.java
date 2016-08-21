package testsample.altvr.com.testsample.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.util.DatabaseBitmapUtil;
import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.util.ItemImageTransformation;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoDBVo;
import testsample.altvr.com.testsample.vo.PhotoVo;

/**
 * Created by root on 8/19/16.
 */
public class ItemsSavedListAdapter extends ItemsBaseAdapter
{
    private List<PhotoDBVo> mItems;
    private Context mContext;
    private DatabaseUtil mDbUtil;

    public ItemsSavedListAdapter(List<PhotoDBVo> items, ItemListener listener, int imageWidth, Context context)
    {
        //mItems = items;
        super(listener, imageWidth, context);
        log = new LogUtil(ItemsSavedListAdapter.class);
        mItems = new ArrayList<>();
        mItems.addAll(items);
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
        ItemViewHolder itemholder = (ItemViewHolder) holder;
        PhotoDBVo photo = mItems.get(position);
        itemholder.itemName.setText(photo.getTags());
        itemholder.itemImage.setTag(photo.getId());
        itemholder.saveText.setText(mSavedText);
        itemholder.saveText.setTag(itemholder);
        Bitmap bitmap = DatabaseBitmapUtil.getImage(photo.getPhoto());
        itemholder.itemImage.setImageBitmap(bitmap);
        itemholder.saveText.setVisibility(View.VISIBLE);    //show save option after the image is loaded
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
    public void swap(List<PhotoDBVo> photoDBVoList)
    {
        if(mItems != null)
        {
            mItems.clear();
        }
        mItems.addAll(photoDBVoList);
        notifyDataSetChanged();
    }
}
