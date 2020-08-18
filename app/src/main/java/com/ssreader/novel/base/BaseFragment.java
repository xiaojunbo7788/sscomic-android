package com.ssreader.novel.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.ssreader.novel.eventbus.StoreScrollStatus;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.MyContentLinearLayoutManager;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

import static com.ssreader.novel.constant.Constant.GETNotchHeight;

public abstract class BaseFragment<T> extends Fragment implements  BaseInterface{

    protected ReaderParams readerParams;
    protected HttpUtils httpUtils;
    protected String http_URL;
    protected FragmentActivity activity;
    protected Gson gson;
    protected MyContentLinearLayoutManager layoutManager;
    protected boolean SCRecyclerViewRefresh;
    protected boolean SCRecyclerViewLoadMore;
    protected int current_page = 1;
    protected int total_page;
    private SCRecyclerView public_recyclerview_list_SCRecyclerView;
    protected boolean USE_EventBus;//预留参数 ==1 标识当前使用通信
    public boolean isPrepared;
    protected int http_flag;//有多个请求是的标识 默认0  是主请求
    protected int NotchHeight;
    protected long ClickTime;
    protected SCOnItemClickListener scOnItemClickListener = new SCOnItemClickListener<T>() {
        @Override
        public void OnItemClickListener(int flag, int position, T o) {
            setOnItemClickListener(flag, position, o);
        }

        @Override
        public void OnItemLongClickListener(int flag, int position, T o) {
            setOnItemLongClickListener(flag, position, o);
        }
    };

    protected void setOnItemClickListener(int flag, int position, T o) {

    }

    protected void setOnItemLongClickListener(int flag, int position, T o) {

    }

    protected void initSCRecyclerView(SCRecyclerView public_recyclerview_list_SCRecyclerView, int orientation, int GridLayoutManager_spanCount) {
        this.public_recyclerview_list_SCRecyclerView = public_recyclerview_list_SCRecyclerView;
        if (GridLayoutManager_spanCount == 0) {
            layoutManager = new MyContentLinearLayoutManager(activity);
            layoutManager.setOrientation(orientation);
            public_recyclerview_list_SCRecyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, GridLayoutManager_spanCount);
            gridLayoutManager.setOrientation(orientation);
            public_recyclerview_list_SCRecyclerView.setLayoutManager(gridLayoutManager);
        }
        public_recyclerview_list_SCRecyclerView.setLoadingListener(loadingListener);
    }

    public BaseFragment() {

    }

    protected HttpUtils.ResponseListener responseListener = new HttpUtils.ResponseListener() {
        @Override
        public void onResponse(String response) {
            initInfo(response);
            if (SCRecyclerViewRefresh && public_recyclerview_list_SCRecyclerView != null) {
                public_recyclerview_list_SCRecyclerView.refreshComplete();
                if (public_recyclerview_list_SCRecyclerView.storeScrollStatusInterface) {
                    MyToash.setDelayedHandle(180, new MyToash.DelayedHandle() {
                        @Override
                        public void handle() {
                            EventBus.getDefault().post(new StoreScrollStatus(public_recyclerview_list_SCRecyclerView.productType, false, 0));
                        }
                    });
                }
                SCRecyclerViewRefresh = false;
            } else if (SCRecyclerViewLoadMore && public_recyclerview_list_SCRecyclerView != null) {
                public_recyclerview_list_SCRecyclerView.loadMoreComplete();
                SCRecyclerViewLoadMore = false;
            }
        }

        @Override
        public void onErrorResponse(String ex) {
            errorInfo(ex);
            if (SCRecyclerViewRefresh && public_recyclerview_list_SCRecyclerView != null) {
                public_recyclerview_list_SCRecyclerView.refreshComplete();
                if (public_recyclerview_list_SCRecyclerView.storeScrollStatusInterface) {
                    MyToash.setDelayedHandle(180, new MyToash.DelayedHandle() {
                        @Override
                        public void handle() {
                            EventBus.getDefault().post(new StoreScrollStatus(public_recyclerview_list_SCRecyclerView.productType, false, 0));
                        }
                    });
                }
                SCRecyclerViewRefresh = false;
            } else if (SCRecyclerViewLoadMore && public_recyclerview_list_SCRecyclerView != null) {
                public_recyclerview_list_SCRecyclerView.loadMoreComplete();
                SCRecyclerViewLoadMore = false;
            }
        }
    };
    protected SCRecyclerView.LoadingListener loadingListener = new SCRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            SCRecyclerViewRefresh = true;
            current_page = 1;
            initData();
        }

        @Override
        public void onLoadMore() {
            ++current_page;
            SCRecyclerViewLoadMore = true;
            initData();
        }
    };
    @Override
    public void errorInfo(String json) {
        if (SCRecyclerViewRefresh && public_recyclerview_list_SCRecyclerView != null) {
            public_recyclerview_list_SCRecyclerView.refreshComplete();
            SCRecyclerViewRefresh = false;
        } else if (SCRecyclerViewLoadMore && public_recyclerview_list_SCRecyclerView != null) {
            public_recyclerview_list_SCRecyclerView.loadMoreComplete();
            SCRecyclerViewLoadMore = false;
        }

    }

    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(initContentView(), container, false);
            ButterKnife.bind(this, mView);
        }
        isPrepared = true;
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        NotchHeight = GETNotchHeight(activity);
        readerParams = new ReaderParams(activity);
        httpUtils = HttpUtils.getInstance();
        gson = httpUtils.getGson();
        current_page = 1;
        if (USE_EventBus && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // 配置setUserVisibleHint（）方法
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
    }

    protected void onVisible() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        //可见的并且是初始化之后才加载
        if (isPrepared && isVisibleToUser) {
            onVisible();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        readerParams.destroy();
        if (USE_EventBus && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
