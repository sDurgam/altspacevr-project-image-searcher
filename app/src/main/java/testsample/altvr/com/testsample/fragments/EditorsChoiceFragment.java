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
public class EditorsChoiceFragment extends PhotosFragment
{
    public static EditorsChoiceFragment newInstance()
    {
        return new EditorsChoiceFragment();
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
        getEditorsChoicePhotos(); //api call
    }


    protected void setupItemsList()
    {
        super.setupItemsList();
        itemsListRecyclerView.setOnScrollListener(new RecyclerViewScrollListener(getContext(), mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                LogUtil.log("current page:" + page + ",total items count:" + totalItemsCount);
                if (searchQuery == null) {
                    getMoreEditorChoicePhotos(page);
                } else {
                    getMoreEditorChoicesearchPhotos(page);
                }
            }
        });
    }

    //get photos from the first page
    private void getEditorsChoicePhotos()
    {
        //get total count and store it in db
        mService.getEditorsChoicePhotos(1);
    }

    private void getMoreEditorChoicePhotos(int pagenumber)
    {
        mService.getEditorsChoicePhotos(pagenumber + 1);
    }

    private void searchEditosChoicePhotos(String query, int pagenumber)
    {
        fetchingItems.setVisibility(View.VISIBLE);
        mService.searchEditorsChoicePhotos(query, pagenumber);
    }

    private void getMoreEditorChoicesearchPhotos(int pagenumber)
    {
        mService.searchEditorsChoicePhotos(searchQuery, pagenumber + 1);
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
        if(event != null && event.type.equals(getContext().getResources().getString(R.string.editorschoice)))
        {
            fetchingItems.setVisibility(View.GONE);
            updateAdapterDataSet((ArrayList<PhotoVo>) event.data);
        }
    }

    //Event to switch between popular mode and search mode
    @Subscribe
    public void onEvent(SearchPhotosEvent event)
    {
        if(event != null && event.getType().equals(getContext().getResources().getString(R.string.editorschoice)))
        {
            String query = ((SearchPhotosEvent) event).getQuery();
            if (query == null)
            {
                searchQuery = null;
                clearAdapterDataSet();
                getEditorsChoicePhotos();
            }
            else if (searchQuery == null || (!searchQuery.equals(query)))
            {
                searchQuery = query;
                clearAdapterDataSet();
                searchEditosChoicePhotos(query, 1);
            }
        }
    }
}
