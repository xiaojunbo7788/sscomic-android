package com.ssreader.novel.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.view.screcyclerview.MyContentLinearLayoutManager;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.ScreenSizeUtils;

public abstract class BasePopupWindow extends PopupWindow implements BaseInterface {

    protected Activity activity;
    protected ReaderParams readerParams;
    protected HttpUtils httpUtils;
    protected boolean SCRecyclerViewRefresh;
    protected int current_page = 1;
    protected boolean SCRecyclerViewLoadMore;
    protected Gson gson;
    private SCRecyclerView public_recyclerview_list_SCRecyclerView;
    protected View view;
    protected int width, height;

    protected SCOnItemClickListener scOnItemClickListener = new SCOnItemClickListener() {
        @Override
        public void OnItemClickListener(int flag, int position, Object o) {
            OnItemClickListener(flag, position, o);
        }

        @Override
        public void OnItemLongClickListener(int flag, int position, Object o) {
            OnItemLongClickListener(flag, position, o);
        }
    };

    protected void OnItemClickListener(int flag, int position, Object o) {
    }

    protected void OnItemLongClickListener(int flag, int position, Object o) {
    }

    protected void initSCRecyclerView(SCRecyclerView public_recyclerview_list_SCRecyclerView, int orientation, int GridLayoutManager_spanCount) {
        this.public_recyclerview_list_SCRecyclerView = public_recyclerview_list_SCRecyclerView;
        if (GridLayoutManager_spanCount == 0) {
            MyContentLinearLayoutManager layoutManager = new MyContentLinearLayoutManager(activity);
            layoutManager.setOrientation(orientation);
            public_recyclerview_list_SCRecyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, GridLayoutManager_spanCount);
            gridLayoutManager.setOrientation(orientation);
            public_recyclerview_list_SCRecyclerView.setLayoutManager(gridLayoutManager);
        }
        public_recyclerview_list_SCRecyclerView.setLoadingListener(loadingListener);

    }

    protected HttpUtils.ResponseListener responseListener = new HttpUtils.ResponseListener() {
        @Override
        public void onResponse(String response) {
            initInfo(response);
            ++current_page;
            if (SCRecyclerViewRefresh && public_recyclerview_list_SCRecyclerView != null) {
                public_recyclerview_list_SCRecyclerView.refreshComplete();
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
            SCRecyclerViewLoadMore = true;
            initData();
        }
    };

    @Override
    public void errorInfo(String json) {

    }

    public BasePopupWindow(int width, int height) {
        super(width, height);
        this.width = width;
        this.height = height;
    }

    public BasePopupWindow() {
        super();
    }

    protected void onCreate(Activity activity) {
        gson = HttpUtils.getGson();
        this.activity = activity;
        readerParams = new ReaderParams(activity);
        httpUtils = HttpUtils.getInstance();
        view = LayoutInflater.from(activity).inflate(initContentView(), null);
        //消失监听听
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(1f, activity);
            }
        });
        setContentView(view);
        initView();
        initData();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(1f, activity);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(0.4f, activity);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(0.4f, activity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(0.4f, activity);
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     */
    protected static int[] calculatePopWindowPos(Activity activity, final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenSizeUtils.getInstance(activity).getScreenHeight();
        final int screenWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(1f, activity);
    }
}
