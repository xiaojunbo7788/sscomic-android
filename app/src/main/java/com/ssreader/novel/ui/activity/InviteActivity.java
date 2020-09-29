package com.ssreader.novel.ui.activity;

import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;

import butterknife.BindView;

public class InviteActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_title)
    TextView public_sns_topbar_title;

    @Override
    public int initContentView() {
        return R.layout.activity_invite;
    }

    @Override
    public void initView() {
        public_sns_topbar_title.setText("邀请好友");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }
}
