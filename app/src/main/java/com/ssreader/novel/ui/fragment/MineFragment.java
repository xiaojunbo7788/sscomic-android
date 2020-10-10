package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.manager.UserManager;
import com.umeng.socialize.UMShareAPI;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.MineModel;
import com.ssreader.novel.model.UserInfoItem;
import com.ssreader.novel.net.MainHttpTask;
import com.ssreader.novel.ui.activity.BaseOptionActivity;
import com.ssreader.novel.ui.activity.SettingActivity;
import com.ssreader.novel.ui.activity.UserInfoActivity;
import com.ssreader.novel.ui.adapter.MineListAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.*;
import static com.ssreader.novel.ui.utils.LoginUtils.goToLogin;

public class MineFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView recyclerView;

    private ViewHolder viewHolder;
    private List<MineModel> mineModels = new ArrayList<>();
    private MineListAdapter mineListAdapter;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.public_recycleview;
    }

    @Override
    public void initView() {
        initSCRecyclerView(recyclerView, RecyclerView.VERTICAL, 0);
        recyclerView.setLoadingMoreEnabled(false);
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_mine_head, null);
        viewHolder = new ViewHolder(view);
        viewHolder.mine_shubi_text.setText(getCurrencyUnit(activity));
        viewHolder.mine_shuquan_text.setText(getSubUnit(activity));
        int top;
        if (NotchHeight != 0) {
            top = NotchHeight;
        } else {
            top = (ImageUtil.dp2px(activity, 30));
        }
        viewHolder.mine_login_LinearLayout.setPadding(0, top, 0, 0);
        recyclerView.addHeaderView(view);
        mineListAdapter = new MineListAdapter(activity, mineModels);
        recyclerView.setAdapter(mineListAdapter);
        if (!UserUtils.isLogin(activity)) {
            viewHolder.mine_avatar.setImageResource(R.mipmap.icon_def_head);
            viewHolder.mine_login_registe.setText(LanguageUtil.getString(activity, R.string.MineNewFragment_user_login));
            viewHolder.mine_shubi.setText("- -");
            viewHolder.mine_fuli.setText("- -");
            viewHolder.mine_shuquan.setText("- -");
            viewHolder.mine_login.setVisibility(View.GONE);
        } else {
            viewHolder.mine_login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        MainHttpTask.getInstance().getResultString(activity, http_flag != 0, "Mine", new MainHttpTask.GetHttpData() {
            @Override
            public void getHttpData(String result) {
                http_flag = 1;
                responseListener.onResponse(result);
            }
        });
    }

    @Override
    public void initInfo(String json) {
        if (Constant.getMonthlyTicket(activity) == 0) {
            viewHolder.mine_welfare_center.setVisibility(View.GONE);
        } else {
            viewHolder.mine_welfare_center.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(json)) {
            UserInfoItem userInfoItem = gson.fromJson(json, UserInfoItem.class);
            UserManager.getInstance().setIsVip(userInfoItem.getIs_vip());
            Constant.currencyUnit = userInfoItem.getUnit();
            Constant.subUnit = userInfoItem.getSubUnit();
            // 存放货币
            ShareUitls.putString(activity, "currencyUnit", Constant.currencyUnit);
            ShareUitls.putString(activity, "subUnit", Constant.subUnit);
            // 头像
            if (userInfoItem.getAvatar() != null && !TextUtils.isEmpty(userInfoItem.getAvatar())) {
                MyGlide.GlideImageHeadNoSize(activity, userInfoItem.getAvatar(), viewHolder.mine_avatar);
            }
            if (!UserUtils.isLogin(activity)) {
                viewHolder.mine_login.setVisibility(View.GONE);
                viewHolder.mine_avatar.setImageResource(R.mipmap.icon_def_head);
                viewHolder.mine_login_registe.setText(LanguageUtil.getString(activity, R.string.MineNewFragment_user_login));
                viewHolder.mine_shubi.setText("- -");
                viewHolder.mine_fuli.setText("- -");
                viewHolder.mine_shuquan.setText("- -");
            } else {
                if(userInfoItem.getUser_token()==null&&userInfoItem.getUid()==null){
                    SettingActivity.exitUser(activity);
                    return;
                }
                viewHolder.mine_login.setVisibility(View.VISIBLE);
                if (USE_PAY) {
                    viewHolder.mine_vip_icon.setImageResource(userInfoItem.getIs_vip() == 1?R.mipmap.icon_isvip:R.mipmap.icon_novip);
                }
                viewHolder.mine_login_registe.setText(userInfoItem.getNickname());
                viewHolder.mine_user_id.setText("ID: " + userInfoItem.getUid());
                viewHolder.mine_fuli.setText(userInfoItem.ticketRemain);
                viewHolder.mine_shubi.setText(userInfoItem.getGoldRemain());
                viewHolder.mine_shuquan.setText(userInfoItem.getSilverRemain());
            }
            // 条目数据
            mineModels.clear();

            if (userInfoItem.getPanel_list() != null && UserUtils.isLogin(activity)) {
                List<List<MineModel>>newList = userInfoItem.getPanel_list();
                List<MineModel>list = new ArrayList<>();

                MineModel model1 = new MineModel(true,
                        IMAGE_URL+"icon/user/13.png",
                        "喜欢的作者",
                        "",
                        "#39383C",
                        "#39383C",
                        "author",
                        "",
                        1);
                model1.type = 1;

                MineModel model2 = new MineModel(true,
                        IMAGE_URL+"icon/user/14.png",
                        "喜欢的原著",
                        "",
                        "#39383C",
                        "#39383C",
                        "orginal",
                        "",
                        1);
                model2.type = 2;

                MineModel model3 = new MineModel(true,
                        IMAGE_URL+"icon/user/12.png",
                        "喜欢的汉化组",
                        "",
                        "#39383C",
                        "#39383C",
                        "hanhua",
                        "",
                        1);
                model3.type = 3;

                list.add(model1);
                list.add(model2);
                list.add(model3);
                newList.add(1,list);
                if (newList != null && !newList.isEmpty()) {
                    for (List<MineModel> models : newList) {
                        int size_j = models.size();
                        for (int i = 0; i < size_j; i++) {
                            MineModel mineModel = models.get(i);
                            mineModel.setBottomLine(i + 1 == size_j);
                            mineModels.add(mineModel);
                        }
                    }
                }
            } else  {
                if (userInfoItem.getPanel_list() != null && !userInfoItem.getPanel_list().isEmpty()) {
                    for (List<MineModel> models : userInfoItem.getPanel_list()) {
                        int size_j = models.size();
                        for (int i = 0; i < size_j; i++) {
                            MineModel mineModel = models.get(i);
                            mineModel.setBottomLine(i + 1 == size_j);
                            mineModels.add(mineModel);
                        }
                    }
                }
            }
            mineListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 登录回调
     * @param refreshMine
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        initData();
    }

    public class ViewHolder {

        @BindView(R.id.mine_avatar)
        ImageView mine_avatar;
        @BindView(R.id.mine_login_registe)
        TextView mine_login_registe;
        @BindView(R.id.mine_login)
        LinearLayout mine_login;
        @BindView(R.id.mine_user_id)
        TextView mine_user_id;
        @BindView(R.id.mine_vip_icon)
        ImageView mine_vip_icon;
        @BindView(R.id.mine_shubi)
        TextView mine_shubi;
        @BindView(R.id.mine_fuli)
        TextView mine_fuli;
        @BindView(R.id.mine_shuquan)
        TextView mine_shuquan;

        @BindView(R.id.mine_shuquan_text)
        TextView mine_shuquan_text;
        @BindView(R.id.mine_shubi_text)
        TextView mine_shubi_text;

        @BindView(R.id.mine_money_coins)
        LinearLayout mine_money_coins;
        @BindView(R.id.mine_welfare_center)
        LinearLayout mine_welfare_center;
        @BindView(R.id.mine_ticket)
        LinearLayout mine_ticket;

        @BindView(R.id.fragment_mine_head_money)
        View fragment_mine_head_money;
        @BindView(R.id.mine_login_LinearLayout)
        LinearLayout mine_login_LinearLayout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.mine_login_LinearLayout, R.id.mine_money_coins, R.id.mine_welfare_center, R.id.mine_ticket})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mine_login_LinearLayout:
                    if (goToLogin(activity)) {
                        startActivity(new Intent(activity, UserInfoActivity.class));
                    }
                    break;
                case R.id.mine_money_coins:
                    startActivity(new Intent(activity, BaseOptionActivity.class)
                            .putExtra("OPTION", LIUSHUIJIELU)
                            .putExtra("title", LanguageUtil.getString(activity, R.string.MineNewFragment_liushuijilu_title))
                            .putExtra("Extra", false));
                    break;
                case R.id.mine_welfare_center:
                    startActivity(new Intent(activity, BaseOptionActivity.class)
                            .putExtra("title", LanguageUtil.getString(activity, R.string.app_monthly_pass_history))
                            .putExtra("OPTION", MONTHTICKETHISTORY));
                    break;
                case R.id.mine_ticket:
                    startActivity(new Intent(activity, BaseOptionActivity.class)
                            .putExtra("title", LanguageUtil.getString(activity, R.string.MineNewFragment_liushuijilu_title))
                            .putExtra("OPTION", LIUSHUIJIELU)
                            .putExtra("Extra", true));
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }
}
