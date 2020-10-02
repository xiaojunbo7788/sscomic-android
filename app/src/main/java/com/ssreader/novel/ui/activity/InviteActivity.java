package com.ssreader.novel.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.InviteBindBean;
import com.ssreader.novel.model.PurchaseDialogBean;
import com.ssreader.novel.model.ShareBean;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.adapter.InviteAdapter;
import com.ssreader.novel.ui.dialog.SetCodeDialog;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InviteActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_title)
    TextView public_sns_topbar_title;
    @BindView(R.id.invite_top_bg_btn)
    RelativeLayout topBgBtn;
    @BindView(R.id.invite_name)
    TextView invite_name;
    @BindView(R.id.invite_count)
    TextView invite_count;
    @BindView(R.id.invite_code)
    TextView invite_code;
    @BindView(R.id.invite_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.invite_btn)
    RoundedImageView invite_btn;
    private InviteAdapter inviteAdapter;

    private ShareBean shareBean;

    private List<ShareBean.InviteUserItem>userItemList = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_invite;
    }

    @Override
    public void initView() {
        public_sns_topbar_title.setText("邀请好友");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inviteAdapter = new InviteAdapter(R.layout.item_invite,userItemList);
        mRecyclerView.setAdapter(inviteAdapter);
        topBgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (shareBean.bind_user != null && shareBean.bind_user.length() > 0) {
//                    return;
//                }
                String name = "输入邀请人邀请码";
                SetCodeDialog setCodeDialog = new SetCodeDialog();
                setCodeDialog.showSetCodeDialog(activity, name, "");
                setCodeDialog.setOnVerificationSuccess(new SetCodeDialog.onVerificationSuccess() {
                    @Override
                    public void success(String editText) {
                        if (TextUtils.isEmpty(editText)) {
                            MyToash.ToashError(activity, "邀请码不能为空");
                            return;
                        }
                        bindCode(editText);

                    }
                });
            }
        });
        invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用本地分享
                if (shareBean != null) {
                    setShare(activity, shareBean.link, shareBean.title, shareBean.desc);
                }
            }
        });
    }

    /**
     * 系统分享
     *
     * @param activity
     * @param uri
     * @param title
     * @param desc
     */
    public static void setShare(Activity activity, String uri, String title, String desc) {
        if (TextUtils.isEmpty(title) || desc.equals("null")) {
            title = "";
        }
        if (TextUtils.isEmpty(desc) || desc.equals("null")) {
            desc = "";
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // 内容
        if (TextUtils.isEmpty(title)) {
            intent.putExtra(Intent.EXTRA_TEXT, desc + uri);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, "【" + title + "】" + desc + uri);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, title));
    }


    @Override
    public void initData() {
        shareAPP();
    }


    public void shareAPP() {
        final ReaderParams params = new ReaderParams(activity);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.APP_SHARE, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            shareBean = HttpUtils.getGson().fromJson(response, ShareBean.class);
                            if (!TextUtils.isEmpty(shareBean.link) && (shareBean.link.startsWith("www") ||
                                    shareBean.link.startsWith("http"))) {
                                if (shareBean.bind_user != null && shareBean.bind_user.length() > 0) {
                                    invite_name.setText(shareBean.bind_user);
                                } else {
                                    invite_name.setText("");
                                }
                                if (shareBean.inviteInfo!= null && shareBean.inviteInfo.count!=null) {
                                    invite_count.setText(shareBean.inviteInfo.count);
                                } else {
                                    invite_count.setText("0");
                                }
                                invite_code.setText(shareBean.invite_code);
                                if (shareBean.inviteInfo!= null && shareBean.inviteInfo.userList!=null) {
                                    inviteAdapter.addData(shareBean.inviteInfo.userList);
                                }
                            } else {
                                MyToash.ToashError(activity,"数据有误");
                            }
                        } catch (Exception e) {
                            MyToash.ToashError(activity,"数据有误");
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        MyToash.ToashError(activity,"网络失败");
                    }
                }
        );
    }

    public void bindCode(String code) {
        final ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("invite_code",code);
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.APP_BIND_CODE, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            InviteBindBean bindBean = HttpUtils.getGson().fromJson(response, InviteBindBean.class);
                            if (bindBean.code.equals("0")) {
                                MyToash.ToashSuccess(activity,"绑定成功");
                                if (bindBean.data != null && bindBean.data.bind_user != null) {
                                    shareBean.bind_user = bindBean.data.bind_user;
                                    invite_name.setText(bindBean.data.bind_user);
                                }
                            } else {
                                MyToash.ToashError(activity,bindBean.msg);
                            }
                        } catch (Exception e) {
                            MyToash.ToashError(activity,"数据有误");
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        MyToash.ToashError(activity,"网络失败");
                    }
                }
        );
    }


    @Override
    public void initInfo(String json) {

    }
}
