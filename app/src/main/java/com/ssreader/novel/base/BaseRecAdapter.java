package com.ssreader.novel.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;

import java.util.List;

public abstract class BaseRecAdapter<T, K extends BaseRecViewHolder> extends RecyclerView.Adapter<K> {

    public List<T> list;
    public Activity activity;
    private int addSzie;
    public SCOnItemClickListener BasescOnItemClickListener;
    private ViewGroup prent;
    public int NoLinePosition = -1;

    public BaseRecAdapter(List<T> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    public BaseRecAdapter(List<T> list, Activity activity, SCOnItemClickListener scOnItemClickListener) {
        this.list = list;
        this.activity = activity;
        this.BasescOnItemClickListener = scOnItemClickListener;
    }

    public BaseRecAdapter(List<T> list, Activity activity, int addSzie, SCOnItemClickListener scOnItemClickListener) {
        this.list = list;
        this.activity = activity;
        this.addSzie = addSzie;
        this.BasescOnItemClickListener = scOnItemClickListener;
    }

    public BaseRecAdapter(List<T> list, Activity activity, int addSzie) {
        this.list = list;
        this.activity = activity;
        this.addSzie = addSzie;

    }

    public BaseRecAdapter() {

    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        this.prent = parent;
        K holder = onCreateHolder();
        //绑定listener
        return holder;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        T been;
        if (addSzie != 0 && position >= list.size()) {
            been = null;
        } else {
            been = list.get(position);
        }
        onHolder(holder, been, position);
        bindListener(holder, position);

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size() + addSzie;
    }


    /**
     * 填充数据
     */
    public abstract void onHolder(K holder, T bean, int position);


    public abstract K onCreateHolder();


    /**
     * 通过资源res获得view
     */
    public View getViewByRes(int res) {
        return LayoutInflater.from(activity).inflate(res, null);
    }

    /**
     * 通过资源res获得view
     *
     * @param res
     * @return
     */
    public View getViewByRes(int res, boolean usePrent) {
        if (prent != null) {
            MyToash.Log("getViewByRes", "111");
            return LayoutInflater.from(activity).inflate(res, prent, false);
        }
        return getViewByRes(res);
    }

    /**
     * 绑定事件
     */
    private void bindListener(final K holder, int position) {
        if (BasescOnItemClickListener != null) {
            if (holder == null) {
                return;
            }
            View itemView = holder.itemView;
            if (itemView == null) {
                return;
            }
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BasescOnItemClickListener.OnItemClickListener(0, position, list.get(position));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BasescOnItemClickListener.OnItemLongClickListener(0, position, list.get(position));
                    return true;
                }
            });
        }
    }

    public List<T> getData() {
        return list;
    }
}
