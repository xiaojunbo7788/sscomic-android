package com.ssreader.novel.ui.activity;

import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.AboutBean;
import com.ssreader.novel.model.AppUpdate;
import com.ssreader.novel.model.MineModel;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.ui.adapter.MineListAdapter;
import com.ssreader.novel.ui.dialog.UpAppDialogFragment;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.utils.UserUtils.getAppVersionName;

/**
 * 联系客服界面
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.activity_about_appversion)
    TextView mActivityAboutAppversion;
    @BindView(R.id.activity_about_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.activity_about_company)
    TextView mActivityAboutCompany;
    @BindView(R.id.activity_main_vitualkey)
    RelativeLayout mActivityMainVitualkey;

    private List<MineModel> mineModelList;
    private MineListAdapter mineListAdapter;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        public_sns_topbar_title_id = R.string.MineNewFragment_lianxikefu;
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        mActivityAboutAppversion.setText(getResources().getString(R.string.app_version) + getAppVersionName(this));
        mineModelList = new ArrayList<>();
        mineListAdapter = new MineListAdapter(activity, mineModelList, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mineListAdapter);
        initListener();
    }

    private void initListener() {
        mineListAdapter.setOnClickMineItemListener(new MineListAdapter.OnClickMineItemListener() {
            @Override
            public void onClickItem(MineModel mineModel) {
                if (mineModel.action.equals("update")) {
                    inspectionUpDate(0);
                } else {
                    mineModel.aboutIntent(activity);
                }
            }
        });
    }

    @Override
    public void initData() {
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.aBoutUs, readerParams.generateParamsJson(),  responseListener);
    }

    @Override
    public void initInfo(String json) {
        if (json != null) {
            AboutBean aboutBean = HttpUtils.getGson().fromJson(json, AboutBean.class);
            if (!TextUtils.isEmpty(aboutBean.getCompany())) {
                mActivityAboutCompany.setText(aboutBean.getCompany());
            }
            if (aboutBean.getAbout() != null && !aboutBean.getAbout().isEmpty()) {
                for (AboutBean.About about : aboutBean.getAbout()) {
                    if (about.action.equals("email") || about.action.equals("telphone") || about.action.equals("qq") || about.action.equals("wechat")) {
                        mineModelList.add(new MineModel(false, about.getTitle(),
                                about.getContent(), "", "", about.getAction(), 0));
                    } else {
                        mineModelList.add(new MineModel(false, about.getTitle(),
                                about.getContent(), "", "", about.getAction(), 1));
                    }
                }
            }
            inspectionUpDate(1);
            mineListAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }

    public void inspectionUpDate(int flag) {
        try {
            String Update = ShareUitls.getString(activity, "Update", "");
            if (!TextUtils.isEmpty(Update)) {
                AppUpdate dataBean = gson.fromJson(Update, AppUpdate.class);
                if (dataBean.version_update.getStatus() != 0) {
                    if (flag == 0) {
                        UpAppDialogFragment upAppDialogFragment = new UpAppDialogFragment(activity, dataBean.version_update);
                        upAppDialogFragment.show(getSupportFragmentManager(), "UpAppDialogFragment");
                    } else {
                        mineModelList.add(new MineModel(false, LanguageUtil.getString(activity, R.string.app_check_up),
                                LanguageUtil.getString(activity, R.string.app_upapp_old), "", "", "update", 0));
                    }
                } else {
                    if (flag == 0) {
                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.app_upapp_new));
                    } else {
                        mineModelList.add(new MineModel(false, LanguageUtil.getString(activity, R.string.app_check_up),
                                LanguageUtil.getString(activity, R.string.app_upapp_new), "", "", "update", 0));
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
