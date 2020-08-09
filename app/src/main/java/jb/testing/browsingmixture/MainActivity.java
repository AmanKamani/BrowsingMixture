package jb.testing.browsingmixture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static boolean allowSearch = false;
    ImageView img_search;
    EditText et_search;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    ArrayList<Utility.SearchEngine> searchEngines;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_search = findViewById(R.id.btn_search);
        et_search = findViewById(R.id.et_search);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_ENTER) {
                        search();
                        return true;
                    }
                }
                return false;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                allowSearch = false;
                if(savedInstanceState != null) {
                    allowSearch = true;
                }
            }
        });

        searchEngines = new ArrayList<>();
        searchEngines.add(Utility.SearchEngine.GOOGLE);
        searchEngines.add(Utility.SearchEngine.BING);
        searchEngines.add(Utility.SearchEngine.DUCKDUCKGO);
        searchEngines.add(Utility.SearchEngine.YOUTUBE);


        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), searchEngines, this);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        setTabIcon();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabIcon() {
        for (int i = 0; i < searchEngines.size(); i++) {
            tabLayout.getTabAt(i).setIcon(Utility.getSearchEngineIcon(this, searchEngines.get(i)));
        }
    }

    public void searchQuery(View view) {
        search();
    }

    private void search()
    {
        et_search.clearFocus();
        allowSearch = true;
        pagerAdapter.notifyDataSetChanged();
        setTabIcon();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(allowSearch)
            outState.putBoolean("AllowSearch", true);
    }
}