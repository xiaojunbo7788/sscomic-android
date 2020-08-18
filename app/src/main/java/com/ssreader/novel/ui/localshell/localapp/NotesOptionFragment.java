package com.ssreader.novel.ui.localshell.localapp;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.ui.view.IndexPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NotesOptionFragment extends BaseFragment {

    @BindView(R.id.fragment_store_XTabLayout)
    SmartTabLayout publicSelectionXTabLayout;
    @BindView(R.id.fragment_pubic_main_ViewPager)
    ViewPager fragmentPubicMainViewPager;

    private List<String> stringList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private Map<Integer, TextView> selection_TextViewStoreScrollStatus;

    public NotesOptionFragment() {

    }

    @Override
    public int initContentView() {
        return R.layout.fragment_local_option;
    }

    @OnClick({R.id.add_notes_img})
    public void onClick(View view) {
        Intent intent = new Intent(activity, LocalEditNotesActivity.class);
        startActivity(intent);
    }

    @Override
    public void initView() {
        try {
            stringList.add("热门笔记");
            stringList.add("我的笔记");
            fragmentList.add(new LocalNotesFragment(0));
            fragmentList.add(new LocalNotesFragment(1));
            IndexPagerAdapter indexPagerAdapter = new IndexPagerAdapter(getChildFragmentManager(), stringList, fragmentList);
            fragmentPubicMainViewPager.setAdapter(indexPagerAdapter);
            publicSelectionXTabLayout.setViewPager(fragmentPubicMainViewPager);
            selection_TextViewStoreScrollStatus = new HashMap<>();
            for (int i = 0; i < 2; i++) {
                TextView textView = publicSelectionXTabLayout.getTabAt(i).findViewById(R.id.item_tablayout_text);
                textView.setTextColor(Color.WHITE);
                if (i == 0) {
                    textView.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                }
                selection_TextViewStoreScrollStatus.put(i, textView);
            }
            initListener();
        } catch (Exception e) {
        }
    }

    private void initListener() {
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
                        } else {
                            textView.setTextColor(Color.WHITE);
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
