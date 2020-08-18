package com.ssreader.novel.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.umeng.socialize.UMShareAPI;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.ToStore;
import com.ssreader.novel.model.TaskCenter;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.TaskCenterAdapter;
import com.ssreader.novel.ui.dialog.TaskCenterSignPopupWindow;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.getCurrencyUnit;
import static com.ssreader.novel.ui.utils.LoginUtils.goToLogin;

/**
 * 任务中心
 */
public class TaskCenterActivity extends BaseActivity {

    @BindView(R.id.public_sns_topbar_title)
    TextView mPublicSnsTopbarTitle;
    @BindView(R.id.public_sns_topbar_right_other)
    RelativeLayout public_sns_topbar_right_other;
    @BindView(R.id.public_sns_topbar_right_img)
    ImageView public_sns_topbar_right_img;
    @BindView(R.id.activity_taskcenter_listview)
    ListView mActivityTaskcenterListview;

    private ViewHolder viewHolder;
    private List<TaskCenter.TaskCenter2.Taskcenter> task_list;
    private TaskCenter.Sign_info sign_info;
    private TaskCenterAdapter taskCenterAdapter;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        USE_PUBLIC_BAR = true;
        public_sns_topbar_title_id = R.string.MineNewFragment_fuli;
        return R.layout.activity_task_center;
    }

    @Override
    public void initView() {
        task_list = new ArrayList();
        public_sns_topbar_right_other.setVisibility(View.VISIBLE);
        public_sns_topbar_right_img.setBackgroundResource(R.mipmap.img_directions);
        View view = LayoutInflater.from(this).inflate(R.layout.head_task_center_list, null);
        viewHolder = new ViewHolder(view);
        viewHolder.mActivityTaskcenterLianxuday.setBackground(MyShape.setMyshapeMineStroke(activity, 4, 24));
        mActivityTaskcenterListview.addHeaderView(view, null, false);
        mActivityTaskcenterListview.setHeaderDividersEnabled(true);
        mActivityTaskcenterListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                TaskCenter.TaskCenter2.Taskcenter taskCenter2 = task_list.get(i - 1);
                if (taskCenter2.getTask_state() != 1) {
                    switch (taskCenter2.getTask_action()) {
                        case "finish_info":
                            if (goToLogin(activity)) {
                                intent.setClass(activity, UserInfoActivity.class);
                                startActivity(intent);
                            }
                            return;
                        case "add_book":
                        case "read_book":
                        case "comment_book":
                            if (!UserUtils.isLogin(activity)) {
                                startActivity(new Intent(activity, LoginActivity.class));
                                return;
                            }
                            if (!Constant.getProductTypeList(activity).isEmpty()) {
                                int index = Integer.parseInt(Constant.getProductTypeList(activity).get(0)) - 1;
                                EventBus.getDefault().post(new ToStore(index));
                                finish();
                            }
                            break;
                        case "recharge":
                            intent.putExtra("RechargeTitle", getCurrencyUnit(activity) + LanguageUtil.getString(activity, R.string.MineNewFragment_chongzhi));
                            intent.putExtra("RechargeRightTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_chongzhijilu));
                            intent.putExtra("RechargeType", "gold");
                            intent.setClass(activity, NewRechargeActivity.class);
                            startActivity(intent);
                            break;
                        case "vip":
                            intent.putExtra("RechargeTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_title));
                            intent.putExtra("RechargeType", "vip");
                            intent.setClass(activity, NewRechargeActivity.class);
                            startActivity(intent);
                            break;
                        case "share_app":
                            if (!UserUtils.isLogin(activity)) {
                                startActivity(new Intent(activity, LoginActivity.class));
                                return;
                            }
                            new MyShare(activity).ShareAPP();
                            break;
                    }
                }
            }
        });

    }

    @Override
    public void initData() {
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.taskcenter, readerParams.generateParamsJson(),responseListener);
    }

    @Override
    public void initInfo(String json) {
        TaskCenter taskCenter = gson.fromJson(json, TaskCenter.class);
        setData(taskCenter);
    }

    @OnClick({R.id.public_sns_topbar_right_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_sns_topbar_right_img:
                startActivity(new Intent(activity, TaskExplainActivity.class).
                        putExtra("sign_rules", sign_info.sign_rules));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        initData();
    }

    public void setData(TaskCenter taskCenter) {
        if (taskCenter != null) {
            sign_info = taskCenter.sign_info;
            if (sign_info.sign_status == 1) {
                viewHolder.mActivityTaskcenterSign.setImageResource(R.mipmap.icon_sign);
            } else {
                viewHolder.mActivityTaskcenterSign.setImageResource(R.mipmap.icon_unsign);
            }
            viewHolder.mActivityTaskcenterLianxuday.setText(sign_info.sign_days + "");
            viewHolder.mActivityTaskcenterGetshuquan.setText(sign_info.max_award + "" + sign_info.unit);
            if (!task_list.isEmpty()) {
                task_list.clear();
            }
            task_list.addAll(taskCenter.getTask_menu().get(0).getTask_list());
            task_list.addAll(taskCenter.getTask_menu().get(1).getTask_list());
            taskCenterAdapter = new TaskCenterAdapter(task_list, this, taskCenter.getTask_menu().get(0).getTask_list().size(),
                    taskCenter.getTask_menu().get(0).getTask_title(), taskCenter.getTask_menu().get(1).getTask_title());
            mActivityTaskcenterListview.setAdapter(taskCenterAdapter);
            taskCenterAdapter.notifyDataSetChanged();
        }
    }

    public class ViewHolder {

        @BindView(R.id.activity_taskcenter_lianxuday)
        TextView mActivityTaskcenterLianxuday;
        @BindView(R.id.activity_taskcenter_sign)
        ImageView mActivityTaskcenterSign;
        @BindView(R.id.activity_taskcenter_getshuquan)
        TextView mActivityTaskcenterGetshuquan;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(value = {R.id.activity_taskcenter_sign})
        public void getEvent(View view) {
            switch (view.getId()) {
                case R.id.activity_taskcenter_sign:
                    if (goToLogin(activity)) {
                        signHttp(activity);
                    } else {
                        finish();
                    }
                    break;
            }
        }
    }

    public void signHttp(final Activity activity) {
        if (sign_info != null && sign_info.sign_status != 1) {
            final ReaderParams params = new ReaderParams(activity);
            String json = params.generateParamsJson();
            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.sIgninhttp, json, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(final String result) {
                            sign_info.sign_status = 1;
                            viewHolder.mActivityTaskcenterSign.setImageResource(R.mipmap.icon_sign);
                            int width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 80);
                            int height = (width - ImageUtil.dp2px(activity, 140)) / 3 * 4 / 3 + ImageUtil.dp2px(activity, 200);
                            new TaskCenterSignPopupWindow(activity, false, result, width, height);
                            initData();
                        }

                        @Override
                        public void onErrorResponse(String ex) {

                        }
                    }
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
