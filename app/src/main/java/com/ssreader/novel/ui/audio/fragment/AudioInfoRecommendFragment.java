package com.ssreader.novel.ui.audio.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.OptionItem;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.audio.adapter.AudioInfoRecommendAdapter;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 有声详情界面（推荐界面）
 */
public class AudioInfoRecommendFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView recyclerView;
    @BindView(R.id.fragment_audio_info_noResult)
    NestedScrollView noResultLayout;

    private long audioId;
    // 数据
    private int size;
    private List<BaseBookComic> audioList;
    private AudioInfoRecommendAdapter audioInfoRecommendAdapter;

    public AudioInfoRecommendFragment() {

    }

    public AudioInfoRecommendFragment(long audioId) {
        this.audioId = audioId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            audioId = savedInstanceState.getLong("audioId");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("audioId", audioId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_audio_info_recommend;
    }

    @Override
    public void initView() {
        audioList = new ArrayList<>();
        audioInfoRecommendAdapter = new AudioInfoRecommendAdapter(audioList, activity);
        initSCRecyclerView(recyclerView, RecyclerView.VERTICAL, 0);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setAdapter(audioInfoRecommendAdapter, true);
        if (!InternetUtils.internet(activity)) {
            setNoResultLayout(true);
        }
    }

    @Override
    public void initData() {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("audio_id", audioId);
        params.putExtraParams("page_num", current_page + "");
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.AUDIO_INFO_RECOMMEND, params.generateParamsJson(), responseListener);
    }

    @Override
    public void initInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            OptionItem optionItem = HttpUtils.getGson().fromJson(json, OptionItem.class);
            if (optionItem.list != null) {
                if (optionItem.current_page <= optionItem.total_page && optionItem.list != null && !optionItem.list.isEmpty()) {
                    if (current_page == 1) {
                        audioInfoRecommendAdapter.NoLinePosition = -1;
                        recyclerView.setLoadingMoreEnabled(true);
                        audioList.clear();
                    }
                    audioList.addAll(optionItem.list);
                }
            }
            if (!audioList.isEmpty()) {
                if (optionItem.current_page >= optionItem.total_page) {
                    audioInfoRecommendAdapter.NoLinePosition = audioList.size() - 1;
                    recyclerView.setLoadingMoreEnabled(false);
                }
                setNoResultLayout(false);
                audioInfoRecommendAdapter.notifyDataSetChanged();
            } else {
                setNoResultLayout(true);
            }
        }
    }

    private void setNoResultLayout(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
        } else {
            noResultLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
