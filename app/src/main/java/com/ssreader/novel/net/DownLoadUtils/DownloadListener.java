package com.ssreader.novel.net.DownLoadUtils;

import java.io.File;

public interface DownloadListener {
    //下载进度显示
    void onProgress(long total,int progress);
    //下载状态显示,成功,失败,暂停,取消
    void onSuccess(File file );
    void onFailed();
    void onPaused();
    void onCanceled();
}
