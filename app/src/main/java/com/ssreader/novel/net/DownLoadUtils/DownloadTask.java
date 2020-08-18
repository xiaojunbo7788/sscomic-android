package com.ssreader.novel.net.DownLoadUtils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.util.Log;

import com.ssreader.novel.ui.utils.MyToash;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    public static final int TYPE_SUCCESS=0;
    public static final int TYPE_FAILED=1;
    public static final int TYPE_PAUSED=2;
    public static final int TYPE_CANCELED=3;

    //初始化对象
    private DownloadListener listener;
    private Context context;

    private boolean isCanceled=false;
    private boolean isPaused=false;
    private int lastProgress=0;
    private  long contentLength;
    File file = null;
    public DownloadTask(DownloadListener listener,Context context){
        this.listener=listener;
        this.context=context;
    }

    @Override
    protected Integer doInBackground(String ... params) {
        InputStream inputStream = null;
        RandomAccessFile savedFile = null;

        try {
            long downloadedLength = 0;//已下载长度
            String downloadUrl = params[0];
          //  String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
           // String directory= Environment.getExternalStorageDirectory().getPath();
          // Log.i("downloadUrl", params[0]+"\n "+params[1]);
            file = new File(params[1]);
            if (file.exists()) {
                downloadedLength = file.length();
            }
             contentLength = getContentLength(downloadUrl);

            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                inputStream = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = inputStream.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                //下载完成调用系统文件扫描机制,否则电脑连接显示不了文件
             //   MediaScannerConnection.scanFile(context, new String[] { file.getAbsolutePath() }, null, null);
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }


    @Override
    protected void onProgressUpdate(Integer...values){
        int progress=values[0];
        if (progress>lastProgress){
            listener.onProgress(contentLength,progress);
            lastProgress=progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status){
        switch (status){
            case TYPE_SUCCESS:
                listener.onSuccess(file);
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload(){
        isPaused=true;
        MyToash.Log("pauseDownload4","1111  "+isPaused);
    }

    public void cancelDownload(){
        isCanceled=true;
    }


    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response=client.newCall(request).execute();
        if (response!=null&&response.isSuccessful()){
            long contentLength=response.body().contentLength();
            response.body().close();
            return  contentLength;
        }
        return 0;
    }
}
