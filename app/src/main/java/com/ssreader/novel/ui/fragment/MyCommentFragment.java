package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;

import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.model.MyCommentItem;
import com.ssreader.novel.net.HttpUtils;

import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.CommentInfoActivity;
import com.ssreader.novel.ui.activity.LoginActivity;
import com.ssreader.novel.ui.adapter.MyCommentAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;

import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.UserUtils;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Api.AUDIO_COMMENT;
import static com.ssreader.novel.constant.Api.COMIC_comment_list_MY;
import static com.ssreader.novel.constant.Api.mUserCommentsUrl;

/**
 * 我的评论界面
 */
public class MyCommentFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView fragment_option_listview;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout fragment_option_noresult;
    @BindView(R.id.fragment_option_noresult_text)
    TextView noResult;
    @BindView(R.id.fragment_option_noresult_try)
    TextView goLogin;

    private int PRODUCT;
    // 适配器
    private List<Comment> commentList;
    private MyCommentAdapter commentAdapter;

    public MyCommentFragment(int PRODUCT) {
        this.PRODUCT = PRODUCT;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_readhistory;
    }

    @Override
    public void initView() {
        commentList = new ArrayList<>();
        initSCRecyclerView(fragment_option_listview, RecyclerView.VERTICAL, 0);
        goLogin.setText(LanguageUtil.getString(activity, R.string.app_login_now));
        goLogin.setBackground(MyShape.setMyshapeStroke(activity, ImageUtil.dp2px(activity, 1),
                1, ContextCompat.getColor(activity, R.color.maincolor)));
        commentAdapter = new MyCommentAdapter(activity, commentList);
        fragment_option_listview.setAdapter(commentAdapter, true);
        setNoResult(true);
        initListener();
    }

    @OnClick({R.id.fragment_option_noresult_try})
    public void onClick(View view) {
        startActivity(new Intent(activity, LoginActivity.class));
    }

    private void initListener() {
        commentAdapter.setOnCommentListener(new MyCommentAdapter.OnCommentListener() {
            @Override
            public void onClick(Comment bean) {
                Intent intent = new Intent();
                intent.putExtra("type", PRODUCT);
                intent.putExtra("comment_id", bean.comment_id);
                intent.setClass(activity, CommentInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        String URL = "";
        if (PRODUCT == Constant.BOOK_CONSTANT) {
            URL = mUserCommentsUrl;
        } else if (PRODUCT == Constant.COMIC_CONSTANT) {
            URL = COMIC_comment_list_MY;
        } else if (PRODUCT == Constant.AUDIO_CONSTANT) {
            URL = AUDIO_COMMENT;
        }
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("page_num", current_page);
        HttpUtils.getInstance().sendRequestRequestParams(activity,URL, readerParams.generateParamsJson(),responseListener);
    }

    @Override
    public void initInfo(String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        MyCommentItem optionItem = gson.fromJson(json, MyCommentItem.class);
        if (optionItem.current_page <= optionItem.total_page && !optionItem.list.isEmpty()) {
            if (current_page == 1) {
                commentList.clear();
                commentAdapter.NoLinePosition=-1;
                fragment_option_listview.setLoadingMoreEnabled(true);
            }
            commentList.addAll(optionItem.list);
        }
        if (!commentList.isEmpty()&&optionItem.current_page >= optionItem.total_page) {
            commentAdapter.NoLinePosition = commentList.size() - 1;
            fragment_option_listview.setLoadingMoreEnabled(false);
        }
        if (commentList.isEmpty()) {
            setNoResult(true);
        } else {
            setNoResult(false);
        }
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 登录回调
     *
     * @param refreshMine
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (UserUtils.isLogin(activity)) {
            setNoResult(false);
            current_page = 1;
            initData();
        }
    }

    /**
     * 设置没有内容时的UI
     *
     * @param isShow
     */
    private void setNoResult(boolean isShow) {
        if (isShow) {
            fragment_option_listview.setVisibility(View.GONE);
            fragment_option_noresult.setVisibility(View.VISIBLE);
            if (!UserUtils.isLogin(activity)) {
                goLogin.setVisibility(View.VISIBLE);
                noResult.setText(LanguageUtil.getString(activity, R.string.app_comment_no_login));
            } else {
                goLogin.setVisibility(View.GONE);
                noResult.setText(LanguageUtil.getString(activity, R.string.app_no_pinglun));
            }
        } else {
            fragment_option_noresult.setVisibility(View.GONE);
            fragment_option_listview.setVisibility(View.VISIBLE);
        }
    }
}
