package com.ssreader.novel.ui.localshell.localapp;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;

/**
 * 个人中心-意见反馈
 */
public class FeedBackActivity extends BaseActivity {

    private EditText activity_feedback_content;
    private TextView activity_feedback_percentage;
    private LinearLayout comment_titlebar_add_feedback;
    private TextView titlebar_finish;

    LinearLayout mBack;
    TextView mTitle;

    @Override
    public int initContentView() {
        return R.layout.activity_feedback_layout;
    }

    @Override
    public void initView() {
        titlebar_finish = findViewById(R.id.titlebar_finish);
        mBack = findViewById(R.id.titlebar_back);
        mTitle = findViewById(R.id.titlebar_text);
        mTitle.setText(LanguageUtil.getString(this, R.string.FeedBackActivity_title));
        titlebar_finish.setText(LanguageUtil.getString(this, R.string.FeedBackActivity_tijiao));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activity_feedback_content = findViewById(R.id.activity_feedback_content);
        activity_feedback_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String percentage = "%s/200";
                int lastWordsNum = 200 - s.length();
                activity_feedback_percentage.setText(String.format(percentage, lastWordsNum + ""));
            }
        });
        activity_feedback_percentage = findViewById(R.id.activity_feedback_percentage);
        comment_titlebar_add_feedback = findViewById(R.id.comment_titlebar_add_comment);
        comment_titlebar_add_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeedback();
            }
        });
    }

    @Override
    public void initData() {

    }

    /**
     * 发请求
     */
    public void addFeedback() {
        if (TextUtils.isEmpty(activity_feedback_content.getText())) {
            MyToash.ToashError(FeedBackActivity.this, LanguageUtil.getString(this, R.string.FeedBackActivity_some));
            return;
        }
        MyToash.setDelayedFinash(activity, R.string.FeedBackActivity_fankui_success);
    }

    @Override
    public void initInfo(String json) {

    }

    public void initTitleBarView(String text) {

    }
}
