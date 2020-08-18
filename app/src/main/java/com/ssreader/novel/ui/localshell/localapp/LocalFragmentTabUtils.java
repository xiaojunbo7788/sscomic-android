package com.ssreader.novel.ui.localshell.localapp;

import android.app.Activity;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.adapter.MyFragmentPagerAdapter;
import com.ssreader.novel.ui.utils.StatusBarUtil;
import com.ssreader.novel.ui.view.CustomScrollViewPager;

import java.util.List;

import static com.ssreader.novel.ui.utils.StatusBarUtil.setStatusTextColor;

/**
 * 这是主界面底部的tab切换的工具类
 */
public class LocalFragmentTabUtils implements RadioGroup.OnCheckedChangeListener {

    private FragmentManager fragmentManager;
    private int possition;
    private Activity activity;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private CustomScrollViewPager customScrollViewPager;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.activity_main_Bookshelf:
                if (possition != 0) {
                    StatusBarUtil.setStatusStoreColor(activity,true);
                    IntentFragment(0);
                }
                break;
            case R.id.activity_main_Bookstore:
                if (possition != 1) {
                    setStatusTextColor(false, activity);
                    IntentFragment(1);
                }
                break;
            case R.id.activity_main_discovery:
                if (possition != 2) {
                    setStatusTextColor(true, activity);
                    IntentFragment(2);
                }
                break;
            case R.id.activity_main_mine:
                if (possition != 3) {
                    setStatusTextColor(true, activity);
                    IntentFragment(3);
                }
                break;
        }
    }

    public LocalFragmentTabUtils(FragmentActivity activity, List<Fragment> fragments, CustomScrollViewPager customScrollViewPager, RadioGroup radioGroup) {
        this.activity = activity;
        this.fragmentManager = activity.getSupportFragmentManager();
        this.customScrollViewPager = customScrollViewPager;
        radioGroup.setOnCheckedChangeListener(this);
        possition = 4;
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragments);
        customScrollViewPager.setAdapter(myFragmentPagerAdapter);
        customScrollViewPager.post(new Runnable() {
            @Override
            public void run() {
                customScrollViewPager.setOffscreenPageLimit(3);
            }
        });
    }

    private void IntentFragment(int i) {
        customScrollViewPager.setCurrentItem(i);
    }

    private FragmentTransaction obtainFragmentTransaction() {
        return fragmentManager.beginTransaction();
    }
}