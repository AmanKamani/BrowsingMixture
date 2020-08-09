package jb.testing.browsingmixture;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    FragmentManager fragmentManager;
    WebBrowserSearch[] fragments;
    ArrayList<Utility.SearchEngine> searchEngines;

    public ViewPagerAdapter(@NonNull FragmentManager fm,  ArrayList<Utility.SearchEngine> engines,Context context) {
        super(fm,engines.size());

        this.fragmentManager = fm;
        this.context = context;
        searchEngines = engines;
        fragments = new WebBrowserSearch[engines.size()];

        for(int i=0;i<fragments.length;i++)
        {
            fragments[i] = WebBrowserSearch.newInstance(Utility.getSearchUrl(searchEngines.get(i)));
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

}
