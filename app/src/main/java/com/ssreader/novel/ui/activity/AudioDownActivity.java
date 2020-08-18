package com.ssreader.novel.ui.activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.AudioAddDown;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.AudioSelectionsBean;
import com.ssreader.novel.model.Downoption;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.audio.adapter.AudioCatalogDownAdapter;
import com.ssreader.novel.ui.audio.dialog.AudioSelectionsDialogFragment;
import com.ssreader.novel.ui.dialog.PublicPurchaseDialog;
import com.ssreader.novel.ui.dialog.WaitDialog;
import com.ssreader.novel.ui.audio.fragment.AudioInfoCatalogFragment;
import com.ssreader.novel.ui.service.DownAudioService;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.SystemUtil;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static com.ssreader.novel.utils.FileManager.isExistAudioLocal;

/**
 * 有声下载
 */
public class AudioDownActivity extends BaseActivity {

    @BindViews({R.id.audio_catalog_down_num})
    List<TextView> headText;
    @BindView(R.id.audio_catalog_down_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.audio_catalog_down_checkBox)
    CheckBox checkBox;
    @BindView(R.id.audio_catalog_down_text)
    TextView audio_catalog_down_text;
    @BindView(R.id.audio_catalog_down_anthology)
    LinearLayout anthologyLayout;
    @BindView(R.id.audio_catalog_down_yetsize)
    TextView audio_catalog_down_yetsize;

    @BindView(R.id.audio_catalog_down_allChoose)
    TextView audio_catalog_down_allChoose;
    @BindView(R.id.audio_catalog_down_yetall_down)
    View audio_catalog_down_yetall_down;
    @BindView(R.id.audio_catalog_down_noall_down)
    View audio_catalog_down_noall_down;

    private long Size;
    int total_size, down_chapters = 0;
    private long audioId;
    // 是否只查看已下载
    private boolean flag;
    private List<AudioChapter> audioChapterList;
    // 选集
    private List<AudioSelectionsBean> audioSelectionsBeanList;
    private int position = -1;
    private AudioSelectionsDialogFragment dialogFragment;
    // 适配器
    private AudioCatalogDownAdapter audioCatalogDownAdapter;

    private List<AudioChapter> audioChooseChapterList;

    private LinearLayoutManager manager;
    private Audio audio;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private long yetChooseSize;

    // 购买弹窗
    private PublicPurchaseDialog purchaseDialog;

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        audio = (Audio) (formIntent.getSerializableExtra("audio"));
        audioId = audio.audio_id;
        flag = formIntent.getBooleanExtra("flag", false);
        if (!flag) {
            public_sns_topbar_title_id = R.string.audio_down_title;
        }
        return R.layout.activity_audio_catalog_down;
    }

    @Override
    public void initView() {
        if (flag) {
            public_sns_topbar_title.setText(audio.name);
        }
        audioChapterList = new ArrayList<>();
        audioSelectionsBeanList = new ArrayList<>();
        audioChooseChapterList = new ArrayList<>();
        audio_catalog_down_text.setBackgroundColor(ContextCompat.getColor(activity, R.color.gray_b0));
        dialogFragment = new AudioSelectionsDialogFragment(this, audioSelectionsBeanList);
        audioCatalogDownAdapter = new AudioCatalogDownAdapter(this, flag, audioChapterList, audioId, audioChooseChapterList);
        manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(audioCatalogDownAdapter);
        initListener();
        if (flag) {
            anthologyLayout.setVisibility(View.GONE);
            audio_catalog_down_text.setText(LanguageUtil.getString(activity, R.string.app_delete));
        }
    }

    private void initListener() {
        audioCatalogDownAdapter.setOnCatalogDownListener(new AudioCatalogDownAdapter.OnCatalogDownListener() {
            @Override
            public void onChoose(int position, AudioChapter audioChapter) {
                if (!audioChooseChapterList.contains(audioChapter)) {
                    audioChooseChapterList.add(audioChapter);
                    yetChooseSize += audioChapter.getSize();
                    // 判断是否加入了全部的章节
                    if (audioCatalogDownAdapter.getChooseSumNum() == audioChooseChapterList.size()) {
                        // 设置全选按钮, 判断是否为全选状态
                        audioCatalogDownAdapter.isChooseAll = true;
                        audio_catalog_down_allChoose.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
                        checkBox.setChecked(true);
                    }
                } else if (audioChooseChapterList.contains(audioChapter)) {
                    audioChooseChapterList.remove(audioChapter);
                    yetChooseSize -= audioChapter.getSize();
                    // 设置全选按钮, 判断是否为全选状态
                    if (audioCatalogDownAdapter.isChooseAll) {
                        audioCatalogDownAdapter.isChooseAll = false;
                        audio_catalog_down_allChoose.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
                        checkBox.setChecked(false);
                    }
                }
                setRamTipsUi();
            }
        });
        // 选章
        dialogFragment.setOnSelectionsDialogListener(new AudioSelectionsDialogFragment.OnSelectionsDialogListener() {
            @Override
            public void onSelection(int index) {
                position = index;
                if (!audioSelectionsBeanList.isEmpty() && audioChapterList != null) {
                    // 切换章节位置
                    int x = audioSelectionsBeanList.get(position).getStartNum() - 1;
                    manager.scrollToPositionWithOffset(x, 0);
                }
            }
        });
    }

    @OnClick({R.id.audio_catalog_down_checkBox_layout, R.id.audio_catalog_down_text, R.id.audio_catalog_down_anthology})
    public void onCatalogDownClick(View view) {
        switch (view.getId()) {
            case R.id.audio_catalog_down_checkBox_layout:
                // 全选
                yetChooseSize = 0;
                audioChooseChapterList.clear();
                audioCatalogDownAdapter.isChooseAll = !audioCatalogDownAdapter.isChooseAll;
                if (audioCatalogDownAdapter.isChooseAll) {
                    for (AudioChapter audioChapter : audioChapterList) {
                        // 选中没有下载的章节
                        if (!flag) {
                            if (TextUtils.isEmpty(audioChapter.path) || !new File(audioChapter.path).exists()) {
                                yetChooseSize += audioChapter.getSize();
                                audioChapter.setIsChoose(true);
                                audioChooseChapterList.add(audioChapter);
                            }
                        } else {
                            // 删除
                            if (!TextUtils.isEmpty(audioChapter.path) && new File(audioChapter.path).exists()) {
                                yetChooseSize += audioChapter.getSize();
                                audioChapter.setIsChoose(true);
                                audioChooseChapterList.add(audioChapter);
                            }
                        }
                    }
                    audio_catalog_down_allChoose.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
                } else {
                    for (AudioChapter audioChapter : audioChapterList) {
                        audioChapter.setIsChoose(false);
                    }
                    audio_catalog_down_allChoose.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
                }
                audioCatalogDownAdapter.setAudioChooseChapterList(audioChooseChapterList);
                audioCatalogDownAdapter.notifyDataSetChanged();
                // 设置全选按钮
                checkBox.setChecked(audioCatalogDownAdapter.isChooseAll);
                // 设置内存提示
                setRamTipsUi();
                break;
            case R.id.audio_catalog_down_text:
                if (!audioChooseChapterList.isEmpty()) {
                    // 删除
                    if (flag) {
                        for (AudioChapter audioChapter : audioChooseChapterList) {
                            if (!TextUtils.isEmpty(audioChapter.path)) {
                                new File(audioChapter.path).delete();
                            }
                        }
                        audio.down_chapters -= audioChooseChapterList.size();
                        ObjectBoxUtils.addData(audio, Audio.class);
                        EventBus.getDefault().post(audio);
                        audioChapterList.removeAll(audioChooseChapterList);
                        audioChooseChapterList.clear();
                        // 设置全选按钮, 判断是否为全选状态
                        if (audioCatalogDownAdapter.isChooseAll) {
                            audioCatalogDownAdapter.isChooseAll = false;
                            checkBox.setChecked(false);
                            audio_catalog_down_allChoose.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
                        }
                        audioCatalogDownAdapter.notifyDataSetChanged();
                        headText.get(0).setText(String.format(LanguageUtil.getString(activity, R.string.audio_info_chapter_num), audioChapterList.size()));
                        MyToash.ToashSuccess(activity, R.string.local_delete_Success);
                    } else {
                        // 下载
                        if (yetChooseSize / 1073741824 > (Size)) {
                            Toast.makeText(activity, LanguageUtil.getString(activity, R.string.not_enough_storage), Toast.LENGTH_LONG).show();
                            return;
                        }
                        String chapter_id = "";
                        for (AudioChapter audioChapter : audioChooseChapterList) {
                            chapter_id += "," + audioChapter.chapter_id;
                        }
                        chapter_id = chapter_id.substring(1);
                        downAudioMp3(chapter_id, audioChooseChapterList.size());
                    }
                }
                break;
            case R.id.audio_catalog_down_anthology:
                // 选章
                if (!audioChapterList.isEmpty() && dialogFragment != null) {
                    dialogFragment.show(getSupportFragmentManager(), "AudioSelectionsDialogFragment");
                }
                break;
        }
    }

    private void setRamTipsUi() {
        if (audioChooseChapterList != null && !audioChooseChapterList.isEmpty()) {
            if (!flag) {
                audio_catalog_down_yetsize.setText(String.format(LanguageUtil.getString(activity, R.string.audio_change_down_yetsize), audioChooseChapterList.size(),
                        decimalFormat.format((float) yetChooseSize / 1048576), decimalFormat.format(Size)));
            } else {
                audio_catalog_down_yetsize.setText(String.format(LanguageUtil.getString(activity, R.string.audio_change_downed), audioChooseChapterList.size(),
                        decimalFormat.format((float) yetChooseSize / 1048576)));
            }
            audio_catalog_down_text.setBackgroundColor(ContextCompat.getColor(activity, R.color.maincolor));
        } else {
            audio_catalog_down_text.setBackgroundColor(ContextCompat.getColor(activity, R.color.gray_b0));
            audio_catalog_down_yetsize.setText(String.format(LanguageUtil.getString(activity, R.string.audio_change_enable_down),
                    decimalFormat.format(Size)));
        }
    }

    /**
     * 下载MP3
     *
     * @param chapter_id
     * @param num
     */
    private void downAudioMp3(String chapter_id, int num) {
        showWaitDialog(true);
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("audio_id", audioId);
        readerParams.putExtraParams("chapter_id", chapter_id);
        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.audio_chapter_down, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                for (AudioChapter audioChapter : audioChooseChapterList) {
                    audioChapter.status = 1;
                }
                ObjectBoxUtils.addData(audioChooseChapterList, AudioChapter.class);
                EventBus.getDefault().post(new AudioAddDown(true, 0));
                audioCatalogDownAdapter.notifyDataSetChanged();
                initInfo(response);
            }

            @Override
            public void onErrorResponse(String ex) {
                showWaitDialog(false);
                if (ex != null && ex.equals("701") || ex.equals("601")) {
                    purchaseDialog = new PublicPurchaseDialog(activity, Constant.AUDIO_CONSTANT, false, new PublicPurchaseDialog.BuySuccess() {
                        @Override
                        public void buySuccess(long[] ids, int num) {
                            for (AudioChapter audioChapter : audioChooseChapterList) {
                                audioChapter.setIs_preview(0);
                            }
                            downAudioMp3(chapter_id, audioChooseChapterList.size());
                        }
                    }, true);
                    Downoption downoption = new Downoption();
                    downoption.down_num = num;
                    purchaseDialog.initData(audioId, chapter_id + "", true, downoption);
                    purchaseDialog.show();
                } else {
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.ComicDownActivity_downfail));
                    for (AudioChapter audioChapter : audioChooseChapterList) {
                        audioChapter.setIs_preview(0);
                    }
                    audioCatalogDownAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initData() {
        audio.getAudioChapterList(activity, new AudioInfoCatalogFragment.GetAudioChapterList() {
            @Override
            public void getAudioChapterList(List<AudioChapter> audioChapters) {
                if (flag) {
                    for (AudioChapter audioChapter : audioChapters) {
                        if (TextUtils.isEmpty(audioChapter.path) || !new File(audioChapter.path).exists()) {
                            audioChapter.path = isExistAudioLocal(audioId, audioChapter.chapter_id);
                        }
                        if (!TextUtils.isEmpty(audioChapter.path)) {
                            audioChapterList.add(audioChapter);
                        }
                    }
                    audioCatalogDownAdapter.notifyDataSetChanged();
                    headText.get(0).setText(String.format(LanguageUtil.getString(activity, R.string.audio_info_chapter_num), audioChapterList.size()));
                } else {
                    audioChapterList.addAll(audioChapters);
                    audioCatalogDownAdapter.notifyDataSetChanged();
                    headText.get(0).setText(String.format(LanguageUtil.getString(activity, R.string.audio_info_chapter_num), audioChapterList.size()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            total_size = audioChapterList.size();
                            for (AudioChapter audioChapter : audioChapterList) {
                                if (TextUtils.isEmpty(audioChapter.path) || !new File(audioChapter.path).exists()) {
                                    audioChapter.path = isExistAudioLocal(audioId, audioChapter.chapter_id);
                                    if (!TextUtils.isEmpty(audioChapter.path)) {
                                        audioChapter.status = -1;
                                        ObjectBoxUtils.addData(audioChapter, AudioChapter.class);
                                        EventBus.getDefault().post(new AudioAddDown(-1, audioChapter.getChapter_id(), audioChapter.path));
                                    }
                                }
                                if (!TextUtils.isEmpty(audioChapter.path) && new File(audioChapter.path).exists()) {
                                    ++down_chapters;
                                }
//                                boolean isLive = SystemUtil.isServiceExisted(activity, DownAudioService.class.getName());
//                                MyToash.Log("audio_down", isLive + "");
//                                if (audioChapter.status == 1 && !isLive) {
//                                    audioChapter.status = 2;
//                                }
                            }
                            if (down_chapters != audio.down_chapters) {
                                audio.down_chapters = down_chapters;
                                ObjectBoxUtils.addData(audio, Audio.class);
                            }

                            // 获取选集数据
                            getSelectionsBean(audioChapterList.size());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (down_chapters == total_size) {
                                        audio_catalog_down_yetall_down.setVisibility(View.VISIBLE);
                                        audio_catalog_down_noall_down.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Size = FileManager.getSize() / 1073741824;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audio_catalog_down_yetsize.setText(String.format(LanguageUtil.getString(activity, R.string.audio_change_enable_down),
                                decimalFormat.format(Size)));
                    }
                });
            }
        }).start();
    }

    @Override
    public void initInfo(String result) {
        audioChooseChapterList.clear();
        // 设置全选按钮, 判断是否为全选状态
        if (audioCatalogDownAdapter.isChooseAll) {
            audioCatalogDownAdapter.isChooseAll = false;
            checkBox.setChecked(false);
            audio_catalog_down_allChoose.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
            audioCatalogDownAdapter.notifyDataSetChanged();
        }
        yetChooseSize = 0;
        audio_catalog_down_yetsize.setText(String.format(LanguageUtil.getString(activity, R.string.audio_change_enable_down),
                decimalFormat.format(Size)));
        Intent intent = new Intent();
        intent.setAction(BuildConfig.APPLICATION_ID + ".ui.service.DownAudioService");
        intent.setPackage(BuildConfig.APPLICATION_ID);
        String fileName = System.currentTimeMillis() + "";
        boolean re = FileManager.writeToXML(fileName, result);
        if (re) {
            intent.putExtra("isFromDownActivity", true);
            intent.putExtra("result", fileName);
            intent.putExtra("audio_id", audioId);
            startService(intent);
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

    /**
     * 刷新下载状态
     *
     * @param audioAddDown
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(AudioAddDown audioAddDown) {
        if (activity != null && !activity.isFinishing()) {
            if (audioAddDown.status == 0) {
                if (audioAddDown.isFromDownActivity) {
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_adddown));
                    showWaitDialog(false);
                }
            } else if (audioAddDown.status == 1) {
                if (audioAddDown.isFromDownActivity) {
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_downcomplete));
                }
            } else if (audioAddDown.status == 2 || audioAddDown.status == -1) {
                for (AudioChapter audioChapter : audioChapterList) {
                    if (audioChapter.chapter_id == audioAddDown.chapter_id) {
                        audioChapter.status = audioAddDown.status;
                        if (audioChapter.status == -1) {
                            audioChapter.path = audioAddDown.path;
                            ObjectBoxUtils.addData(audioChapter, AudioChapter.class);
                            ++down_chapters;
                            if (down_chapters == total_size) {
                                audio_catalog_down_yetall_down.setVisibility(View.VISIBLE);
                                audio_catalog_down_noall_down.setVisibility(View.GONE);
                            }
                        }
                        audioCatalogDownAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    private WaitDialog waitDialog;

    private void showWaitDialog(boolean flag) {
        if (flag) {
            if (waitDialog == null || !waitDialog.isShowing()) {
                waitDialog = null;
                waitDialog = new WaitDialog(activity, 1).ShowDialog(true);
            }
        } else {
            if (waitDialog != null) {
                waitDialog.ShowDialog(false);
            }
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
}
