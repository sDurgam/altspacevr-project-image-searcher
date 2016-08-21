package testsample.altvr.com.testsample.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView ;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.events.ApiErrorEvent;
import testsample.altvr.com.testsample.events.SearchPhotosEvent;
import testsample.altvr.com.testsample.events.UpdateSavedEvent;
import testsample.altvr.com.testsample.fragments.SavedFragment;
import testsample.altvr.com.testsample.service.ApiService;
import testsample.altvr.com.testsample.util.LogUtil;
import testsample.altvr.com.testsample.viewpager.PhotosPagerAdapter;

import static android.support.design.widget.TabLayout.*;

public class MainActivity extends AppCompatActivity
{
    private LogUtil log = new LogUtil(MainActivity.class);
    private ApiService mService;
    SearchView mSearchView;
    ViewPager mViewPager;
    PhotosPagerAdapter photosadapter;
    TabLayout mTabLayout;
    String[] tabTitles;
    EventBus mEventBus; //to communicate with fragment about searchview

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabTitles = new String[] {getResources().getString(R.string.popular), getResources().getString(R.string.latest), getResources().getString(R.string.saved), getResources().getString(R.string.editorschoice)};
        initViews();
        //displayFragment(SavedFragment.newInstance(), R.string.toolbar_main_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(queryTextListener);
        mSearchView.setOnCloseListener(searchCloseListener);
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews()
    {
        mService = new ApiService(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mEventBus = EventBus.getDefault();
        photosadapter = new PhotosPagerAdapter(getSupportFragmentManager(), tabTitles);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mViewPager.setAdapter(photosadapter);
        mViewPager.setOffscreenPageLimit(tabTitles.length - 1);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab)
            {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab)
            {
                if(mSearchView != null)
                {
                    mSearchView.onActionViewCollapsed();
                }
            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }


    //event handler to communicate search view status to photos fragment
    final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
    {
        @Override
        public boolean onQueryTextChange(String newText) {
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query)
        {
            mSearchView.clearFocus();
            //filter results based on the search request
            //clearphotos in the fragment
            String type = getCurrentPageType();
            SearchPhotosEvent searchPhotosEvent = new SearchPhotosEvent(query, getCurrentPageType());
            mEventBus.post(searchPhotosEvent);
            return true;
        }
    };

    //Added methos to display default photos when searchview is closed
    final SearchView.OnCloseListener searchCloseListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose()
        {
            mSearchView.onActionViewCollapsed();
            SearchPhotosEvent searchPhotosEvent = new SearchPhotosEvent(null, getCurrentPageType());
            mEventBus.post(searchPhotosEvent);
            return false;
        }
    };

    String getCurrentPageType()
    {
        return tabTitles[mTabLayout.getSelectedTabPosition()];
    }

//    private void displayFragment(Fragment fragment, int title) {
//        setTitle(title);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
//    }
}
