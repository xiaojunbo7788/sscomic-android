package com.ssreader.novel.ui.utils;

import android.content.Intent;
import android.net.Uri;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gyf.immersionbar.ImmersionBar;
import com.ssreader.novel.R;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.AppUpdate;
import com.ssreader.novel.ui.activity.MainActivity;
import com.ssreader.novel.ui.adapter.MyFragmentPagerAdapter;
import com.ssreader.novel.ui.fragment.Public_main_fragment;
import com.ssreader.novel.ui.view.CustomScrollViewPager;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.ssreader.novel.ui.utils.StatusBarUtil.setStatusTextColor;

/**
 * 这是主界面底部的tab切换的工具类
 */
public class MainFragmentTabUtils implements RadioGroup.OnCheckedChangeListener {

    private FragmentManager fragmentManager;
    private List<Fragment> fragments;//一个tab页面对应一个Fragment
    private int possition;
    private MainActivity mainActivity;
    public static boolean UserCenterRefarsh;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.activity_main_Bookshelf:
                if (possition != 0) {
                    setStatusTextColor(true, mainActivity);
                    IntentFragment(0);
                }
                break;
            case R.id.activity_main_Bookstore:
                if (possition != 1) {
                    IntentFragment(1);
                    if (!public_main_fragment2.Status) {
                        StatusBarUtil.setStatusStoreColor(mainActivity, false);
                    }
                }
                break;
            case R.id.activity_main_fan_group:
                clickYu();
                break;
            case R.id.activity_main_discovery:
                if (possition != 3) {
                    setStatusTextColor(true, mainActivity);
                    IntentFragment(3);
                }
                break;
            case R.id.activity_main_mine:
                if (possition != 4) {
                    ImmersionBar.with(mainActivity).transparentStatusBar().statusBarDarkFont(true).statusBarColor(R.color.white).init();
                    IntentFragment(4);
                    if (UserCenterRefarsh) {
                        UserCenterRefarsh=false;
                        EventBus.getDefault().post(new RefreshMine());
                    }
                }
                break;
        }
    }

    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private CustomScrollViewPager customScrollViewPager;
    private Public_main_fragment public_main_fragment2;

    public MainFragmentTabUtils(int i, MainActivity activity, List<Fragment> fragments, CustomScrollViewPager customScrollViewPager, RadioGroup radioGroup) {
        if (i == 0) {
            this.mainActivity = activity;
        }
        possition = 5;
        this.fragmentManager = activity.getSupportFragmentManager();
        this.fragments = fragments;
        public_main_fragment2 = (Public_main_fragment) fragments.get(1);
        this.customScrollViewPager = customScrollViewPager;
        radioGroup.setOnCheckedChangeListener(this);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragments);
        customScrollViewPager.setAdapter(myFragmentPagerAdapter);
        int MainCurrentPosition = ShareUitls.getInt(mainActivity, "MainCurrentPosition", 1);
        if (MainCurrentPosition == 1) {
            IntentFragment(1);
            ((RadioButton) radioGroup.findViewById(R.id.activity_main_Bookstore)).setChecked(true);
            customScrollViewPager.post(new Runnable() {
                @Override
                public void run() {
                    StatusBarUtil.setStatusStoreColor(mainActivity, false);
                    customScrollViewPager.setOffscreenPageLimit(5);
                }
            });
        } else {
            customScrollViewPager.post(new Runnable() {
                @Override
                public void run() {
                    customScrollViewPager.setOffscreenPageLimit(5);
                }
            });
        }
    }

    private void IntentFragment(int i) {
        customScrollViewPager.setCurrentItem(i, false);
        if (i == 0 || i == 1) {
            ShareUitls.putInt(mainActivity, "MainCurrentPosition", i);
        }
    }

    private FragmentTransaction obtainFragmentTransaction() {
        return fragmentManager.beginTransaction();
    }

    private void clickYu() {
        if (possition != 2) {
//            RadioButton radioButton = mainActivity.findViewById(R.id.activity_main_fan_group);
//            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_home_5_2, 0, 0);
//            radioButton.setTextColor(mainActivity.getResources().getColor(R.color.home_color_selector_2));
            List<AppUpdate.WebURLBean>list = ShareUitls.getDataList(mainActivity,"web_view_urlist", AppUpdate.WebURLBean.class);
            if (list !=null && list.size() > 0) {
                AppUpdate.WebURLBean webURLBean = list.get(0);
                if (webURLBean.getWebview() == 0) {
                    final Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(webURLBean.getPlay_url()));
                    // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
                    // 官方解释 : Name of the component implementing an activity that can display the intent
                    mainActivity.startActivity(intent);
                    return;
                }
            }
            setStatusTextColor(true, mainActivity);
            IntentFragment(2);
        }
    }
}