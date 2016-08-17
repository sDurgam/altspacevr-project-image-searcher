package testsample.altvr.com.testsample.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

import com.squareup.picasso.Picasso;

/**
 * Created by root on 8/16/16.
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener
{
    Context mContext;

    public RecyclerViewScrollListener(Context context)
    {
        super();
        mContext = context;
    }


    public RecyclerViewScrollListener() {
        super();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState)
    {
        super.onScrollStateChanged(recyclerView, newState);
        Picasso picasso = Picasso.with(mContext);
        if(newState == RecyclerView.SCROLL_STATE_IDLE || newState ==  RecyclerView.SCROLL_STATE_SETTLING)
        {
            picasso.resumeTag(mContext);
        }
        else
        {
            picasso.pauseTag(mContext);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);
    }

}
