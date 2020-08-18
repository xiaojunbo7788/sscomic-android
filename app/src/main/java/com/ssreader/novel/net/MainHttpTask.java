package com.ssreader.novel.net;

import android.app.Activity;

import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.utils.ShareUitls;

/**
 * 主界面接口数据缓存
 */
public class MainHttpTask {

    private MainHttpTask() {
    }

    private static MainHttpTask mainHttpTask;

    public static MainHttpTask getInstance() {
        if (mainHttpTask == null) {
            mainHttpTask = new MainHttpTask();
        }
        return mainHttpTask;
    }

    private String ShelfBook;
    private String ShelfComic;

    private String StoreBookMan;
    private String StoreBookWoMan;

    private String StoreComicMan;
    private String StoreComicWoMan;

    private String DiscoverBook;
    private String DiscoverComic;
    private String StoreAudioMan;
    private String StoreAudioWoMan;
    private String DiscoverAudio;
    private String ShelfAudio;
    private String Mine;

    public void clean() {
        ShelfBook = null;
        ShelfComic = null;
        ShelfAudio = null;
        StoreBookMan = null;
        StoreBookWoMan = null;
        StoreAudioMan = null;
        StoreAudioWoMan = null;
        StoreComicMan = null;
        StoreComicWoMan = null;
        DiscoverAudio = null;
        DiscoverBook = null;
        DiscoverComic = null;
        Mine = null;
    }

    public void InitHttpData(Activity activity) {
        try {
            boolean isMan = ShareUitls.getInt(activity, "sex", 1) == 1;
            if (activity != null && !activity.isFinishing()) {
                for (String strType : Constant.getProductTypeList(activity)) {
                    if (strType.equals("2")) {
                        if (isMan) {
                            httpSend(activity, Api.COMIC_home_stock, "StoreComicMan", null);
                        } else {
                            httpSend(activity, Api.COMIC_home_stock, "StoreComicWoMan", null);
                        }
                        httpSend(activity, Api.COMIC_featured, "DiscoverComic", null);
                        httpSend(activity, Api.COMIC_SHELF, "ShelfComic", null);
                    } else if (strType.equals("1")) {
                        if (isMan) {
                            httpSend(activity, Api.mBookStoreUrl, "StoreBookMan", null);
                        } else {
                            httpSend(activity, Api.mBookStoreUrl, "StoreBookWoMan", null);
                        }
                        httpSend(activity, Api.mDiscoveryUrl, "DiscoverBook", null);
                        httpSend(activity, Api.mBookCollectUrl, "ShelfBook", null);
                    } else if (strType.equals("3")) {
                        if (isMan) {
                            httpSend(activity, Api.AUDIO_INDEX, "StoreAudioMan", null);
                        } else {
                            httpSend(activity, Api.AUDIO_INDEX, "StoreAudioWoMan", null);
                        }
                        httpSend(activity, Api.AUDIO_NEW_FEATURED, "DiscoverAudio", null);
                        httpSend(activity, Api.AUDIO_FAV_USER_FAV, "ShelfAudio", null);
                    }
                }
                httpSend(activity, Api.mUserCenterUrl, "Mine", null);
            }
        } catch (Exception e) {
        }
    }

    public interface GetHttpData {
        void getHttpData(String result);
    }

    public void httpSend(Activity activity, String url, String Option, GetHttpData getHttpData) {
        ReaderParams params = new ReaderParams(activity);
        if (Option.equals("StoreBookMan") || Option.equals("StoreComicMan") || Option.equals("StoreAudioMan")) {
            params.putExtraParams("channel_id", "1");
        } else if (Option.equals("StoreBookWoMan") || Option.equals("StoreComicWoMan") || Option.equals("StoreAudioWoMan")) {
            params.putExtraParams("channel_id", "2");
        }
        HttpUtils.getInstance().sendRequestRequestParams(activity,url, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            switch (Option) {
                                case "ShelfBook":
                                    ShelfBook = result;
                                    break;
                                case "ShelfComic":
                                    ShelfComic = result;
                                    break;
                                case "ShelfAudio":
                                    ShelfAudio = result;
                                    break;
                                case "StoreBookMan":
                                    StoreBookMan = result;
                                    break;
                                case "StoreBookWoMan":
                                    StoreBookWoMan = result;
                                    break;
                                case "StoreComicMan":
                                    StoreComicMan = result;
                                    break;
                                case "StoreComicWoMan":
                                    StoreComicWoMan = result;
                                    break;
                                case "StoreAudioMan":
                                    StoreAudioMan = result;
                                    break;
                                case "StoreAudioWoMan":
                                    StoreAudioWoMan = result;
                                    break;
                                case "DiscoverBook":
                                    DiscoverBook = result;
                                    break;
                                case "DiscoverComic":
                                    DiscoverComic = result;
                                    break;
                                case "DiscoverAudio":
                                    DiscoverAudio = result;
                                    break;
                                case "Mine":
                                    Mine = result;
                                    break;

                            }
                            if (getHttpData != null) {
                                getHttpData.getHttpData(result);
                            }
                            ShareUitls.putMainHttpTaskString(activity, Option, result);
                        } catch (Exception E) {
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        String s = ShareUitls.getMainHttpTaskString(activity, Option, null);
                        if (getHttpData != null && s != null) {
                            getHttpData.getHttpData(s);
                        }
                    }
                }
        );
    }

    public void getResultString(Activity activity, boolean reFarsh, String Option, GetHttpData getHttpData) {
        switch (Option) {
            case "ShelfBook":
                if (ShelfBook != null && !reFarsh) {
                    getHttpData.getHttpData(ShelfBook);
                } else {
                    httpSend(activity, Api.mBookCollectUrl, Option, getHttpData);
                }
                break;
            case "ShelfComic":
                if (ShelfComic != null && !reFarsh) {
                    getHttpData.getHttpData(ShelfComic);
                } else {
                    httpSend(activity, Api.COMIC_SHELF, Option, getHttpData);
                }
                break;
            case "StoreBookMan":
                if (StoreBookMan != null && !reFarsh) {
                    getHttpData.getHttpData(StoreBookMan);
                } else {
                    httpSend(activity, Api.mBookStoreUrl, Option, getHttpData);
                }
                break;
            case "StoreBookWoMan":
                if (StoreBookWoMan != null && !reFarsh) {
                    getHttpData.getHttpData(StoreBookWoMan);
                } else {
                    httpSend(activity, Api.mBookStoreUrl, Option, getHttpData);
                }
                break;
            case "StoreComicMan":
                if (StoreComicMan != null && !reFarsh) {
                    getHttpData.getHttpData(StoreComicMan);
                } else {
                    httpSend(activity, Api.COMIC_home_stock, Option, getHttpData);
                }
                break;
            case "StoreComicWoMan":
                if (StoreComicWoMan != null && !reFarsh) {
                    getHttpData.getHttpData(StoreComicWoMan);
                } else {
                    httpSend(activity, Api.COMIC_home_stock, Option, getHttpData);
                }
                break;
            case "DiscoverBook":
                if (DiscoverBook != null && !reFarsh) {
                    getHttpData.getHttpData(DiscoverBook);
                } else {
                    httpSend(activity, Api.mDiscoveryUrl, Option, getHttpData);
                }
                break;
            case "DiscoverComic":
                if (DiscoverComic != null && !reFarsh) {
                    getHttpData.getHttpData(DiscoverComic);
                } else {
                    httpSend(activity, Api.COMIC_featured, Option, getHttpData);
                }
                break;
            case "Mine":
                if (Mine != null && !reFarsh) {
                    getHttpData.getHttpData(Mine);
                } else {
                    httpSend(activity, Api.mUserCenterUrl, Option, getHttpData);
                }
                break;
            case "StoreAudioMan":
                if (StoreAudioMan != null && !reFarsh) {
                    getHttpData.getHttpData(StoreAudioMan);
                } else {
                    httpSend(activity, Api.AUDIO_INDEX, Option, getHttpData);
                }
                break;
            case "StoreAudioWoMan":
                if (StoreAudioWoMan != null && !reFarsh) {
                    getHttpData.getHttpData(StoreAudioWoMan);
                } else {
                    httpSend(activity, Api.AUDIO_INDEX, Option, getHttpData);
                }
                break;
            case "DiscoverAudio":
                if (DiscoverAudio != null && !reFarsh) {
                    getHttpData.getHttpData(DiscoverAudio);
                } else {
                    httpSend(activity, Api.AUDIO_NEW_FEATURED, Option, getHttpData);
                }
                break;
            case "ShelfAudio":
                if (ShelfAudio != null && !reFarsh) {
                    getHttpData.getHttpData(ShelfAudio);
                } else {
                    httpSend(activity, Api.AUDIO_FAV_USER_FAV, Option, getHttpData);
                }
                break;
        }
    }
}
