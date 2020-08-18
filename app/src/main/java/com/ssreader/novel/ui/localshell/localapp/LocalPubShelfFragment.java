package com.ssreader.novel.ui.localshell.localapp;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.ui.utils.StatusBarUtil;
import com.ssreader.novel.ui.view.IndexPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class LocalPubShelfFragment extends BaseFragment {

    @BindView(R.id.fragment_store_XTabLayout)
    SmartTabLayout fragmentStoreXTabLayout;
    @BindView(R.id.add_notes_img)
    ImageView addNotesImg;
    @BindView(R.id.fragment_pubic_main_ViewPager)
    ViewPager fragmentPubicMainViewPager;

    private List<String> stringList;
    private List<Fragment> fragmentList;
    private Map<Integer, TextView> selection_TextViewStoreScrollStatus;
    public View activity_main_RadioGroup;

    public LocalPubShelfFragment() {
    }

    public LocalPubShelfFragment(View activity_main_RadioGroup) {
        this.activity_main_RadioGroup = activity_main_RadioGroup;
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_local_option;
    }

    @Override
    public void initView() {
        StatusBarUtil.setStatusStoreColor(activity, false);
        addNotesImg.setVisibility(View.GONE);
        stringList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        stringList.add("本地书籍");
        stringList.add("民著精选");
        fragmentList.add(new LocalShelfFragment(0, activity_main_RadioGroup));
        fragmentList.add(new LocalShelfFragment(1, activity_main_RadioGroup));
        IndexPagerAdapter indexPagerAdapter = new IndexPagerAdapter(getChildFragmentManager(), stringList, fragmentList);
        fragmentPubicMainViewPager.setAdapter(indexPagerAdapter);
        fragmentStoreXTabLayout.setViewPager(fragmentPubicMainViewPager);
        selection_TextViewStoreScrollStatus = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            TextView textView = fragmentStoreXTabLayout.getTabAt(i).findViewById(R.id.item_tablayout_text);
            textView.setTextColor(Color.WHITE);
            if (i == 0) {
                textView.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
            }
            selection_TextViewStoreScrollStatus.put(i, textView);
        }
        fragmentPubicMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int k = 0; k < 2; k++) {
                    if (selection_TextViewStoreScrollStatus.get(k) != null) {
                        TextView textView = selection_TextViewStoreScrollStatus.get(k);
                        if (k == position) {
                            textView.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                            // textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                        } else {
                            textView.setTextColor(Color.WHITE);
                            //  textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        }
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }
}
