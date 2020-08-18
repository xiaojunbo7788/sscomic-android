package com.ssreader.novel.ui.audio.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.MemoryFile;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.lzx.starrysky.control.OnPlayerEventListener;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.provider.SongInfo;
import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.eventbus.AudioAiServiceRefresh;
import com.ssreader.novel.eventbus.AudioProgressRefresh;
import com.ssreader.novel.eventbus.AudioServiceRefresh;
import com.ssreader.novel.eventbus.AudioSoundServiceRefresh;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.ui.activity.MainActivity;
import com.ssreader.novel.ui.audio.AudioAiActivity;
import com.ssreader.novel.ui.audio.AudioSoundActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.PublicStaticMethod;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * 用于播放
 * 只有播放时才创建
 */
public class Mp3Service extends Service {

    // 前台通知id
    public static final int CHANNEL_ID = 0x111;
    private final String NOTIFY_ID = "audio";
    // 通知管理类
    private NotificationManager notificationManager;

    // 播放工具类
    private AudioManager instance;
    // 第三方播放工具
    private PlayerControl playerControl;
    private SpeechSynthesizer mTts;
    private MemoryFile memFile;
    private volatile long mTotalSize = 0;
    private Vector<byte[]> container = new Vector<>();

    // 是否显示通知栏
    private boolean isShowNotify = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        if (instance == null) {
            instance = AudioManager.getInstance(BWNApplication.applicationContext.getActivity());
        }
    }

    /**
     * 用于传递有声
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshAudioService(AudioSoundServiceRefresh refresh) {
        createNotify();
        startSong(refresh.getChapterId(), refresh.getPath());
    }

    /**
     * 用于传递ai
     *
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshAiService(AudioAiServiceRefresh refresh) {
        createNotify();
        startText(refresh.getBookChapter());
    }

    /**
     * 播放有声
     * @param audioChapterId
     * @param path
     */
    public void startSong(long audioChapterId, String path) {
        if (instance == null) {
            instance = AudioManager.getInstance(BWNApplication.applicationContext.getActivity());
        }
        if (playerControl == null) {
            playerControl = instance.getPlayerControl();
        }
        if (playerControl.isPlaying()) {
            playerControl.stopMusic();
        }
        if (playerControl.getPlayerEventListeners().isEmpty()) {
            playerControl.addPlayerEventListener(onPlayerEventListener);
        }
        playerControl.prepare();
        SongInfo info = new SongInfo();
        info.setSongId(audioChapterId + "");
        info.setSongUrl(path);
        float derailleur = PublicStaticMethod.getSpeed(instance.currentPlayAudio.getSpeed());
        playerControl.onDerailleur(false, derailleur);
        playerControl.playMusicByInfoDirect(info);
        if (instance.audioCurrentChapter != null) {
            AudioChapter localChapter = ObjectBoxUtils.getAudioChapter(instance.audioCurrentChapter.getChapter_id());
            if (localChapter != null && localChapter.getRead_progress() != 0) {
                instance.audioCurrentChapter.setDuration_second(localChapter.getDuration_second());
                instance.audioCurrentChapter.setRead_progress(localChapter.getRead_progress());
                // 跳转到保存的位置
                playerControl.seekTo(localChapter.getRead_progress());
            }
            if (instance.audioCurrentChapter.is_read == 0) {
                instance.audioCurrentChapter.is_read = 1;
                ObjectBoxUtils.addData(instance.audioCurrentChapter, AudioChapter.class);
            }
        }
    }

    /**
     * 播放监听
     */
    private OnPlayerEventListener onPlayerEventListener = new OnPlayerEventListener() {

        @Override
        public void onMusicSwitch(@NotNull SongInfo songInfo) {
            // 切换歌曲（先调用）
        }

        @Override
        public void onPlayerStart() {
            // 开始播放
            instance.isPlayer = 1;
            instance.setShowPlayBall(true);
            instance.setPlayerError(false);
            if (instance.getTimerTaskManager() != null) {
                instance.getTimerTaskManager().startToUpdateProgress();
            }
            if (instance.getOnPlayerListener() != null && playerControl != null) {
                instance.getOnPlayerListener().onStart((int) (playerControl.getDuration() / 1000));
            }
            EventBus.getDefault().post(new AudioProgressRefresh(true, true, 0, 0));
            // 开启前台通知
            if (instance.currentPlayAudio != null && instance.audioCurrentChapter != null) {
                instance.audioCurrentChapter.setDuration_second((int) (playerControl.getDuration() / 1000));
                isShowNotify = true;
                startForeground(CHANNEL_ID, buildNotification(true, true, true, instance.currentPlayAudio.getCover(),
                        instance.currentPlayAudio.getName(), instance.audioCurrentChapter.getChapter_title()));
            }
        }

        @Override
        public void onPlayerPause() {
            // 暂停播放
            instance.isPlayer = 2;
            if (instance.getTimerTaskManager() != null) {
                instance.getTimerTaskManager().stopToUpdateProgress();
            }
            if (instance.getOnPlayerListener() != null) {
                instance.getOnPlayerListener().onPause(true);
            }
            EventBus.getDefault().post(new AudioProgressRefresh(false, true, 0, 0));
            if (instance.currentPlayAudio != null && instance.audioCurrentChapter != null) {
                if (playerControl.getPlayingPosition() != 0) {
                    instance.audioCurrentChapter.setRead_progress(playerControl.getPlayingPosition());
                    instance.audioCurrentChapter.setDuration_second((int) (playerControl.getDuration() / 1000));
                    ObjectBoxUtils.addData(instance.audioCurrentChapter, AudioChapter.class);
                }
                if (isShowNotify) {
                    notificationManager.notify(CHANNEL_ID, buildNotification(false, true, false, instance.currentPlayAudio.getCover(),
                            instance.currentPlayAudio.getName(), instance.audioCurrentChapter.getChapter_title()));
                }
            }
        }

        @Override
        public void onPlayerStop() {
            // 停止播放（停止播放，当前播放位置会被置0）
            instance.isPlayer = 2;
            if (instance.getTimerTaskManager() != null) {
                instance.getTimerTaskManager().stopToUpdateProgress();
            }
            if (instance.getOnPlayerListener() != null) {
                instance.getOnPlayerListener().onPause(true);
            }
            EventBus.getDefault().post(new AudioProgressRefresh(false, true, 0, 0));
            if (instance.currentPlayAudio != null && instance.audioCurrentChapter != null && isShowNotify) {
                notificationManager.notify(CHANNEL_ID, buildNotification(false, true, false, instance.currentPlayAudio.getCover(),
                        instance.currentPlayAudio.getName(), instance.audioCurrentChapter.getChapter_title()));
            }
        }

        @Override
        public void onPlayCompletion(@NotNull SongInfo songInfo) {
            // 播放完成(根据songInfo获取信息), 判断是否由下一章
            if (instance.getTimerTaskManager() != null) {
                instance.getTimerTaskManager().stopToUpdateProgress();
            }
            instance.isPlayer = 2;
            EventBus.getDefault().post(new AudioProgressRefresh(false, true, 0, 100));
            if (instance.audioSpeedBean != null && instance.audioSpeedBean.audioDate == -1) {
                instance.setStopPlayer();
            } else {
                if (instance.audioCurrentChapter != null) {
                    instance.getAudioContent(instance.audioCurrentChapter.getNext_chapter());
                }
            }
            if (instance.getOnPlayerListener() != null) {
                instance.getOnPlayerListener().onCompletion(null);
            }
        }

        @Override
        public void onBuffering() {
            // 正在缓冲
        }

        @Override
        public void onError(int errorCode, @NotNull String errorMsg) {
            if (playerControl != null && playerControl.isPlaying()) {
                playerControl.pauseMusic();
            }
            instance.isPlayer = 2;
            instance.setPlayerError(true);
            if (instance.getOnPlayerListener() != null) {
                instance.getOnPlayerListener().onError();
            }
            EventBus.getDefault().post(new AudioProgressRefresh(false, true, 0, 0));
            if (instance.currentPlayAudio != null && instance.audioCurrentChapter != null && isShowNotify) {
                notificationManager.notify(CHANNEL_ID, buildNotification(false, true, false, instance.currentPlayAudio.getCover(),
                        instance.currentPlayAudio.getName(), instance.audioCurrentChapter.getChapter_title()));
            }
        }
    };

    /***************************************  讯飞语音 ***************************************/

    /**
     * 播放ai
     * @param currentChapter
     */
    private void startText(BookChapter currentChapter) {
        if (instance == null) {
            instance = AudioManager.getInstance(BWNApplication.applicationContext.getActivity());
        }
        if (mTts == null) {
            mTts = instance.getTts();
            if (instance.currentPlayBook != null) {
                mTts.setParameter(SpeechConstant.SPEED, instance.currentPlayBook.getSpeed());
            }
        }
        currentChapter.is_read = 1;
        currentChapter.is_preview = 0;
        ObjectBoxUtils.addData(currentChapter, BookChapter.class);
        try {
            // 添加章节名
            if (!instance.getChapterContent().getContent().startsWith(currentChapter.getChapter_title())) {
                instance.getChapterContent().setContent(currentChapter.getChapter_title() + instance.getChapterContent().getContent());
            }
            byte[] b = instance.getChapterContent().getContent().getBytes("UTF-8");
            instance.setCurrentBookChapterId(currentChapter.getChapter_id());
            if (b.length < 8000) {
                int code = mTts.startSpeaking(instance.getChapterContent().getContent(), mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    instance.setCurrentBookChapterId(0);
                    if (instance.getOnPlayerListener() != null) {
                        instance.getOnPlayerListener().onError();
                    }
                    EventBus.getDefault().post(new AudioProgressRefresh(false, false, 0, 0));
                }
            } else {
                int code = mTts.startSpeaking(instance.getChapterContent().getContent().substring(0,
                        instance.getChapterContent().getContent().length() / 4), mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    instance.setCurrentBookChapterId(0);
                    if (instance.getOnPlayerListener() != null) {
                        instance.getOnPlayerListener().onError();
                    }
                    EventBus.getDefault().post(new AudioProgressRefresh(false, false, 0, 0));
                } else {
                    // 中间部分
                    instance.getChapterContent().setContent2(instance.getChapterContent().getContent().substring(
                            instance.getChapterContent().getContent().length() / 4,
                            instance.getChapterContent().getContent().length() / 2));
                    instance.getChapterContent().setContent3(instance.getChapterContent().getContent().substring(
                            instance.getChapterContent().getContent().length() / 2,
                            instance.getChapterContent().getContent().length() * 3 / 4));
                    instance.getChapterContent().setContent4(instance.getChapterContent().getContent().substring(
                            instance.getChapterContent().getContent().length() * 3 / 4));
                }
            }
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
     * 合成回调监听。
     */
    public SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            instance.isPlayer = 1;
            instance.setShowPlayBall(true);
            instance.setPlayerError(false);
            if (instance.getOnPlayerListener() != null) {
                instance.getOnPlayerListener().onStart(100);
            }
            EventBus.getDefault().post(new AudioProgressRefresh(true, false, 0, 0));
            if (instance.currentPlayBook != null && instance.bookCurrentChapter != null) {
                isShowNotify = true;
                startForeground(CHANNEL_ID, buildNotification(true, false, true, instance.currentPlayBook.getCover(),
                        instance.currentPlayBook.getName(), instance.bookCurrentChapter.getChapter_title()));
            }
        }

        @Override
        public void onSpeakPaused() {
            instance.isPlayer = 2;
            if (instance.getOnPlayerListener() != null) {
                instance.getOnPlayerListener().onPause(false);
            }
            EventBus.getDefault().post(new AudioProgressRefresh(false, false, 0, 0));
            if (instance.currentPlayBook != null && instance.bookCurrentChapter != null && isShowNotify) {
                notificationManager.notify(CHANNEL_ID, buildNotification(false, false, false, instance.currentPlayBook.getCover(),
                        instance.currentPlayBook.getName(), instance.bookCurrentChapter.getChapter_title()));
            }
        }

        @Override
        public void onSpeakResumed() {
            instance.isPlayer = 1;
            instance.setShowPlayBall(true);
            instance.setPlayerError(false);
            if (instance.getOnPlayerListener() != null) {
                instance.getOnPlayerListener().onStart(100);
            }
            EventBus.getDefault().post(new AudioProgressRefresh(true, false, 0, 0));
            if (instance.currentPlayBook != null && instance.bookCurrentChapter != null) {
                isShowNotify = true;
                startForeground(CHANNEL_ID, buildNotification(true, false, true, instance.currentPlayBook.getCover(),
                        instance.currentPlayBook.getName(), instance.bookCurrentChapter.getChapter_title()));
            }
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            if (instance.getOnPlayerListener() != null) {
                instance.getOnPlayerListener().onProgress(100, percent);
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error != null) {
                instance.isPlayer = 2;
                instance.setPlayerError(true);
                if (mTts.isSpeaking()) {
                    mTts.pauseSpeaking();
                }
                if (instance.getOnPlayerListener() != null) {
                    instance.getOnPlayerListener().onError();
                }
                if (instance.currentPlayBook != null && instance.bookCurrentChapter != null && isShowNotify) {
                    notificationManager.notify(CHANNEL_ID, buildNotification(false, false, false, instance.currentPlayBook.getCover(),
                            instance.currentPlayBook.getName(), instance.bookCurrentChapter.getChapter_title()));
                }
                return;
            }
            if (TextUtils.isEmpty(instance.getChapterContent().getContent2()) || instance.getChapterContent().getContent2().equals("null")) {
                if (TextUtils.isEmpty(instance.getChapterContent().getContent3()) || instance.getChapterContent().getContent3().equals("null")) {
                    if (TextUtils.isEmpty(instance.getChapterContent().getContent4()) || instance.getChapterContent().getContent4().equals("null")) {
                        if (instance.getTimerTaskManager() != null) {
                            instance.getTimerTaskManager().stopToUpdateProgress();
                        }
                        instance.isPlayer = 2;
                        EventBus.getDefault().post(new AudioProgressRefresh(false, false, 0, 100));
                        if (instance.audioSpeedBean != null && instance.audioSpeedBean.audioDate == -1) {
                            instance.setPlayer(false);
                        } else {
                            if (instance.bookCurrentChapter != null) {
                                instance.getBookChapter(instance.bookCurrentChapter.getNext_chapter());
                            }
                        }
                        if (instance.getOnPlayerListener() != null) {
                            instance.getOnPlayerListener().onCompletion(null);
                        }
                        try {
                            for (int i = 0; i < container.size(); i++) {
                                writeToFile(container.get(i));
                            }
                        } catch (IOException e) {
                        }
                    } else {
                        int code = mTts.startSpeaking(instance.getChapterContent().getContent4(), mTtsListener);
                        if (code != ErrorCode.SUCCESS) {
                            setCode(code);
                        }
                        instance.getChapterContent().setContent4("");
                    }
                } else {
                    int code = mTts.startSpeaking(instance.getChapterContent().getContent3(), mTtsListener);
                    if (code != ErrorCode.SUCCESS) {
                        setCode(code);
                    }
                    instance.getChapterContent().setContent3("");
                }
            } else {
                int code = mTts.startSpeaking(instance.getChapterContent().getContent2(), mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    setCode(code);
                }
                instance.getChapterContent().setContent2("");
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            }
            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                container.add(buf);
            }
        }
    };

    /**
     * 语音播放出错
     * @param code
     */
    private void setCode(int code) {
        instance.setPlayerError(true);
        if (instance.getOnPlayerListener() != null) {
            instance.getOnPlayerListener().onError();
        }
        EventBus.getDefault().post(new AudioProgressRefresh(false, false, 0, 0));
        if (instance.currentPlayBook != null && instance.bookCurrentChapter != null && isShowNotify) {
            notificationManager.notify(CHANNEL_ID, buildNotification(false, false, false, instance.currentPlayBook.getCover(),
                    instance.currentPlayBook.getName(), instance.bookCurrentChapter.getChapter_title()));
        }
    }

    private void writeToFile(byte[] data) throws IOException {
        if (data == null || data.length == 0)
            return;
        try {
            if (memFile == null) {
                String mFilepath = Environment.getExternalStorageDirectory() + "/1.pcm";
                memFile = new MemoryFile(mFilepath, 1920000);
                memFile.allowPurging(false);
            }
            memFile.writeBytes(data, 0, (int) mTotalSize, data.length);
            mTotalSize += data.length;
        } finally {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (playerControl != null && !playerControl.getPlayerEventListeners().isEmpty()) {
            playerControl.clearPlayerEventListener();
        }
        if (mTts != null) {
            mTts = null;
        }
        isShowNotify = false;
        // 移除前台通知
        stopForeground(true);
    }

    /************************************  通知栏  ****************************************/

    private void createNotify() {
        if (BWNApplication.applicationContext.getActivity() != null && !BWNApplication.applicationContext.getActivity().isFinishing()
                && notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFY_ID,
                        LanguageUtil.getString(this, R.string.noverfragment_audio), NotificationManager.IMPORTANCE_MIN);
                //是否在桌面icon右上角展示小红
                channel.enableLights(false);
                //通知显示
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                //是否在久按桌面图标时显示此渠道的通知
                channel.setShowBadge(true);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyRefresh(AudioServiceRefresh refresh) {
        if (!refresh.isShow()) {
            // 移除前台通知
            isShowNotify = false;
            stopForeground(true);
            if (refresh.isCancel()) {
                // 关闭有声管理器
                AudioManager.getInstance(BWNApplication.applicationContext.getActivity()).onCancel(false);
            }
        }
    }

    /**
     * @param isPlayer
     * @param isSound
     * @param isReset
     * @param cover
     * @param title
     * @param name
     * @return 通知栏
     */
    private Notification buildNotification(boolean isPlayer, boolean isSound, boolean isReset, String cover, String title, String name) {
        Intent intent = new Intent();
        intent.putExtra("special_click", true);
        if (isSound) {
            intent.setClass(this, AudioSoundActivity.class);
            intent.putExtra("audio", instance.currentPlayAudio);
        } else {
            intent.setClass(this, AudioAiActivity.class);
            intent.putExtra("book", instance.currentPlayBook);
        }
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFY_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.appic)
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setCustomContentView(getRemoteViews(isPlayer, isSound, isReset, cover, title, name));
        return builder.build();
    }

    /**
     * 设置UI
     *
     *
     * @param isPlayer
     * @param isSound
     * @param isReset
     * @param cover
     * @param title
     * @param name
     * @return
     */
    private RemoteViews getRemoteViews(boolean isPlayer, boolean isSound, boolean isReset, String cover, String title, String name) {
        RemoteViews remoteViews = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_audio);
        Glide.with(Mp3Service.this).asBitmap().load(cover).into(new SimpleTarget<Bitmap>(ImageUtil.dp2px(this, 60),
                ImageUtil.dp2px(this, 80)) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                remoteViews.setImageViewBitmap(R.id.notification_image, resource);
                if (isReset&&notificationManager!=null) {
                    notificationManager.notify(CHANNEL_ID, buildNotification(isPlayer, isSound, false, cover, title, name));
                }
            }
        });
        remoteViews.setTextViewText(R.id.notification_audio_title, title);
        remoteViews.setTextViewText(R.id.notification_audio_name, name);
        remoteViews.setImageViewResource(R.id.notification_audio_play_status_image,
                isPlayer ? R.mipmap.notification_pause : R.mipmap.notification_player);
        // 添加点击事件
        Intent playIntent = new Intent(Mp3Receiver.NOTIFICATION_AUDIO_ACTION);
        playIntent.setAction(Mp3Receiver.NOTIFICATION_AUDIO_PLAY_PAUSE);
        playIntent.setPackage(getPackageName());
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_audio_play_status_layout, playPendingIntent);

        Intent nextIntent = new Intent(Mp3Receiver.NOTIFICATION_AUDIO_ACTION);
        nextIntent.setAction(Mp3Receiver.NOTIFICATION_AUDIO_NEXT);
        nextIntent.setPackage(getPackageName());
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 1, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_audio_play_next_layout, nextPendingIntent);

        Intent cancelIntent = new Intent(Mp3Receiver.NOTIFICATION_AUDIO_ACTION);
        cancelIntent.setAction(Mp3Receiver.NOTIFICATION_AUDIO_CANCEL);
        cancelIntent.setPackage(getPackageName());
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, 2, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_audio_play_cancel_layout, cancelPendingIntent);
        return remoteViews;
    }
}
