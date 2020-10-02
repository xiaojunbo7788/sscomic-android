package com.ssreader.novel.ui.audio;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzx.starrysky.provider.SongInfo;
import com.umeng.socialize.UMShareAPI;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.AudioListenerChapterRefresh;
import com.ssreader.novel.eventbus.AudioPlayerRefresh;
import com.ssreader.novel.eventbus.AudioPurchaseRefresh;
import com.ssreader.novel.eventbus.AudioSpeedBeanEventbus;
import com.ssreader.novel.eventbus.AudioSwitchRefresh;
import com.ssreader.novel.eventbus.CommentRefresh;
import com.ssreader.novel.eventbus.RefreshAudioShelf;
import com.ssreader.novel.eventbus.RefreshBookInfo;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioBean;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.model.BaseLabelBean;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.eventbus.ChapterBuyRefresh;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.model.CommentItem;
import com.ssreader.novel.model.InfoAudioItem;
import com.ssreader.novel.model.InfoBook;
import com.ssreader.novel.model.InfoBookItem;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.AudioDownActivity;
import com.ssreader.novel.ui.activity.CommentActivity;
import com.ssreader.novel.ui.adapter.CommentAdapter;
import com.ssreader.novel.ui.adapter.PublicMainAdapter;
import com.ssreader.novel.ui.audio.dialog.AudioSpeechSetDialog;
import com.ssreader.novel.ui.audio.fragment.AudioInfoCatalogFragment;
import com.ssreader.novel.ui.audio.dialog.AudioBookCatalogDialogFragment;
import com.ssreader.novel.ui.audio.dialog.AudioTimingDialog;
import com.ssreader.novel.ui.audio.manager.AudioManager;
import com.ssreader.novel.ui.dialog.PublicPurchaseDialog;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.audio.view.AudioLoadingView;
import com.ssreader.novel.ui.audio.view.TimeSeekBar;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.PublicStaticMethod;
import com.ssreader.novel.ui.view.Input;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Api.AUDIO_CHAPTER_COMMENT_LIST;
import static com.ssreader.novel.constant.Api.mBookInfoUrl;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;
import static com.ssreader.novel.constant.Constant.AgainTime;

public class AudioSoundActivity extends BaseActivity {

    @BindView(R.id.public_recycleview)
    SCRecyclerView recyclerView;
    @BindView(R.id.audio_bottom_input)
    EditText audioInput;
    @BindViews({R.id.audio_toolbar_add_book, R.id.audio_bottom_comment_num})
    List<TextView> audioTexts;
    @BindView(R.id.audio_bottom_comment_layout)
    LinearLayout audio_bottom_comment_layout;
    @BindView(R.id.audio_bottom_share)
    LinearLayout audioShareLayout;

    // 头部view
    public ViewHolder viewHolder;

    // 播放管理类
    private AudioManager instance;
    // 管理类监听接口
    private AudioManager.OnCurrentChapterListen onCurrentChapterListen;
    private AudioManager.OnPlayerListener onPlayerListener;
    // 有声
    private long audioId = 0, ClickTime;
    private Audio audio;
    private AudioChapter audioCurrentChapter;
    private List<AudioChapter> audioChapterList = new ArrayList<>();
    // 用于区分是否有关联
    private InfoAudioItem.Relation relation;
    // 书籍信息
    private String bookCover, bookName, bookChapterName;

    // 进度框值
    private int maxProgress;
    // 用于设置提示框的位置
    private int pageWidth, paddingWidth;
    // 用于动态设置提示框的位置
    private RelativeLayout.LayoutParams tipsParams;

    private List<BaseLabelBean> list = new ArrayList<>();

    private PublicMainAdapter bookStoareAdapter;
    private List<Comment> commentList;
    private CommentAdapter commentAdapter;
    private int minSize;
    // 购买弹窗
    private PublicPurchaseDialog purchaseDialog;

    @Override
    public int initContentView() {
        USE_AUDIO_STATUS_LAYOUT = false;
        USE_EventBus = true;
        return R.layout.activity_audio;
    }

    @Override
    public void initView() {
        instance = AudioManager.getInstance(activity);
        instance.setOnCurrentChapterListen(onCurrentChapterListen);
        instance.setOnPlayerListener(onPlayerListener);
        // 设置类型
        paddingWidth = ImageUtil.dp2px(activity, 33);
        pageWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        // 设置recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);
        // 头部View
        View view = layoutInflater.inflate(R.layout.item_audio_head, null);
        recyclerView.addHeaderView(view);
        // 布局样式
        viewHolder = new ViewHolder(view);
        ViewGroup.LayoutParams layoutParams = viewHolder.audio_book_layout.getLayoutParams();
        layoutParams.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        viewHolder.audio_book_layout.setLayoutParams(layoutParams);
        audio_bottom_comment_layout.setBackground(MyShape.setMyshapeStroke2(activity, 20, 1,
                ContextCompat.getColor(activity, R.color.grayline), ContextCompat.getColor(activity, R.color.graybg)));
        if (Constant.USE_SHARE) {
            audioShareLayout.setVisibility(View.VISIBLE);
        } else {
            audioShareLayout.setVisibility(View.GONE);
        }
        if (!InternetUtils.internet(activity)) {
            viewHolder.audio_recycleview.setVisibility(View.GONE);
            viewHolder.likeLine.setVisibility(View.GONE);
            viewHolder.audio_v1.setVisibility(View.GONE);
        }
        audioTexts.get(1).setBackground(MyShape.setMyshape(ImageUtil.dp2px(this, 6),
                ContextCompat.getColor(this, R.color.white)));
        // 评论
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(activity, commentList, new SCOnItemClickListener<Comment>() {
            @Override
            public void OnItemClickListener(int flag, int position, Comment comment) {
                if (comment.comment_id == null) {
                    comment = null;
                }
                IntentComment(comment);
            }

            @Override
            public void OnItemLongClickListener(int flag, int position, Comment O) {

            }
        });
        recyclerView.setAdapter(commentAdapter);
        initListener();
        initComment();
        // 根据类型设置布局
        getAudio(audioId, formIntent.getBooleanExtra("is_jump", false));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        formIntent = intent;
        // 置空
        audioId = 0;
        audio = null;
        relation = null;
        getAudio(audioId, formIntent.getBooleanExtra("is_jump", false));
    }

    /**
     * 事件监听
     */
    private void initListener() {
        // 返回的当前章节信息
        onCurrentChapterListen = new AudioManager.OnCurrentChapterListen() {
            @Override
            public void onCurrentAudioChapter(long id, AudioChapter audioChapter) {
                if (audioChapter != null && audioId == id) {
                    audioCurrentChapter = audioChapter;
                    audio.current_listen_chapter_id = audioChapter.getChapter_id();
                    viewHolder.timeSeekBar.setMaxProgress(0);
                    viewHolder.timeSeekBar.setProgress(0);
                    setLastImageStatus(audioChapter.getLast_chapter() != 0);
                    setNextImageStatus(audioChapter.getNext_chapter() != 0);
                    bookChapterName = audioChapter.getChapter_title();
                    viewHolder.bookTexts.get(0).setText(audioChapter.getChapter_title());
                    initHttpComment((int) audioChapter.getChapter_id());
                    // 用于更新有声详情（续播）
                    EventBus.getDefault().post(new AudioListenerChapterRefresh(true, audioId,
                            audioChapter.getChapter_id(), audioChapter.getChapter_title()));
                    // 用于更新切换时的章节显示
                    if (relation != null && relation.getBook_id() != 0) {
                        EventBus.getDefault().post(new AudioSwitchRefresh(false, relation.getBook_id(),
                                audioChapter.getChapter_id(), audioChapter.getChapter_title()));
                    }
                }
            }

            @Override
            public void onCurrentBookChapter(long id, BookChapter bookChapter) {

            }
        };
        // 播放监听
        onPlayerListener = new AudioManager.OnPlayerListener() {
            @Override
            public void onLoading() {
                if (instance.mAudioId == audioId) {
                    viewHolder.audioPlayerImage.setVisibility(View.GONE);
                    viewHolder.audioLoadingImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStart(int duration) {
                if (instance.mAudioId == audioId && instance.audioCurrentChapter != null && audioCurrentChapter != null &&
                        instance.audioCurrentChapter.chapter_id == audioCurrentChapter.chapter_id) {
                    maxProgress = duration;
                    viewHolder.timeSeekBar.setMaxProgress(maxProgress);
                    setPlayStatus();
                }
            }

            @Override
            public void onProgress(int duration, int progress) {
                // 有声
                if (instance.mAudioId == audioId && instance.audioCurrentChapter != null && audioCurrentChapter != null &&
                        instance.audioCurrentChapter.chapter_id == audioCurrentChapter.chapter_id) {
                    // 同一章节
                    maxProgress = duration;
                    viewHolder.timeSeekBar.setMaxProgress(maxProgress);
                    viewHolder.timeSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onPause(boolean isSound) {
                if (isSound) {
                    setPauseStatus();
                }
            }

            @Override
            public void onCompletion(SongInfo songInfo) {
                // 表示播放已经完成
                if (instance.audioSpeedBean != null && instance.audioSpeedBean.audioDate == -1) {
                    instance.audioSpeedBean.audioName = LanguageUtil.getString(activity, R.string.audio_not_open);
                    instance.audioSpeedBean.audioDate = 0;
                    if (activity != null && !activity.isFinishing() && viewHolder.audioTimeTv != null) {
                        viewHolder.audioTimeTv.setText(LanguageUtil.getString(activity, R.string.audio_timing));
                    }
                }
                // 进度条拉满
                if (viewHolder.timeSeekBar != null) {
                    viewHolder.timeSeekBar.setProgress(Integer.MAX_VALUE);
                }
                setPauseStatus();
            }

            @Override
            public void onError() {
                if (!InternetUtils.internet(activity)) {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_switch_sound_nonet));
                } else {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_play_error));
                }
                setPauseStatus();
            }
        };
    }

    @OnClick({R.id.audio_back, R.id.audio_toolbar_add_book, R.id.audio_bottom_comment, R.id.audio_bottom_share,
            R.id.audio_bottom_download})
    public void onAudioClick(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.audio_back:
                    this.finish();
                    break;
                case R.id.audio_toolbar_add_book:
                    // 收藏
                    addAudioToLocalShelf(audio.is_collect);
                    break;
                case R.id.audio_bottom_comment:
                    if (InternetUtils.internet(activity)) {
                        IntentComment(null);
                    } else {
                        MyToash.ToashError(activity, R.string.splashactivity_nonet);
                    }
                    break;
                case R.id.audio_bottom_share:
                    if (InternetUtils.internet(activity)) {
                        MyShare.chapterShare(activity, audioId, audio.current_chapter_id, 3);
                    } else {
                        MyToash.ToashError(activity, R.string.splashactivity_nonet);
                    }
                    break;
                case R.id.audio_bottom_download:
                    if (InternetUtils.internet(activity)) {
                        // 下载
                        if (audio != null) {
                            Intent downIntent = new Intent();
                            downIntent.putExtra("audio", audio);
                            downIntent.setClass(activity, AudioDownActivity.class);
                            startActivity(downIntent);
                        }
                    } else {
                        MyToash.ToashError(activity, R.string.splashactivity_nonet);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = AudioManager.getInstance(activity);
        instance.setOnCurrentChapterListen(onCurrentChapterListen);
        instance.setOnPlayerListener(onPlayerListener);
        if (instance.isPlayer == 0) {
            setPauseStatus();
        }
        // 如果设置了定时
        if (instance.audioSpeedBean != null) {
            if (instance.audioSpeedBean.audioDate == -1) {
                // 显示播放完本章
                viewHolder.audioTimeTv.setText(instance.audioSpeedBean.audioName);
            } else if (instance.audioSpeedBean.audioDate == 0) {
                viewHolder.audioTimeTv.setText(LanguageUtil.getString(activity, R.string.audio_timing));
            }
        }
    }

    /**
     * 用于打开评论界面
     *
     * @param comment
     */
    private void IntentComment(Comment comment) {
        if (audioCurrentChapter == null) {
            return;
        }
        Intent intent = new Intent(activity, CommentActivity.class);
        if (comment != null) {
            intent.putExtra("comment", comment);
        }
        intent.putExtra("current_id", audioId);
        intent.putExtra("chapter_id", audioCurrentChapter.chapter_id);
        intent.putExtra("productType", Constant.AUDIO_CONSTANT);
        startActivity(intent);
    }

    /**
     * 评论设置
     */
    private void initComment() {
        Drawable drawable = getResources().getDrawable(R.mipmap.edit_pen);
        drawable.setBounds(0, 0, ImageUtil.dp2px(activity, 12), ImageUtil.dp2px(activity, 12));
        audioInput.setCompoundDrawables(drawable, null, null, null);
        audioInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (InternetUtils.internet(activity)) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        if (audioCurrentChapter == null) {
                            return false;
                        }
                        String str = audioInput.getText().toString();
                        if (TextUtils.isEmpty(str) || Pattern.matches("\\s*", str)) {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.CommentListActivity_some));
                            return true;
                        }
                        CommentActivity.sendComment(activity, false, Constant.AUDIO_CONSTANT, audioId, "",
                                audioCurrentChapter.chapter_id, str, new CommentActivity.SendSuccess() {
                                    @Override
                                    public void Success(Comment comment) {
                                        audioInput.setText("");
                                        // 隐藏键盘
                                        Input.getInstance().hindInput(audioInput, activity);
                                    }
                                });
                        return true;
                    }
                } else {
                    MyToash.ToashError(activity, R.string.splashactivity_nonet);
                }
                return false;
            }
        });
    }

    /**
     * 评论
     *
     * @param chapter_id
     */
    public void initHttpComment(int chapter_id) {
        if (audioId != 0) {
            ReaderParams readerParams = new ReaderParams(activity);
            readerParams.putExtraParams("audio_id", audioId);
            if (audio != null && audio.current_listen_chapter_id != 0) {
                readerParams.putExtraParams("chapter_id", chapter_id);
            }
            HttpUtils.getInstance().sendRequestRequestParams(activity, AUDIO_CHAPTER_COMMENT_LIST, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                @Override
                public void onResponse(String response) {
                    if (!TextUtils.isEmpty(response)) {
                        CommentItem commentItem = gson.fromJson(response, CommentItem.class);
                        commentAdapter.total_count = commentItem.total_count;
                        commentList.clear();
                        minSize = Math.min(commentItem.list.size(), 3);
                        List<Comment> comments = commentItem.list.subList(0, minSize);
                        commentList.addAll(comments);
                        commentList.add(new Comment());
                        audioTexts.get(1).setText(commentItem.total_count >= 100 ?
                                (commentItem.total_count + "+") : (commentItem.total_count + ""));
                        viewHolder.audio_comment_label_image.setVisibility(View.VISIBLE);
                        viewHolder.audio_comment_label_title.setText(String.format(LanguageUtil.getString(activity, R.string.audio_comment),
                                commentItem.total_count + ""));
                        commentAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onErrorResponse(String ex) {

                }
            });
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {
    }

    public class ViewHolder {

        @BindView(R.id.audio_book_cover)
        ImageView cover;
        @BindViews({R.id.audio_book_chapter_name, R.id.audio_book_author})
        List<TextView> bookTexts;
        @BindView(R.id.audio_time_seekBar)
        TimeSeekBar timeSeekBar;
        @BindView(R.id.audio_time_seekBar_tips)
        TextView timeSeekBarTips;
        @BindView(R.id.audio_speech_speed_image)
        ImageView speechSpeedImage;
        @BindView(R.id.audio_time_layout_tv)
        TextView audioTimeTv;
        @BindView(R.id.audio_chapter_Loading)
        AudioLoadingView audioLoadingImage;
        @BindView(R.id.audio_chapter_player)
        ImageView audioPlayerImage;
        @BindView(R.id.audio_sound_layout)
        LinearLayout audio_sound_layout;
        @BindView(R.id.audio_sound_layout_tv)
        TextView audio_sound_tv;
        @BindView(R.id.audio_recycleview)
        RecyclerView audio_recycleview;
        @BindView(R.id.audio_like_line)
        View likeLine;
        @BindView(R.id.audio_v1)
        View audio_v1;


        @BindView(R.id.audio_comment_label_image)
        ImageView audio_comment_label_image;
        @BindView(R.id.audio_comment_label_title)
        TextView audio_comment_label_title;

        @BindView(R.id.audio_look_book)
        LinearLayout audio_look_book;
        @BindView(R.id.audio_last_chapter)
        public ImageView audio_last_chapter;
        @BindView(R.id.audio_next_chapter)
        public ImageView audio_next_chapter;

        @BindView(R.id.audio_switch_type_layout)
        LinearLayout audio_switch_type_layout;
        @BindView(R.id.audio_switch_type_image)
        ImageView audio_switch_type_image;
        @BindViews({R.id.audio_switch_type_title, R.id.audio_switch_type_chapter_name})
        List<TextView> audio_switch_type_text;

        @BindView(R.id.audio_book_layout)
        View audio_book_layout;

        @BindView(R.id.list_ad_view_layout)
        FrameLayout list_ad_view_layout;

        @BindView(R.id.item_head_audio_progress)
        View item_head_audio_progress;

        // 文字提示的宽度
        private int tipsWidth;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            initHeadView();
            initHeadListener();
        }

        private void initHeadView() {
            // 设置图片的样式
            ViewGroup.LayoutParams coverParams = cover.getLayoutParams();
            int coverWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth() / 3;
            coverParams.width = coverWidth;
            coverParams.height = coverWidth / 3 * 4;
            cover.setLayoutParams(coverParams);
            // 设置seekBar
            timeSeekBarTips.setVisibility(View.GONE);
            timeSeekBarTips.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 20),
                    ContextCompat.getColor(activity, R.color.black)));
            tipsParams = (RelativeLayout.LayoutParams) timeSeekBarTips.getLayoutParams();
            tipsParams.rightMargin = paddingWidth;
            timeSeekBar.setMaxProgress(0);
            timeSeekBar.setEnableSlide(true);
            // 声音
            audio_sound_tv.setText(PublicStaticMethod.getVoice(activity, ShareUitls.getString(activity,
                    "voice_preference", "xiaoyan")));
            audio_switch_type_layout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 8),
                    ContextCompat.getColor(activity, R.color.graybg)));
            bookStoareAdapter = new PublicMainAdapter(list, Constant.AUDIO_CONSTANT, activity, false, false);
            audio_recycleview.setLayoutManager(new LinearLayoutManager(activity));
            audio_recycleview.setAdapter(bookStoareAdapter);
        }

        private void initHeadListener() {
            timeSeekBar.setProgressListener(new TimeSeekBar.SeekBarProgressListener() {
                @Override
                public void onStartSlide(int progress, String tipsText, float width) {
                    if (timeSeekBarTips.getVisibility() != View.VISIBLE) {
                        timeSeekBarTips.setVisibility(View.VISIBLE);
                    }
                    timeSeekBarTips.setText(tipsText);
                    tipsWidth = timeSeekBarTips.getMeasuredWidth();
                    setSeekBarTips(progress, tipsWidth);
                }

                @Override
                public void onProgressChanged(int progress, String tipsText, float width) {
                    if (timeSeekBarTips.getVisibility() != View.VISIBLE) {
                        timeSeekBarTips.setVisibility(View.VISIBLE);
                    }
                    timeSeekBarTips.setText(tipsText);
                    tipsWidth = timeSeekBarTips.getMeasuredWidth();
                    setSeekBarTips(progress, tipsWidth);
                }

                @Override
                public void onStopSlide(int progress) {
                    timeSeekBarTips.setVisibility(View.GONE);
                    if (progress < 0) {
                        // 上一首或者开头
                        if (instance.mAudioId == audioId && instance.isPlayer == 1 && instance.audioCurrentChapter != null &&
                                audioCurrentChapter != null && instance.audioCurrentChapter.chapter_id == audioCurrentChapter.chapter_id) {
                            if (audioCurrentChapter.getLast_chapter() > 0) {
                                instance.openAudioChapterId(audio, audioCurrentChapter.getLast_chapter(), audioChapterList);
                            } else {
                                instance.setAudioProgress(0);
                            }
                        }
                    } else if (progress > maxProgress) {
                        // 下一首或者末尾
                        if (instance.mAudioId == audioId && instance.isPlayer == 1 && instance.audioCurrentChapter != null &&
                                audioCurrentChapter != null && instance.audioCurrentChapter.chapter_id == audioCurrentChapter.chapter_id) {
                            if (audioCurrentChapter.getNext_chapter() > 0) {
                                instance.openAudioChapterId(audio, audioCurrentChapter.getNext_chapter(), audioChapterList);
                            } else {
                                instance.setAudioProgress(maxProgress * 1000);
                            }
                        }
                    } else {
                        // 选择进度
                        if (instance.mAudioId == audioId && instance.audioCurrentChapter != null && audioCurrentChapter != null &&
                                instance.audioCurrentChapter.chapter_id == audioCurrentChapter.chapter_id) {
                            instance.setAudioProgress(progress * 1000);
                        }
                    }
                }
            });
        }

        @OnClick({R.id.audio_last_chapter, R.id.audio_chapter_player_layout, R.id.audio_next_chapter,
                R.id.item_head_audio_rewind, R.id.item_head_audio_forward,
                R.id.audio_time_layout, R.id.audio_speed_layout, R.id.audio_chapter_layout, R.id.audio_sound_layout,
                R.id.audio_look_book,
                R.id.audio_switch_type_layout})
        public void onAudioClick(View view) {
            long ClickTimeNew = System.currentTimeMillis();
            if (ClickTimeNew - ClickTime > AgainTime) {
                ClickTime = ClickTimeNew;
                switch (view.getId()) {
                    case R.id.audio_look_book:
                        // 看书
                        if (audio != null && relation.getBook_id() != 0) {
                            Book localBook = ObjectBoxUtils.getBook(relation.getBook_id());
                            if (localBook == null) {
                                getBookInfo(relation.getBook_id(), new OnGetBookInfo() {
                                    @Override
                                    public void onResponse(InfoBook infoBook) {
                                        Book book = new Book();
                                        book.book_id = infoBook.book_id;
                                        book.name = infoBook.name;
//                                        if (infoBook.author != null && !infoBook.author.isEmpty() && !TextUtils.isEmpty(infoBook.author.get(0))) {
//                                            StringBuilder name = new StringBuilder();
//                                            for (int i = 0; i < infoBook.author.size(); i++) {
//                                                if (i == 0) {
//                                                    name.append(infoBook.author.get(i));
//                                                } else {
//                                                    name.append(" ").append(infoBook.author.get(i));
//                                                }
//                                            }
//                                            book.author = name.toString();
//                                        }
                                        book.author = infoBook.author.replaceAll(",", " ");
                                        book.cover = infoBook.cover;
                                        book.description = infoBook.description;
                                        book.total_chapter = infoBook.total_chapters;
                                        ObjectBoxUtils.addData(book, Book.class);
                                        ChapterManager.getInstance().openBook(activity,book);
                                    }

                                    @Override
                                    public void onErrorResponse() {
                                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.lookBook_error));
                                    }
                                });
                            } else {
                                ChapterManager.getInstance().openBook(activity,localBook);
                            }
                        }
                        break;
                    case R.id.item_head_audio_rewind:
                        // -15
                        if (viewHolder != null) {
                            if (instance.mAudioId != audioId || instance.audioCurrentChapter == null || audioCurrentChapter == null ||
                                    instance.audioCurrentChapter.chapter_id != audioCurrentChapter.chapter_id) {
                                return;
                            }
                            int maxProgress = timeSeekBar.getMaxProgress();
                            int progress = timeSeekBar.getProgress();
                            if (maxProgress > 0) {
                                if (progress - 15 > 0) {
                                    instance.setAudioProgress((progress - 15) * 1000);
                                    if (instance.isPlayer != 1) {
                                        timeSeekBar.setProgress(progress - 15);
                                    }
                                } else {
                                    instance.setAudioProgress(0);
                                    timeSeekBar.setProgress(0);
                                    /*if (instance.isPlayer != 1) {

                                    } else {
                                        if (audioCurrentChapter != null && audioCurrentChapter.getLast_chapter() != 0) {
                                            instance.openAudioChapterId(audio, audioCurrentChapter.getLast_chapter(), audioChapterList);
                                        } else {
                                            instance.setAudioProgress(0);
                                        }
                                    }*/
                                }
                            }
                        }
                        break;
                    case R.id.item_head_audio_forward:
                        // +15
                        if (instance.mAudioId != audioId || instance.audioCurrentChapter == null || audioCurrentChapter == null ||
                                instance.audioCurrentChapter.chapter_id != audioCurrentChapter.chapter_id) {
                            return;
                        }
                        int maxProgress = timeSeekBar.getMaxProgress();
                        int progress = timeSeekBar.getProgress();
                        if (maxProgress > 0) {
                            if (progress + 15 < maxProgress) {
                                instance.setAudioProgress((progress + 15) * 1000);
                                if (instance.isPlayer != 1) {
                                    timeSeekBar.setProgress(progress + 15);
                                }
                            } else {
                                if (instance.isPlayer != 1) {
                                    instance.setAudioProgress((maxProgress) * 1000);
                                    timeSeekBar.setProgress(maxProgress);
                                } else {
                                    if (audioCurrentChapter != null && audioCurrentChapter.getNext_chapter() != 0) {
                                        instance.openAudioChapterId(audio, audioCurrentChapter.getNext_chapter(), audioChapterList);
                                    } else {
                                        instance.setAudioProgress((maxProgress - 1) * 1000);
                                    }
                                }
                            }
                        }
                        break;
                    case R.id.audio_last_chapter:
                        //上一章
                        if (audioCurrentChapter != null) {
                            instance.openAudioChapterId(audio, audioCurrentChapter.getLast_chapter(), audioChapterList);
                        }
                        break;
                    case R.id.audio_next_chapter:
                        //下一章
                        if (audioCurrentChapter != null) {
                            instance.openAudioChapterId(audio, audioCurrentChapter.getNext_chapter(), audioChapterList);
                        }
                        break;
                    case R.id.audio_chapter_player_layout:
                        // 播放控制
                        if (audioChapterList == null || audioChapterList.isEmpty() || audioCurrentChapter == null) {
                            if (InternetUtils.internet(activity)) {
                                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
                            } else {
                                MyToash.ToashError(activity, R.string.audio_switch_sound_nonet);
                            }
                            return;
                        }
                        // 再点击前先判断播放工具类中是否为这本书
                        if (instance.isIsPlayerError() || instance.mAudioId != audioId || instance.audioCurrentChapter == null ||
                                instance.audioCurrentChapter.chapter_id != audioCurrentChapter.chapter_id) {
                            // 不是这本书或者不是本章节
                            instance.openAudioChapterId(audio, audioCurrentChapter.getChapter_id(), audioChapterList);
                        } else {
                            // 此时可以确定是同本书，并且为同一章
                            if (instance.isPlayer == 1) {
                                instance.setAudioPlayerStatus(false, audio, audioCurrentChapter.getChapter_id(), audioChapterList);
                            } else {
                                instance.setAudioPlayerStatus(true, audio, audioCurrentChapter.getChapter_id(), audioChapterList);
                            }
                        }
                        break;
                    case R.id.audio_time_layout:
                        // 定时
                        List<AudioSpeedBean> audioTimeDate = PublicStaticMethod.getTimeDate(activity);
                        AudioTimingDialog.showDownoption(activity, audioTimeDate, instance.audioSpeedBean);
                        break;
                    case R.id.audio_speed_layout:
                        // 语速
                        if (audio == null) {
                            return;
                        }
                        AudioSpeechSetDialog.showSpeechDialog(activity, 1, audio.getSpeed(), new AudioSpeechSetDialog.OnSpeechSpeedListener() {
                            @Override
                            public void onClick(String name, String value) {
                                viewHolder.speechSpeedImage.setImageResource(PublicStaticMethod.getSpeedImage(value));
                                audio.setSpeed(value);
                                ObjectBoxUtils.addData(audio, Audio.class);
                                if (instance.mAudioId == audioId) {
                                    instance.setSpeechSpeed(value);
                                }
                            }
                        });
                        break;
                    case R.id.audio_chapter_layout:
                        // 目录
                        AudioBookCatalogDialogFragment dialogFragment = new AudioBookCatalogDialogFragment(activity, true,
                                null, audio, new AudioBookCatalogDialogFragment.OnAudioBookCatalogClickListener() {
                            @Override
                            public void onChapter(long id, long chapterId, List<Object> objects) {
                                // 可以根据章节id进行播放，因为播放类中已经存在这本书
                                instance.openAudioChapterId(audio, chapterId, audioChapterList);
                            }
                        });
                        dialogFragment.show(getSupportFragmentManager(), "AudioBookCatalogDialogFragment");
                        break;
                    case R.id.audio_sound_layout:
                        break;
                    case R.id.audio_switch_type_layout:
                        if (audioLoadingImage.getVisibility() == View.VISIBLE) {
                            return;
                        }
                        // 语音切换，目前的策略：满足切换条件后会重新拉起一个界面进行语音的切换
                        if (relation != null && relation.getBook_id() > 0) {
                            if (!InternetUtils.internet(activity)) {
                                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_no_support_online_player));
                                return;
                            }
                            // 打开服务
                            AudioManager.openService(activity);
                            // 打开ai界面
                            Intent bookIntent = new Intent();
                            bookIntent.putExtra("book_id", relation.getBook_id());
                            if (relation.getChapter_id() != 0) {
                                bookIntent.putExtra("chapter_id", relation.getChapter_id());
                            }
                            if (audioCurrentChapter != null) {
                                bookIntent.putExtra("current_chapter", audioCurrentChapter);
                            }
                            bookIntent.putExtra("is_jump", true);
                            bookIntent.setClass(activity, AudioAiActivity.class);
                            startActivity(bookIntent);
                            overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
                        }
                        break;
                }
            }
        }

        /**
         * 设置提示位置和属性
         *
         * @param progress
         * @param width
         */
        private void setSeekBarTips(int progress, int width) {
            if (progress <= 0) {
                tipsParams.leftMargin = paddingWidth;
            } else if (progress >= maxProgress) {
                tipsParams.leftMargin = pageWidth - width - paddingWidth;
            } else {
                int leftWidth = ((pageWidth - width) - 2 * paddingWidth) * progress / maxProgress;
                if (leftWidth <= 0) {
                    tipsParams.leftMargin = paddingWidth;
                } else if (leftWidth > pageWidth - paddingWidth - width) {
                    tipsParams.leftMargin = pageWidth - width - paddingWidth;
                } else {
                    tipsParams.leftMargin = leftWidth + paddingWidth;
                }
            }
            timeSeekBarTips.setLayoutParams(tipsParams);
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        EventBus.getDefault().post(new ChapterBuyRefresh(AUDIO_CONSTANT, true, 0));
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0, R.anim.activity_top_bottom_close);
    }

    /**
     * 用于更新关联书籍的数据
     *
     * @param audioSwitchRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchRefresh(AudioSwitchRefresh audioSwitchRefresh) {
        if (relation != null && relation.getBook_id() != 0) {
            // 可以切换ai
            if (audioSwitchRefresh.isSound() && audioSwitchRefresh.getId() == audioId) {
                relation.setChapter_id(audioSwitchRefresh.getChapterId());
                viewHolder.audio_switch_type_text.get(1).setText(audioSwitchRefresh.getChapterName());
            }
        }
    }

    /**
     * 用于更换播放图标
     *
     * @param audioPlayerRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerStatus(AudioPlayerRefresh audioPlayerRefresh) {
        if (audioPlayerRefresh.isSound() && audioPlayerRefresh.isPause()) {
            setPauseStatus();
        }
    }

    /**
     * 有声收藏
     *
     * @param isCollect
     */
    public void addAudioToLocalShelf(int isCollect) {
        if (isCollect == 0) {
            audio.is_collect = 1;
            MyToash.ToashSuccess(activity, LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujias));
            ObjectBoxUtils.addData(audio, Audio.class);
            EventBus.getDefault().post(new RefreshShelf(Constant.AUDIO_CONSTANT, new RefreshAudioShelf(audio, 1)));
            EventBus.getDefault().post(new RefreshBookInfo(audio, true));
            setAddShelfStatus(true);
        }
    }

    /**
     * 用于刷新收藏的回调
     *
     * @param refreshBookInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioSelf(RefreshBookInfo refreshBookInfo) {
        if (refreshBookInfo.audio != null && audio != null) {
            // 有声
            if (refreshBookInfo.audio.audio_id == audio.audio_id) {
                audio.is_collect = refreshBookInfo.audio.is_collect;
                setAddShelfStatus(refreshBookInfo.audio.is_collect == 1);
                if (instance.currentPlayAudio.audio_id == audio.audio_id) {
                    instance.currentPlayAudio.setIs_collect(refreshBookInfo.audio.is_collect);
                }
            }
        }
    }

    /**
     * 用于更新定时时当前的时间
     *
     * @param audioDate
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSpeedDate(AudioSpeedBeanEventbus audioDate) {
        if (audioDate.isOpen) {
            instance.setAudioSpeedBean(audioDate.audioSpeedBean);
        }
        if (audioDate.audioSpeedBean.audioDate == 0) {
            viewHolder.audioTimeTv.setText(LanguageUtil.getString(activity, R.string.audio_timing));
        } else if (audioDate.audioSpeedBean.audioDate == -1) {
            viewHolder.audioTimeTv.setText(audioDate.audioSpeedBean.audioName);
        } else {
            String s = "";
            if (audioDate.audioSpeedBean.audioDate / 60 < 10) {
                s = "0" + (audioDate.audioSpeedBean.audioDate / 60) + ":" + (audioDate.audioSpeedBean.audioDate % 60);
            } else {
                s = (audioDate.audioSpeedBean.audioDate / 60) + ":" + (audioDate.audioSpeedBean.audioDate % 60);
            }
            viewHolder.audioTimeTv.setText(s);
        }
    }

    /**
     * 刷新评论
     *
     * @param refreshBookInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(CommentRefresh refreshBookInfo) {
        if (refreshBookInfo.productType == Constant.AUDIO_CONSTANT && audioId == refreshBookInfo.id) {
            initHttpComment((int) audio.current_listen_chapter_id);
        }
    }

    /**
     * 登录回调
     *
     * @param refreshMine
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (UserUtils.isLogin(activity)) {
            getAudioDetailData();
            if (purchaseDialog != null && purchaseDialog.isShowing()) {
                purchaseDialog.Dismiss();
            }
        }
    }

    /**
     * 购买弹窗
     *
     * @param purchaseRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void purchaseRefresh(AudioPurchaseRefresh purchaseRefresh) {
        if (!purchaseRefresh.isSound() && purchaseRefresh.getAudioChapter() != null) {
            if (purchaseDialog != null && purchaseDialog.isShowing()){
                return;
            }
            purchaseDialog = new PublicPurchaseDialog(activity,"", Constant.AUDIO_CONSTANT, purchaseRefresh.isDown(), new PublicPurchaseDialog.BuySuccess() {
                @Override
                public void buySuccess(long[] ids, int num) {
                    purchaseRefresh.getAudioChapter().setIs_preview(0);
                    ObjectBoxUtils.addData(purchaseRefresh.getAudioChapter(), AudioChapter.class);
                    instance.startAudioHttpData(purchaseRefresh.getAudioChapter().getChapter_id());
                }
            }, true);
            purchaseDialog.initData(purchaseRefresh.getAudioChapter().audio_id, purchaseRefresh.getAudioChapter().getChapter_id() + "",
                    purchaseRefresh.isDown(), null);
            purchaseDialog.show();
        }
    }

    /**
     * 更新是否收藏时的UI
     *
     * @param isCollect
     */
    private void setAddShelfStatus(boolean isCollect) {
        if (isCollect) {
            audioTexts.get(0).setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujias));
            audioTexts.get(0).setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
            audioTexts.get(0).setEnabled(false);
        } else {
            audioTexts.get(0).setEnabled(true);
            audioTexts.get(0).setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujia));
            audioTexts.get(0).setTextColor(ContextCompat.getColor(activity, R.color.gray_title));
        }
    }

    /**
     * 设置上一首图片的样式
     *
     * @param isEnable
     */
    private void setLastImageStatus(boolean isEnable) {
        if (!isEnable) {
            viewHolder.audio_last_chapter.setImageResource(R.mipmap.audio_no_last);
            viewHolder.audio_last_chapter.setEnabled(false);
        } else {
            viewHolder.audio_last_chapter.setImageResource(R.mipmap.audio_last);
            viewHolder.audio_last_chapter.setEnabled(true);
        }
    }

    /**
     * 设置下一首图片的样式
     *
     * @param isEnable
     */
    private void setNextImageStatus(boolean isEnable) {
        if (!isEnable) {
            viewHolder.audio_next_chapter.setImageResource(R.mipmap.audio_no_next);
            viewHolder.audio_next_chapter.setEnabled(false);
        } else {
            viewHolder.audio_next_chapter.setImageResource(R.mipmap.audio_next);
            viewHolder.audio_next_chapter.setEnabled(true);
        }
    }

    private void setPauseStatus() {
        if (viewHolder.audioLoadingImage.getVisibility() == View.VISIBLE) {
            viewHolder.audioLoadingImage.setVisibility(View.GONE);
        }
        if (viewHolder.audioPlayerImage.getVisibility() == View.GONE) {
            viewHolder.audioPlayerImage.setVisibility(View.VISIBLE);
        }
        viewHolder.audioPlayerImage.setImageResource(R.mipmap.audio_player);
    }

    private void setPlayStatus() {
        if (viewHolder.audioLoadingImage.getVisibility() == View.VISIBLE) {
            viewHolder.audioLoadingImage.setVisibility(View.GONE);
        }
        if (viewHolder.audioPlayerImage.getVisibility() == View.GONE) {
            viewHolder.audioPlayerImage.setVisibility(View.VISIBLE);
        }
        viewHolder.audioPlayerImage.setImageResource(R.mipmap.audio_pause);
    }

    /**
     * 获取有声数据
     *
     * @param id
     * @param isJump
     */
    private void getAudio(long id, boolean isJump) {
        if (audio == null) {
            audio = (Audio) formIntent.getSerializableExtra("audio");
            if (audio == null) {
                if (id == 0) {
                    audioId = formIntent.getLongExtra("audio_id", 0);
                }
                if (id != 0) {
                    audioId = id;
                    audio = ObjectBoxUtils.getAudio(audioId);
                }
            } else {
                audioId = audio.audio_id;
                initSoundBean(isJump);
                getAudioDetailData();
                initIsSound();
                return;
            }
            if (audio == null) {
                if (audioId != 0) {
                    ReaderParams readerParams = new ReaderParams(activity);
                    readerParams.putExtraParams("audio_id", audioId);
                    HttpUtils.getInstance().sendRequestRequestParams(activity, Api.AUDIO_INFO, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String json) {
                            if (!TextUtils.isEmpty(json)) {
                                AudioBean audioBean = HttpUtils.getGson().fromJson(json, AudioBean.class);
                                if (audioBean.getAudio() != null) {
                                    AudioBean.Audio beanAudio = audioBean.getAudio();
                                    // 更新本地数据
                                    audio = new Audio();
                                    audio.audio_id = audioId = beanAudio.getAudio_id();
                                    audio.name = beanAudio.getName();
//                                    if (beanAudio.author != null && !beanAudio.author.isEmpty() && !TextUtils.isEmpty(beanAudio.author.get(0))) {
//                                        StringBuilder name = new StringBuilder();
//                                        for (int i = 0; i < beanAudio.author.size(); i++) {
//                                            if (i == 0) {
//                                                name.append(beanAudio.author.get(i));
//                                            } else {
//                                                name.append(" ").append(beanAudio.author.get(i));
//                                            }
//                                        }
//                                        audio.author = name.toString();
//                                    }
                                    audio.author = beanAudio.author;
                                    audio.cover = beanAudio.getCover();
                                    audio.description = beanAudio.getDescription();
                                    audio.total_chapter = beanAudio.getTotal_chapter();
                                    initSoundBean(isJump);
                                    getAudioDetailData();
                                    initIsSound();
                                }
                            }
                        }

                        @Override
                        public void onErrorResponse(String ex) {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_switch_error));
                        }
                    });
                }
            } else {
                initSoundBean(isJump);
                getAudioDetailData();
                initIsSound();
            }
        } else {
            initSoundBean(isJump);
            getAudioDetailData();
            initIsSound();
        }
    }

    /**
     * 有声
     * 根据类型获取数据, 并设置播放
     *
     * @param isJump
     */
    private void initSoundBean(boolean isJump) {
        if (audio != null) {
            viewHolder.audioPlayerImage.setVisibility(View.GONE);
            viewHolder.audioLoadingImage.setVisibility(View.VISIBLE);
            audioId = audio.getAudio_id();
            bookName = audio.getName();
            bookCover = audio.getCover();
            bookChapterName = audio.getCurrent_chapter_text();
            // 界面赋值
            Glide.with(activity).load(bookCover).into(viewHolder.cover);
            viewHolder.bookTexts.get(1).setText(bookName);
            // 设置当前id
            if (formIntent.getLongExtra("chapter_id", 0) != 0) {
                audio.setCurrent_listen_chapter_id(formIntent.getLongExtra("chapter_id", 0));
            }
            // 获取章节，不需要显示加载
            audio.getAudioChapterList(activity, new AudioInfoCatalogFragment.GetAudioChapterList() {
                @Override
                public void getAudioChapterList(List<AudioChapter> audioChapters) {
                    if (audioChapters != null && !audioChapters.isEmpty()) {
                        if(!audioChapterList.isEmpty()) {
                            audioChapterList.clear();
                        }
                        audioChapterList.addAll(audioChapters);
                        if (audio.getCurrent_listen_chapter_id() != 0) {
                            for (AudioChapter audioChapter : audioChapters) {
                                if (audio.getCurrent_listen_chapter_id() == audioChapter.getChapter_id()) {
                                    audioCurrentChapter = audioChapter;
                                    break;
                                }
                            }
                        } else {
                            audioCurrentChapter = audioChapters.get(0);
                        }
                        if (instance.isPlayer == 0) {
                            // 打开服务
                            AudioManager.openService(activity);
                            // 打开播放器
                            instance.openAudio(audio, audioChapterList);
                        } else if (instance.isPlayer == 1 || instance.isPlayer == 2 || instance.isPlayer == 3) {
                            // 不管是否为同一本书, 进入时都应该获取播放时需要的内容
                            setPauseStatus();
                            // 设置播放
                            if (audioCurrentChapter != null) {
                                bookChapterName = audioCurrentChapter.getChapter_title();
                                audio.current_listen_chapter_id = audioCurrentChapter.chapter_id;
                                audio.setCurrent_chapter_text(audioCurrentChapter.getChapter_title());
                                audio.current_chapter_listen_displayOrder = audioChapters.indexOf(audioCurrentChapter);
                                if (audio.is_collect == 1) {
                                    ObjectBoxUtils.addData(audio, Audio.class);
                                }
                                // 界面赋值
                                viewHolder.bookTexts.get(0).setText(bookChapterName);
                                setLastImageStatus(audioCurrentChapter.getLast_chapter() != 0);
                                setNextImageStatus(audioCurrentChapter.getNext_chapter() != 0);
                                // 不管播放器是否播放，根据要求，直接切换到当前的章节进行播放
                                if (instance.mAudioId == audioId && instance.audioCurrentChapter != null &&
                                        instance.audioCurrentChapter.chapter_id == audioCurrentChapter.chapter_id) {
                                    // 正在播放同一章,如果没有播放则恢复播放
                                    // （除非用户通过通知栏，播放拖动条进入）
                                    if (instance.isPlayer == 2) {
                                        if (instance.isPlayerCurrentId(true, audioCurrentChapter.getChapter_id())) {
                                            instance.openAudioChapterId(audio, audioCurrentChapter.getChapter_id(), audioChapterList);
                                        } else {
                                            if (!formIntent.getBooleanExtra("special_click", false) && !isJump) {
                                                instance.setPlayer(true);
                                            } else {
                                                // 需要更新进度条的位置
                                                if (instance.audioCurrentChapter.getDuration_second() != 0) {
                                                    viewHolder.timeSeekBar.setMaxProgress(instance.audioCurrentChapter.getDuration_second());
                                                    if (instance.audioCurrentChapter.read_progress != 0) {
                                                        int progress = (int) (instance.audioCurrentChapter.read_progress / 1000);
                                                        if (progress <= instance.audioCurrentChapter.getDuration_second()) {
                                                            viewHolder.timeSeekBar.setProgress(progress);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else if (instance.isPlayer == 1) {
                                        setPlayStatus();
                                    }
                                    initHttpComment((int) audioCurrentChapter.getChapter_id());
                                } else {
                                    // 先判断是否是切换有声或ai, 是切换时不改变播放状态
                                    if (!isJump) {
                                        // 不是同一章，并且不是切换，那就直接播放，
                                        instance.setStopPlayer();
                                        instance.setAudioPlayerStatus(true, audio, audioCurrentChapter.getChapter_id(), audioChapterList);
                                    }
                                }
                            } else {
                                setPauseStatus();
                                if (InternetUtils.internet(activity)) {
                                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
                                } else {
                                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_switch_sound_nonet));
                                }
                            }
                        }
                    } else {
                        setPauseStatus();
                        if (InternetUtils.internet(activity)) {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
                        } else {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_switch_sound_nonet));
                        }
                    }
                }
            });
        }
    }

    /**
     * 用于控制有声或ai界面的切换
     */
    private void initIsSound() {
        // 设置界面
        Glide.with(activity).load(bookCover).into(viewHolder.cover);
        viewHolder.bookTexts.get(0).setText(bookChapterName);
        viewHolder.bookTexts.get(1).setText(bookName);
        if (audio != null) {
            if (formIntent.getBooleanExtra("special_click", false)) {
                // 从播放拖动框或通知栏进入
                Audio localAudio = ObjectBoxUtils.getAudio(audioId);
                if (localAudio != null) {
                    audio.is_collect = localAudio.is_collect;
                }
            }
            if (audio.getIs_collect() == 1) {
                setAddShelfStatus(true);
            } else {
                setAddShelfStatus(false);
            }
            viewHolder.speechSpeedImage.setImageResource(PublicStaticMethod.getSpeedImage(audio.getSpeed()));
        }
        viewHolder.audio_sound_layout.setVisibility(View.GONE);
        viewHolder.item_head_audio_progress.setVisibility(View.VISIBLE);
    }

    private void getAudioDetailData() {
        if (audioId > 0) {
            ReaderParams params = new ReaderParams(activity);
            params.putExtraParams("audio_id", audioId);
            HttpUtils.getInstance().sendRequestRequestParams(activity, Api.AUDIO_RECOMMEND_COMMENT, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                @Override
                public void onResponse(String response) {
                    InfoAudioItem optionItem = gson.fromJson(response, InfoAudioItem.class);
                    if (optionItem.getRelation() != null) {
                        relation = optionItem.getRelation();
                        // 刷新界面
                        refreshLayout(relation);
                        // 只有首次进入界面，会判断是否为切换过来的，是否携带当前章节的信息
                        if (http_flag == 0) {
                            BookChapter chapter = (BookChapter) formIntent.getSerializableExtra("current_chapter");
                            if (chapter != null) {
                                relation.setAudio_id(chapter.getBook_id());
                                relation.setChapter_id(chapter.getChapter_id());
                                relation.setChapter_title(chapter.getChapter_title());
                            }
                        }
                    }
                    if (http_flag == 0) {
                        if (optionItem.list != null && !optionItem.list.isEmpty()) {
                            BaseLabelBean baseLabelBean = new BaseLabelBean();
                            baseLabelBean.setList(optionItem.list);
                            baseLabelBean.setLabel(LanguageUtil.getString(activity, R.string.audio_like_recommend));
                            baseLabelBean.setStyle(1);
                            list.clear();
                            list.add(baseLabelBean);
                        }
                        viewHolder.likeLine.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
                        bookStoareAdapter.notifyDataSetChanged();
                        if (optionItem.advert != null && optionItem.advert.ad_type != 0) {
                            viewHolder.list_ad_view_layout.setVisibility(View.VISIBLE);
                            optionItem.advert.setAd(activity, viewHolder.list_ad_view_layout, 1);
                        }
                    }
                    http_flag = 1;
                }

                @Override
                public void onErrorResponse(String ex) {

                }
            });
        }
    }

    /**
     * 用于刷新是否关联的布局
     *
     * @param relation
     */
    private void refreshLayout(InfoAudioItem.Relation relation) {
        if (relation.getBook_id() != 0) {
            viewHolder.audio_look_book.setVisibility(View.VISIBLE);
            if (Constant.USE_AUDIO_AI) {
                viewHolder.audio_switch_type_layout.setVisibility(View.VISIBLE);
                viewHolder.audio_switch_type_image.setImageResource(R.mipmap.icon_ai);
                viewHolder.audio_switch_type_text.get(0).setText(LanguageUtil.getString(activity, R.string.audio_switch_ai));
                if (!TextUtils.isEmpty(relation.getChapter_title())) {
                    viewHolder.audio_switch_type_text.get(1).setText(relation.getChapter_title());
                }
            } else {
                viewHolder.audio_switch_type_layout.setVisibility(View.GONE);
            }
        } else {
            viewHolder.audio_switch_type_layout.setVisibility(View.GONE);
            viewHolder.audio_look_book.setVisibility(View.GONE);
        }
    }

    /***************************************  详情接口  ********************************************/

    /**
     * 获取book详情
     *
     * @param id
     * @param onGetBookInfo
     */
    private void getBookInfo(long id, OnGetBookInfo onGetBookInfo) {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("book_id", id + "");
        HttpUtils.getInstance().sendRequestRequestParams(activity,mBookInfoUrl, params.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String json) {
                if (!TextUtils.isEmpty(json)) {
                    InfoBookItem infoBookItem = gson.fromJson(json, InfoBookItem.class);
                    InfoBook infoBook = infoBookItem.getBook();
                    if (onGetBookInfo != null) {
                        onGetBookInfo.onResponse(infoBook);
                    }
                }
            }

            @Override
            public void onErrorResponse(String ex) {
                if (onGetBookInfo != null) {
                    onGetBookInfo.onErrorResponse();
                }
            }
        });
    }

    public interface OnGetBookInfo {

        void onResponse(InfoBook infoBook);

        void onErrorResponse();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }
}
