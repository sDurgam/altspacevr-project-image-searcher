package testsample.altvr.com.testsample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.util.ItemImageTransformation;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoVo;

/**
 * Created by root on 8/19/16.
 */
public abstract class ItemsBaseAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    protected LogUtil log;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 0;
    protected static final int INVALID_DIMEN = -1;

    protected final ItemListener mListener;
    protected final int mImageWidth;
    protected Context mContext;
    protected DatabaseUtil mDbUtil;
    String mSavedText;
    String mSaveText;


    public interface ItemListener
    {
        void itemClicked(ItemViewHolder rowView, int position, WeakReference<Context> ctx);
    }

    public ItemsBaseAdapter(ItemListener listener, int imageWidth, WeakReference<Context> context)
    {
        mListener = listener;
        mImageWidth = imageWidth;
        mContext = context.get();
        mDbUtil = new DatabaseUtil(mContext);
        mSavedText = mContext.getResources().getString(R.string.unsave);
        mSaveText = mContext.getResources().getString(R.string.save);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_photos_item, viewGroup, false);
        return new ItemViewHolder(view);
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {
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
}
