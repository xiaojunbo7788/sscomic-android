package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshMineListItem;
import com.ssreader.novel.eventbus.RefreshUserInfo;
import com.ssreader.novel.eventbus.WeChatLoginRefresh;
import com.ssreader.novel.model.MineModel;
import com.ssreader.novel.model.UserInfoItem;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.UserInfoAdapter;
import com.ssreader.novel.ui.dialog.BottomMenuDialog;
import com.ssreader.novel.ui.dialog.SetCodeDialog;
import com.ssreader.novel.ui.utils.LoginUtils;
import com.ssreader.novel.ui.utils.MyOpenCameraAlbum;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.SystemUtil;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.USE_QQ;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.user_info_recyclerView)
    SCRecyclerView recyclerView;

    private List<MineModel> mineModels;
    private List<List<MineModel>> panelList;
    private UserInfoAdapter userInfoAdapter;

    private String mEdit;
    private ImageView imageView;
    private IWXAPI iwxapi;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.UserInfoActivity_title;
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        mineModels = new ArrayList<>();
        panelList = new ArrayList<>();
        initSCRecyclerView(recyclerView, RecyclerView.VERTICAL, 0);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);
        userInfoAdapter = new UserInfoAdapter(activity, mineModels);
        recyclerView.setAdapter(userInfoAdapter);
        initListener();
    }

    private void initListener() {
        userInfoAdapter.setAvatarCallBack(new UserInfoAdapter.avatarCallBack() {
            @Override
            public void setAvatar(ImageView avatar) {
                imageView = avatar;
            }

            @Override
            public void onClickItem(MineModel model) {
                switch (model.getAction()) {
                    case "avatar":
                        //相册相机
                        new BottomMenuDialog().showBottomMenuDialog(activity, new String[]{
                                LanguageUtil.getString(activity, R.string.MineUserInfo_PaiZhao),
                                LanguageUtil.getString(activity, R.string.MineUserInfoXiangCe)
                        }, new SCOnItemClickListener() {
                            @Override
                            public void OnItemClickListener(int flag, int position, Object O) {
                                if (position == 0) {
                                    MyOpenCameraAlbum.openCamera(activity, MyOpenCameraAlbum.CAMERA);
                                } else if (position == 1) {
                                    MyOpenCameraAlbum.openPhotoAlbum(activity, MyOpenCameraAlbum.GALLERY);
                                }
                            }

                            @Override
                            public void OnItemLongClickListener(int flag, int position, Object O) {

                            }
                        });
                        break;
                    case "nickname":
                        String name = "";
                        if (!TextUtils.isEmpty(model.getDesc())) {
                            name = model.getDesc();
                        } else {
                            name = LanguageUtil.getString(activity, R.string.UserInfoActivity_edit_name);
                        }
                        SetCodeDialog setCodeDialog = new SetCodeDialog();
                        setCodeDialog.showSetCodeDialog(activity, LanguageUtil.getString(activity, R.string.UserInfoActivity_update_name), name);
                        setCodeDialog.setOnVerificationSuccess(new SetCodeDialog.onVerificationSuccess() {
                            @Override
                            public void success(String editText) {
                                if (TextUtils.isEmpty(editText)) {
                                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.UserInfoActivity_namenonull));
                                    return;
                                }
                                if (!TextUtils.isEmpty(model.getDesc()) && editText.equals(model.getDesc())) {
                                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.UserInfoActivity_name_again));
                                    return;
                                }
                                mEdit = editText;
                                modifyNickname(0);
                            }
                        });
                        break;
                    case "gender":
                        //性别
                        new BottomMenuDialog().showBottomMenuDialog(activity, new String[]{
                                LanguageUtil.getString(activity, R.string.UserInfoActivity_boy),
                                LanguageUtil.getString(activity, R.string.UserInfoActivity_gril)}, new SCOnItemClickListener() {
                            @Override
                            public void OnItemClickListener(int flag, int position, Object O) {
                                switch (position) {
                                    case 0:
                                        modifyNickname(2);
                                        break;
                                    case 1:
                                        modifyNickname(1);
                                        break;
                                }
                            }

                            @Override
                            public void OnItemLongClickListener(int flag, int position, Object O) {

                            }
                        });
                        break;
                    case "cancel":
                        startActivity(new Intent(activity, UserOutActivity.class));
                        break;
                    case "mobile":
                        // 绑定手机号
                        bindPhone();
                        break;
                    case "wechat":
                        if (SystemUtil.checkAppInstalled(activity, SystemUtil.WEChTE_PACKAGE_NAME)) {
                            ShareUitls.putBoolean(activity, "isBindWeiXin", true);
                            UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.WEIXIN, authListener);
                        } else {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.Mine_no_install_wechat));
                        }
                        break;
                    case "qq":
                        if ((SystemUtil.checkAppInstalled(activity, SystemUtil.QQ_PACKAGE_NAME) && USE_QQ) ||
                                (SystemUtil.checkAppInstalled(activity, SystemUtil.TIM_PACKAGE_NAME) && USE_QQ)) {
                            new LoginUtils(activity).qqLogin(false, false);
                        } else {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.Mine_no_install_qq));
                        }
                        break;
                }
            }
        });
    }

    public void bindPhone() {
        startActivityForResult(new Intent(this, BindPhoneActivity.class), 111);
    }

    @Override
    public void initData() {
        readerParams = new ReaderParams(activity);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.USER_DATA, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        if (json != null && !TextUtils.isEmpty(json)) {
            UserInfoItem userInfoItem = gson.fromJson(json, UserInfoItem.class);
            if (userInfoItem.getPanel_list() != null && !userInfoItem.getPanel_list().isEmpty()) {
                mineModels.clear();
                panelList = userInfoItem.getPanel_list();
                for (List<MineModel> models : panelList) {
                    int size = models.size();
                    for (int i = 0; i < size; i++) {
                        MineModel mineModel = models.get(i);
                        mineModel.setBottomLine(i + 1 == size);
                        mineModels.add(mineModel);
                    }
                }
            }
            userInfoAdapter.notifyDataSetChanged();
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            MyToash.Log("SHARE_MEDIA1   " + platform.toString());
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (iwxapi == null) {
                iwxapi = WXAPIFactory.createWXAPI(activity, Constant.WEIXIN_PAY_APPID, true);
            }

            if (!iwxapi.isWXAppInstalled()) {
                return;
            }
            iwxapi.registerApp(Constant.WEIXIN_PAY_APPID);
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_xb_live_state";
            iwxapi.sendReq(req);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            MyToash.Log("SHARE_MEDIA 2   " + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };

    public void modifyNickname(final int flag) {
        String requestParams;
        if (flag == 0) {
            requestParams = Api.mUserSetNicknameUrl;
        } else {
            requestParams = Api.mUserSetGender;
        }
        ReaderParams params = new ReaderParams(activity);
        if (flag == 0) {
            params.putExtraParams("nickname", mEdit);
        } else {
            params.putExtraParams("gender", flag + "");
        }
        String json = params.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity,requestParams, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        MyToash.ToashSuccess(activity,LanguageUtil.getString(activity,R.string.updateSuccess));
                        EventBus.getDefault().post(new RefreshMine());
                        initData();
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    /**
     * 刷新自己
     *
     * @param refreshUserInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshSelf(RefreshUserInfo refreshUserInfo) {
        if (UserUtils.isLogin(activity) && refreshUserInfo.isRefresh) {
            // 绑定成功
            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.UserInfoActivity_bangdingyes));
            initData();
        } else {
            finish();
        }
    }

    /**
     * 更换头像
     *
     * @param refreshMineListItem
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshMineListItem(RefreshMineListItem refreshMineListItem) {
        MyToash.ToashSuccess(activity,LanguageUtil.getString(activity,R.string.updateSuccess));
        for (MineModel mineModel : mineModels) {
            if (mineModel.getAction().equals("avatar")) {
                mineModel.setDesc(refreshMineListItem.mCutUri);
            }
        }
        userInfoAdapter.notifyDataSetChanged();
    }

    /**
     * 微信登录
     *
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isWeChatLogin(WeChatLoginRefresh refresh) {
        if (refresh.isLogin && refresh.isBind) {
            new LoginUtils(this).getWeiXinLogin(refresh.code, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            initData();
        } else {
            MyOpenCameraAlbum.resultCramera(activity, requestCode, resultCode, data, imageView, true);
        }
    }
}
