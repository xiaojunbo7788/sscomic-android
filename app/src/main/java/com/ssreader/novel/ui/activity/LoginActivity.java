package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.LoginRefreshShelf;
import com.ssreader.novel.eventbus.WeChatLoginRefresh;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.PublicCallBackSpan;
import com.ssreader.novel.ui.utils.TimeCount;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.RegularUtlis;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.SystemUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.DEVICE_LOGIN;
import static com.ssreader.novel.constant.Constant.USE_QQ;
import static com.ssreader.novel.constant.Constant.USE_WEIXIN;

/**
 * 用户登录页
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.activity_login_phone_username)
    EditText activity_login_phone_username;
    @BindView(R.id.activity_login_phone_message)
    EditText activity_login_phone_message;
    @BindView(R.id.activity_bind_phone_get_message_btn)
    TextView activity_login_phone_get_message_btn;
    @BindView(R.id.activity_login_phone_btn)
    Button activity_login_phone_btn;
    @BindView(R.id.activity_login_phone_clear)
    ImageView activity_login_phone_clear;
    @BindView(R.id.activity_login_contract)
    TextView activity_login_contract;
    @BindView(R.id.activity_login_tourist_login)
    LinearLayout activity_login_tourist_login;
    @BindView(R.id.activity_login_weixin_login)
    LinearLayout activity_login_weixin_login;
    @BindView(R.id.activity_login_qq_login)
    LinearLayout activity_login_qq_login;

    private LoginUtils loginUtils;
    private IWXAPI iwxapi;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.activity_login;
    }

    @OnClick(value = {R.id.activity_bind_phone_get_message_btn, R.id.activity_login_phone_btn,
            R.id.activity_login_phone_clear, R.id.activity_login_contract, R.id.activity_login_close,
            R.id.activity_login_tourist_login, R.id.activity_login_weixin_login, R.id.activity_login_qq_login})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.activity_login_close:
                finish();
                break;
            case R.id.activity_bind_phone_get_message_btn:
                loginUtils.getMessage(activity_login_phone_username.getText().toString(),
                        activity_login_phone_get_message_btn, false, null);
                break;
            case R.id.activity_login_phone_btn:
                loginUtils.phoneUserLogin(activity_login_phone_username.getText().toString(),
                        activity_login_phone_message.getText().toString(), null);
                break;
            case R.id.activity_login_phone_clear:
                activity_login_phone_username.setText("");
                break;
            case R.id.activity_login_contract:
                startActivity(new Intent(activity, WebViewActivity.class)
                        .putExtra("url", ShareUitls.getString(activity, "app_user", PublicCallBackSpan.USER))
                        .putExtra("title", LanguageUtil.getString(activity, R.string.AboutActivity_xieyi)));
                break;
            case R.id.activity_login_tourist_login:
                // 游客登录
                loginUtils.deviceUserLogin(false, null);
                break;
            case R.id.activity_login_weixin_login:
                // 微信登录
                if (iwxapi != null) {
                    loginUtils.weixinLogin(iwxapi, true);
                }
                break;
            case R.id.activity_login_qq_login:
                // QQ登录
                loginUtils.qqLogin(false, true);
                break;
        }
    }

    @Override
    public void initView() {
        TimeCount.getInstance().setActivity(activity_login_phone_username);
        // 判断按钮是否显示
        if (DEVICE_LOGIN) {
            activity_login_tourist_login.setVisibility(View.VISIBLE);
        } else {
            activity_login_tourist_login.setVisibility(View.GONE);
        }
        if ((SystemUtil.checkAppInstalled(activity, SystemUtil.QQ_PACKAGE_NAME) && USE_QQ) ||
                (SystemUtil.checkAppInstalled(activity, SystemUtil.TIM_PACKAGE_NAME) && USE_QQ)) {
            activity_login_qq_login.setVisibility(View.VISIBLE);
        } else {
            activity_login_qq_login.setVisibility(View.GONE);
        }
        if (SystemUtil.checkAppInstalled(activity, SystemUtil.WEChTE_PACKAGE_NAME) && USE_WEIXIN) {
            activity_login_weixin_login.setVisibility(View.VISIBLE);
        } else {
            activity_login_weixin_login.setVisibility(View.GONE);
        }
        // 设置工具类
        loginUtils = new LoginUtils(activity);
        loginUtils.setTimeCount(activity_login_phone_get_message_btn);
        // 微信登录
        iwxapi = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_PAY_APPID, true);
        iwxapi.registerApp(Constant.WEIXIN_PAY_APPID);
        activity_login_phone_btn.setBackground(MyShape.setMyCustomizeShape(this, 22, R.color.lightgray1));
        activity_login_phone_get_message_btn.setEnabled(false);
        activity_login_phone_btn.setEnabled(false);
        activity_login_phone_message.setEnabled(false);
        activity_login_phone_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    activity_login_phone_clear.setVisibility(View.GONE);
                }else {
                    activity_login_phone_clear.setVisibility(View.VISIBLE);
                    if (RegularUtlis.isMobile(s.toString())) {
                        activity_login_phone_get_message_btn.setEnabled(true);
                        activity_login_phone_get_message_btn.setTextColor(ContextCompat.getColor(activity, R.color.red));
                        activity_login_phone_message.setEnabled(true);
                        setLoginUI(!TextUtils.isEmpty(activity_login_phone_message.getText().toString()));
                    } else {
                        if (activity_login_phone_get_message_btn.isEnabled()) {
                            activity_login_phone_get_message_btn.setEnabled(false);
                            activity_login_phone_get_message_btn.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                            activity_login_phone_message.setEnabled(false);
                            setLoginUI(false);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_login_phone_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setLoginUI(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_login_phone_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    activity_login_phone_clear.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(activity_login_phone_username.getText().toString())) {
                        activity_login_phone_clear.setVisibility(View.VISIBLE);
                    } else {
                        activity_login_phone_clear.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void setLoginUI(boolean b) {
        activity_login_phone_btn.setEnabled(b);
        if (b) {
            activity_login_phone_btn.setBackground(MyShape.setMyCustomizeShape(this, 22, R.color.red));
            activity_login_phone_btn.setTextColor(Color.WHITE);
        } else {
            activity_login_phone_btn.setBackground(MyShape.setMyCustomizeShape(this, 22, R.color.login_button));
            activity_login_phone_btn.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    /**
     * 用于输出登录成功
     * @param refreshShelf
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshSelf(LoginRefreshShelf refreshShelf) {
        if (refreshShelf.isFinish) {
            MyToash.setDelayedFinash(activity, R.string.LoginActivity_success);
        }
    }

    /**
     * 微信登录
     *
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isWeChatLogin(WeChatLoginRefresh refresh) {
        if (refresh.isLogin && !refresh.isBind) {
            loginUtils.getWeiXinLogin(refresh.code, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
