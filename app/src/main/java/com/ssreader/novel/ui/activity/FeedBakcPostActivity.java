package com.ssreader.novel.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.FeedBackPhotoBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.FeedBackPhotoAdapter;
import com.ssreader.novel.ui.dialog.PublicDialog;
import com.ssreader.novel.ui.utils.MyOpenCameraAlbum;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.GridViewForScrollView;
import com.ssreader.novel.utils.LanguageUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedBakcPostActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_right_tv)
    TextView publicSnsTopbarRightTv;
    @BindView(R.id.public_sns_topbar_right_other)
    RelativeLayout publicSnsTopbarRightOther;
    @BindView(R.id.fragment_comicinfo_mulu_xu)
    TextView fragmentComicinfoMuluXu;
    @BindView(R.id.fragment_comicinfo_mulu_xu_img)
    ImageView fragmentComicinfoMuluXuImg;
    @BindView(R.id.fragment_comicinfo_mulu_layout)
    RelativeLayout fragmentComicinfoMuluLayout;
    @BindView(R.id.public_sns_topbar_right)
    LinearLayout publicSnsTopbarRight;
    @BindView(R.id.public_sns_topbar_back_img)
    ImageView publicSnsTopbarBackImg;
    @BindView(R.id.public_sns_topbar_back)
    RelativeLayout publicSnsTopbarBack;
    @BindView(R.id.public_sns_topbar_title)
    TextView publicSnsTopbarTitle;
    @BindView(R.id.public_sns_topbar_layout)
    RelativeLayout publicSnsTopbarLayout;
    @BindView(R.id.activity_feedback_content)
    EditText activityFeedbackContent;
    @BindView(R.id.activity_feedback_photo)
    GridViewForScrollView activityFeedbackPhoto;
    @BindView(R.id.activity_edit_photoNumber)
    EditText activityEditPhotoNumber;
    @BindView(R.id.activitySubmit)
    TextView activitySubmit;
    @BindView(R.id.activity_feed_back_submit)
    RelativeLayout activityFeedBackSubmit;
    @BindView(R.id.activity_feedback_percentage)
    TextView activity_feedback_percentage;

    public static boolean isCommentSuccess;

    private List<FeedBackPhotoBean> list;
    private FeedBackPhotoAdapter feedBackPhotoAdapter;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.MineNewFragment_fankui;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_new_feed_bakc;
    }

    @OnClick({R.id.activity_feed_back_submit,R.id.public_sns_topbar_back,R.id.activity_feedback_content,R.id.activity_edit_photoNumber})
    public void onClick(View view) {
        String content = activityFeedbackContent.getText().toString();
        String photoNumber = activityEditPhotoNumber.getText().toString();
        switch (view.getId()) {
            case R.id.activity_feed_back_submit:
                if (!content.isEmpty() && !photoNumber.isEmpty()) {
                    readerParams = new ReaderParams(activity);
                    //信息
                    readerParams.putExtraParams("content", content);
                    //手机号
                    readerParams.putExtraParams("contact", photoNumber);
                    //相册照片
                    String imaPath = "";
                    if (list != null && !list.isEmpty()) {
                        for (FeedBackPhotoBean feedBackPhotoBean : list) {
                            imaPath += "||" + feedBackPhotoBean.img;
                        }
                        readerParams.putExtraParams("imgs", imaPath.substring(2));
                    }
                    HttpUtils.getInstance().sendRequestRequestParams(activity,Api.PostFaceBcakContent, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            isCommentSuccess = true;
                            finish();
                        }

                        @Override
                        public void onErrorResponse(String ex) {

                        }
                    });
                } else {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, content.isEmpty()?R.string.activityNumberContent2:R.string.activityNumberContent1));
                }
                break;
            case R.id.activity_feedback_content:
                showSoftInputFromWindow(activity, activityFeedbackContent);
                break;
            case R.id.activity_edit_photoNumber:
                showSoftInputFromWindow(activity, activityEditPhotoNumber);
                break;

            case R.id.public_sns_topbar_back:
                if (!list.isEmpty()|| !TextUtils.isEmpty(content)|| !TextUtils.isEmpty(photoNumber)) {
                    PublicDialog.publicDialogVoid(activity, "",
                            LanguageUtil.getString(activity, R.string.activityAwayFeedBack),
                            LanguageUtil.getString(activity, R.string.app_cancle),
                            LanguageUtil.getString(activity, R.string.app_confirm), new PublicDialog.OnPublicListener() {
                                @Override
                                public void onClickConfirm(boolean isConfirm) {
                                    if(isConfirm) {
                                        finish();
                                    }
                                }
                            });
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void initView() {
        list = new ArrayList<>();
        activityFeedbackContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String percentage = "%s/200";
                int lastWordsNum = s.length();
                activity_feedback_percentage.setText(String.format(percentage, lastWordsNum + ""));
            }
        });
        activityFeedbackPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3 || position == list.size()) {
                    MyOpenCameraAlbum.openPhotoAlbum(activity, MyOpenCameraAlbum.FeedBackCAMERA);
                } else {
                    Intent intent = new Intent(activity, LookBigImageActivity.class);
                    intent.putExtra("click_position", position);
                    intent.putExtra("lookbigimgcontent", "");
                    List<String> stringList=new ArrayList<>();
                    for(FeedBackPhotoBean feedBackPhotoBean1:list){
                        stringList.add(feedBackPhotoBean1.show_img);
                    }
                    intent.putExtra("snsShowPictures", (Serializable) stringList);
                    startActivity(intent);
                }

            }
        });
        feedBackPhotoAdapter = new FeedBackPhotoAdapter(activity, list);
        activityFeedbackPhoto.setAdapter(feedBackPhotoAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyOpenCameraAlbum.FeedBackCAMERA) {
            MyOpenCameraAlbum.resultCramera(activity, requestCode, resultCode, data, null, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshMineListItem(FeedBackPhotoBean refreshMineListItem) {
        if (refreshMineListItem != null) {
            if (list.size() <= 2) {
                list.add(refreshMineListItem);
            } else if (list.size() == 2) {
                feedBackPhotoAdapter.imageView.setVisibility(View.GONE);
                feedBackPhotoAdapter.imageViewadd.setVisibility(View.GONE);
            } else {
                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.activitymaxThree));
            }
            feedBackPhotoAdapter.notifyDataSetChanged();
        }
    }

    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String content = activityFeedbackContent.getText().toString();
            String photoNumber = activityEditPhotoNumber.getText().toString();
            if (!list.isEmpty()|| !TextUtils.isEmpty(content)|| !TextUtils.isEmpty(photoNumber)) {
                PublicDialog.publicDialogVoid(activity, "",
                        LanguageUtil.getString(activity, R.string.activityAwayFeedBack),
                        LanguageUtil.getString(activity, R.string.app_cancle),
                        LanguageUtil.getString(activity, R.string.GivpXiugai), new PublicDialog.OnPublicListener() {
                            @Override
                            public void onClickConfirm(boolean isConfirm) {
                                if(isConfirm) {
                                    finish();
                                }
                            }
                        });
            } else {
                finish();
            }
        }
        return true;
    }
}
