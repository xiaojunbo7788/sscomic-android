package com.ssreader.novel.net.UploadUtils;

public interface ProgressListener {

    void onProgress(long currentBytes, long contentLength, boolean done);
}
