package com.ssreader.novel.ui.audio.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.eventbus.AudioProgressRefresh;
import com.ssreader.novel.eventbus.AudioServiceRefresh;
import com.ssreader.novel.ui.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 用于控制音乐通知栏
 */
public class Mp3Receiver extends BroadcastReceiver {

    // 统一名称
    public static final String NOTIFICATION_AUDIO_ACTION = "audio.player.receiver";
    // 开始、暂停
    public static final String NOTIFICATION_AUDIO_PLAY_PAUSE = "AUDIO_PLAY_PAUSE";
    // 下一首
    public static final String NOTIFICATION_AUDIO_NEXT = "AUDIO_NEXT";
    // 取消
    public static final String NOTIFICATION_AUDIO_CANCEL = "AUDIO_CANCEL";

    private AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        if (audioManager == null) {
            audioManager = AudioManager.getInstance(BWNApplication.applicationContext.getActivity());
        }
        switch (intent.getAction()) {
            case NOTIFICATION_AUDIO_PLAY_PAUSE:
                if (!audioManager.isIsPlayerError() && !audioManager.isPreview()) {
                    // 只有播放不失败时才能点击
                    if (AudioManager.isSound) {
                        if (audioManager.currentPlayAudio != null && audioManager.audioCurrentChapter != null && audioManager.audioChapterList != null) {
                            audioManager.setAudioPlayerStatus((audioManager.isPlayer != 1), audioManager.currentPlayAudio,
                                    audioManager.audioCurrentChapter.getChapter_id(), null);
                        }
                    } else {
                        if (audioManager.currentPlayBook != null && audioManager.bookCurrentChapter != null && audioManager.bookChapterList != null) {
                            audioManager.setBookPlayerStatus((audioManager.isPlayer != 1), audioManager.currentPlayBook,
                                    audioManager.bookCurrentChapter.getChapter_id(), null);
                        }
                    }
                }
                break;
            case NOTIFICATION_AUDIO_NEXT:
                if (AudioManager.isSound) {
                    if (audioManager.currentPlayAudio != null && audioManager.audioCurrentChapter != null &&
                            audioManager.audioChapterList != null && audioManager.audioCurrentChapter.getNext_chapter() != 0) {
                        audioManager.openAudioChapterId(audioManager.currentPlayAudio,
                                audioManager.audioCurrentChapter.getNext_chapter(), null);
                    }
                } else {
                    if (audioManager.currentPlayBook != null && audioManager.bookCurrentChapter != null &&
                            audioManager.bookChapterList != null && audioManager.bookCurrentChapter.getNext_chapter() != 0) {
                        audioManager.openBookChapterId(audioManager.currentPlayBook,
                                audioManager.bookCurrentChapter.getNext_chapter(), null);
                    }
                }
                break;
            case NOTIFICATION_AUDIO_CANCEL:
                // 关闭通知栏
                audioManager.setPlayer(false);
                EventBus.getDefault().post(new AudioProgressRefresh(false, false, 0, 0));
                EventBus.getDefault().post(new AudioServiceRefresh(false, false));
                break;
        }
    }
}
