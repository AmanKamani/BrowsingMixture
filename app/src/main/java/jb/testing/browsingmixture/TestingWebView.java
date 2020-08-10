package jb.testing.browsingmixture;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import jb.testing.browsingmixture.Adapters.BrowsingFragmentAdapter;
import jb.testing.browsingmixture.Fragments.BrowsingFragment;

public class TestingWebView extends AppCompatActivity {

    private ArrayList<Utility.SearchEngine> searchEngineTypes;
    private BrowsingFragmentAdapter adapter;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;
    private FloatingActionButton fab_additional, fab_search, fab_setting, fab_show;
    private boolean isButtonOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_web_view);

        fab_additional = findViewById(R.id.fab_additional_btn);
        fab_show = findViewById(R.id.fab_show_btn);
        fab_search = findViewById(R.id.fab_search_btn);
        fab_setting = findViewById(R.id.fab_settings_btn);

        viewPager2 = findViewById(R.id.test_viewPager);
        tabLayout = findViewById(R.id.test_tabLayout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        searchEngineTypes = new ArrayList<>();
        searchEngineTypes.add(Utility.SearchEngine.GOOGLE);
        searchEngineTypes.add(Utility.SearchEngine.BING);
        searchEngineTypes.add(Utility.SearchEngine.DUCKDUCKGO);
        searchEngineTypes.add(Utility.SearchEngine.YOUTUBE);

        adapter = new BrowsingFragmentAdapter(this, searchEngineTypes);
        viewPager2.setAdapter(adapter);

        viewPager2.setOffscreenPageLimit(searchEngineTypes.size()-1);
        viewPager2.setUserInputEnabled(false); //used to disable swiping
        
        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //tab.setText(Utility.getSearchEngineName(searchEngineTypes.get(position)));
                tab.setIcon(Utility.getSearchEngineIcon(getApplicationContext(), searchEngineTypes.get(position)));
            }
        });
        tabLayoutMediator.attach();

        fab_additional.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                hideAdditionalOption();
                view.setVisibility(View.INVISIBLE);
                fab_show.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        BrowsingFragment bf = (BrowsingFragment)adapter.getCurrentFragment(viewPager2.getCurrentItem());
        if(bf.canGoBack())
            bf.goBack();
        else
            super.onBackPressed();
    }

    // if any of the setting button will be clicked this function will be called
    public void additionalOptions(View view) {

        switch (view.getId()) {
            case R.id.fab_additional_btn:
                if (isButtonOpen)
                    hideAdditionalOption();
                else
                    showAdditionalOption();
                break;
            case R.id.fab_search_btn:
                searchEveryWhere(view);
                break;
            case R.id.fab_settings_btn:
                Toast.makeText(this, "Clicked on Setting button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // this method will be called when search button will be clicked
    private void searchEveryWhere(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_search_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        final EditText et = dialogView.findViewById(R.id.test_et_search);
        final ImageButton speak_btn = dialogView.findViewById(R.id.speak_btn);

        //et.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        final AlertDialog dialog = builder.create();

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().isEmpty())
                    speak_btn.setVisibility(View.VISIBLE);
                else
                    speak_btn.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH && !et.getText().toString().trim().isEmpty()) {
                    adapter.refreshAll(et.getText().toString());
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                    dialog.dismiss();
                }
                return false;
            }
        });


        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.y = 100;

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    // this function will be called on the click of "additional Setting button" to hide the "additional option buttons"
    private void hideAdditionalOption() {
        ObjectAnimator setting_anim = ObjectAnimator.ofFloat(fab_setting, "translationY", 0f);

        setting_anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fab_setting.setVisibility(View.INVISIBLE);
                fab_search.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        ObjectAnimator.ofFloat(fab_additional, "rotation", 135f, 0f).start();
        setting_anim.setDuration(100).start();
        ObjectAnimator.ofFloat(fab_search, "translationX", 0f).setDuration(100).start();

        isButtonOpen = false;
    }

    // this function will be called on the click of "additional Setting button" to show the "additional option buttons"
    private void showAdditionalOption() {
        fab_setting.setVisibility(View.VISIBLE);
        fab_search.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(fab_additional, "rotation", 0f, 135f).start();
        ObjectAnimator.ofFloat(fab_setting, "translationY", -200f).setDuration(100).start();
        ObjectAnimator.ofFloat(fab_search, "translationX", -200f).setDuration(100).start();
        isButtonOpen = true;
    }

    // this will be called while clicking on the Show button (with Eye icon)
    public void showAdditionalOptionButton(View view) {
        fab_additional.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }
}