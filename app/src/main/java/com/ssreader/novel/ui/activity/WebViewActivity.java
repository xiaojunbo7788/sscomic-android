package com.ssreader.novel.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.net.Phpcallback;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.ScrollWebView;
import com.ssreader.novel.ui.view.base64decoder.BASE64Decoder;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.LanguageUtil;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_title)
    TextView public_sns_topbar_title;
    @BindView(R.id.activity_webview)
    public WebView mWebView;

    private String flag, title;
    private boolean is_otherBrowser;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        public_sns_topbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BWNApplication.applicationContext.isMainActivityStartUp() && (flag == null || !flag.equals("yinsi"))) {
                    startActivity(new Intent(activity, MainActivity.class));
                }
                finish();
            }
        });
        http_URL = formIntent.getStringExtra("url");
        flag = formIntent.getStringExtra("flag");
        title = formIntent.getStringExtra("title");
        is_otherBrowser = formIntent.getBooleanExtra("is_otherBrowser", false);
        if (is_otherBrowser && http_URL != null && !TextUtils.isEmpty(http_URL)) {
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_VIEW);
            Uri content_uri_browsers = Uri.parse(http_URL);
            intent2.setData(content_uri_browsers);
            this.startActivity(intent2);
            finish();
        }
        if (title != null) {
            public_sns_topbar_title.setText(title);
        }
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());  //在前面加入下载监听器
        mWebView.setWebViewClient(new DemoWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                // 判断权限
                if (PermissionsUtil.hasPermission(activity, Manifest.permission.CAMERA)) {
                    take();
                } else {
                    setPermission();
                }
                return true;
            }

            //<3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                take();
            }

            //>3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                take();
            }

            //>4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                take();
            }
        });
        js();
        mWebView.loadUrl(http_URL);
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

    private class MyWebViewDownLoadListener implements DownloadListener {
        //添加监听事件即可
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    if (!BWNApplication.applicationContext.isMainActivityStartUp() && (flag == null || !flag.equals("yinsi"))) {
                        startActivity(new Intent(activity, MainActivity.class));
                    }
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * js事件
     */
    private void js() {
        mWebView.addJavascriptInterface(new Phpcallback() {
            @JavascriptInterface //必须加的注解
            @Override
            public void share(String data) {
                JSONObject jsonObject;
                try {
                    MyToash.Log("mWebView", data);
                    jsonObject = new JSONObject(data);
                    String image = jsonObject.getString("image");
                    if (!TextUtils.isEmpty(image)) {
                        image = image.substring(image.indexOf(",") + 1);
                        String local = FileManager.getImgs() + System.currentTimeMillis() + ".jpg";
                        BASE64Decoder.Base64ToImage(image, local);
                    }
                } catch (Exception e) {
                }
            }

            @JavascriptInterface //必须加的注解
            @Override
            public void gohome(String A) {
                MyToash.Log("JavascriptInterface ", "gohome");
            }

            @JavascriptInterface //必须加的注解
            @Override
            public void mine(String type) {
                MyToash.Log("JavascriptInterface ", "mine");
            }

            @JavascriptInterface //必须加的注解
            @Override
            public void login(String string) {
                MyToash.Log("JavascriptInterface ", "login");
            }

            @JavascriptInterface //必须加的注解
            @Override
            public void watch(String type) {
            }

            @JavascriptInterface //必须加的注解
            @Override
            public void webview(String webview) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyToash.Log("JavascriptInterface ", webview);
                        try {
                            JSONObject jsonObject = new JSONObject(webview);
                            startActivity(new Intent(activity, WebViewActivity.class)
                                    .putExtra("url", jsonObject.getString("url"))
                                    .putExtra("title", jsonObject.getString("title")));
                        } catch (Exception e) {
                        }
                    }
                });
            }

            @JavascriptInterface //必须加的注解
            @Override
            public void copy(String copy) {
                MyToash.Log("copy", copy);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(copy);
                            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setText(jsonObject.getString("copy"));
                            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.AboutActivity_yetcopy));
                        } catch (Exception e) {
                        }
                    }
                });
            }

            @JavascriptInterface //必须加的注解
            @Override
            public void gopubfeedback(String copy) {
            }

            @JavascriptInterface //必须加的注解
            @Override
            public void gowelfare(String copy) {
                finish();
            }
        }, "decoObject");
    }

    /**
     * 获取相机权限
     */
    private void setPermission() {
        PermissionsUtil.requestPermission(WebViewActivity.this, new PermissionListener() {
            @Override
            public void permissionGranted(@androidx.annotation.NonNull String[] permission) {
                take();
            }

            @Override
            public void permissionDenied(@androidx.annotation.NonNull String[] permission) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                if (result == null) {
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
        return;
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调
    private Uri imageUri;
}
