package com.ssreader.novel.ui.audio.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.lzx.starrysky.StarrySky;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.provider.SongInfo;
import com.lzx.starrysky.utils.TimerTaskManager;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.AudioAiServiceRefresh;
import com.ssreader.novel.eventbus.AudioPlayerRefresh;
import com.ssreader.novel.eventbus.AudioProgressRefresh;
import com.ssreader.novel.eventbus.AudioPurchaseRefresh;
import com.ssreader.novel.eventbus.AudioSoundServiceRefresh;
import com.ssreader.novel.eventbus.AudioSpeedBeanEventbus;
import com.ssreader.novel.eventbus.RefreshShelfCurrent;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.ChapterContent;
import com.ssreader.novel.model.GetBookChapterList;
import com.ssreader.novel.model.ReadHistory;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.MainActivity;
import com.ssreader.novel.ui.audio.AudioAiActivity;
import com.ssreader.novel.ui.audio.AudioSoundActivity;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.PublicStaticMethod;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ssreader.novel.utils.FileManager.isExistAudioLocal;

/**
 * 听书工具类
 * 里面保存两种数据，一种为有声，一种为ai
 * 两个章节当前的数据都使用当前章节来传递
 * 每次播放都保存数据：当前章节信息、当前播放MP3的信息
 */
public class AudioManager {

    private static AudioManager mReadingManager;
    private static Activity mContext;

    // 第三方播放工具
    private PlayerControl playerControl;
    // 播放进度监听
    private TimerTaskManager timerTaskManager;

    // 讯飞语音
    private SpeechSynthesizer mTts;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    // 是否为有声
    public static boolean isSound = true;
    // 是否正在播放 0 = 未播放，1 = 已播放，2 = 暂停, 3 = 播放已完成
    public int isPlayer = 0;
    // 用于控制播放球是否显示（与isPlayer一起使用，单独使用不准确）
    private boolean isShowPlayBall = false;
    // 是否播放错误
    private boolean isPlayerError = false;
    // 主要用于判断ai是否播放
    private long currentBookChapterId;
    // 文件存放地址
    public String path = "";
    // 书籍封面
    public String bookCover;
    // 用于定时
    public AudioSpeedBean audioSpeedBean;

    // 参数
    public long mAudioId, mBookId;
    // 有声数据
    public Audio currentPlayAudio;
    public AudioChapter audioCurrentChapter;
    public List<AudioChapter> audioChapterList;
    // 书籍数据
    public Book currentPlayBook;
    public BookChapter bookCurrentChapter;
    public ChapterContent chapterContent;
    public List<BookChapter> bookChapterList;

    // 播放监听
    private OnPlayerListener onPlayerListener;
    private OnCurrentChapterListen onCurrentChapterListen;

    /**
     * 用于传递当前播放的章节id
     *
     * @param onCurrentChapterListen
     */
    public void setOnCurrentChapterListen(AudioManager.OnCurrentChapterListen onCurrentChapterListen) {
        this.onCurrentChapterListen = onCurrentChapterListen;
    }

    /**
     * 播放监听
     *
     * @param onPlayerListener
     */
    public void setOnPlayerListener(AudioManager.OnPlayerListener onPlayerListener) {
        this.onPlayerListener = onPlayerListener;
    }

    public OnPlayerListener getOnPlayerListener() {
        return onPlayerListener;
    }

    public AudioManager() throws Exception {
        // 讯飞
        setMTts();
        // 有声播放器
        setPlayerControl();
        if (timerTaskManager == null) {
            timerTaskManager = new TimerTaskManager();
            timerTaskManager.setUpdateProgressTask(new Runnable() {
                @Override
                public void run() {
                    if (onPlayerListener != null) {
                        onPlayerListener.onProgress((int) (playerControl.getDuration() / 1000),
                                (int) (playerControl.getPlayingPosition() / 1000));
                    }
                    EventBus.getDefault().post(new AudioProgressRefresh(true, true, (int) (playerControl.getDuration() / 1000),
                            (int) (playerControl.getPlayingPosition() / 1000)));
                }
            });
            if (!playerControl.isPlaying()) {
                timerTaskManager.stopToUpdateProgress();
            }
        }
    }

    private void setMTts() {
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(BWNApplication.applicationContext, mTtsInitListener);
            if (mTts != null) {
                setParam();
            }
        }
    }

    private void setPlayerControl() {
        if (playerControl == null) {
            playerControl = StarrySky.with();
            playerControl.setRepeatMode(4);
        }
    }

    /**
     * 获取实例，基于main
     *
     * @param activity
     * @return
     */
    public static AudioManager getInstance(Activity activity) {
        if (BWNApplication.applicationContext.getActivity() != null && !BWNApplication.applicationContext.getActivity().isFinishing()) {
            mContext = BWNApplication.applicationContext.getActivity();
        } else {
            mContext = activity;
        }
        if (mReadingManager == null) {
            try {
                mReadingManager = new AudioManager();
            } catch (Exception e) {
            }
        }
        return mReadingManager;
    }

    private static InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {

            }
        }
    };

    /***************************************  sound  ****************************************/

    /**
     * 播放有声
     * @param audio
     * @param audioChapters
     */
    public void openAudio(Audio audio, List<AudioChapter> audioChapters) {
        isSound = true;
        if (audio != null) {
            currentPlayAudio = audio;
            mAudioId = audio.getAudio_id();
            if (audioChapterList == null) {
                audioChapterList = new ArrayList<>();
            }
            audioChapterList.clear();
            audioChapterList.addAll(audioChapters);
            bookCover = audio.getCover();
            if (audio.current_listen_chapter_id == 0) {
                currentPlayAudio.current_listen_chapter_id = audioChapters.get(0).chapter_id;
            }
            getAudioContent(currentPlayAudio.current_listen_chapter_id);
        }
    }

    /**
     * 获取MP3
     *
     * @param audioChapterId
     */
    public void getAudioContent(long audioChapterId) {
        if (onPlayerListener != null) {
            // 开始加载
            onPlayerListener.onLoading();
        }
        if (audioChapterList != null && !audioChapterList.isEmpty()) {
            for (AudioChapter audioChapter : audioChapterList) {
                if (audioChapter.chapter_id == audioChapterId) {
                    audioCurrentChapter = audioChapter;
                    break;
                }
            }
        }
        if (audioCurrentChapter == null) {
            if (onPlayerListener != null) {
                onPlayerListener.onError();
            }
            ReadHistory.addReadHistory(mContext, Constant.AUDIO_CONSTANT, mAudioId, audioChapterId);
            return;
        }
        if (!TextUtils.isEmpty(audioCurrentChapter.path) && new File(audioCurrentChapter.path).exists()) {
            startSong(audioChapterId, audioCurrentChapter.path);
        } else {
            String path = "";
            if (currentPlayAudio != null) {
                path = isExistAudioLocal(currentPlayAudio.audio_id, audioChapterId);
                if (path != null) {
                    // 本地存在
                    AudioChapter localChapter = ObjectBoxUtils.getAudioChapter(audioChapterId);
                    if (localChapter != null && localChapter.getRead_progress() != 0) {
                        audioCurrentChapter.setDuration_second(localChapter.getDuration_second());
                        audioCurrentChapter.setRead_progress(localChapter.getRead_progress());
                    }
                    audioCurrentChapter.path = path;
                    ObjectBoxUtils.addData(audioCurrentChapter, AudioChapter.class);
                    startSong(audioChapterId, path);
                } else {
                    startAudioHttpData(audioChapterId);
                }
            }
        }
        ReadHistory.addReadHistory(mContext, Constant.AUDIO_CONSTANT, mAudioId, audioChapterId);
    }

    /**
     * 获取有声数据
     * @param audioChapterId
     */
    public void startAudioHttpData(long audioChapterId) {
        ReaderParams params = new ReaderParams(mContext);
        params.putExtraParams("audio_id", mAudioId);
        params.putExtraParams("chapter_id", audioChapterId);
        HttpUtils.getInstance().sendRequestRequestParams(mContext, Api.AUDIO_CHAPTER_INFO, params.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ChapterContent chapterContent = HttpUtils.getGson().fromJson(response, ChapterContent.class);
                    // 是否收费
                    if (chapterContent.getIs_preview() == 0) {
                        startSong(chapterContent.getChapter_id(), chapterContent.getContent());
                    } else {
                        setPlayer(false);
                        if (onPlayerListener != null) {
                            onPlayerListener.onPause(true);
                        }
                        // 传递值
                        currentPlayAudio.current_listen_chapter_id = audioChapterId;
                        if (currentPlayAudio.is_read == 0) {
                            currentPlayAudio.is_read = 1;
                            currentPlayAudio.isRecommend = false;
                            EventBus.getDefault().post(new RefreshShelfCurrent(Constant.AUDIO_CONSTANT, currentPlayAudio));
                        }
                        ObjectBoxUtils.addData(currentPlayAudio, Audio.class);
                        // 如果已加入书架，会刷新书架中的数据
                        EventBus.getDefault().post(new RefreshShelfCurrent(Constant.AUDIO_CONSTANT, currentPlayAudio));
                        if (audioCurrentChapter.is_read == 0) {
                            audioCurrentChapter.is_read = 1;
                            ObjectBoxUtils.addData(audioCurrentChapter, AudioChapter.class);
                        }
                        if (onCurrentChapterListen != null) {
                            onCurrentChapterListen.onCurrentAudioChapter(mAudioId, audioCurrentChapter);
                        }
                        if (!SystemUtil.isForeground(mContext, AudioSoundActivity.class.getName())) {
                            MyToash.ToashError(mContext, LanguageUtil.getString(mContext, R.string.audio_vip_chapter));
                        } else {
                            EventBus.getDefault().post(new AudioPurchaseRefresh(false, audioCurrentChapter, false));
                        }
                    }
                } else {
                    if (onPlayerListener != null) {
                        onPlayerListener.onError();
                    }
                }
            }

            @Override
            public void onErrorResponse(String ex) {
                if (onPlayerListener != null) {
                    onPlayerListener.onError();
                }
            }
        });
    }

    /**
     * 播放MP3、wav
     *
     * @param audioChapterId
     * @param path
     */
    private void startSong(long audioChapterId, String path) {
        currentPlayAudio.current_listen_chapter_id = audioChapterId;
        if (currentPlayAudio.is_read == 0) {
            currentPlayAudio.is_read = 1;
            currentPlayAudio.isRecommend = false;
            EventBus.getDefault().post(new RefreshShelfCurrent(Constant.AUDIO_CONSTANT, currentPlayAudio));
        }
        ObjectBoxUtils.addData(currentPlayAudio, Audio.class);
        // 如果已加入书架，会刷新书架中的数据
        EventBus.getDefault().post(new RefreshShelfCurrent(Constant.AUDIO_CONSTANT, currentPlayAudio));
        if (onCurrentChapterListen != null) {
            onCurrentChapterListen.onCurrentAudioChapter(mAudioId, audioCurrentChapter);
        }
        // 使用EventBus传值
        EventBus.getDefault().post(new AudioSoundServiceRefresh(audioChapterId, path));
    }

    /***************************************    ai   ****************************************/

    /**
     * 设置book
     * @param book
     * @param bookChapters
     */
    public void openBook(Book book, List<BookChapter> bookChapters) {
        isSound = false;
        if (book != null) {
            mBookId = book.getBook_id();
            currentPlayBook = book;
            if (bookChapterList == null) {
                bookChapterList = new ArrayList<>();
            }
            bookChapterList.clear();
            bookChapterList.addAll(bookChapters);
            bookCover = book.getCover();
            if (book.current_listen_chapter_id == 0) {
                currentPlayBook.current_listen_chapter_id = bookChapters.get(0).chapter_id;
                currentPlayBook.current_chapter_listen_displayOrder = 0;
            }
            getBookChapter(currentPlayBook.current_listen_chapter_id);
        }
    }

    /**
     * 获取章节信息
     */
    public void getBookChapter(long chapterId) {
        if (onPlayerListener != null) {
            // 开始加载
            onPlayerListener.onLoading();
        }
        getChapter(chapterId, new QueryChapterListener() {
            @Override
            public void success(BookChapter currentChapter) {
                if (currentChapter == null) {
                    if (onPlayerListener != null) {
                        onPlayerListener.onError();
                    }
                    return;
                }
                getChapterContent(currentChapter, mBookId, chapterId, new ChapterDownload() {
                    @Override
                    public void finish(ChapterContent c) {
                        chapterContent = c;
                        bookCurrentChapter = currentChapter;
                        chapterContent.setChapter_id(chapterId);
                        // 当前id
                        currentPlayBook.current_listen_chapter_id = chapterId;
                        // 如果该书籍在书架中，则会刷新数据
                        EventBus.getDefault().post(new RefreshShelfCurrent(Constant.BOOK_CONSTANT, currentPlayBook));
                        ObjectBoxUtils.addData(currentPlayBook, Book.class);
                        if (onCurrentChapterListen != null) {
                            onCurrentChapterListen.onCurrentBookChapter(mBookId, currentChapter);
                        }
                        // 判断是否收费
                        if (currentChapter.getIs_preview() == 1) {
                            setPlayer(false);
                            if (onPlayerListener != null) {
                                onPlayerListener.onPause(false);
                            }
                            if (!SystemUtil.isForeground(mContext, AudioAiActivity.class.getName())) {
                                MyToash.ToashError(mContext, LanguageUtil.getString(mContext, R.string.audio_vip_chapter));
                            } else {
                                EventBus.getDefault().post(new AudioPurchaseRefresh(false, currentChapter, false));
                            }
                        } else {
                            EventBus.getDefault().post(new AudioAiServiceRefresh(currentChapter));
                        }
                    }
                });
            }

            @Override
            public void fail() {
                if (onPlayerListener != null) {
                    onPlayerListener.onError();
                }
            }
        });
    }

    /**
     * 获取章节
     *
     * @param chapterId
     * @param queryChapterListener
     */
    public void getChapter(final long chapterId, QueryChapterListener queryChapterListener) {
        if (chapterId != 0) {
            if (!bookChapterList.isEmpty()) {
                BookChapter bookChapter = null;
                for (BookChapter chapterItem : bookChapterList) {
                    if (chapterItem.getChapter_id() == chapterId) {
                        bookChapter = chapterItem;
                        break;
                    }
                }
                if (bookChapter == null) {
                    bookChapter = bookChapterList.get(0);
                }
                queryChapterListener.success(bookChapter);
            } else {
                queryChapterListener.fail();
            }
        } else {
            if (!bookChapterList.isEmpty()) {
                queryChapterListener.success(bookChapterList.get(0));
            } else {
                queryChapterListener.fail();
            }
        }
    }

    /**
     * 获取章节内容
     *
     * @param bookChapter
     * @param bookId
     * @param chapterId
     * @param download
     */
    private void getChapterContent(final BookChapter bookChapter, final long bookId, final long chapterId, final ChapterDownload download) {
        // 获取到内容
        getChapterContent(bookId, chapterId, new GetChapterContent() {
                    @Override
                    public void getChapterContent(ChapterContent chapterContent) {
                        if (chapterContent != null) {
                            bookChapter.setUpdate_time(chapterContent.getUpdate_time());
                            bookChapter.setIs_preview(chapterContent.getIs_preview());
                            bookChapter.chapter_text = chapterContent.getContent();
                            String filepath = FileManager.getLocalBookTxtPath(bookChapter);
                            bookChapter.setChapter_path(filepath);
                            FileManager.createFile(filepath, chapterContent.getContent().getBytes());
                            ObjectBoxUtils.addData(bookChapter, BookChapter.class);
                        }
                        download.finish(chapterContent);
                    }
                }
        );
    }

    /**
     * 获取章节内容，之后保存内容
     *
     * @param book_id
     * @param chapter_id
     * @param getChapterContent
     */
    private void getChapterContent(final long book_id, final long chapter_id, GetChapterContent getChapterContent) {
        ReaderParams params = new ReaderParams(mContext);
        params.putExtraParams("book_id", book_id);
        params.putExtraParams("chapter_id", chapter_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(mContext, Api.chapter_text, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        ChapterContent chapterContent = HttpUtils.getGson().fromJson(result, ChapterContent.class);
                        getChapterContent.getChapterContent(chapterContent);
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (onPlayerListener != null) {
                            onPlayerListener.onError();
                        }
                    }
                }
        );
    }

    private void setParam() {
        path = Environment.getExternalStorageDirectory() + "/ssreader/msc/audio.wav";
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, ShareUitls.getString(mContext, "voice_preference", "xiaoyan"));
            //设置合成语速
            if (currentPlayBook != null) {
                mTts.setParameter(SpeechConstant.SPEED, currentPlayBook.getSpeed());
            } else {
                mTts.setParameter(SpeechConstant.SPEED, "60");
            }
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, ShareUitls.getString(mContext, "pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, ShareUitls.getString(mContext, "volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, ShareUitls.getString(mContext, "voice_preference", "xiaoyan"));
        }
        // 设置采样率
        mTts.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, ShareUitls.getString(mContext, "stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, path);
    }

    /***********************************************************************************************/

    /**
     * 定时
     *
     * @param speedBean
     */
    public void setAudioSpeedBean(AudioSpeedBean speedBean) {
        if (timerTaskManager != null && speedBean != null) {
            audioSpeedBean = speedBean;
            timerTaskManager.cancelCountDownTask();
            if (speedBean.audioDate == 0) {
                // 取消定时
            } else if (speedBean.audioDate == -1) {
                // 播完这章，在播放完成的监听中暂停
                EventBus.getDefault().post(new AudioSpeedBeanEventbus((audioSpeedBean)));
            } else {
                if (speedBean.audioDate > 0) {
                    timerTaskManager.startCountDownTask(speedBean.audioDate * 1000, new TimerTaskManager.OnCountDownFinishListener() {
                        @Override
                        public void onFinish() {
                            // 停止播放
                            timerTaskManager.stopToUpdateProgress();
                            audioSpeedBean.audioName = LanguageUtil.getString(mContext, R.string.audio_not_open);
                            audioSpeedBean.audioDate = 0;
                            setStopPlayer();
                            EventBus.getDefault().post(new AudioSpeedBeanEventbus((audioSpeedBean)));
                        }

                        @Override
                        public void onTick(long millisUntilFinished) {
                            audioSpeedBean.audioDate = (int) (millisUntilFinished / 1000);
                            EventBus.getDefault().post(new AudioSpeedBeanEventbus((audioSpeedBean)));
                        }
                    });
                }
            }
        }
    }

    /**
     * 设置语速
     *
     * @param audioName
     */
    public void setSpeechSpeed(String audioName) {
        if (isSound) {
            currentPlayAudio.setSpeed(audioName);
            if (playerControl != null) {
                float derailleur = PublicStaticMethod.getSpeed(audioName);
                playerControl.onDerailleur(false, derailleur);
            }
        } else {
            currentPlayBook.setSpeed(audioName);
            if (mTts != null) {
                mTts.setParameter(SpeechConstant.SPEED, audioName);
            }
        }
    }

    /**
     * 设置语速
     *
     * @param voice
     */
    public void setSpeechVoice(String voice) {
        if (mTts != null) {
            mTts.setParameter(SpeechConstant.VOICE_NAME, voice);
        }
    }

    /**
     * 是否显示听书状态弹窗
     *
     * @return
     */
    public boolean isShowAudioStatusLayout() {
        return isPlayer != 0 && isShowPlayBall;
    }

    public void setShowPlayBall(boolean isPlayerError) {
        this.isShowPlayBall = isPlayerError;
    }

    /**
     * 点击播放拖动条进入听书界面
     * @param activity
     */
    public void IntoAudioActivity(Activity activity, boolean isError) {
        if (activity != null && !activity.isFinishing()) {
            Intent audioIntent = new Intent();
            if (!isError) {
                audioIntent.putExtra("special_click", true);
            }
            if (isSound && currentPlayAudio != null) {
                audioIntent.putExtra("audio", currentPlayAudio);
                audioIntent.setClass(mContext, AudioSoundActivity.class);
            } else if (!isSound && currentPlayBook != null) {
                audioIntent.putExtra("book", currentPlayBook);
                audioIntent.setClass(mContext, AudioAiActivity.class);
            }
            activity.startActivity(audioIntent);
            activity.overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
        }
    }

    /**
     * 清除对应的数据
     *
     * @param isSound true = 有声
     */
    public void clearOtherBean(boolean isSound) {
        if (audioCurrentChapter != null && playerControl != null && playerControl.getPlayingPosition() != 0) {
            audioCurrentChapter.setDuration_second((int) (playerControl.getDuration() / 1000));
            audioCurrentChapter.setRead_progress(playerControl.getPlayingPosition());
            ObjectBoxUtils.addData(audioCurrentChapter, AudioChapter.class);
        }
        if (!isSound) {
            // 清除ai
            if (mTts != null) {
                mTts.pauseSpeaking();
            }
            mBookId = 0;
            currentBookChapterId = 0;
            currentPlayBook = null;
            bookCurrentChapter = null;
            EventBus.getDefault().post(new AudioPlayerRefresh(false, true));
        } else {
            if (playerControl != null) {
                playerControl.stopMusic();
                if (!playerControl.getNowPlayingSongId().isEmpty() && audioCurrentChapter != null) {
                    // 移除播放信息
                    playerControl.removeSongInfo(String.valueOf(audioCurrentChapter.getChapter_id()));
                }
            }
            mAudioId = 0;
            currentPlayAudio = null;
            audioCurrentChapter = null;
            EventBus.getDefault().post(new AudioPlayerRefresh(true, true));
        }
    }

    /**
     * 销毁
     */
    public void onCancel(boolean isMain) {
        isPlayer = 0;
        isShowPlayBall = false;
        if (mTts != null) {
            mTts.stopSpeaking();
            EventBus.getDefault().post(new AudioPlayerRefresh(false, true));
        }
        if (playerControl != null) {
            // 保存记录
            if (currentPlayAudio != null && audioCurrentChapter != null && playerControl.getPlayingPosition() != 0) {
                audioCurrentChapter.setRead_progress(playerControl.getPlayingPosition());
                audioCurrentChapter.setDuration_second((int) (playerControl.getDuration() / 1000));
                ObjectBoxUtils.addData(audioCurrentChapter, AudioChapter.class);
            }
            playerControl.stopMusic();
            EventBus.getDefault().post(new AudioPlayerRefresh(true, true));
        }
        if (timerTaskManager != null) {
            timerTaskManager.cancelCountDownTask();
        }
        if (isMain) {
            if (timerTaskManager != null) {
                timerTaskManager.removeUpdateProgressTask();
                timerTaskManager.cancelCountDownTask();
            }
            if (mReadingManager != null) {
                mReadingManager = null;
            }
        }
        // 销毁服务
        if (mContext != null) {
            mContext.stopService(new Intent(mContext, Mp3Service.class));
        }
    }

    /**
     * 获取章节
     */
    public interface QueryChapterListener {

        void success(BookChapter bookChapter);

        void fail();
    }

    /**
     * 用于获取章节内容
     * 并存到本地
     */
    public interface ChapterDownload {

        void finish(ChapterContent chapterContent);
    }

    /**
     * 用于获取章节内容
     */
    public interface GetChapterContent {

        void getChapterContent(ChapterContent chapterContent);
    }

    /************************************    公共方法    ****************************************/

    /**
     * 设置播放状态
     *
     * @param status
     */
    public void setPlayer(boolean status) {
        if (status) {
            // 恢复播放
            if (isSound) {
                if (playerControl != null && !playerControl.isPlaying()) {
                    playerControl.restoreMusic();
                }
            } else {
                if (mTts != null) {
                    mTts.resumeSpeaking();
                }
            }
        } else {
            isPlayer = 2;
            if (playerControl != null) {
                playerControl.pauseMusic();
            }
            if (mTts != null && mTts.isSpeaking()) {
                mTts.pauseSpeaking();
            }
        }
    }

    /**
     * 停止播放
     */
    public void setStopPlayer() {
        isPlayer = 2;
        if (playerControl != null) {
            playerControl.stopMusic();
        }
        if (mTts != null && mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
    }

    /**
     * 控制有声播放
     * @param status
     * @param audio
     * @param chapterId
     * @param audioChapters
     */
    public void setAudioPlayerStatus(boolean status, Audio audio, long chapterId, List<AudioChapter> audioChapters) {
        isSound = true;
        // 清除另一类型数据
        clearOtherBean(false);
        if (chapterId == 0 || audio == null) {
            if (onPlayerListener != null) {
                onPlayerListener.onError();
            }
            return;
        }
        bookCover = audio.getCover();
        if (audio.audio_id != mAudioId) {
            mAudioId = audio.audio_id;
            currentPlayAudio = audio;
        }
        if (audioChapters != null) {
            if (audioChapterList == null) {
                audioChapterList = new ArrayList<>();
            }
            if (!audioChapters.isEmpty()) {
                audioChapterList.clear();
                audioChapterList.addAll(audioChapters);
            }
        }
        currentPlayAudio.is_collect = audio.is_collect;
        currentPlayAudio.current_listen_chapter_id = chapterId;
        if (status) {
            if (audioCurrentChapter == null) {
                getAudioContent(chapterId);
            } else if (audioCurrentChapter.chapter_id != chapterId || audioCurrentChapter.getAudio_id() != audio.getAudio_id()) {
                getAudioContent(chapterId);
            } else if (isPlayerCurrentId(true, chapterId)) {
                getAudioContent(chapterId);
            } else {
                setPlayer(true);
            }
        } else {
            setPlayer(false);
        }
    }

    /**
     * 控制ai播放
     *  @param status
     * @param book
     * @param chapterId
     * @param bookChapters
     */
    public void setBookPlayerStatus(boolean status, Book book, long chapterId, List<BookChapter> bookChapters) {
        isSound = false;
        // 清除另一类型数据
        clearOtherBean(true);
        if (chapterId == 0 || book == null) {
            if (onPlayerListener != null) {
                onPlayerListener.onError();
            }
            return;
        }
        bookCover = book.getCover();
        if (book.book_id != mBookId) {
            mBookId = book.book_id;
            currentPlayBook = book;
        }
        if (bookChapters != null) {
            if (bookChapterList == null) {
                bookChapterList = new ArrayList<>();
            }
            if (!bookChapters.isEmpty()) {
                bookChapterList.clear();
                bookChapterList.addAll(bookChapters);
            }
        }
        currentPlayBook.is_collect = book.is_collect;
        currentPlayBook.current_chapter_id = book.current_chapter_id;
        currentPlayBook.current_listen_chapter_id = chapterId;
        mTts.setParameter(SpeechConstant.SPEED, currentPlayBook.getSpeed());
        if (status) {
            if (bookCurrentChapter == null) {
                getBookChapter(chapterId);
            } else if (bookCurrentChapter.chapter_id != chapterId || bookCurrentChapter.getBook_id() != book.getBook_id()) {
                getBookChapter(chapterId);
            } else if (isPlayerCurrentId(false, chapterId)) {
                getBookChapter(chapterId);
            } else {
                setPlayer(true);
            }
        } else {
            setPlayer(false);
        }
    }

    /**
     * 获取听书信息
     *  @param audio
     * @param chapterId
     * @param audioChapters
     */
    public void openAudioChapterId(Audio audio, long chapterId, List<AudioChapter> audioChapters) {
        isSound = true;
        // 清除另一类型数据
        clearOtherBean(false);
        if (chapterId == 0 || audio == null) {
            if (onPlayerListener != null) {
                onPlayerListener.onError();
            }
            return;
        }
        bookCover = audio.getCover();
        if (audio.audio_id != mAudioId) {
            currentPlayAudio = audio;
            mAudioId = audio.audio_id;
        }
        if (audioChapters != null) {
            if (audioChapterList == null) {
                audioChapterList = new ArrayList<>();
            }
            if (!audioChapters.isEmpty()) {
                audioChapterList.clear();
                audioChapterList.addAll(audioChapters);
            }
        }
        currentPlayAudio.is_collect = audio.is_collect;
        currentPlayAudio.current_listen_chapter_id = chapterId;
        getAudioContent(chapterId);
    }

    /**
     * 获取听书信息
     *  @param book
     * @param chapterId
     * @param bookChapters
     */
    public void openBookChapterId(Book book, long chapterId, List<BookChapter> bookChapters) {
        isSound = false;
        // 清除另一类型数据
        clearOtherBean(true);
        if (chapterId == 0 || book == null) {
            if (onPlayerListener != null) {
                onPlayerListener.onError();
            }
            return;
        }
        bookCover = book.getCover();
        if (book.book_id != mBookId) {
            currentPlayBook = book;
            mBookId = book.book_id;
        }
        if (bookChapters != null) {
            if (bookChapterList == null) {
                bookChapterList = new ArrayList<>();
            }
            if (!bookChapters.isEmpty()) {
                bookChapterList.clear();
                bookChapterList.addAll(bookChapters);
            }
        }
        currentPlayBook.is_collect = book.is_collect;
        currentPlayBook.current_listen_chapter_id = chapterId;
        currentPlayBook.current_chapter_id = book.current_chapter_id;
        mTts.setParameter(SpeechConstant.SPEED, currentPlayBook.getSpeed());
        getBookChapter(chapterId);
    }

    /**
     * 是否收费
     * @return
     */
    public boolean isPreview() {
        if (isSound) {
            if (audioCurrentChapter != null) {
                return audioCurrentChapter.getIs_preview() == 1;
            }
        } else {
            if (bookCurrentChapter != null) {
                return bookCurrentChapter.getIs_preview() == 1;
            }
        }
        return false;
    }

    /**
     * 滑动到指定位置
     *
     * @param progress 单位：毫秒
     */
    public void setAudioProgress(long progress) {
        if (isSound) {
            // 有声
            playerControl.seekTo(progress);
        }
    }

    /**
     * 获取当前音乐的最大进度值
     *
     * @return
     */
    public int getMaxProgress() {
        if (isSound) {
            if (playerControl != null) {
                return (int) (playerControl.getDuration() / 1000);
            }
        } else {
            if (mTts != null) {
                return 100;
            }
        }
        return 0;
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getProgress() {
        if (isSound) {
            if (playerControl != null) {
                return (int) (playerControl.getPlayingPosition() / 1000);
            }
        } else {
            if (isPlayer != 1 && mTts != null) {
                return 0;
            }
        }
        return 0;
    }

    public void setPlayerError(boolean errorStatus) {
        isPlayerError = errorStatus;
    }

    public boolean isIsPlayerError() {
        return isPlayerError;
    }

    /**
     * 判断是否播放的当前id
     * @param isSound
     * @param chapterId
     * @return
     */
    public boolean isPlayerCurrentId(boolean isSound, long chapterId) {
        if (isSound) {
            if (!playerControl.getNowPlayingSongId().equals(String.valueOf(chapterId))) {
                return true;
            }
        } else {
            if (currentBookChapterId != chapterId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置ai当前播放的id
     * @param id
     */
    public void setCurrentBookChapterId(long id) {
        currentBookChapterId = id;
    }

    /**
     * 获取ai当前章节的内容
     * @return
     */
    public ChapterContent getChapterContent() {
        return chapterContent;
    }

    /**
     * 获取第三方控制器
     * @return
     */
    public PlayerControl getPlayerControl() {
        if (playerControl == null) {
            playerControl = StarrySky.with();
            playerControl.setRepeatMode(4);
        }
        return playerControl;
    }

    public SpeechSynthesizer getTts() {
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(BWNApplication.applicationContext, mTtsInitListener);
            if (mTts != null) {
                setParam();
            }
        }
        return mTts;
    }

    /**
     * 获取时间进度控制器
     * @return
     */
    public TimerTaskManager getTimerTaskManager() {
        return timerTaskManager;
    }

    /**
     * 打开服务
     * @param activity
     */
    public static void openService(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        // 打开服务
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(activity, Mp3Service.class);
        activity.startService(serviceIntent);
    }

    /************************************      监听      ****************************************/

    /**
     * 播放监听
     */
    public interface OnPlayerListener {

        // 用于表示播放章节正在加载中
        void onLoading();

        // 开始播放
        void onStart(int duration);

        // 播放监听（一秒一下）
        void onProgress(int duration, int minProgress);

        void onPause(boolean isSound);

        // 播放结束,传递播放id
        void onCompletion(SongInfo songInfo);

        // 报错
        void onError();
    }

    /**
     * 用于传递章节id
     */
    public interface OnCurrentChapterListen {

        void onCurrentAudioChapter(long audioId, AudioChapter audioChapter);

        void onCurrentBookChapter(long bookId, BookChapter bookChapter);
    }
}
