package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.RefreshUserInfo;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.PublicCallBackSpan;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.ui.activity.SettingActivity.exitUser;

public class UserOutActivity extends BaseActivity {

    @BindView(R.id.activity_userout_layout)
    LinearLayout activityUseroutLayout;
    @BindView(R.id.activity_userout_img)
    ImageView activityUseroutImg;
    @BindView(R.id.activity_userout_xieyi)
    TextView activityUseroutXieyi;
    @BindView(R.id.activity_userout_apply)
    TextView activityUseroutApply;

    private boolean AGREE;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.UserInfoActivity_outuser;
        return R.layout.activity_userout;
    }

    @OnClick({R.id.activity_userout_img_layout, R.id.activity_userout_text,
            R.id.activity_userout_xieyi, R.id.activity_userout_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_userout_img_layout:
            case R.id.activity_userout_text:
                AGREE = !AGREE;
                if (AGREE) {
                    activityUseroutImg.setImageResource(R.mipmap.book_checked);
                    activityUseroutApply.setAlpha(1);
                    activityUseroutApply.setClickable(true);
                } else {
                    activityUseroutImg.setImageResource(R.mipmap.cancel_unselected);
                    activityUseroutApply.setAlpha(0.6f);
                    activityUseroutApply.setClickable(false);
                }
                break;
            case R.id.activity_userout_xieyi:
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("title", LanguageUtil.getString(activity, R.string.UserInfoActivity_outuser_zhuxiaoxieyi2));
                intent.putExtra("url", ShareUitls.getString(activity, "app_logoff", PublicCallBackSpan.LOGOFF));
                startActivity(intent);
                break;
            case R.id.activity_userout_apply:
                if (AGREE) {
                    HttpUtils.getInstance().sendRequestRequestParams(activity,Api.CANCELACCOUNT, readerParams.generateParamsJson(),responseListener);
                }
                break;
        }
    }

    @Override
    public void initView() {
        activityUseroutLayout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 10), ContextCompat.getColor(activity, R.color.graybg)));
        activityUseroutApply.setBackgroundColor(ContextCompat.getColor(activity, R.color.red));
        activityUseroutApply.setClickable(false);
        activityUseroutApply.setAlpha(0.6f);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {
        exitUser(this);
        EventBus.getDefault().post(new RefreshUserInfo(false));
        MyToash.setDelayedFinash(activity, R.string.UserInfoActivity_outuser_applyyet);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }
}
