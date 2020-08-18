package com.ssreader.novel.net;

import android.webkit.JavascriptInterface;

public interface Phpcallback {

    @JavascriptInterface
    void share(String data);

    @JavascriptInterface
    void gohome(String S);

    @JavascriptInterface
    void mine(String type);

    @JavascriptInterface
    void login(String string);

    @JavascriptInterface
    void watch(String type);

    @JavascriptInterface
    void webview(String webview);

    @JavascriptInterface
    void copy(String copy);

    @JavascriptInterface
    void gopubfeedback(String copy);

    @JavascriptInterface
    void gowelfare(String copy);
}
