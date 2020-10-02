package com.ssreader.novel.net.UploadUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.utils.UserUtils;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.ui.dialog.WaitDialog;
import com.ssreader.novel.ui.utils.MyToash;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().
            connectTimeout(50000, TimeUnit.MILLISECONDS)
            .readTimeout(50000, TimeUnit.MILLISECONDS)
            .writeTimeout(50000, TimeUnit.MILLISECONDS).build();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    public static void postFile(int type,//0 视频  1-相册，2-头像，3-动态, 4-视频认证图
                                Activity context,
                                String url,
                                File[] files,
                                final ProgressListener listener,
                                HttpUtils.ResponseListener responseListener) {

        url = Constant.BASE_URL + url;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.i("http1", files[0].getAbsolutePath());
        //第一个参数要与Servlet中的一致
        if (type != 11 && type != 12) {
            for (File file : files) {
                String rety = file.getAbsolutePath();
                String houzhui = rety.substring(rety.lastIndexOf("."));//后缀

                MyToash.Log("houzhui", houzhui + "");
                builder.addFormDataPart("file[]", file.getName(),//"application/octet-stream"
                        RequestBody.create(MediaType.parse("image/" + houzhui), file));
            }
            builder.addFormDataPart("type", type + "");
            builder.addFormDataPart("token", UserUtils.getToken(context));
        } else {
            if (files.length != 0) {
                builder.addFormDataPart("file", files[0].getName(),
                        RequestBody.create(MediaType.parse("video/mp4"), files[0]));
            }
            builder.addFormDataPart("type", type + "");
            builder.addFormDataPart("token", UserUtils.getToken(context));
        }
        WaitDialog waitDialog = new WaitDialog(context, 0).ShowDialog(true);
        MultipartBody multipartBody = builder.build();
        Request request = new Request.Builder().url(url).post(new ProgressRequestBody(multipartBody, listener)).build();
        final String fUrl = url;
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        responseListener.onErrorResponse(e.getMessage());
                        MyToash.Log("bwhttp1", "call  yichnag");
                        waitDialog.ShowDialog(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                waitDialog.ShowDialog(false);
                String result = response.body().string();
                HttpUtils.getInstance().handleData(false,fUrl, context, result, responseListener);
            }
        });
    }

    public static Uri geturi(Context context, android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                        Log.i("urishi", uri.toString());
                    }
                }
            }
        }
        return uri;
    }

    public static String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
