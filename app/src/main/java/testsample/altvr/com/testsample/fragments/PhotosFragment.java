package testsample.altvr.com.testsample.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.adapter.ItemsBaseAdapter;
import testsample.altvr.com.testsample.adapter.ItemsListAdapter;
import testsample.altvr.com.testsample.events.ApiErrorEvent;
import testsample.altvr.com.testsample.events.PhotosEvent;
import testsample.altvr.com.testsample.events.SearchPhotosEvent;
import testsample.altvr.com.testsample.events.UpdateSavedEvent;
import testsample.altvr.com.testsample.listeners.RecyclerViewScrollListener;
import testsample.altvr.com.testsample.service.ApiService;
import testsample.altvr.com.testsample.util.DatabaseBitmapUtil;
import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.vo.PhotoDBVo;
import testsample.altvr.com.testsample.vo.PhotoVo;

public class PhotosFragment extends Fragment
{
    private LogUtil log = new LogUtil(PhotosFragment.class);
    protected LinearLayout fetchingItems;
    protected RecyclerView itemsListRecyclerView;
    protected ApiService mService;


    private ArrayList<PhotoVo> mItemsData;
    private ItemsListAdapter mListAdapter;
    protected DatabaseUtil mDatabaseUtil;
    protected String searchQuery = null;   //to distinguish between search photos and getdefault photos
    protected LinearLayoutManager mLinearLayoutManager;
    //list of photo ids in database
    List<String> photoIDsList;

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
        photoIDsList = new ArrayList<>();
        setupViews();
    }

    private void initViews(View view)
    {
        fetchingItems = (LinearLayout) view.findViewById(R.id.listEmptyView);
        itemsListRecyclerView = (RecyclerView) view.findViewById(R.id.photosListRecyclerView);
    }

    private void setupViews()
    {
        fetchingItems.setVisibility(View.VISIBLE);
        setupItemsList();
        EventBus.getDefault().register(this);
    }

    protected void setupItemsList()
    {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        itemsListRecyclerView.setHasFixedSize(true);
        itemsListRecyclerView.setLayoutManager(mLinearLayoutManager);
        mListAdapter = new ItemsListAdapter(new ArrayList<PhotoVo>(), new ItemClickedListener(), getResources().getDisplayMetrics().widthPixels, getContext());
        itemsListRecyclerView.setAdapter(mListAdapter);
    }

    protected class ItemClickedListener implements ItemsBaseAdapter.ItemListener
    {

        @Override
        public void itemClicked(ItemsBaseAdapter.ItemViewHolder rowView, int position)
        {
            String saveStatus = rowView.saveText.getText().toString();
            String unsavestr = getContext().getResources().getString(R.string.unsave);
            String savestr = getContext().getResources().getString(R.string.save);
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
                    EventBus.getDefault().post(new UpdateSavedEvent()); //refresh all fragments
                }
                else
                {
                    displaySnackBar(getContext().getResources().getString(R.string.saveimage_error));
                }
            }
            else
            {
                boolean isdeleted = mDatabaseUtil.deleteImage(rowView.itemImage.getTag().toString());
                if(isdeleted)
                {
                    rowView.saveText.setText(savestr);  //save if db returns a success message
                    EventBus.getDefault().post(new UpdateSavedEvent()); //refresh all fragment
                }
            }
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mItemsData = new ArrayList<>();
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
     //   getDefaultPhotos(); //api call
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

    protected void displaySnackBar(String message)
    {
        fetchingItems.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.mainCoordinateLayout),message, Snackbar.LENGTH_SHORT);
        snackbar.show();
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
        if(event != null)
        {
            String message = event.errorDescription;
            displaySnackBar(message);
        }
    }

    protected void clearAdapterDataSet()
    {
        if(mItemsData != null && mListAdapter != null)
        {
            mItemsData.clear();
            mListAdapter.swap(mItemsData);
        }
    }

    protected void updateAdapterDataSet(ArrayList<PhotoVo> photosList)
    {
        if(photosList != null && mItemsData != null && mListAdapter != null)
        {
            mItemsData.addAll(photosList);
            mListAdapter.swap (mItemsData);
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
            mListAdapter.updateDBPhotosList();
        }
    }
}

