package testsample.altvr.com.testsample.listeners;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

import com.squareup.picasso.Picasso;

/**
 * Created by root on 8/16/16.
 */
public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener
{
    Context mContext;

    private int mvisibleTreshold = 5;
    private int mcurrentPage = 0;
    private int mpreviousItemTotalCount = 0;
    private boolean m_isloading = true;
    private int mstartingPageIndex = 0;
    private RecyclerView.LayoutManager mLayoutManager;

    public RecyclerViewScrollListener()
    {
        super();
    }

    public RecyclerViewScrollListener(Context context, LinearLayoutManager layoutManager)
    {
        super();
        mContext = context;
        mLayoutManager = layoutManager;
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
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        if(mLayoutManager instanceof  LinearLayoutManager)
        {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }
        if(totalItemCount < mpreviousItemTotalCount)
        {
            mcurrentPage = mstartingPageIndex;
            mpreviousItemTotalCount = totalItemCount;
            if(totalItemCount == 0)
            {
                m_isloading = true;
            }
        }
        if(m_isloading && totalItemCount > mpreviousItemTotalCount)
        {
            m_isloading = false;
            mpreviousItemTotalCount = totalItemCount;
        }
        if(!m_isloading && (lastVisibleItemPosition + mvisibleTreshold) > totalItemCount)
        {
            mcurrentPage ++;
            onLoadMore(mcurrentPage, totalItemCount);
            m_isloading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount);
}
