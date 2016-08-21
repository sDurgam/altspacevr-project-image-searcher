package testsample.altvr.com.testsample.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import testsample.altvr.com.testsample.fragments.EditorsChoiceFragment;
import testsample.altvr.com.testsample.fragments.LatestFragment;
import testsample.altvr.com.testsample.fragments.PopularFragment;
import testsample.altvr.com.testsample.fragments.SavedFragment;

/**
 * Created by root on 8/20/16.
 */
public class PhotosPagerAdapter extends FragmentPagerAdapter
{
    protected static int NUM_ITEMS;
    private String[] tabTitles;

    public PhotosPagerAdapter(FragmentManager fm, String[] _tabTitles)
    {
        super(fm);
        tabTitles = _tabTitles;
        NUM_ITEMS = tabTitles.length;
    }

    @Override
    public Fragment getItem(int position)
    {
        if(position == 0)
        {
            return PopularFragment.newInstance();
        }
        else if(position == 1)
        {
            return LatestFragment.newInstance();
        }
        else if(position == 2)
        {
            return SavedFragment.newInstance();
        }
        return EditorsChoiceFragment.newInstance();
    }

    @Override
    public int getCount()
    {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        // Generate title based on item position
        return tabTitles[position];
    }
}
