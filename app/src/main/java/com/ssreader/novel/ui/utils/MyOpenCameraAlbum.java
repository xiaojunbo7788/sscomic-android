package com.ssreader.novel.ui.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshMineListItem;
import com.ssreader.novel.model.FeedBackPhotoBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.dialog.WaitDialog;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class MyOpenCameraAlbum {

    public static final int GALLERY = 1077;
    public static final int CAMERA = 1078;
    public static final int REQUEST_CROP = 1079;
    public static final int FeedBackCAMERA = 1080;
    public static final int LocalCAMERA = 1081;
    public static final int LocalGALLERY = 1082;
    public static File cameraSavePath;//拍照照片路径
    public static Uri uri;//照片uri
    public static Uri mCutUri;

    /**
     * 相机
     * @param activity
     * @param request
     */
    public static void openCamera(Activity activity, int request) {
        cameraSavePath = new File(getPath() + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, "com.ssreader.novel.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, request);
    }

    /**
     * 相册
     * @param activity
     * @param request
     */
    public static void openPhotoAlbum(Activity activity, int request) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        activity.startActivityForResult(intent, request);
    }

    private static String getPath() {
        String path = FileManager.getSDCardRoot() + "/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    /**
     * 图片裁剪
     * @param activity
     * @param uri
     * @param fromCapture
     */
    private static void cropPhoto(Activity activity, Uri uri, boolean fromCapture) {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        // 注意一定要添加该项权限，否则会提示无法裁剪
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);
        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
            mCutUri = Uri.fromFile(cameraSavePath);
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
            String fileName = "photo_" + time;
            File mCutFile = new File(Environment.getExternalStorageDirectory() + "/take_photo", fileName + ".jpeg");
            if (!mCutFile.getParentFile().exists()) {
                mCutFile.getParentFile().mkdirs();
            }
            mCutUri = Uri.fromFile(mCutFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        activity.sendBroadcast(intentBc);
        activity.startActivityForResult(intent, REQUEST_CROP); //设置裁剪参数显示图片至ImageVie
    }

    public static void resultCramera(Activity activity, int requestCode, int resultCode, Intent data, ImageView user_info_avatar, boolean isUploadImg) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA) {
                    cropPhoto(activity, uri, true);
                } else if (requestCode == GALLERY) {
                    Uri uri = data.getData();
                    cropPhoto(activity, uri, false);
                } else if (requestCode == REQUEST_CROP) {
                    ShareUitls.putString(activity, "LocalPhoto", mCutUri.getPath());
                    EventBus.getDefault().post(new RefreshMineListItem(mCutUri.getPath()));
                    Glide.with(activity).load(mCutUri.getPath()).into(user_info_avatar);
                    if(isUploadImg) {
                        uploadImg(activity, uriToFile(mCutUri, activity), 1);
                    }
                } else if (requestCode == FeedBackCAMERA) {
                    File file = uriToFile(data.getData(), activity);
                    if(isUploadImg) {
                        uploadImg(activity, file, 2);
                    }
                } else if (requestCode == LocalCAMERA) {
                    cropPhoto(activity, uri, true);
                } else if (requestCode == LocalGALLERY) {
                    Uri uri = data.getData();
                    cropPhoto(activity, uri, false);
                }
            }
        } catch (Exception e) {
            MyToash.Log("requestCode", e.getMessage());
        }
    }

    public static void uploadImg(Activity activity, File file, int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageUtil.Getluban(activity, file.getAbsolutePath(), new ImageUtil.LubanSunccess() {
                    @Override
                    public void getluban(File file) {
                        final String info = "data:image/jpeg;base64," + ImageUtil.imageToBase64(file);
                        if (!info.isEmpty()) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final WaitDialog waitDialog = new WaitDialog(activity, 1);
                                    waitDialog.showDailog();
                                    switch (type) {
                                        case 1:
                                            ReaderParams params = new ReaderParams(activity);
                                            params.putExtraParams("avatar", info);
                                            String json = params.generateParamsJson();
                                            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.mUserSetAvatarUrl, json, new HttpUtils.ResponseListener() {
                                                        @Override
                                                        public void onResponse(final String result) {
                                                            //  initData(true);
                                                            EventBus.getDefault().post(new RefreshMine());
                                                            waitDialog.dismissDialog();
                                                        }

                                                        @Override
                                                        public void onErrorResponse(String ex) {
                                                            waitDialog.dismissDialog();
                                                        }
                                                    }

                                            );
                                            break;
                                        case 2:
                                            ReaderParams params1 = new ReaderParams(activity);
                                            params1.putExtraParams("image", info);
                                            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.UPLoadImage, params1.generateParamsJson(), new HttpUtils.ResponseListener() {
                                                @Override
                                                public void onResponse(String response) {
                                                    MyToash.Log("requestCode666666666", response);
                                                    FeedBackPhotoBean feedBackPhotoBean = new Gson().fromJson(response, FeedBackPhotoBean.class);
                                                    EventBus.getDefault().post(feedBackPhotoBean);
                                                    waitDialog.dismissDialog();
                                                }

                                                @Override
                                                public void onErrorResponse(String ex) {
                                                    waitDialog.dismissDialog();
                                                    MyToash.Log("requestCode666666666", ex);
                                                }
                                            });

                                            break;
                                    }
                                }
                            });
                        }

                    }
                });
            }
        }).start();
    }

    public static File uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
        }
        return null;
    }
}
