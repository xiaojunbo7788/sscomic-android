package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.BaoyueUser;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.BaseLabelBean;
import com.ssreader.novel.model.BaseStoreMemberCenterBean;
import com.ssreader.novel.model.CategoryItem;
import com.ssreader.novel.model.OptionItem;
import com.ssreader.novel.model.SearchBox;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.BaseOptionActivity;
import com.ssreader.novel.ui.activity.LoginActivity;
import com.ssreader.novel.ui.activity.NewRechargeActivity;
import com.ssreader.novel.ui.adapter.MonthlyHeadMemberAdapter;
import com.ssreader.novel.ui.adapter.PublicMainAdapter;
import com.ssreader.novel.ui.adapter.PublicStoreListAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.MyRadioButton;
import com.ssreader.novel.ui.view.banner.ConvenientBanner;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Api.*;
import static com.ssreader.novel.constant.Constant.*;

public class BaseListFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView public_recycleview;

    @BindView(R.id.fragment_base_noResult)
    LinearLayout fragment_option_noresult;
    @BindView(R.id.fragment_base_noResult_text)
    TextView noResultText;

    private LinearLayout temphead;
    private Map<String, String> map;
    private ViewHolder viewHolder;
    private TextView titlebar_text;
    private String recommend_id;

    private List<BaseBookComic> baseBookComics;
    private PublicStoreListAdapter bottomDateAdapter;
    private List<BaseLabelBean> baseLabelBeans;
    private PublicMainAdapter bookStoareAdapter;

    private int productType;
    private int SEX, OPTION;
    private String rank_type;

    public BaseListFragment(int productType, int OPTION, int SEX) {
        this.productType = productType;
        this.SEX = SEX;
        this.OPTION = OPTION;
    }

    public BaseListFragment(int productType, int OPTION, String recommend_id, TextView titlebar_text) {
        this.productType = productType;
        this.recommend_id = recommend_id;
        this.OPTION = OPTION;
        this.titlebar_text = titlebar_text;
    }

    public BaseListFragment(int productType, int OPTION, String rank_type, int SEX) {
        this.productType = productType;
        this.rank_type = rank_type;
        this.OPTION = OPTION;
        this.SEX = SEX;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_base_book_stoare_banner_bottom_list_date;
    }

    @Override
    public void initView() {
        initSCRecyclerView(public_recycleview, RecyclerView.VERTICAL, 0);
        baseBookComics = new ArrayList<>();
        baseLabelBeans = new ArrayList<>();

        if (productType == COMIC_CONSTANT) {
            switch (OPTION) {
                case MIANFEI:
                    http_URL = COMIC_free_time;
                    break;
                case WANBEN:
                    http_URL = COMIC_finish;
                    break;
                case SHUKU:
                    temphead = new LinearLayout(activity);
                    temphead.setOrientation(LinearLayout.VERTICAL);
                    http_URL = COMIC_list;
                    map = new <String, String>HashMap();
                    break;
                case PAIHANG:
                    http_URL = COMIC_rank_list;
                    break;
                case BAOYUE_SEARCH:
                    temphead = new LinearLayout(activity);
                    temphead.setOrientation(LinearLayout.VERTICAL);
                    http_URL = COMIC_baoyue_list;
                    map = new <String, String>HashMap();
                    break;
                case BAOYUE:
                    http_URL = MEMBER_CENTER;
                    public_recycleview.setLoadingMoreEnabled(false);
                    temphead = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.header_baoyue, null, false);
                    viewHolder = new ViewHolder(temphead);
                    break;
                case LOOKMORE:
                    http_URL = COMIC_recommend;
                    break;
            }
        } else if (productType == BOOK_CONSTANT) {
            switch (OPTION) {
                case MIANFEI:
                    http_URL = mFreeTimeUrl;
                    break;
                case WANBEN:
                    http_URL = mFinishUrl;
                    break;
                case SHUKU:
                    temphead = new LinearLayout(activity);
                    temphead.setOrientation(LinearLayout.VERTICAL);
                    http_URL = Api.mCategoryIndexUrl;
                    map = new <String, String>HashMap();
                    break;
                case PAIHANG:
                    http_URL = mRankListUrl;
                    break;
                case BAOYUE_SEARCH:
                    temphead = new LinearLayout(activity);
                    temphead.setOrientation(LinearLayout.VERTICAL);
                    http_URL = mBaoyueIndexUrl;
                    map = new <String, String>HashMap();
                    break;
                case BAOYUE:
                    public_recycleview.setLoadingMoreEnabled(false);
                    http_URL = MEMBER_CENTER;
                    temphead = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.header_baoyue, null, false);
                    viewHolder = new ViewHolder(temphead);
                    break;
                case LOOKMORE:
                    if (recommend_id != null) {
                        http_URL = mRecommendUrl;
                    } else {
                        http_URL = free_time;
                    }
                    break;
            }
        } else if (productType == AUDIO_CONSTANT) {
            switch (OPTION) {
                case MIANFEI:
                    http_URL = AUDIO_FREE;
                    break;
                case WANBEN:
                    http_URL = AUDIO_FINISHED;
                    break;
                case SHUKU:
                    temphead = new LinearLayout(activity);
                    temphead.setOrientation(LinearLayout.VERTICAL);
                    http_URL = Api.AUDIO_CATEGORY_INDEX;
                    map = new <String, String>HashMap();
                    break;
                case PAIHANG:
                    http_URL = AUDIO_TOP_List;
                    break;
                case BAOYUE_SEARCH:
                    temphead = new LinearLayout(activity);
                    temphead.setOrientation(LinearLayout.VERTICAL);
                    http_URL = AUDIO_BAOYUE_LIST;
                    map = new <String, String>HashMap();
                    break;
                case BAOYUE:
                    public_recycleview.setLoadingMoreEnabled(false);
                    http_URL = MEMBER_CENTER;
                    temphead = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.header_baoyue, null, false);
                    viewHolder = new ViewHolder(temphead);
                    break;
                case LOOKMORE:
                    if (recommend_id != null) {
                        http_URL = AUDIO_RECOMMEND;
                    } else {
                        http_URL = AUDIO_FREE_TIME;
                    }
                    break;
            }
        }
        if (OPTION == SHUKU || OPTION == BAOYUE_SEARCH || OPTION == BAOYUE) {
            public_recycleview.addHeaderView(temphead);
        }
        if (OPTION != BAOYUE) {
            bottomDateAdapter = new PublicStoreListAdapter(activity, 4, baseBookComics, true, OPTION);
            public_recycleview.setAdapter(bottomDateAdapter, true);
        } else {
            bookStoareAdapter = new PublicMainAdapter(baseLabelBeans, productType, activity, true, true);
            public_recycleview.setAdapter(bookStoareAdapter);
            public_recycleview.setPadding(0, 0, 0, ImageUtil.dp2px(activity, 10));
        }
        noResultText.setText(LanguageUtil.getString(activity, R.string.app_noresult));
    }

    @Override
    public void initData() {
        if (http_URL == null) {
            return;
        }
        readerParams = new ReaderParams(activity);
        switch (OPTION) {
            case LOOKMORE:
                if (recommend_id != null) {
                    readerParams.putExtraParams("recommend_id", recommend_id + "");
                }
                readerParams.putExtraParams("channel_id", SEX + "");
                readerParams.putExtraParams("page_num", current_page + "");
                break;
            case BAOYUE:
                readerParams.putExtraParams("site_id", productType + 1);
                break;
            case PAIHANG:
                readerParams.putExtraParams("channel_id", SEX + "");
                readerParams.putExtraParams("rank_type", rank_type);
                readerParams.putExtraParams("page_num", current_page + "");
                break;
            case BAOYUE_SEARCH:
            case SHUKU:
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    readerParams.putExtraParams(entry.getKey(), entry.getValue());
                }
                readerParams.putExtraParams("page_num", current_page + "");
                break;
            default:
                MyToash.Log("channel_id", SEX + "");
                readerParams.putExtraParams("channel_id", SEX);
                readerParams.putExtraParams("page_num", current_page + "");
                break;
        }
        httpUtils = HttpUtils.getInstance();
        httpUtils.sendRequestRequestParams(activity, http_URL, readerParams.generateParamsJson(), responseListener);
    }

    private boolean isRefarshHead;

    @Override
    public void initInfo(String json) {
        if (OPTION == BAOYUE) {
            BaseStoreMemberCenterBean memberCenterBean = gson.fromJson(json, BaseStoreMemberCenterBean.class);
            setMemberCenterUser(memberCenterBean.getUser());
            if (memberCenterBean.getBanner() != null) {
                viewHolder.convenientBanner.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = viewHolder.convenientBanner.getLayoutParams();
                layoutParams.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
                layoutParams.height = (layoutParams.width - ImageUtil.dp2px(activity, 20)) / 4;
                viewHolder.convenientBanner.setLayoutParams(layoutParams);
                ConvenientBanner.initBanner(activity, 3, memberCenterBean.getBanner(), viewHolder.convenientBanner, productType);
            } else {
                viewHolder.convenientBanner.setVisibility(View.GONE);
            }
            if (memberCenterBean.getPrivilege() != null && !memberCenterBean.getPrivilege().isEmpty()) {
                viewHolder.vipHeadGridView.setVisibility(View.VISIBLE);
                viewHolder.vipHeadGridView.setNumColumns(memberCenterBean.getPrivilege().size());
                viewHolder.privileges.clear();
                viewHolder.privileges.addAll(memberCenterBean.getPrivilege());
                viewHolder.monthlyHeadMemberAdapter.notifyDataSetChanged();
            } else {
                viewHolder.vipHeadGridView.setVisibility(View.GONE);
            }
            if (memberCenterBean.getLabel() != null && !memberCenterBean.getLabel().isEmpty()) {
                baseLabelBeans.clear();
                baseLabelBeans.addAll(memberCenterBean.getLabel());
                viewHolder.baoyueHeadNoResult.setVisibility(View.GONE);
            } else {
                viewHolder.baoyueHeadNoResult.setVisibility(View.VISIBLE);
            }
            bookStoareAdapter.notifyDataSetChanged();
        } else if (OPTION == BAOYUE_SEARCH || OPTION == SHUKU) {
            CategoryItem categoryItem = gson.fromJson(json, CategoryItem.class);
            if (current_page == 1) {
                if (isRefarshHead || temphead.getChildCount() == 0) {
                    if (isRefarshHead) {
                        temphead.removeAllViews();
                    }
                    int raw = 0;
                    for (SearchBox searchBox : categoryItem.search_box) {
                        ++raw;
                        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.serach_head, null, false);
                        RadioGroup srach_head_RadioGroup = linearLayout.findViewById(R.id.srach_head_RadioGroup);
                        int id = 0;
                        for (SearchBox.SearchBoxLabe searchBoxLabe : searchBox.list) {
                            final MyRadioButton radioButton = (MyRadioButton) LayoutInflater.from(activity)
                                    .inflate(R.layout.activity_radiobutton, null, false);
                            radioButton.setId(id);
                            radioButton.setfield(searchBox.field);
                            radioButton.setRaw(raw);
                            radioButton.setBackgroundResource(R.drawable.selector_search_box_item);
                            RadioGroup.LayoutParams readerParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                                    ImageUtil.dp2px(activity, 20));
                            readerParams.rightMargin = 30;
                            radioButton.setText(searchBoxLabe.getDisplay());
                            srach_head_RadioGroup.addView(radioButton, readerParams);
                            if (searchBoxLabe.checked == 1) {
                                map.put(searchBox.field, searchBoxLabe.value);
                                radioButton.setChecked(true);
                            }
                            ++id;
                        }
                        srach_head_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                MyRadioButton radioButton = (MyRadioButton) group.getChildAt(checkedId);
                                // 小说、有声的分类属性不一样，需要刷新
                                if ((productType == BOOK_CONSTANT && radioButton.getField().equals("channel_id")) ||
                                        (productType == AUDIO_CONSTANT && radioButton.getField().equals("channel_id"))) {
                                    isRefarshHead = true;
                                   // map.clear();
                                } else {
                                    isRefarshHead = false;
                                }
                                map.put(searchBox.field, searchBox.list.get(checkedId).value);
                                current_page = 1;
                                initData();
                            }
                        });
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fragment_option_noresult.getLayoutParams();
                        layoutParams.topMargin = ImageUtil.dp2px(activity, 55) * raw;
                        fragment_option_noresult.setLayoutParams(layoutParams);
                        temphead.addView(linearLayout);
                    }
                }
                bottomDateAdapter.NoLinePosition = -1;
                public_recycleview.setLoadingMoreEnabled(true);
                baseBookComics.clear();
                baseBookComics.addAll(categoryItem.list.list);
            } else if (current_page <= categoryItem.list.current_page) {
                if (!categoryItem.list.list.isEmpty()) {
                    baseBookComics.addAll(categoryItem.list.list);
                }
            }
            if (!baseBookComics.isEmpty()) {
                // 判断数据
                if (categoryItem.list.current_page >= categoryItem.list.total_page) {
                    public_recycleview.setLoadingMoreEnabled(false);
                    bottomDateAdapter.NoLinePosition = baseBookComics.size() - 1;
                }
                if (fragment_option_noresult.isShown()) {
                    fragment_option_noresult.setVisibility(View.GONE);
                }
            } else {
                if (!fragment_option_noresult.isShown()) {
                    fragment_option_noresult.setVisibility(View.VISIBLE);
                }
            }
            bottomDateAdapter.notifyDataSetChanged();
        } else if (OPTION == LOOKMORE) {
            if (recommend_id != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (current_page == 1) {
                        String title = jsonObject.getJSONObject("recommend").getString("title");
                        titlebar_text.setText(title);
                    }
                    CommonData(jsonObject.getString("list"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                CommonData(json);
            }
        } else {
            CommonData(json);
        }
    }

    /**
     * 设置会员中心头部数据
     *
     * @param baoyueUser
     */
    private void setMemberCenterUser(BaoyueUser baoyueUser) {
        if (baoyueUser == null) {
            return;
        }
        if (UserUtils.isLogin(activity)) {
            viewHolder.mAvatar = baoyueUser.avatar;
            viewHolder.mActivityBaoyueNickname.setText(baoyueUser.nickname);
            if (baoyueUser.expiry_date.length() == 0) {
                viewHolder.mActivityBaoyueDesc.setText(baoyueUser.vip_desc);
            } else {
                viewHolder.mActivityBaoyueDesc.setText(baoyueUser.expiry_date);
            }
            MyGlide.GlideImageHeadNoSize(activity, viewHolder.mAvatar, viewHolder.vipHeadImage.get(0));
            viewHolder.vipHeadImage.get(1).setVisibility(View.VISIBLE);
            if (baoyueUser.baoyue_status == 0) {
                viewHolder.vipHeadConfirm.setText(LanguageUtil.getString(activity, R.string.BaoyueActivity_open_vip));
                viewHolder.vipHeadImage.get(1).setImageResource(R.mipmap.icon_novip);
            } else {
                viewHolder.vipHeadConfirm.setText(LanguageUtil.getString(activity, R.string.BaoyueActivity_yikaitong));
                viewHolder.vipHeadImage.get(1).setImageResource(R.mipmap.icon_isvip);
            }
        } else {
            viewHolder.vipHeadImage.get(0).setBackgroundResource(R.mipmap.hold_user_avatar);
            viewHolder.vipHeadImage.get(1).setVisibility(View.GONE);
            viewHolder.mActivityBaoyueNickname.setText(LanguageUtil.getString(activity, R.string.MineNewFragment_nologin));
            viewHolder.mActivityBaoyueDesc.setText(baoyueUser.vip_desc);
        }
    }

    /**
     * 查看更多
     *
     * @param result
     */
    private void CommonData(String result) {
        OptionItem snsShowItem = gson.fromJson(result, OptionItem.class);
        if (snsShowItem.current_page <= snsShowItem.total_page && !snsShowItem.list.isEmpty()) {
            if (current_page == 1) {
                bottomDateAdapter.NoLinePosition = -1;
                public_recycleview.setLoadingMoreEnabled(true);
                baseBookComics.clear();
            }
            baseBookComics.addAll(snsShowItem.list);
        }
        if (baseBookComics.isEmpty()) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fragment_option_noresult.getLayoutParams();
            layoutParams.topMargin = ImageUtil.dp2px(activity, 100);
            fragment_option_noresult.setLayoutParams(layoutParams);
            if (!fragment_option_noresult.isShown()) {
                fragment_option_noresult.setVisibility(View.VISIBLE);
            }
        } else {
            if (snsShowItem.current_page >= snsShowItem.total_page) {
                bottomDateAdapter.NoLinePosition = baseBookComics.size() - 1;
                public_recycleview.setLoadingMoreEnabled(false);
            }
            if (fragment_option_noresult.isShown()) {
                fragment_option_noresult.setVisibility(View.GONE);
            }
        }
        bottomDateAdapter.notifyDataSetChanged();
    }

    public class ViewHolder {

        @BindView(R.id.activity_baoyue_noResult)
        LinearLayout baoyueHeadNoResult;
        @BindView(R.id.activity_baoyue_banner_male)
        ConvenientBanner convenientBanner;
        @BindViews({R.id.activity_baoyue_circle_img, R.id.activity_baoyue_vip_image})
        List<ImageView> vipHeadImage;
        @BindView(R.id.activity_baoyue_nickname)
        TextView mActivityBaoyueNickname;
        @BindView(R.id.activity_baoyue_desc)
        TextView mActivityBaoyueDesc;
        @BindView(R.id.activity_baoyue_ok)
        TextView vipHeadConfirm;
        @BindView(R.id.activity_baoyue_member_gridView)
        GridView vipHeadGridView;

        private String mAvatar;
        // 会员特权
        private List<BaseStoreMemberCenterBean.MemberPrivilege> privileges;
        private MonthlyHeadMemberAdapter monthlyHeadMemberAdapter;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
            if (privileges == null) {
                privileges = new ArrayList<>();
            }
            if (monthlyHeadMemberAdapter == null) {
                monthlyHeadMemberAdapter = new MonthlyHeadMemberAdapter(activity, privileges);
            }
            vipHeadConfirm.setBackground(MyShape.setGradient(Color.parseColor("#FEE6BA"),
                    Color.parseColor("#F9D490"), ImageUtil.dp2px(activity, 40), 0));
            vipHeadGridView.setAdapter(monthlyHeadMemberAdapter);
            vipHeadGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (privileges.size() > 0) {
                        if (privileges.get(position).getAction().equals("vip")) {
                            Intent intent = new Intent();
                            if (UserUtils.isLogin(activity)) {
                                intent.putExtra("RechargeTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_title));
                                intent.putExtra("RechargeType", "vip");
                                intent.setClass(activity, NewRechargeActivity.class);
                            } else {
                                intent.setClass(activity, LoginActivity.class);
                            }
                            startActivity(intent);
                        } else if (privileges.get(position).getAction().equals("library")) {
                            Intent intent = new Intent();
                            intent.setClass(activity, BaseOptionActivity.class);
                            intent.putExtra("productType", productType);
                            intent.putExtra("OPTION", BAOYUE_SEARCH);
                            intent.putExtra("title", LanguageUtil.getString(activity, R.string.BaoyueActivity_baoyueshuku));
                            startActivity(intent);
                        }
                    }
                }
            });
        }

        @OnClick({R.id.activity_baoyue_ok})
        public void getEvent(View view) {
            Intent intent = new Intent();
            intent.putExtra("RechargeTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_title));
            intent.putExtra("RechargeType", "vip");
            intent.setClass(activity, NewRechargeActivity.class);
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        initData();
    }
}
