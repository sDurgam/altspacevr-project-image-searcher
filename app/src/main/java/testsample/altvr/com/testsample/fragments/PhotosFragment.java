package testsample.altvr.com.testsample.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.adapter.ItemsListAdapter;
import testsample.altvr.com.testsample.events.ApiErrorEvent;
import testsample.altvr.com.testsample.events.PhotosEvent;
import testsample.altvr.com.testsample.events.SearchEvent;
import testsample.altvr.com.testsample.listeners.RecyclerViewScrollListener;
import testsample.altvr.com.testsample.service.ApiService;
import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoVo;

public class PhotosFragment extends Fragment{
    private LogUtil log = new LogUtil(PhotosFragment.class);
    private LinearLayout fetchingItems;
    private RecyclerView itemsListRecyclerView;
    private ApiService mService;

    private ArrayList<PhotoVo> mItemsData = new ArrayList<>();
    private ItemsListAdapter mListAdapter;
    private DatabaseUtil mDatabaseUtil;
    private String searchQuery = null;   //to distinguish between search photos and getdefault photos

    public static PhotosFragment newInstance()
    {
        return new PhotosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mService = new ApiService(getActivity());
        mDatabaseUtil = new DatabaseUtil(getActivity());
        setupViews();
    }

    private void initViews(View view)
    {
        fetchingItems = (LinearLayout) view.findViewById(R.id.listEmptyView);
        itemsListRecyclerView = (RecyclerView) view.findViewById(R.id.photosListRecyclerView);
    }

    private void setupViews() {
        fetchingItems.setVisibility(View.VISIBLE);
        setupItemsList();
        EventBus.getDefault().register(this);
    }

    private void setupItemsList()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        itemsListRecyclerView.setLayoutManager(linearLayoutManager);
        itemsListRecyclerView.setHasFixedSize(true);
        itemsListRecyclerView.setOnScrollListener(new RecyclerViewScrollListener(getContext(), linearLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                LogUtil.log( "current page:" + page + ",total items count:" + totalItemsCount);
                if(searchQuery == null)
                {
                    getMoreDefaultPhotos(page);
                }
                else
                {
                    getMoresearchPhotos(page);
                }
            }
        });

        mListAdapter = new ItemsListAdapter(mItemsData, new ItemClickedListener(), getResources().getDisplayMetrics().widthPixels, getContext());
        itemsListRecyclerView.setAdapter(mListAdapter);
    }

    private class ItemClickedListener implements ItemsListAdapter.ItemListener
    {

        @Override
        public void itemClicked(ItemsListAdapter.ItemViewHolder rowView, int position)
        {

        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        getDefaultPhotos();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mItemsData.clear();
        mListAdapter = null;
    }

    //Event to switch between default mode and search mode
    @Subscribe
    public void onEvent(SearchEvent event)
    {
        String query = ((SearchEvent)event).query;
        if(query == null)
        {
            searchQuery = null;
            getDefaultPhotos();
        }
        else if(searchQuery == null || (!searchQuery.equals(query)))
        {
            searchQuery = query;
            searchPhotos(query, 1);
        }
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
        fetchingItems.setVisibility(View.GONE);
        LogUtil.log("photos fetch success event");
        updateAdapterDataSet((ArrayList<PhotoVo>)event.data);
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event)
    {
        /**
         * YOUR CODE HERE
         *
         * This will be the event posted via the EventBus when an API error has occured.
         *
         * For part 1a you should clear the fragment and notify the user of the error.
         */
    }

    //get photos from the first page
    private void getDefaultPhotos()
    {
        clearAdapterDataSet(); //for the first time
        //get total count and store it in db
        mService.getDefaultPhotos(1);
    }

    private void getMoreDefaultPhotos(int pagenumber)
    {
        mService.getDefaultPhotos(pagenumber + 1);
    }

    public void searchPhotos(String query, int pagenumber)
    {
        clearAdapterDataSet(); //clear the data
        fetchingItems.setVisibility(View.VISIBLE);
        mService.searchPhotos(query, pagenumber);
    }

    private void getMoresearchPhotos(int pagenumber)
    {
        mService.searchPhotos(searchQuery, pagenumber + 1);
    }

    public void clearAdapterDataSet()
    {
        mItemsData.clear();
        mListAdapter.swap(mItemsData);
    }

    public void updateAdapterDataSet(ArrayList<PhotoVo> photosList)
    {
        if(photosList != null)
        {
            mItemsData.addAll(photosList);
            mListAdapter.swap (mItemsData);
        }
    }
}

