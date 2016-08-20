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
import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.util.ItemImageTransformation;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoVo;

public class ItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LogUtil log = new LogUtil(ItemsListAdapter.class);
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 0;
    private static final int INVALID_DIMEN = -1;

    private final ItemListener mListener;
    private final int mImageWidth;
    private List<PhotoVo> mItems;
    private Context mContext;
    private DatabaseUtil mDbUtil;
    private List<String> mImageIdsList;  //list of images in DB
    String mSavedText;


    public interface ItemListener
    {
        void itemClicked(ItemViewHolder rowView, int position);
    }

    public ItemsListAdapter(List<PhotoVo> items, ItemListener listener, int imageWidth, Context context)
    {
        //mItems = items;
        mItems = new ArrayList<>();
        mItems.addAll(items);
        mListener = listener;
        mImageWidth = imageWidth;
        mContext = context;
        mDbUtil = new DatabaseUtil(mContext);
        mSavedText = mContext.getResources().getString(R.string.unsave);
        mImageIdsList = getPhotosFromDB();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_photos_item, viewGroup, false);
        return new ItemViewHolder(view);
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
        PhotoVo photo = mItems.get(position);
        itemholder.itemName.setText(photo.tags);
        itemholder.itemImage.setTag(photo.id);
        if(mImageIdsList.contains(photo.id))
        {
            itemholder.saveText.setText(mSavedText);
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
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isImageSizeGiven() {
        return mImageWidth != INVALID_DIMEN;
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemName;
        public TextView saveText;

        public ItemViewHolder(View itemView)
        {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            saveText = (TextView) itemView.findViewById(R.id.saveText);
        }
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

    private List<String> getPhotosFromDB()
    {
        return mDbUtil.getImagesId();
    }
}
