package com.ssreader.novel.ui.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.OptionItem;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.PublicStoreListAdapter;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ShareUitls;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

public class TagListFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView public_recycleview;

    @BindView(R.id.fragment_base_noResult)
    LinearLayout fragment_option_noresult;
    @BindView(R.id.fragment_base_noResult_text)
    TextView noResultText;
    private String tab;
    private List<BaseBookComic> baseBookComics;
    private PublicStoreListAdapter bottomDateAdapter;
    private int SEX;
    private int classType = 0; //0 作者、1、标签、2、分类、3、汉化组、4、原著

    private TextView sortView1;
    private TextView sortView2;
    private TextView sortView3;

    @Override
    public int initContentView() {
        return R.layout.fragment_base_book_stoare_banner_bottom_list_date;
    }

    @Override
    public void initView() {
        this.SEX = ShareUitls.getInt(getActivity(), "sex", 1);
        if (getArguments() != null) {
            this.tab = (String) getArguments().getString("tab");
            this.classType = getArguments().getInt("classType");
        } else {
            this.tab = "未知";
            this.classType = 1;
        }



        Log.e("sex",this.SEX+"");
        initSCRecyclerView(public_recycleview, RecyclerView.VERTICAL, 0);
        baseBookComics = new ArrayList<>();
        bottomDateAdapter = new PublicStoreListAdapter(activity, 4, baseBookComics, true, 1);
        public_recycleview.setAdapter(bottomDateAdapter, true);

        View headerView = getLayoutInflater().inflate(R.layout.tag_header_layout, (ViewGroup) public_recycleview.getParent(), false);
        TextView titleView = headerView.findViewById(R.id.tag_title_view);
        if (tab != null) {
            titleView.setText(tab);
        } else {
            titleView.setText("未知");
        }
        sortView1 = headerView.findViewById(R.id.tag_sort_view1);
        sortView2 = headerView.findViewById(R.id.tag_sort_view2);
        sortView3 = headerView.findViewById(R.id.tag_sort_view3);
        sortView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortView1.setBackgroundResource(R.drawable.tab_sel_border);
                sortView1.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                sortView2.setBackgroundResource(R.drawable.tag_unsel_border);
                sortView2.setTextColor(ContextCompat.getColor(activity, R.color.black));
                sortView3.setBackgroundResource(R.drawable.tag_unsel_border);
                sortView3.setTextColor(ContextCompat.getColor(activity, R.color.black));
                current_page = 1;
                request("");
            }
        });
        sortView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortView2.setBackgroundResource(R.drawable.tab_sel_border);
                sortView2.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                sortView1.setBackgroundResource(R.drawable.tag_unsel_border);
                sortView1.setTextColor(ContextCompat.getColor(activity, R.color.black));
                sortView3.setBackgroundResource(R.drawable.tag_unsel_border);
                sortView3.setTextColor(ContextCompat.getColor(activity, R.color.black));
                current_page = 1;
                request("hot");
            }
        });
        sortView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortView3.setBackgroundResource(R.drawable.tab_sel_border);
                sortView3.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
                sortView2.setBackgroundResource(R.drawable.tag_unsel_border);
                sortView2.setTextColor(ContextCompat.getColor(activity, R.color.black));
                sortView1.setBackgroundResource(R.drawable.tag_unsel_border);
                sortView1.setTextColor(ContextCompat.getColor(activity, R.color.black));
                current_page = 1;
                request("new");
            }
        });

        public_recycleview.addHeaderView(headerView);
        noResultText.setText(LanguageUtil.getString(activity, R.string.app_noresult));
    }

    @Override
    public void initData() {
        request("");
    }

    private void request(String sort) {
        readerParams = new ReaderParams(activity);

        switch (classType) {
            case 0: //作者
                http_URL = Api.MyLikeAuthorList;
                readerParams.putExtraParams("author", tab + "");
                break;
            case 1: //标签
                http_URL = Api.TagBookList;
                readerParams.putExtraParams("tab", tab + "");
                break;
            case 2: //分类
                http_URL = Api.ClassBookList;
                readerParams.putExtraParams("tags", tab + "");
                break;
            case 3: //汉化组
                http_URL = Api.MyLikeSiniciList;
                readerParams.putExtraParams("sinici", tab + "");
                break;
            case 4: //原著
                http_URL = Api.MyLikeOriginalList;
                readerParams.putExtraParams("original", tab + "");
                break;

        }
        readerParams.putExtraParams("sort",  sort);
        readerParams.putExtraParams("channel_id", SEX + "");
        readerParams.putExtraParams("page_num", current_page + "");
        readerParams.putExtraParams("page_size", 10 + "");
        httpUtils = HttpUtils.getInstance();
        httpUtils.sendRequestRequestParams(activity, http_URL, readerParams.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {

        OptionItem snsShowItem = gson.fromJson(json, OptionItem.class);
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

}
