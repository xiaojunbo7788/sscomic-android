package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ssreader.novel.dialog.NoticeAlertDialog;
import com.umeng.socialize.UMShareAPI;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.eventbus.AdStatusRefresh;
import com.ssreader.novel.ui.bwad.AdReadCachePool;
import com.ssreader.novel.ui.dialog.UpAppDialogFragment;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.cache.BitmapCache;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.ToStore;
import com.ssreader.novel.model.AppUpdate;
import com.ssreader.novel.net.MainHttpTask;
import com.ssreader.novel.ui.dialog.TaskCenterSignPopupWindow;
import com.ssreader.novel.ui.fragment.DiscoverFragment;
import com.ssreader.novel.ui.fragment.MineFragment;
import com.ssreader.novel.ui.fragment.Public_main_fragment;
import com.ssreader.novel.ui.fragment.ShelfFragment;
import com.ssreader.novel.ui.audio.manager.AudioManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MainFragmentTabUtils;
import com.ssreader.novel.ui.view.CustomScrollViewPager;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main_FrameLayout)
    public CustomScrollViewPager activity_main_FrameLayout;
    @BindView(R.id.activity_main_RadioGroup)
    public RadioGroup activity_main_RadioGroup;
    @BindView(R.id.activity_main_Bookshelf)
    public RadioButton activity_main_Bookshelf;
    @BindView(R.id.activity_main_Bookstore)
    public RadioButton activity_main_Bookstore;
    @BindView(R.id.activity_main_discovery)
    public RadioButton activity_main_discovery;
    @BindView(R.id.activity_main_mine)
    public RadioButton activity_main_mine;
    private List<Fragment> fragmentArrayList;

    @Override
    public int initContentView() {
        FULL_CCREEN = true;
        USE_EventBus = true;
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        BWNApplication.applicationContext.setMainActivityStartUp(true);
        activity = this;
        // initDrawable();
        fragmentArrayList = new ArrayList<>();
        // 书架
        Public_main_fragment public_main_fragment1 = new Public_main_fragment(1);
        fragmentArrayList.add(public_main_fragment1);
        // 书城
        Public_main_fragment public_main_fragment2 = new Public_main_fragment(2);
        fragmentArrayList.add(public_main_fragment2);
        // 娱乐
        Public_main_fragment public_main_fragment4 = new Public_main_fragment(4);
        fragmentArrayList.add(public_main_fragment4);
        // 发现页
        Public_main_fragment public_main_fragment3 = new Public_main_fragment(3);
        fragmentArrayList.add(public_main_fragment3);
        // 我的界面
        fragmentArrayList.add(new MineFragment());
        new MainFragmentTabUtils(0, this, fragmentArrayList, activity_main_FrameLayout, activity_main_RadioGroup);
        if (!InternetUtils.internet(activity)) {
            MyToash.ToashError(activity, R.string.splashactivity_nonet);
        }
    }

    @Override
    public void initData() {
        String Update = ShareUitls.getString(activity, "Update", "");
        if (!TextUtils.isEmpty(Update)) {
            AppUpdate dataBean = gson.fromJson(Update, AppUpdate.class);

            if (dataBean.getSystem_notice() != null && dataBean.getSystem_notice().length() > 0) {
                //弹出公告
                activity_main_RadioGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        NoticeAlertDialog dialog = new NoticeAlertDialog.Builder(activity).setContent(dataBean.getSystem_notice()).create();
                        dialog.show();
                    }
                });
            }
            if (dataBean.version_update.getStatus() != 0) {
                activity_main_RadioGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        UpAppDialogFragment upAppDialogFragment = new UpAppDialogFragment(activity, dataBean.version_update);
                        upAppDialogFragment.show(getSupportFragmentManager(), "UpAppDialogFragment");
                    }
                });
            } else {
                String sign = ShareUitls.getString(activity, "sign_pop", "");
                if (!TextUtils.isEmpty(sign)) {
                    activity_main_RadioGroup.post(new Runnable() {
                        @Override
                        public void run() {
                            int width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 80);
                            int height = (width - ImageUtil.dp2px(activity, 140)) / 3 * 4 / 3 + ImageUtil.dp2px(activity, 200);
                            new TaskCenterSignPopupWindow(activity, false, sign, width, height);
                        }
                    });
                }
            }
        }
        AdReadCachePool.getInstance().putBaseAd(activity);
    }

    @Override
    public void initInfo(String json) {

    }

    /**
     * 底部跳转
     *
     * @param toStore
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ToStore(ToStore toStore) {
        switch (toStore.PRODUCT) {
            case 0:
            case 1:
            case 2:
                activity_main_Bookstore.setChecked(true);
                break;
            case -1:
                if (toStore.SHELF_DELETE_OPEN) {
                    activity_main_RadioGroup.setVisibility(View.GONE);
                } else {
                    activity_main_RadioGroup.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 刷新广告
     *
     * @param adStatusRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(AdStatusRefresh adStatusRefresh) {
        AdReadCachePool.getInstance().putBaseAd(activity);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        try {
            MainHttpTask.getInstance().clean();
            AudioManager.getInstance(activity).onCancel(true);
            BitmapCache.getInstance().clearCache();
            super.finish();
            activity = null;
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }
}
