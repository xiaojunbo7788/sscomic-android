package com.ssreader.novel.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.ssreader.novel.R;
import com.ssreader.novel.eventbus.AudioProgressRefresh;
import com.ssreader.novel.eventbus.AudioServiceRefresh;
import com.ssreader.novel.eventbus.DownOver;
import com.ssreader.novel.eventbus.FinaShActivity;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.SettingActivity;
import com.ssreader.novel.ui.audio.manager.AudioManager;
import com.ssreader.novel.ui.audio.view.AudioProgressLayout;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.StatusBarUtil;
import com.ssreader.novel.ui.view.screcyclerview.MyContentLinearLayoutManager;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

import static com.ssreader.novel.ui.utils.StatusBarUtil.setStatusTextColor;
import static com.ssreader.novel.ui.utils.StatusBarUtil.setFullScreen;

public abstract class BaseActivity extends FragmentActivity implements BaseInterface {

    protected boolean isImmersionbar;
    protected FragmentActivity activity;
    protected ReaderParams readerParams;
    protected HttpUtils httpUtils;
    protected String http_URL;
    protected Intent formIntent;
    protected boolean SCRecyclerViewRefresh;
    protected int current_page = 1;
    protected int OLD_SIZE;
    protected boolean SCRecyclerViewLoadMore;
    protected Gson gson;
    private SCRecyclerView public_recyclerview_list_SCRecyclerView;
    protected MyContentLinearLayoutManager layoutManager;
    protected boolean USE_EventBus = false;// 标识当前使用通信
    protected boolean FULL_CCREEN;//是否全屏
    protected int TOP_HEIGTH;

    protected boolean USE_PUBLIC_BAR;//是否使用公共bar
    protected RelativeLayout public_sns_topbar_back;
    protected TextView public_sns_topbar_title;
    protected int public_sns_topbar_title_id;
    protected View public_sns_topbar_right;
    protected View public_sns_topbar_layout;
    protected LayoutInflater layoutInflater;
    protected int http_flag;//有多个请求是的标识 默认0  是主请求
    protected long ClickTime;

    // 是否使用
    protected boolean USE_AUDIO_STATUS_LAYOUT = true;
    // 设置听书弹窗
    protected AudioProgressLayout audioProgressLayout;

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

    public BaseActivity() {

    }


    protected HttpUtils.ResponseListener responseListener = new HttpUtils.ResponseListener() {
        @Override
        public void onResponse(String response) {
            initInfo(response);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        // 父类属性
        httpUtils = HttpUtils.getInstance();
        gson = httpUtils.getGson();
        readerParams = new ReaderParams(activity);
        formIntent = getIntent();
        layoutInflater = LayoutInflater.from(activity);
        TOP_HEIGTH = ImmersionBar.with(activity).getStatusBarHeight(activity) + ImageUtil.dp2px(activity, 10);
        // 初始化View注入
        initBaseView();
        if (!isImmersionbar) {
            // 状态栏设置
            if (FULL_CCREEN) {
                setFullScreen(activity, 0);
            } else {
                ImmersionBar.with(activity).statusBarColor(R.color.white).init();
                StatusBarUtil.setFitsSystemWindows(activity, true);
            }
            setStatusTextColor(true, this);
        } else {
            StatusBarUtil.setFitsSystemWindows(activity, true);
            ImmersionBar.with(activity).statusBarDarkFont(false).statusBarColor(R.color.black_7).init();
        }
    }

    private void initBaseView() {
        int view = initContentView();
        if (USE_AUDIO_STATUS_LAYOUT) {
            // 获取听书弹窗
            audioProgressLayout = getAudioProgressLayout();
            // 为布局在套一层相对布局
            View layoutView = LayoutInflater.from(activity).inflate(view, null);
            RelativeLayout relativeLayout = new RelativeLayout(activity);
            relativeLayout.addView(layoutView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            // 添加听书弹窗
            relativeLayout.addView(audioProgressLayout,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            // 设置位置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) audioProgressLayout.getLayoutParams();
            params.topMargin = ScreenSizeUtils.getScreenHeight(this) - ImageUtil.dp2px(this, 100);
            audioProgressLayout.setLayoutParams(params);
            setContentView(relativeLayout);
        } else {
            setContentView(view);
        }
        if (USE_PUBLIC_BAR) {
            public_sns_topbar_layout = findViewById(R.id.public_sns_topbar_layout);
            public_sns_topbar_right = findViewById(R.id.public_sns_topbar_right);
            public_sns_topbar_back = findViewById(R.id.public_sns_topbar_back);
            public_sns_topbar_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            public_sns_topbar_title = findViewById(R.id.public_sns_topbar_title);
            if (public_sns_topbar_title_id != 0) {
                public_sns_topbar_title.setText(LanguageUtil.getString(activity, public_sns_topbar_title_id));
            }
        }
        ButterKnife.bind(this);
        initView();
        initData();
        if (true) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(FinaShActivity refreshMine) {
        if (!refreshMine.NofinishSetActivity || activity instanceof SettingActivity) {
            finish();
        }
    }

    /**
     * 动态设置听书弹窗
     *
     * @return
     */
    protected AudioProgressLayout getAudioProgressLayout() {
        if (audioProgressLayout == null) {
            audioProgressLayout = new AudioProgressLayout(this);
        }
        audioProgressLayout.setLayoutClickListener(layoutClickListener);
        audioProgressLayout.setVisibility(View.GONE);
        return audioProgressLayout;
    }

    /**
     * 听书弹窗事件监听
     */
    protected AudioProgressLayout.OnProgressLayoutClickListener layoutClickListener = new AudioProgressLayout.OnProgressLayoutClickListener() {
        @Override
        public void onIntoAudio() {
            AudioManager.getInstance(activity).IntoAudioActivity(activity, false);
        }

        @Override
        public void onPlayer() {
            AudioManager audioManager = AudioManager.getInstance(activity);
            // 设置听书
            if (!audioManager.isIsPlayerError() && !audioManager.isPreview()) {
                // 只有播放不失败时才能点击
                if (AudioManager.isSound) {
                    if (audioManager.currentPlayAudio != null && audioManager.audioCurrentChapter != null && audioManager.audioChapterList != null) {
                        audioManager.setAudioPlayerStatus((audioManager.isPlayer != 1), audioManager.currentPlayAudio,
                                audioManager.audioCurrentChapter.getChapter_id(), null);
                    }
                } else {
                    if (audioManager.currentPlayBook != null && audioManager.bookCurrentChapter != null && audioManager.bookChapterList != null) {
                        audioManager.setBookPlayerStatus((audioManager.isPlayer != 1), audioManager.currentPlayBook,
                                audioManager.bookCurrentChapter.getChapter_id(), null);
                    }
                }
            } else {
                audioManager.IntoAudioActivity(activity, true);
            }
            if (audioManager.isPlayer != 1 && audioProgressLayout.isPlayer()) {
                audioProgressLayout.setPlayer(false);
            }
        }

        @Override
        public void onCancel() {
            audioProgressLayout.setVisibility(View.GONE);
            EventBus.getDefault().post(new AudioServiceRefresh(false, true));
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgress(AudioProgressRefresh refresh) {
        if (audioProgressLayout != null && audioProgressLayout.getVisibility() == View.VISIBLE) {
            if (refresh.isPlayer) {
                audioProgressLayout.setPlayer(true);
                if (refresh.isSound) {
                    audioProgressLayout.setProgress(refresh.progress * 100 / refresh.duration);
                } else {
                    audioProgressLayout.setProgress(0);
                }
            } else {
                audioProgressLayout.setPlayer(false);
                if (refresh.isSound) {
                    if (refresh.progress == 100) {
                        audioProgressLayout.setProgress(0);
                    }
                } else {
                    audioProgressLayout.setProgress(0);
                }
            }
        }
    }

    //EventBus.getDefault().post(new RefreshMine(true));
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(DownOver downOver) {

        // MyToash.Log("applicationContext", activity.getClass().getName()+"");

        try {
            if (BWNApplication.applicationContext.getActivity() == activity) {

                MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_downcomplete));
            }
        } catch (Exception e) {
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        BWNApplication.applicationContext.setActivity(activity);
        ShareUitls.putBoolean(activity, "HOME", false);
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
        if (audioProgressLayout != null && AudioManager.getInstance(activity) != null) {
            if (AudioManager.getInstance(activity).isShowAudioStatusLayout()) {
                audioProgressLayout.setVisibility(View.VISIBLE);
                if (AudioManager.getInstance(activity).isPlayer == 1) {
                    audioProgressLayout.setPlayer(true);
                } else {
                    audioProgressLayout.setPlayer(false);
                }
                if (!TextUtils.isEmpty(AudioManager.getInstance(activity).bookCover)) {
                    audioProgressLayout.setImageUrl(AudioManager.getInstance(activity).bookCover);
                }
            } else {
                audioProgressLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (true) {
            EventBus.getDefault().unregister(this);
        }
        activity = null;
        http_URL = null;
        gson = null;
        readerParams.destroy();
    }
}
