package com.ssreader.novel.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.OptionItem;
import com.ssreader.novel.model.SerachItem;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.HotWordsAdapter;
import com.ssreader.novel.ui.adapter.PublicStoreListAdapter;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.AdaptionGridViewNoMargin;
import com.ssreader.novel.ui.view.Input;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;

/**
 * 搜索首页
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.activity_serach_serach_layout)
    public LinearLayout activity_serach_serach_layout;
    @BindView(R.id.activity_search_delete)
    public ImageView activity_search_delete;

    @BindView(R.id.activity_search_keywords_listview)
    public SCRecyclerView fragment_option_listview;

    @BindView(R.id.activity_search_keywords)
    public EditText activity_search_keywords;

    @BindView(R.id.activity_search_hotwords_grid)
    public AdaptionGridViewNoMargin activity_search_hotwords_grid;
    @BindView(R.id.activity_search_book_grid)
    public RecyclerView activity_search_book_grid;

    @BindView(R.id.activity_search_keywords_listview_noresult)
    public LinearLayout activity_search_keywords_listview_noresult;
    @BindView(R.id.activity_search_keywords_scrollview)
    public NestedScrollView activity_search_keywords_scrollview;

    private LayoutInflater layoutInflater;
    private List<BaseBookComic> optionBeenList;
    private List<BaseBookComic> searchList;
    private PublicStoreListAdapter searchAdapter;
    private PublicStoreListAdapter bookStoareBannerBottomDateAdapter;

    private String mKeyWord, mKeyWordHint;
    private int productType;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.activity_search;
    }

    @OnClick({R.id.activity_search_delete, R.id.activity_search_cancel})
    public void onSearchClick(View view) {
        switch (view.getId()) {
            case R.id.activity_search_delete:
                activity_search_keywords.setText("");
                break;
            case R.id.activity_search_cancel:
                if (http_flag == 0) {
                    finish();
                } else {
                    http_flag = 0;
                    activity_search_keywords.setText("");
                    if (searchList != null) {
                        searchList.clear();
                    }
                    fragment_option_listview.setVisibility(View.GONE);
                    activity_search_keywords_listview_noresult.setVisibility(View.GONE);
                    activity_search_keywords_scrollview.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        initSCRecyclerView(fragment_option_listview, linearLayoutManager.VERTICAL, 0);
        optionBeenList = new ArrayList<>();
        searchList = new ArrayList<>();
        layoutInflater = LayoutInflater.from(activity);
        fragment_option_listview.addHeaderView((LinearLayout) layoutInflater.inflate(R.layout.item_list_head, null));
        activity_serach_serach_layout.setBackground(MyShape.setMyshape(8, ContextCompat.getColor(activity, R.color.search_bg)));
        productType = getIntent().getIntExtra("productType", 0);
        mKeyWord = getIntent().getStringExtra("mKeyWord");
        if (mKeyWord != null) {
            mKeyWordHint = mKeyWord;
            activity_search_keywords.setHint(mKeyWordHint);
        }
        fragment_option_listview.setPullRefreshEnabled(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        activity_search_book_grid.setLayoutManager(gridLayoutManager);
        searchAdapter = new PublicStoreListAdapter(activity, optionBeenList, new SCOnItemClickListener() {
            @Override
            public void OnItemClickListener(int flag, int position, Object O) {
                if (Input.getInstance().isKeyboardVisible(activity)) {
                    // 关闭弹窗
                    Input.getInstance().hindInput(activity_search_keywords, activity);
                }
            }

            @Override
            public void OnItemLongClickListener(int flag, int position, Object O) {

            }
        });
        activity_search_book_grid.setAdapter(searchAdapter);

        bookStoareBannerBottomDateAdapter = new PublicStoreListAdapter(activity, 4, searchList, true, 0);
        fragment_option_listview.setAdapter(bookStoareBannerBottomDateAdapter, true);

        activity_search_keywords.clearFocus();
        activity_search_keywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    activity_search_delete.setVisibility(View.GONE);
                    activity_search_keywords.setCursorVisible(false);
                    String KeyWord = activity_search_keywords.getText().toString() + "";
                    if (TextUtils.isEmpty(KeyWord) && Pattern.matches("\\s*", KeyWord)) {
                        mKeyWord = mKeyWordHint;
                    } else {
                        mKeyWord = KeyWord;
                    }
                    current_page = 1;
                    gotoSearch(mKeyWord, true);
                    Input.getInstance().hindInput(activity_search_keywords, activity);
                    return true;
                }
                return false;
            }
        });
        activity_search_keywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity_search_keywords.getText().toString().length() > 0) {
                    activity_search_delete.setVisibility(View.VISIBLE);
                } else {
                    activity_search_delete.setVisibility(View.GONE);
                }
                activity_search_keywords.setCursorVisible(true);
            }
        });
        activity_search_keywords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    activity_search_delete.setVisibility(View.GONE);
                    fragment_option_listview.setVisibility(View.GONE);
                    activity_search_keywords_scrollview.setVisibility(View.VISIBLE);
                } else {
                    activity_search_delete.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void initData() {
        if (http_flag == 0) {
            readerParams = new ReaderParams(activity);
            String url = "";
            if (productType == BOOK_CONSTANT) {
                url = Api.mSearchIndexUrl;
            } else if (productType == COMIC_CONSTANT) {
                url = Api.COMIC_search_index;
            } else if (productType == AUDIO_CONSTANT) {
                url = Api.AUDIO_search_index;
            }
            httpUtils.sendRequestRequestParams(activity,url, readerParams.generateParamsJson(),responseListener);
        } else {
            gotoSearch(mKeyWord, false);
        }
    }

    @Override
    public void initInfo(String json) {
        if (http_flag == 0) {
            SerachItem serachItem = gson.fromJson(json, SerachItem.class);
            if (!serachItem.hot_word.isEmpty()) {
                HotWordsAdapter hotWordsAdapter = new HotWordsAdapter(activity, serachItem.hot_word);
                activity_search_hotwords_grid.setAdapter(hotWordsAdapter);
                activity_search_hotwords_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mKeyWord = serachItem.hot_word.get(position);
                        activity_search_keywords.setText(mKeyWord);
                        current_page = 1;
                        gotoSearch(mKeyWord, true);
                    }
                });
            }
            optionBeenList.addAll(serachItem.list);
            searchAdapter.notifyDataSetChanged();
        } else {
            initNextInfo(json);
        }
    }

    /**
     * 开始搜索
     *
     * @param keyword
     * @param isClear
     */
    public void gotoSearch(String keyword, boolean isClear) {
        if (isClear && searchList != null) {
            searchList.clear();
        }
        if (keyword == null) {
            return;
        }
        http_flag = 1;
        readerParams = new ReaderParams(this);
        readerParams.putExtraParams("keyword", keyword);
        readerParams.putExtraParams("page_num", current_page);
        String url = "";
        if (productType == BOOK_CONSTANT) {
            url = Api.mSearchUrl;
        } else if (productType == COMIC_CONSTANT) {
            url = Api.COMIC_search;
        } else if (productType == AUDIO_CONSTANT) {
            url = Api.AUDIO_search;
        }
        HttpUtils.getInstance().sendRequestRequestParams(activity, url, readerParams.generateParamsJson(), responseListener);
    }

    public void initNextInfo(String result) {
        OptionItem optionItem = gson.fromJson(result, OptionItem.class);
        if (optionItem.list != null) {
            if (optionItem.current_page <= optionItem.total_page && !optionItem.list.isEmpty()) {
                if (current_page == 1) {
                    fragment_option_listview.setLoadingMoreEnabled(true);
                    searchList.clear();
                }
                searchList.addAll(optionItem.list);
            }
        }
        if (!searchList.isEmpty()) {
            if (optionItem.current_page >= optionItem.total_page) {
                bookStoareBannerBottomDateAdapter.NoLinePosition = searchList.size() - 1;
                fragment_option_listview.setLoadingMoreEnabled(false);
            }
            activity_search_keywords_listview_noresult.setVisibility(View.GONE);
            activity_search_keywords_scrollview.setVisibility(View.GONE);
            fragment_option_listview.setVisibility(View.VISIBLE);
            bookStoareBannerBottomDateAdapter.notifyDataSetChanged();
        } else {
            fragment_option_listview.setVisibility(View.GONE);
            activity_search_keywords_scrollview.setVisibility(View.GONE);
            activity_search_keywords_listview_noresult.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNullRefresh() {

    }

    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (fragment_option_listview.getVisibility() == View.VISIBLE || activity_search_keywords_listview_noresult.getVisibility() == View.VISIBLE) {
            fragment_option_listview.setVisibility(View.GONE);
            activity_search_keywords_listview_noresult.setVisibility(View.GONE);
            activity_search_keywords_scrollview.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }
}
