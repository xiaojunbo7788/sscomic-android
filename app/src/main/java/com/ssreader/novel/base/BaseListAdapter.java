package com.ssreader.novel.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;

import java.util.List;

/**
 * adapter基类
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected Activity activity;
    protected List<T> mList;
    protected int size;
    protected LayoutInflater layoutInflater;
    protected SCOnItemClickListener scOnItemClickListener;
    public int NoLinePosition;

    public BaseListAdapter(Activity activity, List<T> list) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        mList = list;
        size = list.size();
    }

    public BaseListAdapter(Activity activity, List<T> list, SCOnItemClickListener scOnItemClickListener) {
        this(activity, list);
        this.scOnItemClickListener = scOnItemClickListener;
    }

    @Override
    public void notifyDataSetChanged() {
        size = mList.size();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getOwnView(position, mList.get(position), LayoutInflater.from(activity).inflate(getViewByRes(), null), parent);
    }

    public abstract int getViewByRes();

    public abstract View getOwnView(int position, T been, View convertView, ViewGroup parent);
}
