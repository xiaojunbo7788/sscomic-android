package com.ssreader.novel.ui.audio.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.AudioAddDown;
import com.ssreader.novel.eventbus.AudioListenerChapterRefresh;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.AudioSelectionsBean;
import com.ssreader.novel.eventbus.ChapterBuyRefresh;
import com.ssreader.novel.model.Downoption;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.AudioDownActivity;
import com.ssreader.novel.ui.activity.AudioInfoActivity;
import com.ssreader.novel.ui.audio.adapter.AudioCatalogAdapter;
import com.ssreader.novel.ui.audio.dialog.AudioSelectionsDialogFragment;
import com.ssreader.novel.ui.dialog.PublicPurchaseDialog;
import com.ssreader.novel.ui.audio.AudioSoundActivity;
import com.ssreader.novel.ui.audio.manager.AudioManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;
import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.utils.FileManager.isExistAudioLocal;

/**
 * 有声详情目录界面
 */
public class AudioInfoCatalogFragment extends BaseFragment {

    @BindView(R.id.fragment_audio_info_catalog_top_layout)
    RelativeLayout topLayout;
    @BindView(R.id.fragment_audio_info_chapter_num)
    TextView chapterNum;
    @BindView(R.id.fragment_audio_info_goto_player_bg_layout)
    FrameLayout gotoPlayerBgLayout;
    @BindView(R.id.fragment_audio_info_goto_player_layout)
    LinearLayout gotoPlayerLayout;
    @BindView(R.id.fragment_audio_info_goto_player_layout_image)
    ImageView gotoPlayerLayoutImage;
    @BindView(R.id.fragment_audio_info_goto_player_line)
    View line;
    @BindView(R.id.fragment_audio_info_goto_player_text)
    TextView chapterText;
    @BindView(R.id.fragment_audio_info_sort_image)
    ImageView orderImage;

    @BindView(R.id.fragment_audio_info_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_audio_info_noResult)
    NestedScrollView noResultLayout;

    private long audioId, ClickTime;
    // 已经播放到的章节id
    private long currentChapterId;

    // 是否隐藏续播
    private boolean isHideGoPlayer = false;
    // 是否倒序
    private boolean isReverse = false;

    private LinearLayoutManager manager;
    private AudioSelectionsDialogFragment dialogFragment;

    // 数据
    private List<AudioChapter> audioChapterList;
    private AudioCatalogAdapter audioCatalogAdapter;
    // 选集
    private List<AudioSelectionsBean> audioSelectionsBeanList;
    private int position = -1;
    // 购买弹窗
    private PublicPurchaseDialog purchaseDialog;

    public AudioInfoCatalogFragment() {

    }

    public Audio getAudio() {
        return ((AudioInfoActivity) getActivity()).getAudio();
    }

    public void setAudio() {
        http_flag = 1;
        initData();
    }

    public AudioInfoCatalogFragment(long audioId, long currentChapterId) {
        this.audioId = audioId;
        this.currentChapterId = currentChapterId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            audioId = savedInstanceState.getLong("audioId");
            currentChapterId = savedInstanceState.getLong("currentChapterId");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("audioId", audioId);
        outState.putLong("currentChapterId", currentChapterId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_audio_info_catalog;
    }

    @Override
    public void initView() {
        audioChapterList = new ArrayList<>();
        audioSelectionsBeanList = new ArrayList<>();
        // 续播
        gotoPlayerLayout.setBackground(MyShape.setMyshapeStroke(activity, ImageUtil.dp2px(activity, 30),
                ImageUtil.dp2px(activity, 0.3f), ContextCompat.getColor(activity, R.color.add_shelf_bg)));
        // recyclerView
        manager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        audioCatalogAdapter = new AudioCatalogAdapter(activity, audioChapterList, audioId);
        dialogFragment = new AudioSelectionsDialogFragment(activity, audioSelectionsBeanList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(audioCatalogAdapter);
        if (!InternetUtils.internet(activity)) {
            setNoResultLayout(true);
        }
        initListener();
    }

    @OnClick({R.id.fragment_audio_info_sort, R.id.fragment_audio_info_anthology,
            R.id.fragment_audio_info_goto_player_layout, R.id.fragment_audio_info_goto_player_image})
    public void onCatalogClick(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.fragment_audio_info_sort:
                    // 排序
                    if (!audioChapterList.isEmpty()) {
                        Collections.reverse(audioChapterList);
                        audioCatalogAdapter.notifyDataSetChanged();
                        isReverse = !isReverse;
                        if (isReverse) {
                            // 倒序
                            orderImage.setImageResource(R.mipmap.icon_positive_order);
                        } else {
                            orderImage.setImageResource(R.mipmap.icon_reverse_order);
                        }
                    }
                    break;
                case R.id.fragment_audio_info_anthology:
                    // 选集
                    if (!audioChapterList.isEmpty() && dialogFragment != null) {
                        dialogFragment.show(getFragmentManager(), "AudioSelectionsDialogFragment");
                    }
                    break;
                case R.id.fragment_audio_info_goto_player_layout:
                    if (getAudio() != null) {
                        // 打开有声
                        getAudio().setCurrent_listen_chapter_id(currentChapterId);
                        startSound();
                    }
                    break;
                case R.id.fragment_audio_info_goto_player_image:
                    // 隐藏续播
                    isHideGoPlayer = true;
                    gotoPlayerBgLayout.setVisibility(View.GONE);
                    chapterNum.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void initListener() {
        audioCatalogAdapter.setOnCatalogListener(new AudioCatalogAdapter.OnCatalogListener() {
            @Override
            public void onDown(AudioChapter bean) {
                long ClickTimeNew = System.currentTimeMillis();
                if (ClickTimeNew - ClickTime > AgainTime) {
                    ClickTime = ClickTimeNew;
                    // 下载
                    downAudioMp3(String.valueOf(bean.getChapter_id()), bean);
                }
            }

            @Override
            public void onPlayer(AudioChapter bean) {
                if (getAudio() != null) {
                    // 打开有声界面
                    getAudio().setCurrent_chapter_text(bean.getChapter_title());
                    getAudio().setCurrent_listen_chapter_id(bean.getChapter_id());
                    startSound();
                }
            }
        });
        dialogFragment.setOnSelectionsDialogListener(new AudioSelectionsDialogFragment.OnSelectionsDialogListener() {
            @Override
            public void onSelection(int index) {
                position = index;
                // 调整recyclerView的position
                if (manager != null && !audioChapterList.isEmpty() && !audioSelectionsBeanList.isEmpty()) {
                    // 判断排列顺序
                    int x = audioSelectionsBeanList.get(position).getStartNum() - 1;
                    if (isReverse) {
                        manager.scrollToPositionWithOffset(audioChapterList.size() - 1 - x, 0);
                    } else {
                        manager.scrollToPositionWithOffset(x, 0);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        if (http_flag == 1) {
            getAudio().getAudioChapterList(activity, new GetAudioChapterList() {
                @Override
                public void getAudioChapterList(List<AudioChapter> comicChapterItemList) {
                    if (comicChapterItemList != null && !comicChapterItemList.isEmpty()) {
                        setNoResultLayout(false);
                        chapterNum.setText(String.format(LanguageUtil.getString(activity, R.string.audio_info_chapter_num), comicChapterItemList.size()));
                        if (!audioChapterList.isEmpty()) {
                            audioChapterList.clear();
                        }
                        audioChapterList.addAll(comicChapterItemList);
                        for (AudioChapter audioChapter : audioChapterList) {
                            if (TextUtils.isEmpty(audioChapter.path) || !new File(audioChapter.path).exists()) {
                                audioChapter.path = isExistAudioLocal(audioId, audioChapter.chapter_id);
                                if (!TextUtils.isEmpty(audioChapter.path)) {
                                    audioChapter.status = -1;
                                    ObjectBoxUtils.addData(audioChapter, AudioChapter.class);
                                    EventBus.getDefault().post(new AudioAddDown(-1, audioChapter.getChapter_id(), audioChapter.path));
                                }
                            }
//                            boolean isLive = SystemUtil.isServiceExisted(activity, DownAudioService.class.getName());
//                            MyToash.Log("audio_down", isLive + "");
//                            if (audioChapter.status == 1 &&
//                                    !isLive) {
//                                audioChapter.status = 2;
//                            }
                        }
                        // 该书籍是否正在播放或者已播放
                        if (AudioManager.getInstance(activity).mAudioId == audioId &&
                                AudioManager.getInstance(activity).audioCurrentChapter != null) {
                            currentChapterId = AudioManager.getInstance(activity).audioCurrentChapter.getChapter_id();
                            audioCatalogAdapter.setCurrentChapterId(currentChapterId);
                        }
                        audioCatalogAdapter.notifyDataSetChanged();
                        // 是否显示续播
                        isShowGoPlayer();
                        // 获取选集数据
                        getSelectionsBean(audioChapterList.size());
                    } else {
                        setNoResultLayout(true);
                    }
                }
            });
        }
    }

    /**
     * 刷新列表
     *
     * @param chapterBuyRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(ChapterBuyRefresh chapterBuyRefresh) {
        if (chapterBuyRefresh.productType != AUDIO_CONSTANT) {
            return;
        }
        if (!chapterBuyRefresh.IsRead) {
            for (long id : chapterBuyRefresh.ids) {
                FLAG:
                for (AudioChapter comicChapter : audioChapterList) {
                    if (id == comicChapter.chapter_id) {
                        comicChapter.setIs_preview(0);
                        break FLAG;
                    }
                }
            }
        } else {
            List<AudioChapter> localAudioChapters = ObjectBoxUtils.getAudioChapterItemfData(getAudio().audio_id);
            if (localAudioChapters != null && !localAudioChapters.isEmpty()) {
                for (AudioChapter localChapter : localAudioChapters) {
                    FLAG:
                    for (AudioChapter audioChapter : localAudioChapters) {
                        if (localChapter.chapter_id == audioChapter.chapter_id) {
                            audioChapter.is_read = localChapter.is_read;
                            audioChapter.setIs_preview(localChapter.getIs_preview());
                            break FLAG;
                        }
                    }
                }
            }
        }
        audioCatalogAdapter.notifyDataSetChanged();
    }

    /**
     * 更新当前播放章节
     *
     * @param listenerChapterRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListenerChapterId(AudioListenerChapterRefresh listenerChapterRefresh) {
        if (listenerChapterRefresh != null && !activity.isFinishing() && !audioChapterList.isEmpty()) {
            if (listenerChapterRefresh.isSound()) {
                if (listenerChapterRefresh.getAudioId() == audioId) {
                    currentChapterId = listenerChapterRefresh.getChapterId();
                    audioCatalogAdapter.setCurrentChapterId(currentChapterId);
                    if (gotoPlayerBgLayout.getVisibility() == View.VISIBLE) {
                        gotoPlayerBgLayout.setVisibility(View.GONE);
                    }
                    if (chapterNum.getVisibility() == View.GONE) {
                        chapterNum.setVisibility(View.VISIBLE);
                    }
                } else {
                    isShowGoPlayer();
                    audioCatalogAdapter.setCurrentChapterId(0);
                }
            } else {
                isShowGoPlayer();
                audioCatalogAdapter.setCurrentChapterId(0);
            }
            audioCatalogAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(AudioAddDown audioAddDown) {
        if (audioAddDown.status == -1) {
            // 刷新列表
            for (AudioChapter audioChapter : audioChapterList) {
                if (audioChapter.chapter_id == audioAddDown.chapter_id) {
                    audioChapter.status = -1;
                    audioChapter.path = audioAddDown.path;
                    audioCatalogAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        if (activity != null && !activity.isFinishing()) {
            if (audioAddDown.status == 0) {
                if (!audioAddDown.isFromDownActivity) {
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_adddown));
                } else {
                    for (AudioChapter audioChapter : audioChapterList) {
                        AudioChapter localChapter = ObjectBoxUtils.getAudioChapter(audioChapter.chapter_id);
                        audioChapter.status = localChapter.status;
                        audioCatalogAdapter.notifyDataSetChanged();
                    }
                }
            } else if (audioAddDown.status == 1) {
                if (!audioAddDown.isFromDownActivity) {
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_downcomplete));
                }
            } else if (audioAddDown.status == 2) {
                for (AudioChapter audioChapter : audioChapterList) {
                    if (audioChapter.chapter_id == audioAddDown.chapter_id) {
                        audioChapter.status = 2;
                        audioCatalogAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    /**
     * 登录回调
     *
     * @param refreshMine
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (UserUtils.isLogin(activity)) {
            if (purchaseDialog != null && purchaseDialog.isShowing()) {
                purchaseDialog.dismiss();
            }
        }
    }

    public interface GetAudioChapterList {
        void getAudioChapterList(List<AudioChapter> audioChapterList);
    }

    /**
     * 下载有声
     *
     * @param chapter_id
     * @param audioChapter
     */
    private void downAudioMp3(String chapter_id, AudioChapter audioChapter) {
        if (audioChapter.status == 1) {
            return;
        }
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("audio_id", audioId);
        readerParams.putExtraParams("chapter_id", chapter_id);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.audio_chapter_down, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                audioChapter.status = 1;
                ObjectBoxUtils.addData(audioChapter, AudioChapter.class);
                audioCatalogAdapter.notifyDataSetChanged();
                initInfo(response);
            }

            @Override
            public void onErrorResponse(String ex) {
                if (ex != null && ex.equals("701") || ex.equals("601")) {
                    purchaseDialog = new PublicPurchaseDialog(activity,"", Constant.AUDIO_CONSTANT, true, new PublicPurchaseDialog.BuySuccess() {
                        @Override
                        public void buySuccess(long[] ids, int num) {
                            audioChapter.setIs_preview(0);
                            downAudioMp3(chapter_id, audioChapter);
                        }
                    }, true);
                    Downoption downoption = new Downoption();
                    downoption.down_num = 1;
                    purchaseDialog.initData(audioId, chapter_id + "", true, downoption);
                    purchaseDialog.show();
                } else {
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.ComicDownActivity_downfail));
                    audioChapter.status = 2;
                    audioCatalogAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initInfo(String result) {
        Intent intent = new Intent();
        intent.setAction(BuildConfig.APPLICATION_ID + ".ui.service.DownAudioService");
        intent.setPackage(BuildConfig.APPLICATION_ID);
        String fileName = System.currentTimeMillis() + "";
        boolean re = FileManager.writeToXML(fileName, result);
        if (re) {
            intent.putExtra("result", fileName);
            intent.putExtra("audio_id", audioId);
            getActivity().startService(intent);
        }
    }

    /**
     * 是否显示续播
     */
    private void isShowGoPlayer() {
        if (isHideGoPlayer) {
            if (gotoPlayerBgLayout.getVisibility() == View.VISIBLE) {
                gotoPlayerBgLayout.setVisibility(View.GONE);
            }
            if (chapterNum.getVisibility() == View.GONE) {
                chapterNum.setVisibility(View.VISIBLE);
            }
            return;
        }
        if ((AudioManager.getInstance(activity).currentPlayAudio != null &&
                AudioManager.getInstance(activity).currentPlayAudio.audio_id == audioId) ||
                audioChapterList.isEmpty() || currentChapterId <= 0) {
            gotoPlayerBgLayout.setVisibility(View.GONE);
            chapterNum.setVisibility(View.VISIBLE);
        } else {
            // 是否显示续播
            boolean isShow = false;
            for (AudioChapter audioChapter : audioChapterList) {
                if (audioChapter.getChapter_id() == currentChapterId) {
                    if (!TextUtils.isEmpty(audioChapter.getChapter_title())) {
                        isShow = true;
                        line.setVisibility(View.VISIBLE);
                        chapterText.setText(audioChapter.getChapter_title());
                    }
                    break;
                }
            }
            if (isShow) {
                chapterNum.setVisibility(View.GONE);
                gotoPlayerBgLayout.setVisibility(View.VISIBLE);
            } else {
                gotoPlayerBgLayout.setVisibility(View.GONE);
                chapterNum.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setDown() {
        if (!audioChapterList.isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra("audio", getAudio());
            intent.setClass(activity, AudioDownActivity.class);
            startActivity(intent);
        }else {
           MyToash.ToashError(activity,R.string.chapterupdateing);
        }
    }

    /**
     * 获取选集数据
     *
     * @param size
     */
    private void getSelectionsBean(int size) {
        if (!audioSelectionsBeanList.isEmpty()) {
            audioSelectionsBeanList.clear();
        }
        position = 0;
        int index = size / 30;
        if (index == 0) {
            AudioSelectionsBean audioSpeedBean = new AudioSelectionsBean();
            audioSpeedBean.setStartNum(1);
            audioSpeedBean.setEndNum(size);
            audioSelectionsBeanList.add(audioSpeedBean);
        } else if (index * 30 < size) {
            for (int i = 1; i <= index; i++) {
                AudioSelectionsBean audioSpeedBean = new AudioSelectionsBean();
                audioSpeedBean.setStartNum((i - 1) * 30 + 1);
                audioSpeedBean.setEndNum(i * 30);
                audioSelectionsBeanList.add(audioSpeedBean);
            }
            AudioSelectionsBean audioSpeedBean = new AudioSelectionsBean();
            audioSpeedBean.setStartNum(index * 30 + 1);
            audioSpeedBean.setEndNum(size);
            audioSelectionsBeanList.add(audioSpeedBean);
        } else if (index * 30 == size) {
            for (int i = 1; i <= index; i++) {
                AudioSelectionsBean audioSpeedBean = new AudioSelectionsBean();
                audioSpeedBean.setStartNum((i - 1) * 30 + 1);
                audioSpeedBean.setEndNum(i * 30);
                audioSelectionsBeanList.add(audioSpeedBean);
            }
        }
    }

    private void setNoResultLayout(boolean isEmpty) {
        if (isEmpty) {
            topLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
        } else {
            noResultLayout.setVisibility(View.GONE);
            topLayout.setVisibility(View.VISIBLE);
            if (recyclerView.getVisibility() == View.GONE) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void startSound() {
        // 打开服务
        AudioManager.openService(activity);
        Intent intent = new Intent();
        intent.putExtra("audio", getAudio());
        intent.setClass(activity, AudioSoundActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
    }
}
