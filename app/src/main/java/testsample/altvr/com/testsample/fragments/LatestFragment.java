package testsample.altvr.com.testsample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.events.PhotosEvent;
import testsample.altvr.com.testsample.events.SearchPhotosEvent;
import testsample.altvr.com.testsample.listeners.RecyclerViewScrollListener;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoVo;

/**
 * Created by root on 8/19/16.
 */
public class LatestFragment extends PhotosFragment
{
    public static LatestFragment newInstance()
    {
        return new LatestFragment();
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
        getLatestPhotos(); //api call
    }


    protected void setupItemsList()
    {
        super.setupItemsList();
        itemsListRecyclerView.setOnScrollListener(new RecyclerViewScrollListener(getContext(), mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                LogUtil.log("current page:" + page + ",total items count:" + totalItemsCount);
                if (searchQuery == null) {
                    getMoreLatestPhotos(page);
                } else {
                    getMoreLatestSearchPhotos(page);
                }
            }
        });
    }

    //get photos from the first page
    private void getLatestPhotos()
    {
        //get total count and store it in db
        mService.getLatestPhotos(1);
    }

    private void getMoreLatestPhotos(int pagenumber)
    {
        mService.getLatestPhotos(pagenumber + 1);
    }

    private void searchLatestPhotos(String query, int pagenumber)
    {
        fetchingItems.setVisibility(View.VISIBLE);
        mService.searchLatestPhotos(query, pagenumber);
    }

    private void getMoreLatestSearchPhotos(int pagenumber)
    {
        mService.searchLatestPhotos(searchQuery, pagenumber + 1);
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
        if(event != null && event.type.equals(getContext().getResources().getString(R.string.latest)))
        {
            fetchingItems.setVisibility(View.GONE);
            updateAdapterDataSet((ArrayList<PhotoVo>) event.data);
        }
    }

    //Event to switch between latest mode and search mode
    @Subscribe
    public void onEvent(SearchPhotosEvent event)
    {
        if(event != null && event.getType().equals(getContext().getResources().getString(R.string.latest))) {
            String query = ((SearchPhotosEvent) event).getQuery();
            if (query == null) {
                searchQuery = null;
                clearAdapterDataSet();
                getLatestPhotos();
            } else if (searchQuery == null || (!searchQuery.equals(query))) {
                searchQuery = query;
                clearAdapterDataSet();
                searchLatestPhotos(query, 1);
            }
        }
    }
}
