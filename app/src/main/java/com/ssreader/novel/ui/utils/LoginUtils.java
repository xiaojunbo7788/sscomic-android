package com.ssreader.novel.ui.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.qq.tencent.IUiListener;
import com.umeng.qq.tencent.Tencent;
import com.umeng.qq.tencent.UiError;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.LoginRefreshShelf;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.eventbus.RefreshUserInfo;
import com.ssreader.novel.ui.activity.LoginActivity;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UpdateApp;
import com.ssreader.novel.utils.UserUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.RegularUtlis;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.ssreader.novel.constant.Api.bind_wechat;
import static com.ssreader.novel.constant.Api.login_wechat;
import static com.ssreader.novel.constant.Api.login_qq;
import static com.ssreader.novel.constant.Api.bind_qq;
import static com.ssreader.novel.constant.Constant.DEVICE_LOGIN_DEF;

/**
 * 登录工具类
 */
public class LoginUtils {

    private Activity activity;
    private TimeCount timeCount;

    private IWXAPI iwxapi;
    private Tencent mTencent;
    // 用于判断（微信 or QQ）登录
    private boolean isWeChatLogin = true;
    // 用于判断是QQ登录还是QQ绑定
    private boolean isLogin = true;

    public LoginUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     * 设置倒计时
     * @param textView
     */
    public void setTimeCount(TextView textView) {
        if (timeCount == null) {
            timeCount = TimeCount.getInstance();
            timeCount.setActivity(activity, textView);
        }
    }

    /**
     * 获取验证码
     * @param phoneNum
     * @param mButtonView
     * @param callback
     */
    public void getMessage(String phoneNum, TextView mButtonView, boolean isBind, final LoginResultCallback callback) {
        phoneNum = phoneNum.replaceAll(" ", "");
        if (RegularUtlis.isMobile(phoneNum)) {
            if (timeCount == null) {
                timeCount = TimeCount.getInstance();
                timeCount.setActivity(activity, mButtonView);
            }
            timeCount.startCountTimer(60000);
            ReaderParams params = new ReaderParams(activity);
            params.putExtraParams("mobile", phoneNum);
            String json = params.generateParamsJson();
            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.mMessageUrl, json, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String result) {
                           MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.edit_Invitation_getcode));


                            if (callback != null) {
                                callback.getResult(result);
                            }
                        }

                        @Override
                        public void onErrorResponse(String ex) {

                        }
                    }

            );
        } else {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.LoginActivity_phoneerrpr));
        }
    }

    public void phoneUserLogin(String phone, String code, final SignSuccess signSuccess) {
        if (RegularUtlis.isMobile(phone)) {
            if (!TextUtils.isEmpty(code)) {
                ReaderParams params = new ReaderParams(activity);
                params.putExtraParams("mobile", phone);
                params.putExtraParams("code", code);
                String json = params.generateParamsJson();
                HttpUtils.getInstance().sendRequestRequestParams(activity,Api.mMobileLoginUrl, json, new HttpUtils.ResponseListener() {
                            @Override
                            public void onResponse(String result) {
                                handleLoginData(false, result);
                                if (timeCount != null) {
                                    timeCount.stopCountTimer();
                                }
                            }

                            @Override
                            public void onErrorResponse(String ex) {
                            }
                        }
                );
            } else {
                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.LoginActivity_codenull));
            }
        } else {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.LoginActivity_phoneerrpr));
        }
    }

    public void phoneUserBind(String phone, String code, final SignSuccess signSuccess) {
        if (RegularUtlis.isMobile(phone)) {
            if (!TextUtils.isEmpty(code)) {
                ReaderParams params = new ReaderParams(activity);
                params.putExtraParams("mobile", phone);
                params.putExtraParams("code", code);
                HttpUtils.getInstance().sendRequestRequestParams(activity,Api.mUserBindPhoneUrl, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                            @Override
                            public void onResponse(String result) {
                                EventBus.getDefault().post(new RefreshMine());
                                EventBus.getDefault().post(new RefreshUserInfo(true));
                                signSuccess.success("");
                                if (timeCount != null) {
                                    timeCount.stopCountTimer();
                                }
                            }

                            @Override
                            public void onErrorResponse(String ex) {
                                signSuccess.success(null);
                            }
                        }

                );
            } else {
                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.LoginActivity_codenull));
                signSuccess.success(null);
            }
        } else {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.LoginActivity_phoneerrpr));
            signSuccess.success(null);
        }
    }

    /**
     * 游客登录
     *
     * @param isfirst
     * @param signSuccess
     */
    public void deviceUserLogin(final boolean isfirst, final SignSuccess signSuccess) {
        if (isfirst && !DEVICE_LOGIN_DEF) {
            if(signSuccess!=null) {
                signSuccess.success("");
            }
            return;
        }
        final ReaderParams params = new ReaderParams(activity);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.VLOGIN_device, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        if (!isfirst) {
                            handleLoginData(false, result);
                        } else {
                            putToken(result);
                        }
                        if (signSuccess != null) {
                            signSuccess.success(null);
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (signSuccess != null) {
                            signSuccess.success(null);
                        }
                    }
                }
        );
    }

    /**
     * 微信登录
     *
     * @param code
     * @param isLogin
     */
    public void getWeiXinLogin(final String code, boolean isLogin) {
        String URL;
        // 判断是登录还是绑定
        if (isLogin) {
            URL = login_wechat;
        } else {
            URL = bind_wechat;
        }
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("code", code);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,URL, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        if (!isLogin) {
                            EventBus.getDefault().post(new RefreshMine());
                            EventBus.getDefault().post(new RefreshUserInfo(true));
                        } else
                            handleLoginData(true, result);
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    /**
     * QQ登录
     *
     * @param code
     */
    public void getQQLogin(Boolean isLogin, final String code) {
        String URL;
        if (isLogin) {
            URL = login_qq;
        } else {
            URL = bind_qq;
        }
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("access_token", code);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,URL, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        if (URL.equals(bind_qq)) {
                            EventBus.getDefault().post(new RefreshMine());
                            EventBus.getDefault().post(new RefreshUserInfo(true));
                        } else
                            handleLoginData(true, result);
                        if (mTencent != null) {
                            mTencent = null;
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (mTencent != null) {
                            mTencent = null;
                        }
                    }
                }
        );
    }

    /**
     * 登录回调
     *
     * @param isThird
     * @param s
     */
    private void handleLoginData(Boolean isThird, String s) {
        putToken(s);
        // 调用总数据接口
        if (BWNApplication.applicationContext.getActivity() != null && !BWNApplication.applicationContext.getActivity().isFinishing()) {
            new UpdateApp(BWNApplication.applicationContext.getActivity()).getRequestData(false, null);
        }
        // 刷新
        EventBus.getDefault().post(new RefreshMine());
        EventBus.getDefault().post(new RefreshShelf(Constant.BOOK_CONSTANT, null, null, null));
        EventBus.getDefault().post(new RefreshShelf(Constant.COMIC_CONSTANT, null, null, null));
        EventBus.getDefault().post(new RefreshShelf(Constant.AUDIO_CONSTANT, null, null, null));
        if (!isThird) {
            EventBus.getDefault().post(new LoginRefreshShelf(true));
        } else {
            if (isWeChatLogin) {
                if (!ShareUitls.getBoolean(activity, "isBindWeiXin", true)) {
                    EventBus.getDefault().post(new LoginRefreshShelf(true));
                }
            } else {
                if (isLogin) {
                    EventBus.getDefault().post(new LoginRefreshShelf(true));
                }
            }
        }
    }

    private void putToken(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            UserUtils.putToken(activity, jsonObject.getString("user_token"));
            UserUtils.putUID(activity, jsonObject.getString("uid"));
            if (BWNApplication.applicationContext != null) {
                BWNApplication.applicationContext.syncDeviceID(null);
            }
            ShareUitls.putSetBoolean(activity, Constant.AUTOBUY, jsonObject.getInt("auto_sub") == 1);
            int  ChooseSex=ShareUitls.getInt(activity,"ChooseSex",0);
            if(ChooseSex!=0){
                modifyNickname(ChooseSex) ;
            }
        } catch (JSONException e) {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.LoginActivity_error));
        }
    }

    public void modifyNickname(final int flag) {
        String requestParams  = Api.mUserSetGender;;
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("gender", flag + "");
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,requestParams, json, null);
    }


    public interface LoginResultCallback {

        void getResult(String s);
    }

    /**
     * 微信登录
     *
     * @param iwxapi
     * @param isWeChatLogin
     */
    public void weixinLogin(IWXAPI iwxapi, Boolean isWeChatLogin) {
        this.iwxapi = iwxapi;
        this.isWeChatLogin = isWeChatLogin;
        ShareUitls.putBoolean(activity, "isBindWeiXin", false);
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.WEIXIN, authListener);
    }

    /**
     * qq登录
     *
     * @param isWeChatLogin
     */
    public void qqLogin(Boolean isWeChatLogin, Boolean isLogin) {
        this.isWeChatLogin = isWeChatLogin;
        this.isLogin = isLogin;
        // 使用QQ原生登录
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constant.QQ_APPID, BWNApplication.applicationContext);
        }
        mTencent.login(activity, "all", iUiListener);
    }

    /**
     * QQ登录监听
     */
    IUiListener iUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            try {
                JSONObject jsonObject = new JSONObject(o.toString());
                if (jsonObject.has("access_token")) {
                    getQQLogin(isLogin, jsonObject.getString("access_token"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            MyToash.Log("iUiListener", uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            MyToash.Log("iUiListener", "cancel");
        }
    };

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (isWeChatLogin) {
                // 微信
                if (iwxapi.isWXAppInstalled()) {
                    iwxapi.registerApp(Constant.WEIXIN_PAY_APPID);
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_xb_live_state";
                    // 官方说明：用于保持请求和回调的状态，授权请求后原样带回给第三方。
                    // 该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
                    iwxapi.sendReq(req);
                } else {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.LoginActivity_nowechat));
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(activity, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(activity, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * @param activity
     * @return 登录
     */
    public static boolean goToLogin(Activity activity) {
        if (!UserUtils.isLogin(activity)) {
            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);
            activity.startActivity(intent);
            return false;
        }
        return true;
    }

    public interface SignSuccess {

        void success(String str);
    }
}
