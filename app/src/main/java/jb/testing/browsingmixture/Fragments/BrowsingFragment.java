package jb.testing.browsingmixture.Fragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import jb.testing.browsingmixture.Components.CustomWebChromeClient;
import jb.testing.browsingmixture.Components.CustomWebViewClient;
import jb.testing.browsingmixture.R;
import jb.testing.browsingmixture.Utility;

public class BrowsingFragment extends Fragment {

    //private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private WebView webView;
    Utility.SearchEngine searchEngineType;


    public static BrowsingFragment newInstance(Utility.SearchEngine engineType) {

        Bundle args = new Bundle();

        BrowsingFragment fragment = new BrowsingFragment();
        fragment.searchEngineType = engineType;
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout,container,false);
        Log.e("ERROR","Creating fragment");
        //swipeRefreshLayout = view.findViewById(R.id.layout_swipeRefresh);
        progressBar = view.findViewById(R.id.test_progressBar);
        webView = view.findViewById(R.id.webview);

        setWebViewSettings();

        if(savedInstanceState == null)
            webView.loadUrl(Utility.getSearchUrl(searchEngineType));
        else
            webView.restoreState(savedInstanceState);

        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //on rotation of the screen this method will be called here we are saving the state of WebView
        // and restored in the onCreateView method
        webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebViewSettings() {

        webView.setBackgroundColor(Color.argb(1,0,0,0));
        //webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        webView.getSettings().setUserAgentString(System.getProperty("http.agent")); //for google login (Oath)
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setUseWideViewPort(true); //true-load & scale according to the meta tag of webpage
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        /*this is required as some site will not open without setting DomStorageEnabled to true
         [Ex. https://www.narendramodi.in] so set the DomStorageEnabled to true
        and if you will get the CLEARTEXT_NOT_PERMITTED error in webview then,
        in manifest file put the usesCleartextTraffic to true in the application tag
         */

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient(progressBar,null,getActivity()));

        registerForContextMenu(webView);
    }

    public boolean canGoBack()
    {
        return webView.canGoBack();
    }
    public void goBack()
    {
        //Toast.makeText(getContext(), "called-"+webView.canGoBack(), Toast.LENGTH_SHORT).show();
            webView.goBack();
    }
    
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        final WebView.HitTestResult webHitTestResult = webView.getHitTestResult();

        if(webHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
            webHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

            menu.setHeaderTitle("Download Image");
            menu.setHeaderIcon(R.drawable.ic_outline_download_24);

            menu.add(0,1,0,"Click To Download")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                    String downloadImageURL = webHitTestResult.getExtra();
                    if(URLUtil.isValidUrl(downloadImageURL))
                    {
                        DownloadManager.Request mRequest = new DownloadManager.Request(Uri.parse(downloadImageURL));
                        mRequest.allowScanningByMediaScanner();
                        mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        DownloadManager downloadManager = (DownloadManager)getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadManager.enqueue(mRequest);

                        Toast.makeText(getContext(), "Downloading Image...", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(), "Cannot Download This Image", Toast.LENGTH_SHORT).show();
                    return false;
                    }
                });
        }
    }


    public void reloadWebView(String url) {
        //if(this.isVisible()) {
            webView.loadUrl(url);
            //Toast.makeText(getContext(), "visible-"+url, Toast.LENGTH_SHORT).show();
        //}
    }
}
