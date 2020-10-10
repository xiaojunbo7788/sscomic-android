package com.ssreader.novel.ui.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.DownComicEvenbus;
import com.ssreader.novel.eventbus.DownOver;
import com.ssreader.novel.eventbus.DownScrollCancleEdit;
import com.ssreader.novel.model.BaseComicImage;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.model.ComicChapterItem;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import static com.ssreader.novel.utils.FileManager.getBytesByFile;
import static com.ssreader.novel.utils.FileManager.getLocalComicImageFile;

/**
 * 用于漫画下载
 */
public class DownComicService extends IntentService {

    private Comic comic;
    private boolean cancleTask;
    private boolean isOne;

    public DownComicService() {
        super("sss");
    }

    public DownComicService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProcess(DownScrollCancleEdit downScrollCancleEdit) {
        if (downScrollCancleEdit.productType == Constant.COMIC_CONSTANT) {
            if (comic != null) {
                if (comic.comic_id == downScrollCancleEdit.id) {
                    cancleTask = true;
                }
            }
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        isOne = intent.getBooleanExtra("isOne", false);
        // 创建eventBus
        DownComicEvenbus downComicEvenbus = new DownComicEvenbus();
        long comic_id = intent.getLongExtra("comic_id", 0);
        String fileName = intent.getStringExtra("result");
        // 获取本地json文件
        String result = FileManager.readFromXML(fileName, true);
        if (TextUtils.isEmpty(result)) {
            return;
        }
        comic = ObjectBoxUtils.getComic(comic_id);
        downComicEvenbus.comic = comic;
        try {
            if (!isOne) {
                Down(downComicEvenbus, result);
            } else {
                DownOne(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载章节
     *
     * @param downComicEvenbus
     * @param result
     * @param downChapterCounts
     * @throws JSONException
     */
    boolean isSuccess = true;

    private void Down(DownComicEvenbus downComicEvenbus, String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            String msg = jsonArray.getString(i);
            ComicChapterItem comicChapterItem = HttpUtils.getGson().fromJson(msg, ComicChapterItem.class);
            long chapterId = comicChapterItem.chapter_id;
            ComicChapter comicChapter = ObjectBoxUtils.getComicChapter(chapterId);
            if (comicChapter != null) {
                // 循环下载
                for (BaseComicImage baseComicImage : comicChapterItem.image_list) {
                    if (cancleTask) {
                        return;
                    }
                    try {
                        baseComicImage.comic_id = downComicEvenbus.comic.comic_id;
                        baseComicImage.chapter_id = chapterId;
                        // 获取下载地址
                        File localPathFile = getLocalComicImageFile(baseComicImage);
                        if (localPathFile == null) {
                            // 结束该章节的循环
                            isSuccess = false;
                            setComicChapter(comicChapter, msg, 3, false, downComicEvenbus);
                            break;
                        }
                        if (!localPathFile.exists()) {
                            localPathFile.getParentFile().mkdirs();
                            File file = Glide.with(DownComicService.this)
                                    .load(baseComicImage.getImage())
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                            FileManager.createFile(localPathFile, getBytesByFile(file));

                        }
                        isSuccess = true;
                    } catch (Exception e) {
                        // 该章节下载失败
                        isSuccess = false;
                        setComicChapter(comicChapter, msg, 3, false, downComicEvenbus);
                        break;
                    }
                }
            } else {
                isSuccess = false;
                setComicChapter(comicChapter, msg, 3, false, downComicEvenbus);
            }
            if (isSuccess) {
                setComicChapter(comicChapter, msg, 1, false, downComicEvenbus);
                if (comic.current_chapter_id == 0) {
                    comic.current_chapter_id = chapterId;
                }
                comic.down_chapters++;
                ObjectBoxUtils.addData(comic, Comic.class);
                EventBus.getDefault().post(comic);
            }
        }
        // 整个章节已经遍历完成
     /*   DownComicEvenbus comicEvenbus = new DownComicEvenbus();
        comicEvenbus.comic = comic;
        comicEvenbus.flag = true;
        EventBus.getDefault().post(comicEvenbus);*/

       // MyToash.Log("applicationContext", (BWNApplication.applicationContext.getActivity()).getClass().getName()+"");
       // MyToash.ToashSuccess(BWNApplication.applicationContext.getActivity(), LanguageUtil.getString(BWNApplication.applicationContext.getActivity(), R.string.BookInfoActivity_down_downcomplete));
        EventBus.getDefault().post(new DownOver(1));
    }

    private void DownOne(String result) {
        ComicChapterItem comicChapterItem = HttpUtils.getGson().fromJson(result, ComicChapterItem.class);
        long chapterId = comicChapterItem.chapter_id;
        ComicChapter comicChapter = ObjectBoxUtils.getComicChapter(chapterId);
        if (comicChapter != null) {
            comicChapter.ImagesText = result;
            ObjectBoxUtils.addData(comicChapter, ComicChapter.class);
            // 循环下载
            for (BaseComicImage baseComicImage : comicChapterItem.image_list) {
                try {
                    baseComicImage.comic_id = comic.comic_id;
                    baseComicImage.chapter_id = chapterId;
                    // 获取下载地址
                    File localPathFile = getLocalComicImageFile(baseComicImage);
                    if (localPathFile != null) {
                        if (!localPathFile.exists()) {
                            localPathFile.getParentFile().mkdirs();
                            File file = Glide.with(DownComicService.this)
                                    .load(baseComicImage.getImage())
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                            FileManager.createFile(localPathFile, getBytesByFile(file));
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 设置状态，并传值
     *
     * @param comicChapter
     * @param msg
     * @param status
     * @param flag
     * @param downComicEvenbus
     */
    private void setComicChapter(ComicChapter comicChapter, String msg, int status, boolean flag, DownComicEvenbus downComicEvenbus) {
        downComicEvenbus.flag = flag;
        comicChapter.ImagesText = msg;
        comicChapter.downStatus = status;
        downComicEvenbus.comicChapter = comicChapter;
        ObjectBoxUtils.addData(comicChapter, ComicChapter.class);
        EventBus.getDefault().post(downComicEvenbus);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
