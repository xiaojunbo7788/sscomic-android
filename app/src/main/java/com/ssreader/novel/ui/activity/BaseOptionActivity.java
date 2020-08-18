package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.ui.adapter.MyFragmentPagerAdapter;
import com.ssreader.novel.ui.fragment.BaseListFragment;
import com.ssreader.novel.ui.fragment.DownMangerBookFragment;
import com.ssreader.novel.ui.fragment.DownMangerComicFragment;
import com.ssreader.novel.ui.fragment.GoldRecordFragment;
import com.ssreader.novel.ui.fragment.MyCommentFragment;
import com.ssreader.novel.ui.fragment.RankIndexFragment;
import com.ssreader.novel.ui.fragment.ReadHistoryFragment;
import com.ssreader.novel.ui.fragment.RewardHistoryFragment;
import com.ssreader.novel.ui.utils.MainFragmentTabUtils;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.*;

/**
 * fragment分发界面
 */
public class BaseOptionActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_back)
    RelativeLayout public_sns_topbar_back;
    @BindView(R.id.top_channel_layout)
    View top_channel_layout;

    @BindView(R.id.public_sns_topbar_right_other)
    View public_sns_topbar_right_other;
    @BindView(R.id.public_sns_topbar_right_tv)
    TextView public_sns_topbar_right_tv;
    @BindView(R.id.public_sns_topbar_right_img)
    ImageView public_sns_topbar_right_img;

    @BindView(R.id.base_option_XTabLayout)
    SmartTabLayout public_selection_XTabLayout;
    @BindView(R.id.activity_baseoption_viewpage)
    public ViewPager activity_baseoption_viewpage;
    @BindView(R.id.public_sns_topbar_title)
    TextView public_sns_topbar_title;

    private List<Fragment> fragmentList;
    private List<TextView> textViewList;
    private List<String> tabList;
    private Fragment baseButterKnifeFragment1, baseButterKnifeFragment2, baseButterKnifeFragment3;

    private int OPTION, Old_position;
    private int productType;
    private Intent IntentFrom;
    public boolean Edit1, Edit2, Edit3;
    public GoldRecordFragment goldRecordFragment;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.activity_baseoption;
    }

    @OnClick(value = {R.id.public_sns_topbar_back, R.id.public_sns_topbar_right_other})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.public_sns_topbar_back:
                finish();
                break;
            case R.id.public_sns_topbar_right_other:
                if (OPTION == DOWN) {
                    setEdit();
                } else if (OPTION == READHISTORY) {
                    setClean();
                } else if (OPTION == MONTHTICKETHISTORY) {
                    if (goldRecordFragment != null) {
                        if (goldRecordFragment.rule_url != null && !TextUtils.isEmpty(goldRecordFragment.rule_url)) {
                            Intent intent = new Intent(activity, WebViewActivity.class);
                            intent.putExtra("title", LanguageUtil.getString(activity, R.string.Activity_Monthly_Web_title));
                            intent.putExtra("url", goldRecordFragment.rule_url);
                            intent.setClass(activity, WebViewActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                break;
        }
    }

    /**
     * 编辑下载
     */
    private void setEdit() {
        String strType = Constant.getProductTypeList(activity).get(Old_position);
        switch (strType) {
            case "1":
                Edit1 = !Edit1;
                ((DownMangerBookFragment) baseButterKnifeFragment1).setEdit(Edit1);
                break;
            case "2":
                Edit2 = !Edit2;
                ((DownMangerComicFragment) baseButterKnifeFragment2).setEdit(Edit2);
                break;
            case "3":
                Edit3 = !Edit3;
                ((DownMangerComicFragment) baseButterKnifeFragment3).setEdit(Edit3);
                break;
        }
    }

    /**
     * 清空历史记录
     */
    private void setClean() {
        String strType = Constant.getProductTypeList(activity).get(Old_position);
        switch (strType) {
            case "1":
                ((ReadHistoryFragment) baseButterKnifeFragment1).clean();
                break;
            case "2":
                ((ReadHistoryFragment) baseButterKnifeFragment2).clean();
                break;
            case "3":
                ((ReadHistoryFragment) baseButterKnifeFragment3).clean();
                break;
        }
    }

    @Override
    public void initView() {
        IntentFrom = getIntent();
        fragmentList = new ArrayList<>();
        tabList = new ArrayList<>();
        OPTION = IntentFrom.getIntExtra("OPTION", 0);
        productType = IntentFrom.getIntExtra("productType", 0);
        if (OPTION != LOOKMORE) {
            String title = IntentFrom.getStringExtra("title");
            public_sns_topbar_title.setText(title);
        }
        switch (OPTION) {
            case MIANFEI:
                // 免费
            case WANBEN:
                // 完结
                baseButterKnifeFragment1 = new BaseListFragment(productType, OPTION, 1);
                baseButterKnifeFragment2 = new BaseListFragment(productType, OPTION, 2);
                tabList.add(LanguageUtil.getString(activity, R.string.storeFragment_boy));
                tabList.add(LanguageUtil.getString(activity, R.string.storeFragment_gril));
                fragmentList.add(baseButterKnifeFragment1);
                fragmentList.add(baseButterKnifeFragment2);
                break;
            case PAIHANG:
                // 排行频道
                public_selection_XTabLayout.setVisibility(View.GONE);
                int SEX = IntentFrom.getIntExtra("SEX", 1);
                String rank_type = IntentFrom.getStringExtra("rank_type");
                baseButterKnifeFragment1 = new BaseListFragment(productType, OPTION, rank_type, SEX);
                fragmentList.add(baseButterKnifeFragment1);
                break;
            case PAIHANGINSEX:
                // 排行首页频道
                baseButterKnifeFragment1 = new RankIndexFragment(productType, 1);
                baseButterKnifeFragment2 = new RankIndexFragment(productType, 2);
                fragmentList.add(baseButterKnifeFragment1);
                fragmentList.add(baseButterKnifeFragment2);
                tabList.add(LanguageUtil.getString(activity, R.string.storeFragment_boy));
                tabList.add(LanguageUtil.getString(activity, R.string.storeFragment_gril));
                break;
            case BAOYUE_SEARCH:
            case SHUKU:
            case BAOYUE:
                baseButterKnifeFragment1 = new BaseListFragment(productType, OPTION, 1);
                fragmentList.add(baseButterKnifeFragment1);
                break;
            case DOWN:
                public_sns_topbar_right_img.setVisibility(View.GONE);
                public_sns_topbar_right_other.setVisibility(View.VISIBLE);
                public_sns_topbar_right_tv.setVisibility(View.VISIBLE);
                public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, R.string.app_edit));
                public_sns_topbar_right_tv.setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
                boolean ONLY_NOVER = formIntent.getBooleanExtra("ONLY_NOVER", false);
                if (!ONLY_NOVER) {
                    //下载频道
                    if (!Constant.getProductTypeList(activity).isEmpty()) {
                        fragmentList.clear();
                        tabList.clear();

                        for (String strType : Constant.getProductTypeList(activity)) {
                            if (strType.equals("1")) {
                                tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                                baseButterKnifeFragment1 = new DownMangerBookFragment(public_sns_topbar_right_tv);
                                fragmentList.add(baseButterKnifeFragment1);
                            } else if (strType.equals("2")) {
                                tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
                                baseButterKnifeFragment2 = new DownMangerComicFragment(1, public_sns_topbar_right_tv);
                                fragmentList.add(baseButterKnifeFragment2);
                            } else if (strType.equals("3")) {
                                tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_audio));
                                baseButterKnifeFragment3 = new DownMangerComicFragment(2, public_sns_topbar_right_tv);
                                fragmentList.add(baseButterKnifeFragment3);
                            }
                        }
                    }
                }else {
                    tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                    baseButterKnifeFragment1 = new DownMangerBookFragment(public_sns_topbar_right_tv);
                    fragmentList.add(baseButterKnifeFragment1);
                }
                break;
            case READHISTORY:
                //阅读历史频道
                public_sns_topbar_right_img.setVisibility(View.GONE);
                public_sns_topbar_right_other.setVisibility(View.VISIBLE);
                public_sns_topbar_right_tv.setVisibility(View.VISIBLE);
                public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, R.string.app_clean_all));
                public_sns_topbar_right_tv.setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
                if (!Constant.getProductTypeList(activity).isEmpty()) {
                    fragmentList.clear();
                    tabList.clear();
                    for (String strType : Constant.getProductTypeList(activity)) {
                        if (strType.equals("1")) {
                            tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                            baseButterKnifeFragment1 = new ReadHistoryFragment(BOOK_CONSTANT);
                            fragmentList.add(baseButterKnifeFragment1);
                        } else if (strType.equals("2")) {
                            tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
                            baseButterKnifeFragment2 = new ReadHistoryFragment(COMIC_CONSTANT);
                            fragmentList.add(baseButterKnifeFragment2);
                        } else if (strType.equals("3")) {
                            tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_audio));
                            baseButterKnifeFragment3 = new ReadHistoryFragment(AUDIO_CONSTANT);
                            fragmentList.add(baseButterKnifeFragment3);
                        }
                    }
                }
                break;
            case LIUSHUIJIELU:
                // 流水记录
                tabList.clear();
                tabList.add(getCurrencyUnit(activity));
                tabList.add(getSubUnit(activity));
                baseButterKnifeFragment1 = new GoldRecordFragment("currencyUnit");
                baseButterKnifeFragment2 = new GoldRecordFragment("subUnit");
                fragmentList.add(baseButterKnifeFragment1);
                fragmentList.add(baseButterKnifeFragment2);
                break;
            case REWARDHISTORY:
                public_selection_XTabLayout.setVisibility(View.GONE);
                baseButterKnifeFragment1 = new RewardHistoryFragment();
                fragmentList.add(baseButterKnifeFragment1);
                break;
            case MONTHTICKETHISTORY:
//                public_sns_topbar_right_other.setVisibility(View.VISIBLE);
//                public_sns_topbar_right_img.setImageResource(R.mipmap.img_directions);
                public_selection_XTabLayout.setVisibility(View.GONE);
                goldRecordFragment = new GoldRecordFragment(MONTHTICKETHISTORY);
                fragmentList.add(goldRecordFragment);
                break;
            case LOOKMORE:
                // 查看更多
                String recommend_id = IntentFrom.getStringExtra("recommend_id");
                if (recommend_id.equals("0")) {
                    public_sns_topbar_title.setText(LanguageUtil.getString(activity, R.string.StoreFragment_xianshimianfei));
                    baseButterKnifeFragment1 = new BaseListFragment(productType, OPTION, 1);
                    baseButterKnifeFragment2 = new BaseListFragment(productType, OPTION, 2);
                    fragmentList.add(baseButterKnifeFragment1);
                    fragmentList.add(baseButterKnifeFragment2);
                    tabList.add(LanguageUtil.getString(activity, R.string.storeFragment_boy));
                    tabList.add(LanguageUtil.getString(activity, R.string.storeFragment_gril));
                } else {
                    public_selection_XTabLayout.setVisibility(View.GONE);
                    baseButterKnifeFragment1 = new BaseListFragment(productType, OPTION, recommend_id, public_sns_topbar_title);
                    fragmentList.add(baseButterKnifeFragment1);
                }
                break;
            case MYCOMMENT:
                // 我的评论
                if (!Constant.getProductTypeList(activity).isEmpty()) {
                    fragmentList.clear();
                    tabList.clear();
                    for (String strType : Constant.getProductTypeList(activity)) {
                        if (strType.equals("1")) {
                            tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                            baseButterKnifeFragment1 = new MyCommentFragment(0);
                            fragmentList.add(baseButterKnifeFragment1);
                        } else if (strType.equals("2")) {
                            tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
                            baseButterKnifeFragment2 = new MyCommentFragment(1);
                            fragmentList.add(baseButterKnifeFragment2);
                        } else if (strType.equals("3")) {
                            tabList.add(LanguageUtil.getString(activity, R.string.noverfragment_audio));
                            baseButterKnifeFragment3 = new MyCommentFragment(2);
                            fragmentList.add(baseButterKnifeFragment3);
                        }
                    }
                }
                break;
        }
        if (fragmentList.size() < 2) {
            top_channel_layout.setVisibility(View.GONE);
            activity_baseoption_viewpage.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        } else {
            activity_baseoption_viewpage.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabList));
        }
        activity_baseoption_viewpage.setOffscreenPageLimit(3);
        public_selection_XTabLayout.setViewPager(activity_baseoption_viewpage);
        boolean Extra = false;
        if (OPTION == LIUSHUIJIELU) {
            Extra = IntentFrom.getBooleanExtra("Extra", false);
            if (Extra) {
                activity_baseoption_viewpage.setCurrentItem(1);
                Old_position = 1;
            }
        }
        if (fragmentList.size() >= 2) {
            textViewList = new ArrayList<>();
            textViewList.add(public_selection_XTabLayout.getTabAt(0).findViewById(R.id.item_tablayout_text));
            textViewList.add(public_selection_XTabLayout.getTabAt(1).findViewById(R.id.item_tablayout_text));
            if (fragmentList.size() == 3) {
                textViewList.add(public_selection_XTabLayout.getTabAt(2).findViewById(R.id.item_tablayout_text));
            }
            if (OPTION == LIUSHUIJIELU && Extra) {
                textViewList.get(1).setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
            } else {
                textViewList.get(0).setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
            }
            initListener();
        }
    }

    @OnClick({R.id.public_sns_topbar_back})
    public void onOptionClick(View view) {
        finish();
    }

    private void initListener() {
        activity_baseoption_viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textViewList.get(position).setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                textViewList.get(Old_position).setTextColor(ContextCompat.getColor(activity, R.color.black));
                Old_position = position;
                setEditUiShow();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        MyToash.setDelayedHandle(600, new MyToash.DelayedHandle() {
            @Override
            public void handle() {
                setEditUiShow();
            }
        });
    }

    private void setEditUiShow() {
        if (OPTION == DOWN) {
            String strType = Constant.getProductTypeList(activity).get(Old_position);
            switch (strType) {
                case "1":
                    public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, !Edit1 ? R.string.app_edit : R.string.app_cancle));
                    break;
                case "2":
                    public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, !Edit2 ? R.string.app_edit : R.string.app_cancle));
                    break;
                case "3":
                    public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, !Edit3 ? R.string.app_edit : R.string.app_cancle));
                    break;
            }
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

    @Override
    public void finish() {
        super.finish();
        if(MainFragmentTabUtils.UserCenterRefarsh = true) {
            MainFragmentTabUtils.UserCenterRefarsh = false;
            EventBus.getDefault().post(new RefreshMine());
        }
    }
}
