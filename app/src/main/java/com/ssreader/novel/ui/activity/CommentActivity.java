package com.ssreader.novel.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.BookBottomTabRefresh;
import com.ssreader.novel.eventbus.CommentRefresh;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.model.CommentItem;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.CommentAdapter;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.Input;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;

import static com.ssreader.novel.constant.Api.COMIC_comment_list;
import static com.ssreader.novel.constant.Api.mCommentListUrl;
import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;

/**
 * 发送评论界面
 */
public class CommentActivity extends BaseActivity {

    @BindView(R.id.public_recycleview)
    SCRecyclerView publicRecycleview;
    @BindView(R.id.public_sns_topbar_title)
    TextView activityTitle;

    @BindView(R.id.activity_comment_list_add_comment)
    EditText activity_comment_list_add_comment;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout noResultLayout;
    @BindView(R.id.fragment_option_noresult_text)
    TextView fragment_option_noresult_text;

    // 点击单独评论进入界面时携带的数据
    private Comment infoComment;
    // 用户是否点击评论进入
    private boolean isComicClickComment;
    private long currentId, chapterId;
    private int productType;
    // 界面数据
    private int commentCount;
    private List<Comment> commentList;
    private CommentAdapter mAdapter;
    private CommentItem commentItem;

    @Override
    public int initContentView() {
        FULL_CCREEN = true;
        USE_EventBus = true;
        USE_PUBLIC_BAR=true;
        return R.layout.activity_comment;
    }

    @Override
    public void initView() {
        fragment_option_noresult_text.setText(LanguageUtil.getString(activity,R.string.app_no_pinglun));
        infoComment = (Comment) getIntent().getSerializableExtra("comment");
        currentId = getIntent().getLongExtra("current_id", 0);
        chapterId = getIntent().getLongExtra("chapter_id", 0);
        productType = getIntent().getIntExtra("productType", 0);
        isComicClickComment = getIntent().getBooleanExtra("is_from_comic", false);
        // recyclerView
        initSCRecyclerView(publicRecycleview, RecyclerView.VERTICAL, 0);
        commentList = new ArrayList<>();
        activity_comment_list_add_comment.setBackground(MyShape.setMyCustomizeShape(activity, 20, R.color.white));
        mAdapter = new CommentAdapter(false,activity, commentList, scOnItemClickListener);
        publicRecycleview.setAdapter(mAdapter, true);
        if (isComicClickComment) {
            activityTitle.setText(LanguageUtil.getString(activity, R.string.CommentListActivity_title));
        } else {
            if (chapterId == 0) {
                activityTitle.setText(String.format(LanguageUtil.getString(activity, R.string.commentListActivityBookPing), "--"));
            } else {
                activityTitle.setText(String.format(LanguageUtil.getString(activity, R.string.audio_comment), "--"));
            }
        }
        init();
        initListener();
    }

    private void initListener() {
        activity_comment_list_add_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s) && infoComment != null) {
                    // 取消回复状态
                    activity_comment_list_add_comment.setHint(LanguageUtil.getString(activity, R.string.CommentListActivity_some));
                    infoComment = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 回复
     */
    SCOnItemClickListener scOnItemClickListener = new SCOnItemClickListener<Comment>() {
        @Override
        public void OnItemClickListener(int flag, int position, Comment comment) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            activity_comment_list_add_comment.requestFocus();
            activity_comment_list_add_comment.setHint(LanguageUtil.getString(activity, R.string.CommentListActivity_huifu) + comment.getNickname());
            infoComment = comment;
        }

        @Override
        public void OnItemLongClickListener(int flag, int position, Comment o) {

        }
    };

    private void init() {
        publicRecycleview.setVisibility(View.GONE);
        noResultLayout.setVisibility(View.VISIBLE);
        activity_comment_list_add_comment.setHorizontallyScrolling(false);
        activity_comment_list_add_comment.setMaxLines(Integer.MAX_VALUE);
        activity_comment_list_add_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String str = activity_comment_list_add_comment.getText().toString();
                    if (TextUtils.isEmpty(str) || Pattern.matches("\\s*", str)) {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.CommentListActivity_some));
                        return true;
                    }
                    String commentId = "";
                    if (infoComment != null) {
                        commentId = infoComment.comment_id;
                    }
                    sendComment(activity, true, productType, currentId, commentId, chapterId, str, new SendSuccess() {
                        @Override
                        public void Success(Comment comment) {
                            activity_comment_list_add_comment.setText("");
                            if (comment != null && comment.comment_id != null) {
                                if (comment.comment_num > 0) {
                                    commentCount = comment.comment_num;
                                    if (chapterId == 0) {
                                        if (!isComicClickComment) {
                                            activityTitle.setText(String.format(LanguageUtil.getString(activity, R.string.commentListActivityBookPing), commentCount));
                                        }
                                    } else {
                                        if (!isComicClickComment) {
                                            activityTitle.setText(String.format(LanguageUtil.getString(activity, R.string.audio_comment), commentCount));
                                        }
                                        if (productType == BOOK_CONSTANT) {
                                            EventBus.getDefault().post(new BookBottomTabRefresh(3, commentCount + "", chapterId));
                                        }
                                    }
                                }
                                if (infoComment != null) {
                                    commentList.add(0, comment);
                                    mAdapter.notifyDataSetChanged();
                                    if (commentList.isEmpty()) {
                                        noResultLayout.setVisibility(View.VISIBLE);
                                        publicRecycleview.setVisibility(View.GONE);
                                    } else {
                                        noResultLayout.setVisibility(View.GONE);
                                        publicRecycleview.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    current_page = 1;
                                    initData();
                                }
                            }
                        }
                    });
                    Input.getInstance().hindInput(activity_comment_list_add_comment, activity);
                    // 回复状态取消
                    infoComment = null;
                    return true;
                }
                return false;
            }
        });
        if (infoComment != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            activity_comment_list_add_comment.requestFocus();
            activity_comment_list_add_comment.setHint(LanguageUtil.getString(activity, R.string.CommentListActivity_huifu)
                    + infoComment.nickname);
        }
    }

    @Override
    public void initData() {
        String url = "";
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("page_num", current_page);
        if (chapterId != 0) {
            readerParams.putExtraParams("chapter_id", chapterId + "");
        }
        if (productType == BOOK_CONSTANT) {
            url = mCommentListUrl;
            readerParams.putExtraParams("book_id", currentId + "");
        } else if (productType == COMIC_CONSTANT) {
            url = COMIC_comment_list;
            readerParams.putExtraParams("comic_id", currentId + "");
        } else if (productType == AUDIO_CONSTANT) {
            url = Api.AUDIO_CHAPTER_COMMENT_LIST;
            readerParams.putExtraParams("audio_id", currentId + "");
        }
        HttpUtils.getInstance().sendRequestRequestParams(activity,url, readerParams.generateParamsJson(),  responseListener);
    }

    @Override
    public void initInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            commentItem = gson.fromJson(json, CommentItem.class);
            if(commentItem != null) {
                commentCount = commentItem.total_count;
                if (!isComicClickComment) {
                    if (chapterId == 0) {
                        activityTitle.setText(String.format(LanguageUtil.getString(activity, R.string.commentListActivityBookPing), commentCount));
                    } else {
                        activityTitle.setText(String.format(LanguageUtil.getString(activity, R.string.audio_comment), commentCount));
                    }
                }
                if (!commentItem.list.isEmpty() && commentItem.current_page <= commentItem.total_page) {
                    if (current_page == 1) {
                        mAdapter.NoLinePosition=-1;
                        publicRecycleview.setLoadingMoreEnabled(true);
                        commentList.clear();
                    }
                    commentList.addAll(commentItem.list);
                }
                // 用于判断是否显示“没有更多了”
                if (!commentList.isEmpty() && commentItem.current_page >= commentItem.total_page) {
                    mAdapter.NoLinePosition=commentList.size()-1;
                    publicRecycleview.setLoadingMoreEnabled(false);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
        if (commentList.isEmpty()) {
            publicRecycleview.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
        } else {
            noResultLayout.setVisibility(View.GONE);
            publicRecycleview.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }

    /**
     * 发送评论
     *
     * @param activity
     * @param isSelf
     * @param productType
     * @param id
     * @param mCommentId
     * @param chapter_id
     * @param content
     * @param sendSuccess
     */
    public static void sendComment(Activity activity, boolean isSelf, int productType, long id, String mCommentId,
                                   long chapter_id, String content, SendSuccess sendSuccess) {
        if (!LoginUtils.goToLogin(activity)) {
            return;
        }
        String url = "";
        ReaderParams params = new ReaderParams(activity);
        if (productType == BOOK_CONSTANT) {
            params.putExtraParams("book_id", id);
            url = Api.mCommentPostUrl;
            if (chapter_id != 0) {
                params.putExtraParams("chapter_id", chapter_id);
            }
        } else if (productType == COMIC_CONSTANT) {
            params.putExtraParams("comic_id", id);
            url = Api.COMIC_sendcomment;
        } else if (productType == AUDIO_CONSTANT) {
            params.putExtraParams("audio_id", id);
            if (chapter_id != 0) {
                params.putExtraParams("chapter_id", chapter_id);
            }
            url = Api.AUDIO_CHAPTER_COMMENT_POST;
        }
        if (!TextUtils.isEmpty(mCommentId)) {
            params.putExtraParams("comment_id", mCommentId);
        }
        params.putExtraParams("content", content);
        HttpUtils.getInstance().sendRequestRequestParams(activity, url, params.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                MyToash.ToashSuccess(activity, R.string.CommentListActivity_success);
                EventBus.getDefault().post(new RefreshMine());
                EventBus.getDefault().post(new CommentRefresh(productType, id));
                if (sendSuccess != null) {
                    sendSuccess.Success(HttpUtils.getGson().fromJson(response, Comment.class));
                }
            }

            @Override
            public void onErrorResponse(String ex) {
                if (sendSuccess != null) {
                    sendSuccess.Success(null);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.putExtra("comment_count", commentCount);
            setResult(111, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0, R.anim.activity_top_bottom_close);
    }

    public interface SendSuccess {

        void Success(Comment comment);
    }
}
