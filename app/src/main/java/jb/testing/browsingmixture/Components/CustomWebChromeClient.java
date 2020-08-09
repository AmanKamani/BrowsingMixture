package jb.testing.browsingmixture.Components;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CustomWebChromeClient extends android.webkit.WebChromeClient {

    private Activity activity;
    private View customView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private WebChromeClient.CustomViewCallback customViewCallback;

    private int originalOrientation;
    private int originalSystemUiVisibility;

    public CustomWebChromeClient(ProgressBar loader, SwipeRefreshLayout srl, Activity activity)
    {
        this.progressBar = loader;
        this.activity = activity;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        progressBar.setProgress(newProgress);
        if(newProgress == 100) {
            progressBar.setVisibility(View.GONE);
        }
        else
            progressBar.setVisibility(View.VISIBLE);

    }

    //For full screen video support
    public Bitmap getDefaultVideoPoster()
    {
        if(activity == null)
            return null;
        return BitmapFactory.decodeResource(activity.getApplicationContext().getResources(),2130837573);
    }

    public void onHideCustomView()
    {
        ((FrameLayout)activity.getWindow().getDecorView()).removeView(this.customView);
        this.customView = null;
        activity.getWindow().getDecorView().setSystemUiVisibility(this.originalSystemUiVisibility);
        activity.setRequestedOrientation(this.originalOrientation);
        this.customViewCallback.onCustomViewHidden();
        this.customViewCallback = null;
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if(this.customView != null) {
            onHideCustomView();
            return;
        }
        this.customView = view;
        originalSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
        originalOrientation = activity.getRequestedOrientation();
        customViewCallback = callback;

        ((FrameLayout)activity.getWindow().getDecorView()).addView(this.customView,new FrameLayout.LayoutParams(-1, -1));
        activity.getWindow().getDecorView().setSystemUiVisibility(3846);
        Toast.makeText(activity, "Called", Toast.LENGTH_SHORT).show();
    }
}
