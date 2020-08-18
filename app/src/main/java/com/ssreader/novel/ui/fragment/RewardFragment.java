package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.BookBottomTabRefresh;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.Announce;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.GiftListBean;
import com.ssreader.novel.model.RewardDateBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.NewRechargeActivity;
import com.ssreader.novel.ui.adapter.DialogRewardAdapter;
import com.ssreader.novel.ui.adapter.RewardGiftViewPagerAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.TextStyleUtils;
import com.ssreader.novel.ui.view.MarqueeTextView;
import com.ssreader.novel.ui.view.MarqueeTextViewClickListener;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.constant.Constant.getCurrencyUnit;

public class RewardFragment extends BaseFragment {

    @BindView(R.id.dialog_total_month_ticket)
    TextView dialogTotalMonthTicket;
    @BindView(R.id.dialog_directions_img)
    ImageView dialogDirectionsImg;
    @BindView(R.id.dialog_month_ticket_content)
    TextView dialogMonthTicketContent;
    @BindView(R.id.fragment_reward_mqTV)
    MarqueeTextView fragmentRewardMqTV;
    @BindView(R.id.fragment_reward_Indicator)
    LinearLayout fragmentRewardIndicator;
    @BindView(R.id.fragment_reward_viewpager)
    ViewPager fragmentRewardViewpager;
    @BindView(R.id.dialog_month_foot_layout)
    LinearLayout dialog_month_foot_layout;
    @BindView(R.id.dialog_month_yue_money)
    TextView dialog_month_yue_money;
    @BindView(R.id.dialog_month_yue_money_layout)
    LinearLayout dialog_month_yue_money_layout;
    @BindView(R.id.fragment_reward_mqlayout)
    RelativeLayout fragment_reward_mqlayout;
    @BindView(R.id.dialog_month_ticket_btn)
    TextView dialog_month_ticket_btn;
    private List<GridView> gridList;
    private List<View> IndicatorImageView;
    private List<List<GiftListBean>> snsgift;
    private List<DialogRewardAdapter> adapterList;
    private int item_grid_num = 4;//每一页中GridView中item的数量
    private int number_columns = 4;//gridview一行展示的数目
    private int gift_id;
    private long book_id;
    private RewardGiftViewPagerAdapter squareGiftViewPagerAdapter;
    private DialogFragment dialogFragment;

    public RewardFragment() {
    }

    public RewardFragment(long book_id, DialogFragment dialogFragment) {
        this.book_id = book_id;
        this.dialogFragment = dialogFragment;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_reward;
    }

    @Override
    public void initView() {
        dialogDirectionsImg.setVisibility(View.GONE);
        dialog_month_foot_layout.setVisibility(View.GONE);
        dialogMonthTicketContent.setVisibility(View.GONE);
        dialog_month_yue_money_layout.setVisibility(View.VISIBLE);
        dialog_month_ticket_btn.setText(LanguageUtil.getString(activity, R.string.dialog_Reward));
        IndicatorImageView = new ArrayList<>();
        gridList = new ArrayList<>();
        snsgift = new ArrayList<>();
        adapterList = new ArrayList<>();
        squareGiftViewPagerAdapter = new RewardGiftViewPagerAdapter(gridList);
        fragmentRewardViewpager.setAdapter(squareGiftViewPagerAdapter);
        fragment_reward_mqlayout.setBackground(MyShape.setMyshape(60, ContextCompat.getColor(activity, R.color.grayline)));
        fragmentRewardViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < IndicatorImageView.size(); i++) {
                    if (i == position) {
                        IndicatorImageView.get(i).setBackground(MyShape.setMyshape(20, ContextCompat.getColor(activity, R.color.maincolor)));
                    } else {
                        IndicatorImageView.get(i).setBackground(MyShape.setMyshape(20, ContextCompat.getColor(activity, R.color.gray)));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.dialog_month_ticket_btn})
    public void onClick(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.dialog_month_ticket_btn:
                    // 打赏
                    if (LoginUtils.goToLogin(activity)) {
                        rewardGift();
                    }
                    break;
            }
        }
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("book_id", book_id);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.REWARD_LIST, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        gridList.clear();
        IndicatorImageView.clear();
        fragmentRewardIndicator.removeAllViews();
        RewardDateBean rewardDateBean = gson.fromJson(json, RewardDateBean.class);
        int size = rewardDateBean.gift_option.size();
        int pageSize = size % item_grid_num == 0 ? size / item_grid_num : size / item_grid_num + 1;
        for (int i = 0; i < pageSize; i++) {
            MyToash.Log("RewardFragment",i + "  ---- " + pageSize);
            DialogRewardAdapter dialogRewardAdapter;
            GridView gridView = new GridView(activity);
            gridView.setVerticalSpacing(ImageUtil.dp2px(activity, 5));
            gridView.setColumnWidth(ScreenSizeUtils.getInstance(activity).getScreenWidth() / 4);
            List<GiftListBean> snsGifts = new ArrayList<>();
            gift_id = rewardDateBean.gift_option.get(0).getGift_id();
            rewardDateBean.gift_option.get(0).chose = true;
            if (i < pageSize - 1) {
                snsGifts.addAll(rewardDateBean.gift_option.subList(number_columns * i, (i + 1) * number_columns));
            } else {
                snsGifts.addAll(rewardDateBean.gift_option.subList(number_columns * i, size));
            }
            dialogRewardAdapter = new DialogRewardAdapter(activity, snsGifts);
            snsgift.add(snsGifts);
            adapterList.add(dialogRewardAdapter);
            gridView.setHorizontalSpacing(ImageUtil.dp2px(activity, 5));
            gridView.setNumColumns(number_columns);
            gridView.setSelector(R.color.transparent);
            gridView.setAdapter(dialogRewardAdapter);
            int finalI = i;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i1, long l) {
                    gift_id = snsGifts.get(i1).getGift_id();
                    for (GiftListBean data : snsGifts) {
                        data.chose = false;
                    }
                    snsGifts.get(i1).chose = true;
                    dialogRewardAdapter.notifyDataSetChanged();
                    if(finalI==0){
                        if(snsgift.size()>1){
                            for (GiftListBean giftListBean : snsgift.get(1)) {
                                giftListBean.chose = false;
                            }
                            adapterList.get(1).notifyDataSetChanged();
                        }
                    }else{
                        if(!snsgift.isEmpty()){
                            for (GiftListBean giftListBean : snsgift.get(0)) {
                                giftListBean.chose = false;
                            }
                            adapterList.get(0).notifyDataSetChanged();
                        }
                    }
                }
            });
            gridList.add(gridView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            View view = new View(activity);
            if (i == 0) {
                view.setBackground(MyShape.setMyshape(20, ContextCompat.getColor(activity, R.color.maincolor)));
            } else {
                view.setBackground(MyShape.setMyshape(20, ContextCompat.getColor(activity, R.color.gray)));
            }
            layoutParams.rightMargin = ImageUtil.dp2px(activity, 5);
            layoutParams.width = ImageUtil.dp2px(activity, 5);
            layoutParams.height = ImageUtil.dp2px(activity, 5);
            IndicatorImageView.add(view);
            fragmentRewardIndicator.addView(view, layoutParams);
            squareGiftViewPagerAdapter.notifyDataSetChanged();
            if (rewardDateBean.broadcast_list != null && !rewardDateBean.broadcast_list.isEmpty()) {
                List<Announce> announces = new ArrayList<>();
                for (String s : rewardDateBean.broadcast_list) {
                    announces.add(new Announce(s));
                }
                fragmentRewardMqTV.setTextArraysAndClickListener(announces, new MarqueeTextViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                });
            }
        }
        String money = rewardDateBean.user.getGoldRemain() + Constant.getCurrencyUnit(activity);
        int length = rewardDateBean.user.getGoldRemain().length();
        new TextStyleUtils(activity, money, dialog_month_yue_money)
                .setColorSpan(ContextCompat.getColor(activity, R.color.maincolor), 0, length)
                .setSpanner();
    }

    public void rewardGift() {
        Book book = ObjectBoxUtils.getBook(book_id);
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("gift_id", gift_id);
        readerParams.putExtraParams("book_id", book_id);
        readerParams.putExtraParams("chapter_id", book.current_chapter_id);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.REWARD_GIFT_SEND, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.dialog_reward_success));
                EventBus.getDefault().post(new RefreshMine());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        if (jsonObject.has("reward_num")) {
                            EventBus.getDefault().post(new BookBottomTabRefresh(1, jsonObject.getString("reward_num")));
                        }
                    } catch (JSONException e) {
                    }
                }
                dialogFragment.dismiss();
            }

            @Override
            public void onErrorResponse(String ex) {
                if (!TextUtils.isEmpty(ex) && ex.equals("802")) {
                    activity.startActivity(new Intent(activity, NewRechargeActivity.class)
                            .putExtra("RechargeTitle", getCurrencyUnit(activity) + LanguageUtil.getString(activity, R.string.MineNewFragment_chongzhi))
                            .putExtra("RechargeRightTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_chongzhijilu))
                            .putExtra("RechargeType", "gold"));
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        initData();
    }
}
