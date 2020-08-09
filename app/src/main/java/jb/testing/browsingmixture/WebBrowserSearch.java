package jb.testing.browsingmixture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

public class WebBrowserSearch extends Fragment {

    private String url;
    private WebView wv;
    private Bundle webViewBundle;

    public static WebBrowserSearch newInstance(String url) {
        WebBrowserSearch search = new WebBrowserSearch();
        search.url = url;
        return search;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //when rotating the device current webview is not loading to load it we will call this method
        setRetainInstance(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_web_browser_search, container, false);
        final ProgressBar loader = view.findViewById(R.id.progressbar);
        wv = view.findViewById(R.id.webview);
        EditText search = ((Activity) getContext()).findViewById(R.id.et_search);

        String searchQuery = url;
        if (MainActivity.allowSearch) {
            searchQuery += search.getText().toString();
        }

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                loader.setProgress(newProgress);
                if (newProgress == 100)
                    loader.setVisibility(View.GONE);
                else
                    loader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if(((Activity) getContext()) != null)
                    ((Activity) getContext()).getWindow().setTitle(title);
            }
        });
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        wv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if(keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }
        });


        if(webViewBundle == null) {
            wv.loadUrl(searchQuery);
            Log.e("ERROR","Simple Load-"+wv.getUrl());
        }
        else {
            Log.e("ERROR","restoring-"+wv.getUrl());
            wv.restoreState(webViewBundle);
            webViewBundle = null;
        }
        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        /* to save the state of web view means
        if i will search "hi" in portrait mode and then i rotate my device
        then in landscape mode i cannot see the result of "hi"
         */
        webViewBundle = new Bundle();
        wv.saveState(webViewBundle);
        Log.e("ERROR","Saving-"+wv.getUrl());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*webViewBundle = new Bundle();
        wv.saveState(webViewBundle);*/
        Log.e("ERROR","DestroyView-Saving-"+wv.getUrl());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ERROR","Destroy");
    }

    /**** Handler To Go Back In The WebView ***/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what)
            {
                case 1:
                    webViewGoBack();
                    break;
            }
        }
    };

    private void webViewGoBack() {
        wv.goBack();
    }
}