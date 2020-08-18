package com.ssreader.novel.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class TaskExplainActivity extends BaseActivity {

    @BindView(R.id.activity_task_explain_listview)
    ListView mActivityTaskcenterListview;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.SigninActivity_rule;
        return R.layout.activity_task_explain;
    }

    @Override
    public void initView() {
        final String[] sign_rules = getIntent().getStringArrayExtra("sign_rules");
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return sign_rules.length;
            }

            @Override
            public String getItem(int i) {
                return sign_rules[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView textView=new TextView(TaskExplainActivity.this);
                textView.setText(getItem( i));
                return textView;
            }
        };
        mActivityTaskcenterListview.setAdapter(baseAdapter);
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
