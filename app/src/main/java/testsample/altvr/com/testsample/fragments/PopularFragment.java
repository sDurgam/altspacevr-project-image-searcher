package testsample.altvr.com.testsample.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.events.PhotosEvent;
import testsample.altvr.com.testsample.events.SearchPhotosEvent;
import testsample.altvr.com.testsample.listeners.RecyclerViewScrollListener;
import testsample.altvr.com.testsample.service.ApiService;
import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoVo;

/**
 * Created by root on 8/19/16.
 */
public class PopularFragment extends PhotosFragment
{
    public static PopularFragment newInstance()
    {
        return new PopularFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setupItemsList();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPopularPhotos(); //api call
    }


    protected void setupItemsList()
    {
        super.setupItemsList();
        itemsListRecyclerView.setOnScrollListener(new RecyclerViewScrollListener(getContext(), mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                LogUtil.log("current page:" + page + ",total items count:" + totalItemsCount);
                if (searchQuery == null) {
                    getMorePopularPhotos(page);
                } else {
                    getMoresearchPhotos(page);
                }
            }
        });
    }

    //get photos from the first page
    private void getPopularPhotos()
    {
        //get total count and store it in db
        mService.getPopularPhotos(1);
    }

    private void getMorePopularPhotos(int pagenumber)
    {
        mService.getPopularPhotos(pagenumber + 1);
    }

    private void searchPhotos(String query, int pagenumber)
    {
        fetchingItems.setVisibility(View.VISIBLE);
        mService.searchPhotos(query, pagenumber);
    }

    private void getMoresearchPhotos(int pagenumber)
    {
        mService.searchPhotos(searchQuery, pagenumber + 1);
    }

    @Subscribe
    public void onEvent(PhotosEvent event)
    {
        /**
         * YOUR CODE HERE
         *
         * This will be the event posted via the EventBus when a photo has been fetched for display.
         *
         * For part 1a you should update the data for this fragment (or notify the user no results
         * were found) and redraw the list.
         *
         * For part 2b you should update this to handle the case where the user has saved photos.
         */
        if(event != null && event.type.equals(getContext().getResources().getString(R.string.popular)))
        {
            fetchingItems.setVisibility(View.GONE);
            updateAdapterDataSet((ArrayList<PhotoVo>) event.data);
        }
    }
    //Event to switch between popular mode and search mode
    @Subscribe
    public void onEvent(SearchPhotosEvent event)
    {
        if(event != null && event.getType().equals(getContext().getResources().getString(R.string.popular)))
        {
            String query = ((SearchPhotosEvent) event).getQuery();
            if (query == null) {
                searchQuery = null;
                clearAdapterDataSet();
                getPopularPhotos();
            } else if (searchQuery == null || (!searchQuery.equals(query))) {
                searchQuery = query;
                clearAdapterDataSet();
                searchPhotos(query, 1);
            }
        }
    }
}
