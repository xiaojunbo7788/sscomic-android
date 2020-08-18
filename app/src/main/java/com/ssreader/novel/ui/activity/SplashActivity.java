package com.ssreader.novel.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.base.BaseInterface;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.FinaShActivity;
import com.ssreader.novel.model.AppUpdate;
import com.ssreader.novel.model.PublicIntent;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.MainHttpTask;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.bwad.AdReadCachePool;
import com.ssreader.novel.ui.bwad.TTAdManagerHolder;
import com.ssreader.novel.ui.dialog.GetDialog;
import com.ssreader.novel.ui.localshell.localapp.LoaclMainActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UpdateApp;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

import static com.ssreader.novel.constant.Constant.USE_LANAGUAGE;

/**
 * 软件的门面界面
 * 开屏广告界面
 */
public class SplashActivity extends FragmentActivity implements BaseInterface {

    @BindView(R.id.activity_splash_im)
    public ImageView activity_splash_im;
    @BindView(R.id.activity_home_viewpager_sex_next)
    public TextView activity_home_viewpager_sex_next;
    @BindView(R.id.activity_splash_layout)
    public RelativeLayout activity_splash_layout;

    private boolean isfirst, HOME_AD;
    private String splashactivity_skip;
    private UpdateApp updateApp;
    private PublicIntent startpage;
    private FragmentActivity activity;
    private int time = 6;
    // 界面是否已经跳转
    private boolean isJump = false;
    // 广告
    private TTAdNative mTTAdNative;
    private final int AD_TIME_OUT = 3000;

    @OnClick({R.id.activity_home_viewpager_sex_next, R.id.activity_splash_im})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_home_viewpager_sex_next:
                if (time != 0) {
                    gotoMainActivity();
                }
                break;
            case R.id.activity_splash_im:
                if (activity != null && !activity.isFinishing() && startpage != null &&
                        startpage.skip_type != 5 && startpage.skip_type > 0 && !TextUtils.isEmpty(startpage.content)) {
                    handler.removeMessages(0);
                    startpage.intentTo(activity);
                }
                break;
        }
    }

    @Override
    public int initContentView() {
        activity = this;
        BWNApplication.applicationContext.setActivity(activity);
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void initView() {
        updateApp = new UpdateApp(activity);
        splashactivity_skip = LanguageUtil.getString(activity, R.string.splashactivity_skip);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) activity_home_viewpager_sex_next.getLayoutParams();
        params.topMargin = ImmersionBar.with(activity).getStatusBarHeight(activity) + ImageUtil.dp2px(activity, 10);
        activity_home_viewpager_sex_next.setLayoutParams(params);
        activity_home_viewpager_sex_next.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 30), "#80000000"));
        if (getIntent() != null) {
            HOME_AD = getIntent().getBooleanExtra("HOME_AD", false);
        }
        if (!HOME_AD) {
            if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
            isfirst = ShareUitls.getBoolean(activity, "isfirst", true);
            if (!isfirst && InternetUtils.internet(activity)) {
                updateApp.getRequestData(true, null);
            }
            // 是否展示弹窗
            if (ShareUitls.getBoolean(activity, "PraviteDialog", true) && !USE_LANAGUAGE) {
                activity_home_viewpager_sex_next.post(new Runnable() {
                    @Override
                    public void run() {
                        GetDialog.PraviteDialog(activity, new GetDialog.OkCommit() {
                            @Override
                            public void success() {
                                ShareUitls.putBoolean(activity, "PraviteDialog", false);
                                // 获取权限
                                requestReadPhoneState();
                            }
                        });
                    }
                });
            } else {
                // 获取权限
                requestReadPhoneState();
            }
        } else {
            String response = ShareUitls.getString(activity, "Update", "");
            if (!TextUtils.isEmpty(response)) {
                AppUpdate dataBean = HttpUtils.getInstance().getGson().fromJson(response, AppUpdate.class);
                showSplashAd(dataBean);
            } else {
               gotoMainActivity();
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Override
    public void errorInfo(String json) {

    }

    /**
     * 申请权限
     */
    private void requestReadPhoneState() {
        if (PermissionsUtil.hasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {
            FirstStartApp();
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permission) {
                            // 请求数据
                            FirstStartApp();
                        }

                        @Override
                        public void permissionDenied(@NonNull String[] permission) {//@Nullable String title,  @Nullable String content,  @Nullable String cancel,  @Nullable String ensure
                            finish();
                        }
                    }, new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    true, new PermissionsUtil.TipInfo(
                            LanguageUtil.getString(activity, R.string.app_opnen_permission),
                            String.format(LanguageUtil.getString(activity, R.string.app_opnen_permission_need), LanguageUtil.getString(activity, R.string.app_name)),
                            LanguageUtil.getString(activity, R.string.app_cancle),
                            LanguageUtil.getString(activity, R.string.app_set)));
        }
    }

    private void FirstStartApp() {
        if (isfirst) {
            if (InternetUtils.internet(activity)) {
                updateApp.getRequestData(true, new UpdateApp.UpdateAppInterface() {
                    @Override
                    public void Next(String str, AppUpdate dataBean) {
                        if (dataBean.system_setting != null && dataBean.system_setting.getCheck_status() == 1) {
                            Intent intent = new Intent();
                            intent.setClass(activity, LoaclMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else
                            getCheckSetting(dataBean);
                    }
                });
            } else {
                Intent intent = new Intent();
                intent.setClass(activity, LoaclMainActivity.class);
                startActivity(intent);
            }
        } else {
            getCheckSetting(null);
        }
    }

    /**
     * 请求接口
     */
    private void getCheckSetting(AppUpdate dataBean) {
        if (dataBean == null) {
            String response = ShareUitls.getString(activity, "Update", "");
            if (!TextUtils.isEmpty(response)) {
                dataBean = HttpUtils.getGson().fromJson(response, AppUpdate.class);
            }
        }
        // 签到、自动登录、广告
        if (InternetUtils.internet(activity)) {
            MainHttpTask.getInstance().InitHttpData(activity);
            if (UserUtils.isLogin(activity)) {
                HttpUtils.getInstance().sendRequestRequestParams(activity, Api.sIgninhttp, new ReaderParams(activity).generateParamsJson(), new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        ShareUitls.putString(activity, "sign_pop", response);
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                });
            } else {
                new LoginUtils(activity).deviceUserLogin(true, null);
            }
        }
        showSplashAd(dataBean);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                --time;
                activity_home_viewpager_sex_next.setText((time == 0 ? "" : time + " ") + splashactivity_skip);
                if (time != 0) {
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    gotoMainActivity();
                }
            } else if (msg.what == 3) {
                gotoMainActivity();
            } else {
                gotoMainActivity();
            }
        }
    };

    private void gotoMainActivity() {
        if (isJump) {
            return;
        }
        isJump = true;
        if (HOME_AD) {
            long newTime = System.currentTimeMillis();
            long Home_AD_lastTime = ShareUitls.getLong(activity, "Home_AD_lastTime", newTime);
            if (newTime - Home_AD_lastTime > 300000) {
                EventBus.getDefault().post(new FinaShActivity());
                Intent intent = new Intent();
                intent.setClass(activity, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_alpha_open, R.anim.activity_stay);
            }
            finish();
        } else {
            if (activity != null) {
                Intent intent = new Intent();
                if (InternetUtils.internett(activity) && isfirst) {
                    ShareUitls.putBoolean(activity, "isfirst", false);
                    intent.setClass(activity, FirstStartActivity.class);
                } else {
                    intent.setClass(activity, MainActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.activity_alpha_open, R.anim.activity_stay);
            }
        }
        try {
            if (handler != null) {
                handler.removeMessages(0);
                handler = null;
            }
        } catch (Exception e) {
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(FinaShActivity refreshMine) {
        finish();
    }

    private void showSplashAd(AppUpdate dataBean) {
        if (dataBean != null && InternetUtils.internet(activity)) {
            startpage = dataBean.start_page;
            if (startpage != null && (!TextUtils.isEmpty(startpage.image) || !TextUtils.isEmpty(startpage.ad_android_key))) {
                ShareUitls.putBoolean(activity, "USE_AD_HOME", true);
                if (startpage.skip_type != 5 && startpage.skip_type != 6 && startpage.skip_type != 7) {
                    Glide.with(activity).load(startpage.image).apply(new RequestOptions()
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)).into(activity_splash_im);
                    activity_home_viewpager_sex_next.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(0, 0);
                } else {
                    activity_home_viewpager_sex_next.setVisibility(View.GONE);
                    // 穿山甲广告
                    if (!TextUtils.isEmpty(startpage.ad_android_key) && InternetUtils.internet(activity)) {
                        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity);
                        loadSplashAd(startpage.ad_android_key);
                    } else {
                        gotoMainActivity();
                    }
                }
            } else {
                ShareUitls.putBoolean(activity, "USE_AD_HOME", false);
                gotoMainActivity();
            }
        } else {
            gotoMainActivity();
        }
    }

    /**
     * 加载开屏广告
     *
     * @param adKey
     */
    private void loadSplashAd(String adKey) {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(adKey)
                .setAdCount(1)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(ScreenSizeUtils.getInstance(activity).getScreenWidth(), ScreenSizeUtils.getInstance(activity).getScreenHeight())
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                gotoMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                gotoMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                if (ad == null) {
                    gotoMainActivity();
                    return;
                }
                AdReadCachePool.getInstance().ttSplashAd = ad;
                // 获取SplashView
                View view = ad.getSplashView();
                if (view != null) {
                    activity_splash_layout.removeAllViews();
                    activity_splash_layout.addView(view);
                } else {
                    gotoMainActivity();
                    return;
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {

                    }

                    @Override
                    public void onAdShow(View view, int type) {

                    }

                    @Override
                    public void onAdSkip() {
                        gotoMainActivity();
                    }

                    @Override
                    public void onAdTimeOver() {
                        if (activity == null || activity.isFinishing()) {
                            return;
                        }
                        gotoMainActivity();
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }
            }
        }, AD_TIME_OUT);
    }

    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().unregister(this);
        if (HOME_AD) {
            //关闭窗体动画显示
            this.overridePendingTransition(0, R.anim.activity_alpha_close);
        }
    }
}
