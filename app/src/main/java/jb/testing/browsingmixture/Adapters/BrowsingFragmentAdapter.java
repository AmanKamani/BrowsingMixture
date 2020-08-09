package jb.testing.browsingmixture.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import jb.testing.browsingmixture.Fragments.BrowsingFragment;
import jb.testing.browsingmixture.Utility;

public class BrowsingFragmentAdapter extends FragmentStateAdapter {

    private ArrayList<Utility.SearchEngine> engineTypes;
    private BrowsingFragment fragments[];

    public BrowsingFragmentAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Utility.SearchEngine> searchEngines) {
        super(fragmentActivity);

        this.engineTypes = searchEngines;
        fragments = new BrowsingFragment[engineTypes.size()];
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        fragments[position] = BrowsingFragment.newInstance(engineTypes.get(position));
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return engineTypes.size();
    }

    public Fragment getCurrentFragment(int position){
        return fragments[position];
    }

    public void refreshAll(String url)
    {
        for(int i=0;i<fragments.length;i++)
            fragments[i].reloadWebView(Utility.getSearchUrl(engineTypes.get(i))+url);
    }
}