package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.BookBottomTabRefresh;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.MonthTicketBean;
import com.ssreader.novel.model.MonthTicketListBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.WebViewActivity;
import com.ssreader.novel.ui.adapter.DialogMonthTicketAdapter;
import com.ssreader.novel.ui.dialog.VoteTipsPopWindow;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.TextStyleUtils;
import com.ssreader.novel.ui.view.SizeAnmotionTextview;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MonthTicketFragment extends BaseFragment {

    @BindView(R.id.fragment_dialog_base_head_cover)
    ImageView fragmentDialogBaseHeadCover;
    @BindView(R.id.fragment_dialog_base_head_title)
    TextView fragmentDialogBaseHeadTitle;
    @BindView(R.id.fragment_dialog_base_head_month_tv)
    SizeAnmotionTextview fragmentDialogBaseHeadMonthTv;
    @BindView(R.id.fragment_dialog_base_head_rank_tv)
    SizeAnmotionTextview fragmentDialogBaseHeadRankTv;
    @BindView(R.id.fragment_dialog_base_head_ranking)
    SizeAnmotionTextview fragmentDialogBaseHeadRanking;
    @BindView(R.id.fragment_month_ticket_grv)
    GridView fragmentMonthTicketGrv;
    @BindView(R.id.dialog_total_month_ticket)
    SizeAnmotionTextview dialogTotalMonthTicket;
    @BindView(R.id.dialog_directions_img)
    ImageView dialogDirectionsImg;
    @BindView(R.id.dialog_month_ticket_content)
    SizeAnmotionTextview dialogMonthTicketContent;
    @BindView(R.id.dialog_month_ticket_btn)
    TextView dialog_month_ticket_btn;

    private String url;
    private long current_chapter_id;

    @OnClick({R.id.dialog_month_ticket_btn, R.id.dialog_directions_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_month_ticket_btn:
                if (LoginUtils.goToLogin(activity)) {
                    if (book_id != 0 && current_chapter_id != 0) {
                        vote();
                        VoteTipsPopWindow.setId(book_id, current_chapter_id, num);
                        dialogFragment.dismiss();
                    }
                }
                break;
            case R.id.dialog_directions_img:
                // 月票说明
                if (!url.isEmpty()) {
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("title", LanguageUtil.getString(activity, R.string.Activity_Monthly_Web_title));
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                break;
        }
    }

    public List<MonthTicketListBean> list;
    public int num;
    private DialogMonthTicketAdapter dialogMonthTicketAdapter;
    long book_id;
    DialogFragment dialogFragment;

    public MonthTicketFragment(long book_id, DialogFragment dialogFragment) {
        this.book_id = book_id;
        this.dialogFragment = dialogFragment;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_month_ticket;
    }

    @Override
    public void initView() {
        list = new ArrayList<>();
        current_chapter_id = ObjectBoxUtils.getBook(book_id).current_chapter_id;
        dialogMonthTicketAdapter = new DialogMonthTicketAdapter(activity, list);
        fragmentMonthTicketGrv.setAdapter(dialogMonthTicketAdapter);
        fragmentMonthTicketGrv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (MonthTicketListBean ticketListBean : list) {
                    ticketListBean.isChose = false;
                }
                if (list.get(position).enabled == 1) {
                    list.get(position).isChose = true;
                    num = list.get(position).num;
                    dialogMonthTicketAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("book_id", book_id);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.REWARD_TICKET_OPTION, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        list.clear();
        MonthTicketBean monthTicketBean = gson.fromJson(json, MonthTicketBean.class);
        url = monthTicketBean.ticket_info.ticket_rule;
        MyGlide.GlideImageNoSize(activity, monthTicketBean.ticket_info.cover, fragmentDialogBaseHeadCover);
        fragmentDialogBaseHeadTitle.setText(monthTicketBean.ticket_info.name);
        if (monthTicketBean.ticket_info.can_vote != 1) {
            dialog_month_ticket_btn.setBackgroundResource(R.mipmap.img_foot_btn_noclick);
            dialog_month_ticket_btn.setFocusable(false);
            dialog_month_ticket_btn.setClickable(false);
        }
        fragmentDialogBaseHeadRanking.setText(monthTicketBean.ticket_info.last_distance);
        fragmentDialogBaseHeadRankTv.setMyText(activity, monthTicketBean.ticket_info.rank_tips, 1, ContextCompat.getColor(activity, R.color.black), 1);
        fragmentDialogBaseHeadMonthTv.setMyText(activity, monthTicketBean.ticket_info.current_month_get, 1, ContextCompat.getColor(activity, R.color.black), 1);
        dialogMonthTicketContent.setMyText(activity, monthTicketBean.ticket_info.monthly_tips, 1, ContextCompat.getColor(activity, R.color.maincolor), 0);
        String userRemain = String.format(LanguageUtil.getString(activity, R.string.dialog_have_monthly_pass), monthTicketBean.ticket_info.user_remain + "");

        new TextStyleUtils(activity, userRemain, dialogTotalMonthTicket)
                .setColorSpan(ContextCompat.getColor(activity, R.color.maincolor), 2, userRemain.length() - 2)
                .setSpanner();
        num = monthTicketBean.ticket_option.get(0).num;
        monthTicketBean.ticket_option.get(0).isChose = true;
        list.addAll(monthTicketBean.ticket_option);
        dialogMonthTicketAdapter.notifyDataSetChanged();
    }

    private void vote() {
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("num", num);
        readerParams.putExtraParams("book_id", book_id);
        readerParams.putExtraParams("chapter_id", current_chapter_id);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.REWARD_TICKET_VOTE, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.dialog_vote_success));
                EventBus.getDefault().post(new RefreshMine());
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        if (jsonObject.has("ticket_num")) {
                            EventBus.getDefault().post(new BookBottomTabRefresh(2, jsonObject.getString("ticket_num")));
                        }
                    } catch (JSONException e) {
                    }
                }
                dialogFragment.dismiss();
            }

            @Override
            public void onErrorResponse(String ex) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        initData();
    }
}
