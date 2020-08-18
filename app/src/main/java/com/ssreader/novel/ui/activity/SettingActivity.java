package com.ssreader.novel.ui.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.suke.widget.SwitchButton;
import com.umeng.socialize.UMShareAPI;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.AppUpdate;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.dialog.BottomMenuDialog;
import com.ssreader.novel.ui.dialog.PublicDialog;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.cache.ClearCacheManager;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UpdateApp;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.USE_LANAGUAGE;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.activity_settings_auto)
    SwitchButton mActivitySettingsAuto;
    @BindView(R.id.activity_settings_switch_container)
    LinearLayout mActivitySettingsSwitchContainer;
    @BindView(R.id.activity_settings_clear_cache)
    RelativeLayout mActivitySettingsClearCache;
    @BindView(R.id.activity_settings_support)
    RelativeLayout mActivitySettingsSupport;
    @BindView(R.id.activity_settings_language)
    RelativeLayout mActivitySettingsLanguage;
    @BindView(R.id.activity_settings_about)
    RelativeLayout mActivitySettingsAbout;
    @BindView(R.id.activity_settings_logout)
    TextView mActivitySettingsLogout;
    private boolean isPressed = false;
    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        return R.layout.activity_setting;
    }

    @OnClick(value = {R.id.activity_settings_clear_cache,
            R.id.activity_settings_switch_notify,
            R.id.activity_settings_support,
            R.id.activity_settings_language,
            R.id.activity_settings_about,
            R.id.activity_settings_logout,
            R.id.activity_settings_share})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.activity_settings_clear_cache:
                PublicDialog.publicDialogVoid(activity, "",
                        LanguageUtil.getString(activity, R.string.SettingsActivity_cleanhuancun),
                        LanguageUtil.getString(activity, R.string.app_cancle),
                        LanguageUtil.getString(activity, R.string.app_confirm), new PublicDialog.OnPublicListener() {
                            @Override
                            public void onClickConfirm(boolean isConfirm) {
                                if (isConfirm) {
                                    ClearCacheManager.clearAllCache(activity);
                                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.SettingsActivity_clearSeccess));
                                }
                            }
                        });
                break;
            case R.id.activity_settings_switch_notify:
                startActivity(new Intent(this, NotifycationManagerActivity.class));
                break;
            case R.id.activity_settings_support:
                support(this);
                break;
            case R.id.activity_settings_about:
                break;
            case R.id.activity_settings_share:
               // MyShare.ShareAPP(activity);
                break;
            case R.id.activity_settings_logout:
                //所有登录用户置为none用户
                exitUser(this);
                finish();
                break;
            case R.id.activity_settings_language:
                //所有登录用户置为none用户
                ChengeLangaupage();
                break;
        }
    }

    @Override
    public void initView() {
        if (!USE_LANAGUAGE) {
            mActivitySettingsLanguage.setVisibility(View.GONE);
        }
        public_sns_topbar_title.setText(LanguageUtil.getString(this, R.string.app_set));
        mActivitySettingsLogout.setVisibility(UserUtils.isLogin(this) ? View.VISIBLE : View.GONE);
        mActivitySettingsLogout.setBackground(MyShape.setMyshape(10, ContextCompat.getColor(this, R.color.red)));
        mActivitySettingsSwitchContainer.setVisibility(UserUtils.isLogin(this) ? View.VISIBLE : View.GONE);
        if (Constant.USE_PAY) {
            mActivitySettingsAuto.setChecked(ShareUitls.getSetBoolean(this, Constant.AUTOBUY, true));
            isPressed = ShareUitls.getSetBoolean(this, Constant.AUTOBUY, true);
            mActivitySettingsAuto.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    Auto_sub(activity, new Auto_subSuccess() {
                        @Override
                        public void success(boolean open) {

                        }
                    });
                    isPressed=true;
                }
            });

        }
    }

    public static void exitUser(Activity activity) {
        if (!UserUtils.isLogin(activity)) {
            return;
        }
        UserUtils.putToken(activity, "");
        UserUtils.putUID(activity, "");
        new UpdateApp(BWNApplication.applicationContext.getActivity()).getRequestData(false, new UpdateApp.UpdateAppInterface() {
            @Override
            public void Next(String str, AppUpdate dataBean) {
                if (TextUtils.isEmpty(str) || dataBean == null) {
                    new UpdateApp(BWNApplication.applicationContext.getActivity()).getRequestData(false, null);
                }
            }
        });
        EventBus.getDefault().post(new RefreshMine());
    }

    /**
     * 好评支持
     */
    public static void support(Activity activity) {
        try {
            Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.SettingsActivity_nomark));
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public static void Auto_sub(final Activity activity, final Auto_subSuccess auto_subSuccess) {
        ReaderParams params = new ReaderParams(activity);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.auto_sub, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            int flag = jsonObject.getInt("auto_sub");
                            ShareUitls.putSetBoolean(activity, Constant.AUTOBUY,flag==1);
                            auto_subSuccess.success(flag==1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    private void ChengeLangaupage() {
        new BottomMenuDialog().showBottomMenuDialog(activity,
                new String[]{
                        LanguageUtil.getString(activity, R.string.SettingsActivity_zhcn),
                        "Orang indonesia"},
                new SCOnItemClickListener() {
                    @Override
                    public void OnItemClickListener(int flag, int position, Object O) {
                        if (position == 0) {
                            LanguageUtil.reFreshLanguage(Locale.TRADITIONAL_CHINESE, activity, SettingActivity.class);
                        } else if (position == 1) {
                            LanguageUtil.reFreshLanguage(new Locale("in", "in"), activity, SettingActivity.class);
                        }
                    }

                    @Override
                    public void OnItemLongClickListener(int flag, int position, Object O) {

                    }
                });
    }

    public interface Auto_subSuccess {

        void success(boolean open);
    }
}
