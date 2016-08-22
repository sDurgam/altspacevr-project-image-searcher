package testsample.altvr.com.testsample.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.adapter.ItemsBaseAdapter;
import testsample.altvr.com.testsample.adapter.ItemsListAdapter;
import testsample.altvr.com.testsample.adapter.ItemsSavedListAdapter;
import testsample.altvr.com.testsample.events.ApiErrorEvent;
import testsample.altvr.com.testsample.events.SearchPhotosEvent;
import testsample.altvr.com.testsample.events.UpdateSavedEvent;
import testsample.altvr.com.testsample.listeners.RecyclerViewScrollListener;
import testsample.altvr.com.testsample.util.DatabaseBitmapUtil;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoDBVo;
import testsample.altvr.com.testsample.vo.PhotoVo;

/**
 * Created by root on 8/19/16.
 */
public class SavedFragment extends PhotosFragment
{
    private ArrayList<PhotoDBVo> mItemsDBData = new ArrayList<>();
    private ItemsSavedListAdapter mDBListAdapter;

    public static SavedFragment newInstance()
    {
        return new SavedFragment();
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
        getSavedPhotos(); //api call
    }

    protected void Refresh()
    {
        getSavedPhotos();
    }

    protected void setupItemsList()
    {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        itemsListRecyclerView.setHasFixedSize(true);
        itemsListRecyclerView.setLayoutManager(mLinearLayoutManager);
        mDBListAdapter = new ItemsSavedListAdapter(mItemsDBData, new ItemClickedListener(), getResources().getDisplayMetrics().widthPixels, new WeakReference<Context>(getContext()));
        itemsListRecyclerView.setAdapter(mDBListAdapter);
        itemsListRecyclerView.setOnScrollListener(new RecyclerViewScrollListener(getContext(), mLinearLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                LogUtil.log("current page:" + page + ",total items count:" + totalItemsCount);
            }
        });
    }

    private class ItemClickedListener implements ItemsBaseAdapter.ItemListener
    {

        @Override
        public void itemClicked(ItemsBaseAdapter.ItemViewHolder rowView, int position, WeakReference<Context> ctx)
        {
            String saveStatus = rowView.saveText.getText().toString();
            String unsavestr = ctx.get().getResources().getString(R.string.unsave);
            String savestr = ctx.get().getResources().getString(R.string.save);
            String id = rowView.itemImage.getTag() != null ? rowView.itemImage.getTag().toString() : null;
            String tag = rowView.itemName.getText().toString();
            PhotoDBVo photoObj;
            if(saveStatus.equals(savestr))  //request to save in db
            {
                rowView.itemImage.buildDrawingCache();
                Bitmap bitmap = (rowView.itemImage.getDrawingCache());
                if(bitmap != null)
                {
                    byte[] imageArray = DatabaseBitmapUtil.getBytes(bitmap);
                    photoObj = new PhotoDBVo(id, imageArray, tag);
                    mDatabaseUtil.insertImage(photoObj);
                    rowView.saveText.setText(unsavestr);    //save if db returns a success message
                    EventBus.getDefault().post(new UpdateSavedEvent());
                }
                else
                {
                    displaySnackBar(ctx.get().getResources().getString(R.string.saveimage_error));
                }
            }
            else
            {
                photoObj = mDatabaseUtil.getImage(id);
                boolean isdeleted = mDatabaseUtil.deleteImage(rowView.itemImage.getTag().toString());
                if(isdeleted)
                {
                    //refresh the adapter
                    mItemsDBData.remove(position);
                    EventBus.getDefault().post(new UpdateSavedEvent());
                }
            }
        }
    }


    //get photos from the first page
    private void getSavedPhotos()
    {
        mItemsDBData = (ArrayList<PhotoDBVo>) mDatabaseUtil.getAllImages();
        //get total count and store it in db
        fetchingItems.setVisibility(View.GONE);
        updateAdapterDataSet();
    }

    private void searchSavedPhotos(String query, int pagenumber)
    {
        fetchingItems.setVisibility(View.VISIBLE);
        List<PhotoDBVo> items = (ArrayList<PhotoDBVo>) mDatabaseUtil.getImagesBySearchStr(query);
        fetchingItems.setVisibility(View.GONE);
        if(items.size() == 0)
        {
            String error = getContext().getResources().getString(R.string.no_results_found);
            LogUtil.log(error);
            displaySnackBar(error);
        }
        else
        {
            mItemsDBData.addAll(items);
            updateAdapterDataSet();
        }
    }

    //refresh saved fragment when a image is saved or deleted in other fragments
    @Subscribe
    public void onEvent(UpdateSavedEvent event)
    {
        /**
         * YOUR CODE HERE
         *
         * This will be the event posted via the EventBus when an API error has occured.
         *
         * For part 1a you should clear the fragment and notify the user of the error.
         */
        if(event != null)
        {
           getSavedPhotos();
        }
    }

    //Event to switch between popular mode and search mode
    @Subscribe
    public void onEvent(SearchPhotosEvent event)
    {
        if(event != null && event.getType().equals(getContext().getResources().getString(R.string.saved)))
        {
            String query = ((SearchPhotosEvent) event).getQuery();
            if (query == null)
            {
                searchQuery = null;
                clearAdapterDataSet();
                getSavedPhotos();
            }
            else if (searchQuery == null || (!searchQuery.equals(query)))
            {
                searchQuery = query;
                clearAdapterDataSet();
                searchSavedPhotos(query, 1);
            }
        }
    }

    protected void clearAdapterDataSet()
    {
        if(mItemsDBData != null && mDBListAdapter != null)
        {
            mItemsDBData.clear();
            mDBListAdapter.swap(mItemsDBData);
        }
    }

    private void updateAdapterDataSet()
    {
        if(mItemsDBData != null && mDBListAdapter != null)
        {
            mDBListAdapter.swap (mItemsDBData);
        }
    }
}
