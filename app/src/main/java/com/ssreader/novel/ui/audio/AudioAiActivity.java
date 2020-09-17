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
import com.ssreader.novel.eventbus.AudioAiServiceRefresh;
import com.ssreader.novel.eventbus.AudioListenerChapterRefresh;
import com.ssreader.novel.eventbus.AudioPlayerRefresh;
import com.ssreader.novel.eventbus.AudioPurchaseRefresh;
import com.ssreader.novel.eventbus.AudioSpeedBeanEventbus;
import com.ssreader.novel.eventbus.AudioSwitchRefresh;
import com.ssreader.novel.eventbus.CommentRefresh;
import com.ssreader.novel.eventbus.RefreshBookInfo;
import com.ssreader.novel.eventbus.RefreshBookSelf;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshPageFactoryChapter;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.eventbus.RefreshShelfCurrent;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.model.BaseLabelBean;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.model.CommentItem;
import com.ssreader.novel.model.InfoAudioItem;
import com.ssreader.novel.model.InfoBook;
import com.ssreader.novel.model.InfoBookItem;
;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.CommentActivity;
import com.ssreader.novel.ui.adapter.CommentAdapter;
import com.ssreader.novel.ui.adapter.PublicMainAdapter;
import com.ssreader.novel.ui.audio.dialog.AudioSpeechSetDialog;
import com.ssreader.novel.ui.audio.dialog.AudioBookCatalogDialogFragment;
import com.ssreader.novel.ui.audio.dialog.AudioTimingDialog;
import com.ssreader.novel.ui.audio.manager.AudioManager;
import com.ssreader.novel.ui.dialog.PublicPurchaseDialog;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.audio.view.AudioLayoutManager;
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

import static com.ssreader.novel.constant.Api.mBookInfoUrl;
import static com.ssreader.novel.constant.Api.mCommentListUrl;

public class AudioAiActivity extends BaseActivity {

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
    @BindView(R.id.audio_bottom_download)
    LinearLayout bottomDownLayout;

    // 头部view
    public ViewHolder viewHolder;
    // 布局管理器
    private AudioLayoutManager layoutManager;

    // 播放管理类
    private AudioManager instance;
    // 管理类监听接口
    private AudioManager.OnCurrentChapterListen onCurrentChapterListen;
    private AudioManager.OnPlayerListener onPlayerListener;
    // 小说
    private long bookId = 0;
    private Book book;
    private BookChapter bookCurrentChapter;
    private List<BookChapter> bookChapterList = new ArrayList<>();
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
        // 设置页面属性
        paddingWidth = ImageUtil.dp2px(activity, 33);
        pageWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        // 设置recyclerView
        layoutManager = new AudioLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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
                ContextCompat.getColor(activity, R.color.grayline), ContextCompat.getColor(activity, R.color.navigation_bg)));
        if (Constant.USE_SHARE) {
            audioShareLayout.setVisibility(View.VISIBLE);
        } else {
            audioShareLayout.setVisibility(View.GONE);
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
        initComment();
        initListener();
        // 根据类型设置布局
        getBook(formIntent.getBooleanExtra("is_jump", false));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        formIntent = intent;
        // 置空
        bookId = 0;
        book = null;
        relation = null;
        getBook(formIntent.getBooleanExtra("is_jump", false));
    }

    /**
     * 事件监听
     */
    private void initListener() {
        // 返回的当前章节信息
        onCurrentChapterListen = new AudioManager.OnCurrentChapterListen() {
            @Override
            public void onCurrentAudioChapter(long id, AudioChapter audioChapter) {
            }

            @Override
            public void onCurrentBookChapter(long id, BookChapter bookChapter) {
                if (bookId == id && bookChapter != null) {
                    bookCurrentChapter = bookChapter;
                    book.current_listen_chapter_id = bookChapter.getChapter_id();
                    ObjectBoxUtils.addData(book, Book.class);
                    setLastImageStatus(bookChapter.getLast_chapter() != 0);
                    setNextImageStatus(bookChapter.getNext_chapter() != 0);
                    bookChapterName = bookChapter.getChapter_title();
                    viewHolder.bookTexts.get(0).setText(bookChapter.getChapter_title());
                    initHttpComment((int) bookChapter.getChapter_id());
                    // 用于更新有声详情（续播）
                    EventBus.getDefault().post(new AudioListenerChapterRefresh(false, 0,
                            0, ""));
                    // 用于更新切换时的章节显示
                    if (relation != null && relation.getAudio_id() != 0) {
                        EventBus.getDefault().post(new AudioSwitchRefresh(true, relation.getAudio_id(),
                                bookChapter.getChapter_id(), bookChapter.getChapter_title()));
                    }
                }
            }
        };
        // 播放监听
        onPlayerListener = new AudioManager.OnPlayerListener() {
            @Override
            public void onLoading() {
                if (instance.mBookId == bookId) {
                    viewHolder.audioPlayerImage.setVisibility(View.GONE);
                    viewHolder.audioLoadingImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStart(int duration) {
                if (instance.mBookId == bookId && instance.bookCurrentChapter != null && bookCurrentChapter != null &&
                        instance.bookCurrentChapter.chapter_id == bookCurrentChapter.chapter_id) {
                    maxProgress = duration;
                    viewHolder.timeSeekBar.setMaxProgress(maxProgress);
                    setPlayStatus();
                }
            }

            @Override
            public void onProgress(int duration, int progress) {
                if (instance.mBookId == bookId && instance.bookCurrentChapter != null && bookCurrentChapter != null &&
                        instance.bookCurrentChapter.chapter_id == bookCurrentChapter.chapter_id) {
                    // 同一章节
                    maxProgress = duration;
                    viewHolder.timeSeekBar.setMaxProgress(maxProgress);
                    viewHolder.timeSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onPause(boolean isSound) {
                if (!isSound) {
                    setPauseStatus();
                }
            }

            @Override
            public void onCompletion(SongInfo songInfo) {
                setPauseStatus();
                // 表示播放已经完成
                if (instance.audioSpeedBean != null && instance.audioSpeedBean.audioDate == -1) {
                    instance.audioSpeedBean.audioName = LanguageUtil.getString(activity, R.string.audio_not_open);
                    instance.audioSpeedBean.audioDate = 0;
                    if (activity != null && !activity.isFinishing() && viewHolder.audioTimeTv != null) {
                        viewHolder.audioTimeTv.setText(LanguageUtil.getString(activity, R.string.audio_timing));
                    }
                }
            }

            @Override
            public void onError() {
                if (!InternetUtils.internet(activity)) {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_no_support_online_player));
                } else {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.audio_play_error));
                }
                setPauseStatus();
            }
        };
    }

    @OnClick({R.id.audio_back, R.id.audio_toolbar_add_book, R.id.audio_bottom_comment, R.id.audio_bottom_share})
    public void onAudioClick(View view) {
        switch (view.getId()) {
            case R.id.audio_back:
                this.finish();
                break;
            case R.id.audio_toolbar_add_book:
                // 收藏
                addBookToLocalShelf(book.is_collect);
                break;
            case R.id.audio_bottom_comment:
                IntentComment(null);
                break;
            case R.id.audio_bottom_share:
                MyShare.chapterShare(activity, bookId, book.current_chapter_id, 1);
                break;
        }
    }

    /**
     * 用于打开评论界面
     * @param comment
     */
    private void IntentComment(Comment comment) {
        if (bookCurrentChapter == null) {
            return;
        }
        Intent intent = new Intent(activity, CommentActivity.class);
        if (comment != null) {
            intent.putExtra("comment", comment);
        }
        intent.putExtra("current_id", bookId);
        intent.putExtra("chapter_id", bookCurrentChapter.chapter_id);
        intent.putExtra("productType", Constant.BOOK_CONSTANT);
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
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (bookCurrentChapter == null) {
                        return false;
                    }
                    String str = audioInput.getText().toString();
                    if (TextUtils.isEmpty(str) || Pattern.matches("\\s*", str)) {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.CommentListActivity_some));
                        return true;
                    }
                    CommentActivity.sendComment(activity, false, Constant.BOOK_CONSTANT, bookId, "",
                            bookCurrentChapter.chapter_id, str, new CommentActivity.SendSuccess() {
                                @Override
                                public void Success(Comment comment) {
                                    audioInput.setText("");
                                    Input.getInstance().hindInput(audioInput, activity);
                                }
                            });
                    return true;
                }
                return false;
            }
        });
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
        @BindView(R.id.audio_speed_layout)
        LinearLayout audio_speed_layout;
        @BindView(R.id.audio_sound_layout)
        LinearLayout audio_sound_layout;
        @BindView(R.id.audio_sound_layout_tv)
        TextView audio_sound_tv;
        @BindView(R.id.audio_recycleview)
        RecyclerView audio_recycleview;
        @BindView(R.id.audio_like_line)
        View likeLine;
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
            bookStoareAdapter = new PublicMainAdapter(list, Constant.BOOK_CONSTANT, activity, false, false);
            audio_recycleview.setLayoutManager(new LinearLayoutManager(activity));
            audio_recycleview.setAdapter(bookStoareAdapter);
        }

        @OnClick({R.id.audio_last_chapter, R.id.audio_chapter_player_layout, R.id.audio_next_chapter,
                R.id.item_head_audio_rewind, R.id.item_head_audio_forward,
                R.id.audio_time_layout, R.id.audio_speed_layout, R.id.audio_chapter_layout, R.id.audio_sound_layout,
                R.id.audio_look_book,
                R.id.audio_switch_type_layout})
        public void onAudioClick(View view) {
            switch (view.getId()) {
                case R.id.audio_look_book:
                    // 看书
                    if (book != null) {
                        book.setCurrent_chapter_id(book.current_listen_chapter_id);
                        ChapterManager.getInstance().openBook(activity,book);
                    }
                    break;
                case R.id.item_head_audio_rewind:
                    // -15
                    break;
                case R.id.item_head_audio_forward:
                    // +15
                    break;
                case R.id.audio_last_chapter:
                    //上一章
                    if (bookCurrentChapter != null) {
                        instance.openBookChapterId(book, bookCurrentChapter.getLast_chapter(), bookChapterList);
                    }
                    break;
                case R.id.audio_next_chapter:
                    //下一章
                    if (bookCurrentChapter != null) {
                        instance.openBookChapterId(book, bookCurrentChapter.getNext_chapter(), bookChapterList);
                    }
                    break;
                case R.id.audio_chapter_player_layout:
                    // 播放控制
                    if (bookChapterList == null || bookChapterList.isEmpty() || bookCurrentChapter == null) {
                        if (InternetUtils.internet(activity)) {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
                        } else {
                            MyToash.ToashError(activity, R.string.audio_no_support_online_player);
                        }
                        return;
                    }
                    // 再点击前先判断播放工具类中是否为这本书
                    if (instance.isIsPlayerError() || instance.mBookId != bookId || instance.bookCurrentChapter == null ||
                            instance.bookCurrentChapter.chapter_id != bookCurrentChapter.chapter_id) {
                        // 不是这本书或者不是本章节
                        instance.openBookChapterId(book, bookCurrentChapter.getChapter_id(), bookChapterList);
                    } else {
                        if (instance.isPlayer == 1) {
                            instance.setBookPlayerStatus(false, book, bookCurrentChapter.getChapter_id(), bookChapterList);
                        } else {
                            instance.setBookPlayerStatus(true, book, bookCurrentChapter.getChapter_id(), bookChapterList);
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
                    if (book == null) {
                        return;
                    }
                    AudioSpeechSetDialog.showSpeechDialog(activity, 1, book.getSpeed(), new AudioSpeechSetDialog.OnSpeechSpeedListener() {
                                @Override
                                public void onClick(String name, String value) {
                                    viewHolder.speechSpeedImage.setImageResource(PublicStaticMethod.getSpeedImage(value));
                                    book.setSpeed(value);
                                    ObjectBoxUtils.addData(book, Book.class);
                                    if (instance.mBookId == bookId) {
                                        instance.setSpeechSpeed(value);
                                        if (bookCurrentChapter != null) {
                                            instance.openBookChapterId(book, bookCurrentChapter.getChapter_id(), bookChapterList);
                                        }
                                    }
                                }
                            });
                    break;
                case R.id.audio_chapter_layout:
                    // 目录
                    AudioBookCatalogDialogFragment dialogFragment = new AudioBookCatalogDialogFragment(activity, false,
                            book, null, new AudioBookCatalogDialogFragment.OnAudioBookCatalogClickListener() {
                        @Override
                        public void onChapter(long id, long chapterId, List<Object> objects) {
                            // 可以根据章节id进行播放，因为播放类中已经存在这本书
                            if (!objects.isEmpty()) {
                                bookChapterList.clear();
                                for (int i = 0; i < objects.size(); i++) {
                                    bookChapterList.add((BookChapter) objects.get(i));
                                }
                            }
                            instance.openBookChapterId(book, chapterId, bookChapterList);
                        }
                    });
                    dialogFragment.show(getSupportFragmentManager(), "AudioBookCatalogDialogFragment");
                    break;
                case R.id.audio_sound_layout:
                    AudioSpeechSetDialog.showSpeechDialog(activity, 2,
                            ShareUitls.getString(activity, "voice_preference", "xiaoyan"),
                            new AudioSpeechSetDialog.OnSpeechSpeedListener() {
                                @Override
                                public void onClick(String name, String value) {
                                    ShareUitls.putString(activity, "voice_preference", value);
                                    audio_sound_tv.setText(name);
                                    instance.setSpeechVoice(value);
                                    if (bookCurrentChapter != null) {
                                        instance.openBookChapterId(book, bookCurrentChapter.getChapter_id(), bookChapterList);
                                    }
                                }
                            });
                    break;
                case R.id.audio_switch_type_layout:
                    if (audioLoadingImage.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    // 语音切换，目前的策略：满足切换条件后会重新拉起一个界面进行语音的切换
                    if (relation != null && relation.getAudio_id() > 0) {
                        Intent bookIntent = new Intent();
                        bookIntent.putExtra("audio_id", relation.getAudio_id());
                        if (relation.getChapter_id() != 0) {
                            bookIntent.putExtra("chapter_id", relation.getChapter_id());
                        }
                        if (bookCurrentChapter != null) {
                            bookIntent.putExtra("current_chapter", bookCurrentChapter);
                        }
                        bookIntent.putExtra("is_jump", true);
                        bookIntent.setClass(activity, AudioSoundActivity.class);
                        startActivity(bookIntent);
                        overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
                    }
            }
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
                        if (instance.mBookId == bookId && instance.isPlayer == 1 && instance.bookCurrentChapter != null &&
                                bookCurrentChapter != null && instance.bookCurrentChapter.chapter_id == bookCurrentChapter.chapter_id) {
                            if (bookCurrentChapter.getLast_chapter() > 0) {
                                instance.openBookChapterId(book, bookCurrentChapter.getLast_chapter(), bookChapterList);
                            } else {
                                instance.setAudioProgress(0);
                            }
                        }
                    } else if (progress > maxProgress) {
                        // 下一首或者末尾
                        if (instance.mBookId == bookId && instance.isPlayer == 1 && instance.bookCurrentChapter != null &&
                                bookCurrentChapter != null && instance.bookCurrentChapter.chapter_id == bookCurrentChapter.chapter_id) {
                            if (bookCurrentChapter.getNext_chapter() > 0) {
                                instance.openBookChapterId(book, bookCurrentChapter.getNext_chapter(), bookChapterList);
                            } else {
                                instance.setAudioProgress(maxProgress * 1000);
                            }
                        }
                    }
                }
            });
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
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0, R.anim.activity_top_bottom_close);
    }

    /**
     * 用于更新关联书籍的数据
     * @param audioSwitchRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchRefresh(AudioSwitchRefresh audioSwitchRefresh) {
        if (relation != null && relation.getAudio_id() != 0) {
            // 可以切换ai
            if (!audioSwitchRefresh.isSound() && audioSwitchRefresh.getId() == bookId) {
                relation.setChapter_id(audioSwitchRefresh.getChapterId());
                viewHolder.audio_switch_type_text.get(1).setText(audioSwitchRefresh.getChapterName());
            }
        }
    }

    /**
     * 用于更新关联书籍的数据
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBookSelf(RefreshShelfCurrent s) {
        if (book != null && book.name != null) {
            if (s.productType == Constant.BOOK_CONSTANT) {
                if(s.book!=null&&book.book_id==s.book.book_id) {
                    book = s.book;
                }
            }
        }
    }


    /**
     * 用于更换播放图标
     * @param audioPlayerRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerStatus(AudioPlayerRefresh audioPlayerRefresh) {
        if (!audioPlayerRefresh.isSound() && audioPlayerRefresh.isPause()) {
            setPauseStatus();
        }
    }

    /**
     * 小说收藏
     *
     * @param isCollect
     */
    public void addBookToLocalShelf(int isCollect) {
        if (isCollect == 0) {
            flag=true;
            book.is_collect = 1;
            MyToash.ToashSuccess(activity, LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujias));
            ObjectBoxUtils.addData(book, Book.class);
            EventBus.getDefault().post(new RefreshShelf(Constant.BOOK_CONSTANT, new RefreshBookSelf(book, 1)));
            EventBus.getDefault().post(new RefreshBookInfo(book, true));
            setAddShelfStatus(true);
        }
    }

    private boolean flag;

    /**
     * 用于刷新收藏的回调
     *
     * @param refreshBookInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshBookInfo refreshBookInfo) {
        if (!flag&&refreshBookInfo.book != null && book != null) {
            if (refreshBookInfo.book.book_id == book.book_id) {
                book.is_collect = refreshBookInfo.book.is_collect;
                setAddShelfStatus(refreshBookInfo.book.is_collect == 1);
                if (instance.currentPlayBook.book_id == book.book_id) {
                    instance.currentPlayBook.setIs_collect(refreshBookInfo.book.is_collect);
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
        if (refreshBookInfo.productType == Constant.BOOK_CONSTANT && bookId == refreshBookInfo.id) {
            initHttpComment((int) book.current_listen_chapter_id);
        }
    }

    /**
     * 登录回调
     * @param refreshMine
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (UserUtils.isLogin(activity)) {
            initBookDetailData();
            if (purchaseDialog != null && purchaseDialog.isShowing()) {
                purchaseDialog.Dismiss();
            }
        }
    }

    /**
     * 打开指定章节
     *
     * @param factoryChapter
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshPageFactoryChapter factoryChapter) {
        if (factoryChapter.activity == null) {
            EventBus.getDefault().post(new AudioAiServiceRefresh(factoryChapter.bookChapter));
        }
    }
    /**
     * 购买弹窗
     * @param purchaseRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void purchaseRefresh(AudioPurchaseRefresh purchaseRefresh) {
        if (!purchaseRefresh.isSound() && purchaseRefresh.getBookChapter() != null) {
            if (purchaseDialog != null && purchaseDialog.isShowing()){
                return;
            }
            purchaseDialog = new PublicPurchaseDialog(activity, Constant.BOOK_CONSTANT, false, new PublicPurchaseDialog.BuySuccess() {
                @Override
                public void buySuccess(long[] ids, int num) {
                    purchaseRefresh.getBookChapter().setIs_preview(0);
                    ObjectBoxUtils.addData(purchaseRefresh.getBookChapter(), BookChapter.class);
                    instance.getBookChapter(purchaseRefresh.getBookChapter().getChapter_id());
                }
            },true);
            purchaseDialog.initData(purchaseRefresh.getBookChapter().book_id, purchaseRefresh.getBookChapter().getChapter_id() + "",
                   false, null);
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

    public void initHttpComment(int chapter_id) {
        if (bookId != 0) {
            ReaderParams readerParams = new ReaderParams(activity);
            readerParams.putExtraParams("book_id", bookId);
            if (book != null && book.current_listen_chapter_id != 0) {
                readerParams.putExtraParams("chapter_id", chapter_id);
            }
            HttpUtils.getInstance().sendRequestRequestParams(activity, mCommentListUrl, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                @Override
                public void onResponse(String response) {
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

                @Override
                public void onErrorResponse(String ex) {

                }
            });
        }
    }

    /**
     * 获取书籍数据
     *
     * @param isJump
     */
    private void getBook(boolean isJump) {
        if (book == null) {
            book = (Book) formIntent.getSerializableExtra("book");
            if (book == null) {
                if (bookId == 0) {
                    bookId = formIntent.getLongExtra("book_id", 0);
                }
                if (bookId != 0) {
                    book = ObjectBoxUtils.getBook(bookId);
                }
            } else {
                initAiBean(isJump);
                initBookDetailData();
                initIsSound();
                return;
            }
            if (book == null) {
                if (bookId != 0) {
                    getBookInfo(bookId, new OnGetBookInfo() {
                        @Override
                        public void onResponse(InfoBook infoBook) {
                            setBookInfo(infoBook);
                            if (isJump && relation != null && relation.getBook_id() != 0) {
                                book.setCurrent_listen_chapter_id(relation.getChapter_id());
                            }
                            initAiBean(isJump);
                            initBookDetailData();
                            initIsSound();
                        }

                        @Override
                        public void onErrorResponse() {
                        }
                    });
                }
            } else {
                initAiBean(isJump);
                initBookDetailData();
                initIsSound();
            }
        } else {
            initAiBean(isJump);
            initBookDetailData();
            initIsSound();
        }
    }

    /**
     * 根据类型获取数据
     * @param isJump
     */
    private void initAiBean(boolean isJump) {
        if (book != null) {
            viewHolder.audioPlayerImage.setVisibility(View.GONE);
            viewHolder.audioLoadingImage.setVisibility(View.VISIBLE);
            bookId = book.getBook_id();
            bookName = book.getName();
            bookCover = book.getCover();
            bookChapterName = book.getCurrent_chapter_text();
            // 界面赋值
            Glide.with(activity).load(bookCover).into(viewHolder.cover);
            viewHolder.bookTexts.get(1).setText(bookName);
            // 设置当前id
            if (formIntent.getLongExtra("chapter_id", 0) != 0) {
                book.setCurrent_listen_chapter_id(formIntent.getLongExtra("chapter_id", 0));
            }
            // 获取章节，不需要显示加载
            ChapterManager.downChapter(activity, bookId, new ChapterManager.DownChapter() {
                @Override
                public void success(List<BookChapter> bookChapters) {
                    if (bookChapters != null && !bookChapters.isEmpty()) {
                        if (!bookChapterList.isEmpty()) {
                            bookChapterList.clear();
                        }
                        bookChapterList.addAll(bookChapters);
                        if (book.getCurrent_listen_chapter_id() != 0) {
                            for (BookChapter bookChapter : bookChapters) {
                                if (book.getCurrent_listen_chapter_id() == bookChapter.getChapter_id()) {
                                    bookCurrentChapter = bookChapter;
                                    break;
                                }
                            }
                        } else {
                            bookCurrentChapter = bookChapters.get(0);
                        }
                        if (instance.isPlayer == 0) {
                            // 打开服务、播放器
                            AudioManager.openService(activity);
                            instance.openBook(book, bookChapterList);
                        } else if (instance.isPlayer == 1 || instance.isPlayer == 2 || instance.isPlayer == 3) {
                            // 不管是否为同一本书, 进入时都应该获取播放时需要的内容
                            setPauseStatus();
                            if (bookCurrentChapter != null) {
                                bookChapterName = bookCurrentChapter.getChapter_title();
                                book.current_listen_chapter_id = bookCurrentChapter.chapter_id;
                                book.current_chapter_listen_displayOrder = bookChapters.indexOf(bookCurrentChapter);
                                ObjectBoxUtils.addData(book, Book.class);
                                // 界面赋值
                                viewHolder.bookTexts.get(0).setText(bookChapterName);
                                setLastImageStatus(bookCurrentChapter.getLast_chapter() != 0);
                                setNextImageStatus(bookCurrentChapter.getNext_chapter() != 0);
                                // 小说的播放逻辑与有声的播放逻辑不同，我的做法跟着不跟随当前阅读章节走，跟随当前听书的章节走
                                // 而当前听书的章节只能在听书界面刷新
                                if (instance.mBookId == bookId && instance.bookCurrentChapter != null &&
                                        instance.bookCurrentChapter.chapter_id == bookCurrentChapter.chapter_id) {
                                    // 正在播放同一章,如果没有播放则恢复播放
                                    // （除非用户通过通知栏，播放拖动条进入）
                                    if (instance.isPlayer == 2) {
                                        if (instance.isPlayerCurrentId(false, bookCurrentChapter.getChapter_id())) {
                                            instance.openBookChapterId(book, bookCurrentChapter.getChapter_id(), bookChapterList);
                                        } else {
                                            if (!formIntent.getBooleanExtra("special_click", false) && !isJump) {
                                                instance.setPlayer(true);
                                            }
                                        }
                                    } else if (instance.isPlayer == 1) {
                                        setPlayStatus();
                                    }
                                    initHttpComment((int) bookCurrentChapter.getChapter_id());
                                } else {
                                    // 先判断是否是切换有声或ai，是切换时不改变播放状态
                                    if (!isJump) {
                                        // 不是同一章，并且不是切换，那就直接播放，
                                        instance.setStopPlayer();
                                        instance.setBookPlayerStatus(true, book, bookCurrentChapter.getChapter_id(), bookChapterList);
                                    }
                                }
                            }
                        }
                    } else {
                        setPauseStatus();
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
                    }
                }

                @Override
                public void fail() {
                    setPauseStatus();
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
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
        if (book != null) {
            if (formIntent.getBooleanExtra("special_click", false)) {
                // 从播放拖动框或通知栏进入
                Book localBook = ObjectBoxUtils.getBook(bookId);
                if (localBook != null) {
                    book.is_collect = localBook.is_collect;
                }
            }
            if (book.is_collect == 1) {
                setAddShelfStatus(true);
            } else {
                setAddShelfStatus(false);
            }
            viewHolder.speechSpeedImage.setImageResource(PublicStaticMethod.getSpeedImage(book.getSpeed()));
        }
        bottomDownLayout.setVisibility(View.GONE);
        viewHolder.item_head_audio_progress.setVisibility(View.GONE);
        viewHolder.audio_look_book.setVisibility(View.VISIBLE);
        viewHolder.audio_sound_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 请求获取界面推荐书籍
     * 是否关联有声
     * 广告
     */
    private void initBookDetailData() {
        if (bookId > 0) {
            ReaderParams params = new ReaderParams(activity);
            params.putExtraParams("book_id", bookId);
            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.AUDIO_AI_RECOMMEND, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                @Override
                public void onResponse(String response) {
                    InfoAudioItem optionItem = gson.fromJson(response, InfoAudioItem.class);
                    if (optionItem.getRelation() != null) {
                        relation = optionItem.getRelation();
                        // 刷新界面
                        refreshLayout(relation);
                        // 只有首次进入界面，会判断是否为切换过来的，是否携带当前章节的信息
                        if (http_flag == 0) {
                            AudioChapter chapter = (AudioChapter) formIntent.getSerializableExtra("current_chapter");
                            if (chapter != null) {
                                relation.setAudio_id(chapter.getAudio_id());
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
        HttpUtils.getInstance().sendRequestRequestParams(activity, mBookInfoUrl, params.generateParamsJson(), new HttpUtils.ResponseListener() {
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

    /**
     * 设置书籍信息
     * @param infoBook
     */
    private void setBookInfo(InfoBook infoBook) {
        book = new Book();
        book.book_id = bookId = infoBook.book_id;
        book.name = infoBook.name;
//        if (infoBook.author != null && !infoBook.author.isEmpty() && !TextUtils.isEmpty(infoBook.author.get(0))) {
//            StringBuilder name = new StringBuilder();
//            for (int i = 0; i < infoBook.author.size(); i++) {
//                if (i == 0) {
//                    name.append(infoBook.author.get(i));
//                } else {
//                    name.append(" ").append(infoBook.author.get(i));
//                }
//            }
//        }
        book.author = infoBook.author.replaceAll(",", " ");
        book.cover = infoBook.cover;
        book.description = infoBook.description;
        book.total_chapter = infoBook.total_chapters;
        ObjectBoxUtils.addData(book, Book.class);
    }

    /**
     * 用于刷新是否关联的布局
     * @param relation
     */
    private void refreshLayout(InfoAudioItem.Relation relation) {
        if (relation.getAudio_id() != 0) {
            viewHolder.audio_switch_type_layout.setVisibility(View.VISIBLE);
            viewHolder.audio_switch_type_image.setImageResource(R.mipmap.icon_sound);
            viewHolder.audio_switch_type_text.get(0).setText(LanguageUtil.getString(activity, R.string.audio_switch_sound));
            if (!TextUtils.isEmpty(relation.getChapter_title())) {
                viewHolder.audio_switch_type_text.get(1).setText(relation.getChapter_title());
            }
        } else {
            viewHolder.audio_switch_type_layout.setVisibility(View.GONE);
        }
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
