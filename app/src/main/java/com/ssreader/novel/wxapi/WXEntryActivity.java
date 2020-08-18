package com.ssreader.novel.wxapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.ssreader.novel.R;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.WeChatLoginRefresh;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信登录相关
 */
public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {

    private final int RETURN_MSG_TYPE_LOGIN = 1;
    private final int RETURN_MSG_TYPE_SHARE = 2;
    public IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        setContentView(R.layout.activity_transparent);
    }

    private void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onReq(BaseReq req) {
        // 微信回调数据 req
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_BAN:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                // 用户取消
                break;
            case BaseResp.ErrCode.ERR_OK:
                // 正确返回
                switch (baseResp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        String code = ((SendAuth.Resp) baseResp).code;
                        boolean isBind = ShareUitls.getBoolean(this, "isBindWeiXin", false);
                        EventBus.getDefault().post(new WeChatLoginRefresh(true, code, isBind));
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        MyToash.Log("TYPE_SHARE", "1111");
                        break;
                }
                break;
        }
    }

    @Override
    public void a(com.umeng.weixin.umengwx.b b) {
        // 拦截umeng回调
        if (MyShare.IS_SHARE) {
            super.a(b);
            MyShare.IS_SHARE = false;
        }
    }

    @Override
    protected void a(Intent intent) {
        super.a(intent);
    }

    //在onResume中处理从微信授权通过以后不会自动跳转的问题，手动结束该页面
    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hintKeyboard();
        iwxapi = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_PAY_APPID, true);
        iwxapi.handleIntent(getIntent(), this);
    }
}
