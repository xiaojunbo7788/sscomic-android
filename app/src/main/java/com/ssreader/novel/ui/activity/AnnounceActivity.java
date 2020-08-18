package com.ssreader.novel.ui.activity;

import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * 公告activity
 */
public class AnnounceActivity extends BaseActivity {

    @BindView(R.id.activity_announce_content)
    TextView activity_announce_content;

    private String mContent;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        return R.layout.activity_announce;
    }

    @Override
    public void initView() {
        try {
            mContent = getIntent().getStringExtra("announce_content");
            String str[] = mContent.split("/-/");
            public_sns_topbar_title.setText(str[0]);
            activity_announce_content.setText(str[1]);
        } catch (Exception e) {
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
}
