package com.ssreader.novel.ui.activity;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.CommentInfoBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.CommentInfoAdapter;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;

/**
 * 我的评论详情
 */
public class CommentInfoActivity extends BaseActivity {

    @BindView(R.id.public_recycleview)
    SCRecyclerView recyclerView;

    private int pageType;
    private String commentId = "";

    private ViewHolder viewHolder;
    private CommentInfoAdapter commentInfoAdapter;
    private List<CommentInfoBean.CommentInfo> commentInfoList;
    private CommentInfoBean currentCommentInfoBean;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.CommentInfoActivity_title;
        return R.layout.activity_comment_info;
    }

    @Override
    public void initView() {
        pageType = formIntent.getIntExtra("type", 0);
        commentId = formIntent.getStringExtra("comment_id");
        initSCRecyclerView(recyclerView, RecyclerView.VERTICAL, 0);
        View view = LayoutInflater.from(this).inflate(R.layout.head_comment_info, null);
        viewHolder = new ViewHolder(view);

        ViewGroup.LayoutParams layoutParamsCbg = viewHolder.head_comment_info_book_bg.getLayoutParams();
        layoutParamsCbg.width = (ScreenSizeUtils.getInstance(activity).getScreenWidth()) / 5;
        layoutParamsCbg.height = layoutParamsCbg.width * 4 / 3;
        viewHolder.head_comment_info_book_bg.setLayoutParams(layoutParamsCbg);
        recyclerView.addHeaderView(view);
        commentInfoList = new ArrayList<>();
        commentInfoAdapter = new CommentInfoAdapter(activity, commentInfoList);
        recyclerView.setAdapter(commentInfoAdapter);
    }

    @Override
    public void initData() {
        if (!TextUtils.isEmpty(commentId)) {
            readerParams = new ReaderParams(activity);
            readerParams.putExtraParams("page_num", current_page);
            readerParams.putExtraParams("type", pageType + 1);
            readerParams.putExtraParams("comment_id", commentId);
            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.COMMENT_INFO, readerParams.generateParamsJson(),  responseListener);
        }
    }

    @Override
    public void initInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            CommentInfoBean commentInfoBean = HttpUtils.getGson().fromJson(json, CommentInfoBean.class);
            if (commentInfoBean.getCurrent_page() == 1) {
                // 第一页
                currentCommentInfoBean = commentInfoBean;
                setViewHolder(currentCommentInfoBean);
            }
            if (commentInfoBean.current_page <= commentInfoBean.total_page && !commentInfoBean.getList().isEmpty()) {
                if (commentInfoBean.getCurrent_page() == 1) {
                    commentInfoList.clear();
                }
                commentInfoList.addAll(commentInfoBean.getList());
                commentInfoAdapter.notifyDataSetChanged();
            }
            commentInfoAdapter.notifyDataSetChanged();
        }
    }

    public class ViewHolder {

        @BindViews({R.id.head_comment_info_head_image, R.id.head_comment_info_book_cover, R.id.head_comment_info_vip_image,
                R.id.head_comment_info_audio_mark})
        List<ImageView> imageViews;
        @BindViews({R.id.head_comment_info_name, R.id.head_comment_info_time, R.id.head_comment_info_context,
                R.id.head_comment_info_book_name, R.id.head_comment_info_book_author, R.id.head_comment_info_comment_num})
        List<TextView> textViews;
        @BindView(R.id.head_comment_info_context_reply_info)
        TextView replyInfo;

        @BindView(R.id.head_comment_info_book_bg)
        View head_comment_info_book_bg;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            if (pageType == AUDIO_CONSTANT) {
                imageViews.get(3).setVisibility(View.VISIBLE);
            } else {
                imageViews.get(3).setVisibility(View.GONE);
            }
        }

        @OnClick({R.id.head_comment_info_book_layout})
        public void onCommentClick(View view) {
            // 跳转书籍详情
            if (currentCommentInfoBean != null) {
                Intent intent = new Intent();
                if (pageType == BOOK_CONSTANT) {
                    intent.putExtra("book_id", currentCommentInfoBean.getBook_id());
                    intent.setClass(activity, BookInfoActivity.class);
                } else if (pageType == COMIC_CONSTANT) {
                    intent.putExtra("comic_id", currentCommentInfoBean.getComic_id());
                    intent.setClass(activity, ComicInfoActivity.class);
                } else if (pageType == AUDIO_CONSTANT) {
                    intent.putExtra("audio_id", currentCommentInfoBean.getAudio_id());
                    intent.setClass(activity, AudioInfoActivity.class);
                }
                startActivity(intent);
            }
        }
    }

    /**
     * 设置头部view
     *
     * @param infoBean
     */
    private void setViewHolder(CommentInfoBean infoBean) {
        if (infoBean != null) {
            MyGlide.GlideCicleHead(activity, infoBean.getAvatar(), viewHolder.imageViews.get(0));
            MyGlide.glideNoPlaceholder(activity, infoBean.getCover(), viewHolder.imageViews.get(1));
            if (infoBean.getIs_vip() == 1) {
                viewHolder.imageViews.get(2).setVisibility(View.VISIBLE);
            } else {
                viewHolder.imageViews.get(2).setVisibility(View.GONE);
            }
            viewHolder.textViews.get(0).setText(infoBean.getNickname());
            viewHolder.textViews.get(1).setText(infoBean.getTime());
            viewHolder.textViews.get(2).setText(infoBean.getContent());
            viewHolder.textViews.get(3).setText(infoBean.getBook_name());
            viewHolder.textViews.get(4).setText(infoBean.getAuthor());
            viewHolder.textViews.get(5).setText(String.format(LanguageUtil.getString(activity,
                    R.string.CommentInfoActivity_replay_sum_num), infoBean.getTotal_count()));
            if (TextUtils.isEmpty(infoBean.getReply_info())) {
                viewHolder.replyInfo.setVisibility(View.GONE);
            } else {
                viewHolder.replyInfo.setVisibility(View.VISIBLE);
                viewHolder.replyInfo.setText(infoBean.getReply_info());
            }
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0, R.anim.activity_top_bottom_close);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }
}
