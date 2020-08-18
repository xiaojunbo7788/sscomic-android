package com.ssreader.novel.net;

import android.content.Context;

import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.ui.utils.MyToash;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp3 {

    public static OkHttp3 mInstance;

    private OkHttpClient mOkHttpClient;

    public static OkHttp3 getInstance(Context context) {
        return getInstance(context, true);
    }

    public static OkHttp3 getInstance(Context context, boolean showDialog) {
        if (mInstance == null) {
            synchronized (OkHttp3.class) {
                if (mInstance == null) {
                    mInstance = new OkHttp3(context);
                }
            }
        }
        return mInstance;
    }

    private OkHttp3(Context context) {

    }

    /**
     * 异步get请求
     *
     * @param url
     * @param callback
     */
    public void getAsyncHttp(String url, ResultCallback callback) {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                .build();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response.body().string());
            }
        });
    }

    public void postAsyncHttp(String url, String json, ResultCallback callback) {
        postAsyncHttp(Constant.BASE_URL + url, json, callback, false);
    }

    /**
     * 异步post请求
     *
     * @param url
     * @param json
     * @param callback
     * @param keepGoing 没网情况下是否还是继续往下走
     */
    public void postAsyncHttp(String url, String json, ResultCallback callback, boolean keepGoing) {

        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60 * 1000, TimeUnit.SECONDS)
                .readTimeout(5 * 60 * 1000, TimeUnit.SECONDS)
                .writeTimeout(5 * 60 * 1000, TimeUnit.SECONDS)
                .build();
 /*
        mOkHttpClient = new OkHttpClient().newBuilder()
                .build();*/
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyToash.Log("http_error", e.getMessage());
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response.body().string());
            }
        });
    }
}



