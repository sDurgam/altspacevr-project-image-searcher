package testsample.altvr.com.testsample.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView ;

import org.greenrobot.eventbus.EventBus;

import testsample.altvr.com.testsample.R;
import testsample.altvr.com.testsample.events.SearchPhotosEvent;
import testsample.altvr.com.testsample.fragments.PhotosFragment;
import testsample.altvr.com.testsample.service.ApiService;
import testsample.altvr.com.testsample.util.LogUtil;

public class MainActivity extends AppCompatActivity
{
    private LogUtil log = new LogUtil(MainActivity.class);
    private ApiService mService;
    SearchView mSearchView;
    EventBus mEventBus; //to communicate with fragment about searchview

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mService = new ApiService(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEventBus = EventBus.getDefault();
        displayFragment(PhotosFragment.newInstance(), R.string.toolbar_main_title);
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

    //event handler to communicate search view status to photos fragment


    final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
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
            SearchPhotosEvent searchPhotosEvent = new SearchPhotosEvent(query);
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
            SearchPhotosEvent searchPhotosEvent = new SearchPhotosEvent(null);
            mEventBus.post(searchPhotosEvent);
            return false;
        }
    };

    private void displayFragment(Fragment fragment, int title) {
        setTitle(title);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }
}
