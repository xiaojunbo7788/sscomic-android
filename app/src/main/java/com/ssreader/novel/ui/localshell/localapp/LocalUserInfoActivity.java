package com.ssreader.novel.ui.localshell.localapp;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.ImageView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.model.MineModel;
import com.ssreader.novel.ui.localshell.eventBus.LocalUserInfo;
import com.ssreader.novel.eventbus.RefreshMineListItem;
import com.ssreader.novel.ui.adapter.UserInfoAdapter;
import com.ssreader.novel.ui.dialog.BottomMenuDialog;
import com.ssreader.novel.ui.dialog.SetCodeDialog;
import com.ssreader.novel.ui.utils.MyOpenCameraAlbum;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LocalUserInfoActivity extends BaseActivity {

    @BindView(R.id.user_info_recyclerView)
    SCRecyclerView recyclerView;

    private List<MineModel> mineModels;
    private UserInfoAdapter userInfoAdapter;
    public ImageView imageView;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.UserInfoActivity_title;
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        initSCRecyclerView(recyclerView, RecyclerView.VERTICAL,0);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);
        mineModels = new ArrayList<>();
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
                        new BottomMenuDialog().showBottomMenuDialog(activity, new String[]{
                                LanguageUtil.getString(activity, R.string.MineUserInfo_PaiZhao),
                                LanguageUtil.getString(activity, R.string.MineUserInfoXiangCe)
                        }, new SCOnItemClickListener() {
                            @Override
                            public void OnItemClickListener(int flag, int position, Object O) {
                                if (position == 0) {
                                    MyOpenCameraAlbum.openCamera(activity, MyOpenCameraAlbum.LocalCAMERA);
                                } else if (position == 1) {
                                    MyOpenCameraAlbum.openPhotoAlbum(activity, MyOpenCameraAlbum.LocalGALLERY);
                                }
                            }

                            @Override
                            public void OnItemLongClickListener(int flag, int position, Object O) {

                            }
                        });
                        break;
                    case "nickname":
                        //修改昵称
                        SetCodeDialog setCodeDialog = new SetCodeDialog();
                        setCodeDialog.showSetCodeDialog(activity, LanguageUtil.getString(activity, R.string.UserInfoActivity_update_name),
                                LanguageUtil.getString(activity, R.string.UserInfoActivity_edit_name));
                        setCodeDialog.setOnVerificationSuccess(new SetCodeDialog.onVerificationSuccess() {
                            @Override
                            public void success(String editText) {
                                LocalUserInfo localUserInfo = new LocalUserInfo();
                                localUserInfo.setName(editText);
                                EventBus.getDefault().post(localUserInfo);
                                ShareUitls.putString(activity, "UserName", editText);
                                mineModels.get(1).setDesc(editText);
                                userInfoAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case "description":
                        //描述
                        SetCodeDialog setCodeDialog1 = new SetCodeDialog();
                        setCodeDialog1.showSetCodeDialog(activity, LanguageUtil.getString(activity, R.string.local_update_description),
                                LanguageUtil.getString(activity, R.string.local_editDescription));
                        setCodeDialog1.setOnVerificationSuccess(new SetCodeDialog.onVerificationSuccess() {
                            @Override
                            public void success(String editText) {
                                LocalUserInfo localUserInfo = new LocalUserInfo();
                                localUserInfo.setDes(editText);
                                EventBus.getDefault().post(localUserInfo);
                                ShareUitls.putString(activity, "UserDes", editText);
                                mineModels.get(2).setDesc(editText);
                                userInfoAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        if (!mineModels.isEmpty()){
            mineModels.clear();
        }
        String userTitle = formIntent.getStringExtra("userTitle");
        String userDes = formIntent.getStringExtra("userDes");
        String localPhoto = ShareUitls.getString(activity, "LocalPhoto", "");
        mineModels.add(new MineModel(true, LanguageUtil.getString(activity, R.string.UserInfoActivity_touxiang),
                localPhoto, "", "", "avatar", 1));
        mineModels.add(new MineModel(false, LanguageUtil.getString(activity, R.string.UserInfoActivity_nicheng),
                userTitle, "", "", "nickname", 1));
        mineModels.add(new MineModel(false, LanguageUtil.getString(activity, R.string.local_description),
                userDes, "", "", "description", 1));
        userInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshMineListItem(RefreshMineListItem refreshMineListItem) {
        ShareUitls.putString(activity, "LocalPhoto", refreshMineListItem.mCutUri);
//        list.set(0, new MineUserInfoBean(LanguageUtil.getString(
//                activity, R.string.UserInfoActivity_touxiang), "",
//                true, true, true, refreshMineListItem.mCutUri, true));
        userInfoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyOpenCameraAlbum.resultCramera(activity, requestCode, resultCode, data, imageView, false);
    }
}
