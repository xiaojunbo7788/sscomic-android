package com.ssreader.novel.base;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ssreader.novel.R;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.view.screcyclerview.MyContentLinearLayoutManager;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

public abstract class BaseDialogFragment extends DialogFragment implements BaseInterface {

    protected boolean isBottom = true;
    protected Activity activity;
    protected HttpUtils httpUtils;
    protected ReaderParams readerParams;
    protected boolean USE_EventBus = false;
    protected SCRecyclerView scRecyclerView;
    protected boolean SCRecyclerViewRefresh;
    protected boolean SCRecyclerViewLoadMore;

    protected BaseDialogFragment() {

    }

    protected BaseDialogFragment(boolean isBottom) {
        this.isBottom = isBottom;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 窗口底部弹出
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (isBottom) {
            lp.gravity = Gravity.BOTTOM;
        } else {
            lp.gravity = Gravity.CENTER;
        }
        window.setAttributes(lp);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isBottom) {
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.BottomDialogFragment);
        } else {
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TranslateDialogFragment);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initContentView(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (activity != null && !activity.isFinishing()) {
            httpUtils = HttpUtils.getInstance();
            readerParams = new ReaderParams(activity);
        }
        if (USE_EventBus && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();
    }

    @Override
    public void errorInfo(String json) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        readerParams.destroy();
        if (USE_EventBus && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected void initSCRecyclerView(SCRecyclerView scRecyclerView, int orientation, int GridLayoutManager_spanCount) {
        this.scRecyclerView = scRecyclerView;
        if (GridLayoutManager_spanCount == 0) {
            MyContentLinearLayoutManager layoutManager = new MyContentLinearLayoutManager(activity);
            layoutManager.setOrientation(orientation);
            scRecyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, GridLayoutManager_spanCount);
            gridLayoutManager.setOrientation(orientation);
            scRecyclerView.setLayoutManager(gridLayoutManager);
        }
        scRecyclerView.setLoadingListener(loadingListener);
    }

    protected SCRecyclerView.LoadingListener loadingListener = new SCRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            SCRecyclerViewRefresh = true;
            initData();
        }

        @Override
        public void onLoadMore() {
            SCRecyclerViewLoadMore = true;
            initData();
        }
    };

    protected HttpUtils.ResponseListener responseListener = new HttpUtils.ResponseListener() {
        @Override
        public void onResponse(String response) {
            initInfo(response);
            if (SCRecyclerViewRefresh && scRecyclerView != null) {
                scRecyclerView.refreshComplete();
                SCRecyclerViewRefresh = false;
            } else if (SCRecyclerViewLoadMore && scRecyclerView != null) {
                scRecyclerView.loadMoreComplete();
                SCRecyclerViewLoadMore = false;
            }
        }

        @Override
        public void onErrorResponse(String ex) {
            errorInfo(ex);
            if (SCRecyclerViewRefresh && scRecyclerView != null) {
                scRecyclerView.refreshComplete();
                SCRecyclerViewRefresh = false;
            } else if (SCRecyclerViewLoadMore && scRecyclerView != null) {
                scRecyclerView.loadMoreComplete();
                SCRecyclerViewLoadMore = false;
            }
        }
    };
}
