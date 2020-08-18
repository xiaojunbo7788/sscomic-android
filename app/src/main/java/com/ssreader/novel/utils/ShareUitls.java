package com.ssreader.novel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssreader.novel.net.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class ShareUitls {

    public static String getUserString(Context c, String key, String d) {
        if (c == null || TextUtils.isEmpty(key)) {
            return d;
        }
        SharedPreferences sp = c.getSharedPreferences("UserString.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putUserString(Context c, String key, String msg) {
        if (c == null || TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sp = c.getSharedPreferences("UserString.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.apply();
    }


    public static String getMainHttpTaskString(Context c, String key, String d) {
        if (c == null || TextUtils.isEmpty(key)) {
            return d;
        }
        SharedPreferences sp = c.getSharedPreferences("MainHttpTask.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putMainHttpTaskString(Context c, String key, String msg) {
        if (c == null || TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sp = c.getSharedPreferences("MainHttpTask.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.apply();
    }

    public static boolean getBoolean(Context c, String key, boolean d) {
        if (c == null || TextUtils.isEmpty(key)) {
            return d;
        }
        SharedPreferences sp = c.getSharedPreferences("BOOLEAN.xml", Context.MODE_PRIVATE);
        return sp.getBoolean(key, d);
    }

    public static void putBoolean(Context c, String key, boolean msg) {
        if (c == null || TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sp = c.getSharedPreferences("BOOLEAN.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(key, msg);
        e.apply();
    }

    public static boolean getSetBoolean(Context c, String key, boolean d) {
        if (c == null || TextUtils.isEmpty(key)) {
            return d;
        }
        SharedPreferences sp = c.getSharedPreferences("SetBOOLEAN.xml", Context.MODE_PRIVATE);
        return sp.getBoolean(key, d);
    }

    public static void putSetBoolean(Context c, String key, boolean msg) {
        if (c == null || TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sp = c.getSharedPreferences("SetBOOLEAN.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(key, msg);
        e.apply();
    }

    public static long getLong(Context c, String key, long d) {
        if (c == null || TextUtils.isEmpty(key)) {
            return d;
        }
        SharedPreferences sp = c.getSharedPreferences("SNSBWINT.xml", Context.MODE_PRIVATE);
        return sp.getLong(key, d);
    }

    public static void putLong(Context c, String key, long msg) {
        if (c == null || TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sp = c.getSharedPreferences("SNSBWINT.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putLong(key, msg);
        e.apply();
    }

    public static int getInt(Context c, String key, int d) {
        if (c == null || TextUtils.isEmpty(key)) {
            return d;
        }
        SharedPreferences sp = c.getSharedPreferences("SNSBWINT.xml", Context.MODE_PRIVATE);
        return sp.getInt(key, d);
    }

    public static void putInt(Context c, String key, int msg) {
        if (c == null || TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sp = c.getSharedPreferences("SNSBWINT.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt(key, msg);
        e.apply();
    }

    public static String getString(Context c, String key, String d) {
        if (c == null || TextUtils.isEmpty(key)) {
            return d;
        }
        SharedPreferences sp = c.getSharedPreferences("ReaderString.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putString(Context c, String key, String msg) {
        if (c == null || TextUtils.isEmpty(key)) {
            return;
        }
        if (msg == null) {
            msg = "";
        }
        SharedPreferences sp = c.getSharedPreferences("ReaderString.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.apply();
    }


    /**
     * 存储list数据
     * @param activity
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataList(Activity activity, String key, Class<T> tClass) {
        if (activity == null || activity.isFinishing()) {
            return new ArrayList<>();
        }
        SharedPreferences sp = activity.getSharedPreferences("fanqieshumanstring.xml", Context.MODE_PRIVATE);
        List<T> dataList = new ArrayList<>();
        String str = sp.getString(key, null);
        if (str == null || str.isEmpty()) {
            return dataList;
        }
        JsonArray arry = new JsonParser().parse(str).getAsJsonArray();
        for (JsonElement jsonElement : arry) {
            dataList.add(HttpUtils.getGson().fromJson(jsonElement, tClass));
        }
        return dataList;
    }

    /**
     * 存储list数据
     * @param activity
     * @param key
     * @param list
     * @param <T>
     */
    public static <T> void setDataList(Activity activity, String key, List<T> list) {
        if (activity == null || activity.isFinishing() || list == null || list.isEmpty()) {
            return;
        }
        SharedPreferences sp = activity.getSharedPreferences("fanqieshumanstring.xml", Context.MODE_PRIVATE);
        String str = HttpUtils.getGson().toJson(list);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, str);
        e.apply();
    }
}
