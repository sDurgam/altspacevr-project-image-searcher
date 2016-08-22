package testsample.altvr.com.testsample;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.view.View;
import android.widget.ListView;

import org.apache.tools.ant.Main;
import org.junit.Assert;
import static org.assertj.android.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;
import org.robolectric.util.FragmentTestUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import testsample.altvr.com.testsample.activities.MainActivity;
import testsample.altvr.com.testsample.adapter.ItemsListAdapter;
import testsample.altvr.com.testsample.fragments.EditorsChoiceFragment;
import testsample.altvr.com.testsample.fragments.LatestFragment;
import testsample.altvr.com.testsample.fragments.PhotosFragment;
import testsample.altvr.com.testsample.fragments.PopularFragment;
import testsample.altvr.com.testsample.fragments.SavedFragment;
import testsample.altvr.com.testsample.util.DatabaseUtil;
import testsample.altvr.com.testsample.viewpager.PhotosPagerAdapter;
import testsample.altvr.com.testsample.vo.PhotoVo;

/**
 * Created by root on 8/20/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, manifest = "src/main/AndroidManifest.xml", packageName = "testsample.altvr.com.testsample")
public class MainActivityTest
{
    private ActivityController<MainActivity> mActivityController;
    private MainActivity mActivity;
    private ViewPager mViewPager;
    private String[] tabTitles;
    private Context mContext;
    DatabaseUtil mDb;

    @Before
    public void setUp() throws Exception
    {
        mActivityController = Robolectric.buildActivity(MainActivity.class).attach().create().visible().resume();
        mActivity = mActivityController.get();
        mViewPager = (ViewPager) mActivity.findViewById(R.id.pager);
        mContext = RuntimeEnvironment.application;
    }

    @Test
    public void testActivityNotBeNull() throws Exception
    {
        Assert.assertNotNull(mActivity);
    }

    @Test
    public void testActivityLifeCycle()
    {
        MainActivity testActivity = Robolectric.buildActivity(MainActivity.class).attach().create().start().resume().pause().stop().destroy().get();
        Assert.assertNotNull(testActivity);
    }

    @Test
    public void testActivityScreenRotation()
    {
        int currentOrientation = mContext.getResources().getConfiguration().orientation;
        boolean isPortrait = currentOrientation == Configuration.ORIENTATION_PORTRAIT || currentOrientation == Configuration.ORIENTATION_UNDEFINED;
        int orientation = isPortrait ? Configuration.ORIENTATION_LANDSCAPE : Configuration.ORIENTATION_PORTRAIT;
        mContext.getResources().getConfiguration().orientation = orientation;
        Bundle bundle = new Bundle();
        mActivityController.saveInstanceState(bundle).pause().stop().destroy();
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create(bundle).start().restoreInstanceState(bundle).resume();
        MainActivity activity = controller.get();
        Assert.assertNotNull(activity);
    }

    @Test
    public void testSearchViewNotNull()
    {
        SearchView mSearchView = (SearchView) mActivity.findViewById(R.id.search);
        Assert.assertNotNull(mSearchView);
    }

    @Test
    public void testViewPagerNotNull()
    {
        Assert.assertNotNull(mViewPager);
    }

    @Test
    public void testTabLayoutNotNull()
    {
        TabLayout tabLayout = (TabLayout) mActivity.findViewById(R.id.sliding_tabs);
        Assert.assertNotNull(tabLayout);
    }

    @Test
    public void testViewPagerItemCount()
    {
        tabTitles = new String[]{mContext.getResources().getString(R.string.popular), mContext.getResources().getString(R.string.latest),
                mContext.getResources().getString(R.string.saved), mContext.getResources().getString(R.string.editorschoice)
        };
        Assert.assertSame(mViewPager.getChildCount(), tabTitles.length);
    }


    @Test
    public void testTabLayoutChildCount()
    {
        TabLayout tabLayout = (TabLayout) mActivity.findViewById(R.id.sliding_tabs);
        Assert.assertNotNull(tabLayout);
        Assert.assertTrue(tabLayout.getChildCount() > 0);
    }

    @Test
    public void testPopularFragmentNotNull() throws Exception
    {
        PopularFragment fragment = PopularFragment.newInstance();
        SupportFragmentTestUtil.startFragment(fragment);
        Assert.assertNotNull(fragment.getView());
    }

    @Test
    public void testPopularFragmentRecyclerViewNotNull() throws Exception
    {
        PopularFragment fragment = PopularFragment.newInstance();
        SupportFragmentTestUtil.startFragment(fragment);
        Assert.assertNotNull(fragment.getView());
        RecyclerView mRecyclerView = (RecyclerView) fragment.getView().findViewById(R.id.photosListRecyclerView);
        Assert.assertNotNull(mRecyclerView);
    }


    @Test
    public void testLatestFragmentNotNull() throws Exception
    {
        LatestFragment fragment = LatestFragment.newInstance();
        SupportFragmentTestUtil.startFragment(fragment);
        Assert.assertNotNull(fragment.getView());
    }

    @Test
    public void testSavedFragmentNotNull() throws Exception
    {
        SavedFragment fragment = SavedFragment.newInstance();
        SupportFragmentTestUtil.startFragment(fragment);
        Assert.assertNotNull(fragment.getView());
    }

    @Test
    public void testEditorsChoiceNotNull() throws Exception
    {
        EditorsChoiceFragment fragment = EditorsChoiceFragment.newInstance();
        SupportFragmentTestUtil.startFragment(fragment);
        Assert.assertNotNull(fragment.getView());
    }


    @Test
    public void testPopulatePopularPhotosAdapter()
    {
        List<PhotoVo> photoVoList = new ArrayList<>();
        PhotoVo photo = new PhotoVo();
        photo.id = "12345";
        photo.tags = "apple, plums";
        photo.webformatURL = null;
        photoVoList.add(photo);
        photo = new PhotoVo();
        photo.id = "78910";
        photo.tags = "coffee, beans";
        photo.webformatURL = "https://pixabay.com/get/35bbf209db8dc9f2fa36746403097ae226b796b9e13e39d2_640.jpg";
        photoVoList.add(photo);
        ItemsListAdapter adapter = new ItemsListAdapter(photoVoList,  PopularFragment.newInstance().getListener(), 0, new WeakReference<Context>(mContext));
        RecyclerView recycleview = new RecyclerView(mContext);
        recycleview.setLayoutManager(new LinearLayoutManager(mContext));
        ItemsListAdapter.ItemViewHolder viewHolder = (ItemsListAdapter.ItemViewHolder) adapter.onCreateViewHolder(recycleview, 0);
        adapter.onBindViewHolder(viewHolder, 0);
        adapter.onBindViewHolder(viewHolder, 1);
        Assert.assertEquals(adapter.getItemCount(), 2);
        viewHolder.saveText.performClick();
        Assert.assertEquals(viewHolder.saveText.getText().equals(mContext.getApplicationContext().getResources().getString(R.string.unsave)), true);
        viewHolder.saveText.performClick();
        Assert.assertEquals(viewHolder.saveText.getText().equals(mContext.getApplicationContext().getResources().getString(R.string.save)), true);
    }
}
