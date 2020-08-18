package com.ssreader.novel.net;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by administrator on 2016/4/16.
 */
public abstract class ResultCallback<T> {

    public abstract void onFailure(Request request, Exception e);

    public abstract void onResponse(String response);

    public void onResponse(Response response) {

    }
}

