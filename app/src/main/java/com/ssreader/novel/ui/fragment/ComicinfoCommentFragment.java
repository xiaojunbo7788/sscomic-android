package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.BaseLabelBean;
import com.ssreader.novel.model.BaseTag;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.ui.activity.CommentActivity;
import com.ssreader.novel.ui.adapter.CommentAdapter;
import com.ssreader.novel.ui.adapter.PublicMainAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.AutoTextView;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.LanguageUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class ComicinfoCommentFragment extends BaseFragment {

    @BindViews({R.id.comic_info_layout1, R.id.comic_info_layout2, R.id.comic_info_layout3,
            R.id.comic_info_layout4, R.id.comic_info_layout5, R.id.comic_info_layout6,
            R.id.comic_info_layout7})
    List<LinearLayout> infoLayout;
    @BindViews({R.id.comic_info_text_layout1, R.id.comic_info_text_layout2, R.id.comic_info_text_layout3})
    List<TagFlowLayout> layouts;
    @BindViews({R.id.comic_info_text4, R.id.comic_info_text5, R.id.comic_info_text6, R.id.comic_info_text7})
    List<TextView> textViews;

    @BindView(R.id.activity_book_info_content_comment_container)
    public RecyclerView fragment_comment_rcy;
    @BindView(R.id.activity_book_info_content_comment_des)
    public AutoTextView activity_book_info_content_comment_des;

    @BindView(R.id.activity_book_info_content_label_line)
    View likeLine;
    @BindView(R.id.activity_book_info_content_label_container)
    public RecyclerView fragment_like_rcy;

    @BindView(R.id.list_ad_view_layout)
    FrameLayout list_ad_view_layout;

    @BindView(R.id.activity_Book_info_content_comment_more_text)
    TextView activity_Book_info_content_comment_more_text;
    @BindView(R.id.activity_book_info_content_add_comment)
    public TextView activity_book_info_content_add_comment;

    private BaseBookComic baseComic;
    private List<BaseLabelBean> labelBeans = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.fragment_comicinfo_comment;
    }

    @Override
    public void initView() {

    }

    public ComicinfoCommentFragment() {

    }

    @OnClick({R.id.activity_book_info_content_add_comment, R.id.activity_Book_info_content_comment_more_text})
    public void onCommentClick(View view) {
        intentComment(null);
    }

    @Override
    public void initData() {
        activity_Book_info_content_comment_more_text.setBackground(MyShape.setMyshapeStroke2(activity,
                20, 2, ContextCompat.getColor(activity, R.color.maincolor), 0));
    }

    @Override
    public void initInfo(String json) {

    }

    public void setComicInfo(BaseBookComic comicInfo) {
        if (comicInfo != null) {
            if (!TextUtils.isEmpty(comicInfo.author)) {
                layouts.get(0).removeAllViews();
                infoLayout.get(0).setVisibility(View.VISIBLE);
                String[] temp = comicInfo.author.split(",");
                List<String> labels = new ArrayList<>();
                for (String s : temp) {
                    if (!TextUtils.isEmpty(s)) {
                        labels.add(s);
                    }
                }
                setTag(layouts.get(0), labels);
            } else {
                infoLayout.get(0).setVisibility(View.GONE);
            }

            if (comicInfo.tag != null && !comicInfo.tag.isEmpty()) {
                layouts.get(1).removeAllViews();
                infoLayout.get(1).setVisibility(View.VISIBLE);
                List<String> labels = new ArrayList<>();
                for (BaseTag baseTag : comicInfo.tag) {
                    if (baseTag != null && !TextUtils.isEmpty(baseTag.getTab())) {
                        labels.add(baseTag.getTab());
                    }
                }
                setTag(layouts.get(1), labels);
            } else {
                infoLayout.get(1).setVisibility(View.GONE);
            }

            if (comicInfo.tags != null && !comicInfo.tags.isEmpty()) {
                layouts.get(2).removeAllViews();
                infoLayout.get(2).setVisibility(View.VISIBLE);
                List<String> labels = new ArrayList<>();
                for (String text : comicInfo.tags) {
                    if (!TextUtils.isEmpty(text)) {
                        labels.add(text);
                    }
                }
                setTag(layouts.get(2), labels);
            } else {
                infoLayout.get(2).setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(comicInfo.original)) {
                infoLayout.get(3).setVisibility(View.VISIBLE);
                textViews.get(0).setText(comicInfo.original);
                textViews.get(0).setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 15),
                        ContextCompat.getColor(activity, R.color.graybg)));
            } else {
                infoLayout.get(3).setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(comicInfo.sinici)) {
                infoLayout.get(4).setVisibility(View.VISIBLE);
                textViews.get(1).setText(comicInfo.sinici);
                textViews.get(1).setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 15),
                        ContextCompat.getColor(activity, R.color.graybg)));
            } else {
                infoLayout.get(4).setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(comicInfo.created_at)) {
                infoLayout.get(5).setVisibility(View.VISIBLE);
                textViews.get(2).setText(comicInfo.created_at);
            } else {
                infoLayout.get(5).setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(comicInfo.last_chapter_time)) {
                infoLayout.get(6).setVisibility(View.VISIBLE);
                textViews.get(3).setText(comicInfo.last_chapter_time);
            } else {
                infoLayout.get(6).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置流式布局
     * @param tagFlowLayout
     * @param labels
     */
    private void setTag(TagFlowLayout tagFlowLayout, List<String> labels) {
        tagFlowLayout.setAdapter(new TagAdapter<String>(labels) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(activity)
                        .inflate(R.layout.item_info_tv, tagFlowLayout, false);
                textView.setText(s);
                textView.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 15),
                        ContextCompat.getColor(activity, R.color.graybg)));
                return textView;
            }
        });
    }

    /**
     * 设置数据
     *
     * @param baseComic
     * @param bookInfoComments
     * @param stroreComicLable
     * @param baseAd
     * @param total_comment
     */
    public void senddata(BaseBookComic baseComic, List<Comment> bookInfoComments,
                         BaseLabelBean stroreComicLable, BaseAd baseAd, int total_comment) {
        if (stroreComicLable != null) {
            labelBeans.clear();
            labelBeans.add(stroreComicLable);
        }
        likeLine.setVisibility(labelBeans.isEmpty() ? View.GONE : View.VISIBLE);
        this.baseComic = baseComic;
        activity_book_info_content_comment_des.setAutoText(baseComic.description, null);
        if (baseAd != null) {
            list_ad_view_layout.setVisibility(View.VISIBLE);
            baseAd.setAd(activity, list_ad_view_layout, 1);
        } else {
            list_ad_view_layout.setVisibility(View.GONE);
        }
        setComment(bookInfoComments, total_comment);
    }

    /**
     * 更新评论数据
     *
     * @param bookInfoComments
     * @param size
     */
    public void setComment(List<Comment> bookInfoComments, int size) {
        if (bookInfoComments != null && !bookInfoComments.isEmpty()) {
            CommentAdapter commentAdapter = new CommentAdapter(activity, bookInfoComments, new SCOnItemClickListener<Comment>() {
                @Override
                public void OnItemClickListener(int flag, int position, Comment comment) {
                    intentComment(comment);
                }

                @Override
                public void OnItemLongClickListener(int flag, int position, Comment O) {

                }
            });
            fragment_comment_rcy.setLayoutManager(new LinearLayoutManager(activity));
            fragment_comment_rcy.setAdapter(commentAdapter);
        }
        if (!activity_Book_info_content_comment_more_text.isShown()) {
            activity_Book_info_content_comment_more_text.setVisibility(View.VISIBLE);
        }
        if (size > 0) {
            activity_Book_info_content_comment_more_text.setText(String.format(
                    LanguageUtil.getString(activity, R.string.CommentListActivity_lookpinglun), size));
        } else {
            activity_Book_info_content_comment_more_text.setText(
                    LanguageUtil.getString(activity, R.string.CommentListActivity_no_pinglun));
        }
        fragment_like_rcy.setLayoutManager(new LinearLayoutManager(activity));
        PublicMainAdapter bookStoareAdapter = new PublicMainAdapter(labelBeans, 1, activity, false, false);
        fragment_like_rcy.setAdapter(bookStoareAdapter);
    }

    /**
     * 打开评论界面
     *
     * @param comment
     */
    private void intentComment(Comment comment) {
        Intent intent = new Intent(activity, CommentActivity.class);
        if (comment != null) {
            intent.putExtra("comment", comment);
        }
        intent.putExtra("current_id", baseComic.comic_id);
        intent.putExtra("productType", Constant.COMIC_CONSTANT);
        startActivity(intent);
    }
}
