package com.ssreader.novel.net;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装一层，根据key value转化json
 */
public class ReaderNameValuePair {

    private List<Map<String, String>> paramsList;
    private HashMap<String, String> map;
    private Gson gson;

    public void destroy() {
        map.clear();
        paramsList.clear();
        paramsList = null;
        map = null;
        gson = null;
    }

    public ReaderNameValuePair() {
        paramsList = new ArrayList<>();
        map = new HashMap<>();
        gson = HttpUtils.getGson();
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void remove(String key, String value) {
        map.remove(key);
    }

    public String toJson() {
        paramsList.clear();
        paramsList.add(map);
        String json = gson.toJson(paramsList);
        return json.substring(1, json.length() - 1);
    }
}
