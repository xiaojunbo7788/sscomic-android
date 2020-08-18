package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.ssreader.novel.R;

import java.util.Map;

import static com.ssreader.novel.model.PublicIntent.intentTo;

public class MyAndroidPopupActivity extends AndroidPopupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extraMap) {
        Intent intent = new Intent();
        if (extraMap == null || extraMap.isEmpty()) {
            intent.setClass(MyAndroidPopupActivity.this, SplashActivity.class);
        } else {
            String skip_type = extraMap.get("skip_type");
            if (skip_type != null && extraMap.get("content") != null) {
                try {
                    int skip_typeint = Integer.parseInt(skip_type);
                    intent = intentTo(MyAndroidPopupActivity.this, false, skip_typeint, extraMap.get("content"), title);
                } catch (Exception e) {
                    intent = new Intent(MyAndroidPopupActivity.this, SplashActivity.class);
                }
            } else {
                intent.setClass(MyAndroidPopupActivity.this, SplashActivity.class);
            }
        }
        startActivity(intent);
        finish();
    }
}
