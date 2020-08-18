package com.ssreader.novel.ui.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.reflect.TypeToken;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.AudioAddDown;
import com.ssreader.novel.eventbus.DownScrollCancleEdit;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.ChapterContent;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.ssreader.novel.utils.FileManager.getBytesByFile;

public class DownAudioService extends IntentService {

    public DownAudioService() {
        super("sss");
    }

    public DownAudioService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    private boolean cancleTask, isFromDownActivity;
    private int current_process, total_Size;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProcess(DownScrollCancleEdit downScrollCancleEdit) {
        if (downScrollCancleEdit.productType == Constant.AUDIO_CONSTANT) {
            if (audio != null) {
                if (audio.audio_id == downScrollCancleEdit.id) {
                    cancleTask = true;
                }
            }
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Down(intent);
    }

    private Audio audio;

    private void Down(Intent intent) {
        isFromDownActivity = intent.getBooleanExtra("isFromDownActivity", false);
        String fileName = intent.getStringExtra("result");
        String result = FileManager.readFromXML(fileName, true);
        long audio_id = intent.getLongExtra("audio_id", 0);
        if (TextUtils.isEmpty(result)) {
            return;
        }
        audio = ObjectBoxUtils.getAudio(audio_id);
        EventBus.getDefault().post(new AudioAddDown(isFromDownActivity, 0));
        current_process = 0;
        List<ChapterContent> audioChapters = HttpUtils.getGson().fromJson(result, new TypeToken<List<ChapterContent>>() {}.getType());
        total_Size = audioChapters.size();
        for (ChapterContent chapterContent : audioChapters) {
            if (cancleTask) {
                return;
            }
            try {
                String localpath = FileManager.getAudioLocal(chapterContent);
                int start = chapterContent.getContent().lastIndexOf(".");
                if (start != -1) {
                    String houzhui = chapterContent.getContent().substring(start);//后缀
                    if (houzhui.contains("?")) {
                        houzhui = houzhui.substring(0, houzhui.indexOf("?"));
                    }
                    localpath = localpath + houzhui;
                    File localPathFile = new File(localpath);
                    AudioChapter audioChapter = ObjectBoxUtils.getAudioChapter(chapterContent.getChapter_id());
                    if (!localPathFile.exists()) {
                        localPathFile.getParentFile().mkdirs();
                        String finalLocalpath = localpath;
                        Glide.with(DownAudioService.this).downloadOnly().load(chapterContent.getContent()).into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                FileManager.createFile(localPathFile, getBytesByFile(resource));
                                downChapterOver(audioChapter, finalLocalpath, chapterContent);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                downError(localPathFile, chapterContent);
                            }
                        });
                    } else {
                        downChapterOver(audioChapter, localpath, chapterContent);
                    }
                } else {
                    downError(null, chapterContent);
                }
            } catch (Exception e) {
                downError(null, chapterContent);
            } finally {

            }
        }
    }

    private void downError(File localPathFile, ChapterContent chapterContent) {
        if (localPathFile != null && localPathFile.exists()) {
            localPathFile.delete();
        }
        EventBus.getDefault().post(new AudioAddDown(2, chapterContent.getChapter_id(), null));
        ++current_process;
        if (current_process == total_Size) {
            EventBus.getDefault().post(new AudioAddDown(isFromDownActivity, 1));
        }
    }

    private void downChapterOver(AudioChapter audioChapter, String finalLocalpath, ChapterContent chapterContent) {
        if (audioChapter != null) {
            audioChapter.path = finalLocalpath;
            audioChapter.status = -1;
            ObjectBoxUtils.addData(audioChapter, AudioChapter.class);
        }
        ++audio.down_chapters;
        EventBus.getDefault().post(new AudioAddDown(-1, chapterContent.getChapter_id(), finalLocalpath));
        if (audio.current_chapter_id == 0) {
            audio.current_chapter_id = chapterContent.getChapter_id();
        }
        ObjectBoxUtils.addData(audio, Audio.class);//更新下载的章节数
        EventBus.getDefault().post(audio);
        ++current_process;
        if (current_process == total_Size) {
            EventBus.getDefault().post(new AudioAddDown(isFromDownActivity, 1));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
