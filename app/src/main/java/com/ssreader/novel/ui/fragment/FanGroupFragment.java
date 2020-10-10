package com.ssreader.novel.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.R;
import com.ssreader.novel.utils.ShareUitls;

import butterknife.BindView;

public class FanGroupFragment extends BaseFragment {

    @BindView(R.id.fragment_fan_group_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_fan_group_web)
    WebView webView;

    private String url;

    public static FanGroupFragment getInstance(String url) {
        FanGroupFragment f = new FanGroupFragment();
        Bundle args = new Bundle();
        args.putSerializable("url",url);
        f.setArguments(args);
        return f;
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_fan_group;
    }

    @Override
    public void initView() {
        initWeb();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWeb() {
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebViewClient(new DemoWebViewClient());
    }

    @Override
    public void initData() {
        String url = (String) getArguments().getString("url");
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    @Override
    public void initInfo(String json) {

    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        //添加监听事件即可
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    class DemoWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception s) {
            }
            return true;
        }
    }
}
