package com.ssreader.novel.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.DownScrollCancleEdit;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.ui.adapter.DownMangerAudioAdapter;
import com.ssreader.novel.ui.adapter.DownMangerComicAdapter;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.utils.ObjectBoxUtils.getAudioChapterItemfData;
import static com.ssreader.novel.utils.ObjectBoxUtils.getComicChapterItemfData;

public class DownMangerComicFragment extends BaseFragment<Object> {

    @BindView(R.id.mFragmentBookshelfNoresult)
    LinearLayout mFragmentBookshelfNoresult;
    @BindView(R.id.mActivityDownmangerList)
    ListView mActivityDownmangerList;
    @BindView(R.id.public_recycleview_buttom_1)
    TextView publicRecycleviewButtom1;
    @BindView(R.id.public_recycleview_buttom_2)
    TextView publicRecycleviewButtom2;
    @BindView(R.id.public_recycleview_buttom)
    LinearLayout bottomLayout;

    private DownMangerComicAdapter downMangerAdapter;
    private DownMangerAudioAdapter downMangerAudioAdapter;
    private List<Comic> baseComicList;
    private List<Audio> audioList;
    private int productType;
    private TextView public_sns_topbar_right_tv;

    public static boolean DownMangerComicFragment;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_downmanger;
    }

    public DownMangerComicFragment() {

    }

    public DownMangerComicFragment(int productType, TextView public_sns_topbar_right_tv) {
        this.productType = productType;
        this.public_sns_topbar_right_tv = public_sns_topbar_right_tv;
    }

    @OnClick({R.id.public_recycleview_buttom_1, R.id.public_recycleview_buttom_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.public_recycleview_buttom_1:
                // 是否全选
                if (productType == Constant.COMIC_CONSTANT) {
                    int size = baseComicList.size();
                    boolean allChoose = downMangerAdapter.checkList.size() == size;
                    downMangerAdapter.checkList.clear();
                    if (!allChoose) {
                        downMangerAdapter.checkList.addAll(baseComicList);
                        setDeleteBtn(true, size);
                    } else {
                        setDeleteBtn(false, 0);
                    }
                    downMangerAdapter.notifyDataSetChanged();
                } else {
                    int size = audioList.size();
                    boolean allChoose = downMangerAudioAdapter.checkList.size() == size;
                    downMangerAudioAdapter.checkList.clear();
                    if (!allChoose) {
                        downMangerAudioAdapter.checkList.addAll(audioList);
                        setDeleteBtn(true, size);
                    } else {
                        setDeleteBtn(false, 0);
                    }
                    downMangerAudioAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.public_recycleview_buttom_2:
                // 删除
                if (productType == Constant.COMIC_CONSTANT) {
                    if (!downMangerAdapter.checkList.isEmpty()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (Comic comic : downMangerAdapter.checkList) {
                                    EventBus.getDefault().post(new DownScrollCancleEdit(Constant.COMIC_CONSTANT, comic.comic_id));
                                    comic.down_chapters = 0;
                                    ObjectBoxUtils.addData(comic, Comic.class);
                                    List<ComicChapter> comicChapterList = getComicChapterItemfData(comic.comic_id);
                                    for (ComicChapter comicChapter : comicChapterList) {
                                        comicChapter.ImagesText = "";
                                        comicChapter.downStatus = 0;
                                    }
                                    ObjectBoxUtils.addData(comicChapterList, ComicChapter.class);
                                    String localPath = FileManager.getManhuaSDCardRoot().concat(String.valueOf(comic.getComic_id()));
                                    FileManager.deleteFile(localPath);
                                }
                                baseComicList.removeAll(downMangerAdapter.checkList);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        downMangerAdapter.Edit = false;
                                        downMangerAdapter.notifyDataSetChanged();
                                        if (baseComicList.isEmpty()) {
                                            mFragmentBookshelfNoresult.setVisibility(View.VISIBLE);
                                        }
                                        setIsDelete(false);
                                    }
                                });
                            }
                        }).start();
                    }
                } else {
                    if (!downMangerAudioAdapter.checkList.isEmpty()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (Audio audio : downMangerAudioAdapter.checkList) {
                                    EventBus.getDefault().post(new DownScrollCancleEdit(Constant.AUDIO_CONSTANT, audio.audio_id));
                                    audio.down_chapters = 0;
                                    ObjectBoxUtils.addData(audio, Audio.class);
                                    List<AudioChapter> audioChapters = getAudioChapterItemfData(audio.audio_id);
                                    for (AudioChapter audioChapter : audioChapters) {
                                        audioChapter.path = "";
                                        audioChapter.status = 0;
                                    }
                                    ObjectBoxUtils.addData(audioChapters, AudioChapter.class);
                                    String localPath = FileManager.getSDCardRoot().concat("Reader/audio/").concat(audio.getAudio_id() + "/");
                                    FileManager.deleteFile(localPath);
                                }
                                audioList.removeAll(downMangerAudioAdapter.checkList);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        downMangerAudioAdapter.Edit = false;
                                        downMangerAudioAdapter.notifyDataSetChanged();
                                        if (audioList.size() == 0) {
                                            mFragmentBookshelfNoresult.setVisibility(View.VISIBLE);
                                        }
                                        setIsDelete(false);
                                    }
                                });
                            }
                        }).start();
                    }
                }
                break;
        }
    }

    public void setEdit(boolean edit) {
        if (productType == Constant.COMIC_CONSTANT) {
            if (baseComicList.isEmpty()) {
                return;
            }
            downMangerAdapter.Edit = edit;
        } else {
            if (audioList.isEmpty()) {
                return;
            }
            downMangerAudioAdapter.Edit = edit;
        }
        if (edit) {
            setIsDelete(true);
        } else {
            if (productType == Constant.COMIC_CONSTANT) {
                downMangerAdapter.checkList.clear();
            } else {
                downMangerAudioAdapter.checkList.clear();
            }
            setIsDelete(false);
        }
        if (productType == Constant.COMIC_CONSTANT) {
            downMangerAdapter.notifyDataSetChanged();
        } else {
            downMangerAudioAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initView() {
        DownMangerComicFragment = true;
        View temphead = LayoutInflater.from(activity).inflate(R.layout.item_list_head, null);
        mActivityDownmangerList.addHeaderView(temphead, null, false);
        mActivityDownmangerList.setHeaderDividersEnabled(true);
        if (productType == Constant.COMIC_CONSTANT) {
            baseComicList = ObjectBoxUtils.getyetDownComicList();
            downMangerAdapter = new DownMangerComicAdapter(activity, baseComicList, scOnItemClickListener);
            mActivityDownmangerList.setAdapter(downMangerAdapter);
            if (baseComicList.isEmpty()) {
                mFragmentBookshelfNoresult.setVisibility(View.VISIBLE);
            }
        } else if (productType == Constant.AUDIO_CONSTANT) {
            audioList = ObjectBoxUtils.getyetDownAudioList();
            downMangerAudioAdapter = new DownMangerAudioAdapter(activity, audioList, scOnItemClickListener);
            mActivityDownmangerList.setAdapter(downMangerAudioAdapter);
            if (audioList.isEmpty()) {
                mFragmentBookshelfNoresult.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void setOnItemClickListener(int size, int position, Object o) {
        if (size == 0) {
            publicRecycleviewButtom2.setText(LanguageUtil.getString(activity, R.string.app_delete));
            publicRecycleviewButtom2.setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
        } else {
            publicRecycleviewButtom2.setText(LanguageUtil.getString(activity, R.string.app_delete) + "(" + (size) + ")");
            publicRecycleviewButtom2.setTextColor(ContextCompat.getColor(activity, R.color.red));
        }
        int S = productType == Constant.COMIC_CONSTANT ? baseComicList.size() : audioList.size();
        if (size == S) {
            publicRecycleviewButtom1.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
        } else {
            publicRecycleviewButtom1.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProcess(Audio audio) {
        Audio audio1;
        for (int i = 0; i < audioList.size(); i++) {
            if (audio.equals(audio1 = audioList.get(i))) {
                audio1.down_chapters = audio.down_chapters;
                if (audio1.down_chapters == 0) {
                    audioList.remove(i);
                }
                if (audioList.isEmpty()) {
                    mFragmentBookshelfNoresult.setVisibility(View.VISIBLE);
                }
                downMangerAudioAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 用于刷新数据
     *
     * @param baseComic
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshDownList(Comic baseComic) {
        if (!baseComicList.isEmpty()) {
            int index = baseComicList.indexOf(baseComic);
            if (index != -1) {
                Comic comicLocal = baseComicList.get(index);
                comicLocal.setCurrent_chapter_id(baseComic.getCurrent_chapter_id());
                comicLocal.setCurrent_display_order(baseComic.getCurrent_display_order());
                comicLocal.setDown_chapters(baseComic.getDown_chapters());
                comicLocal.setTotal_chapters(baseComic.getTotal_chapters());
                if(comicLocal.down_chapters==0){
                    baseComicList.remove(comicLocal);
                }
            } else {
                baseComicList.add(baseComic);
            }
        } else {
            baseComicList.add(baseComic);
        }
        if (baseComicList.isEmpty()) {
            mFragmentBookshelfNoresult.setVisibility(View.VISIBLE);
        }else {
            mFragmentBookshelfNoresult.setVisibility(View.GONE);
        }
        downMangerAdapter.notifyDataSetChanged();
    }

    /**
     * 设置是否删除
     * @param isDelete
     */
    private void setIsDelete(boolean isDelete) {
        if (!isDelete) {
            Animation footAnim = AnimationUtils.loadAnimation(activity, R.anim.menu_out);
            bottomLayout.startAnimation(footAnim);
            bottomLayout.setVisibility(View.GONE);
            setDeleteBtn(false, 0);
            public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, R.string.app_edit));
        } else {
            Animation footAnim = AnimationUtils.loadAnimation(activity, R.anim.menu_in);
            bottomLayout.startAnimation(footAnim);
            bottomLayout.setVisibility(View.VISIBLE);
            public_sns_topbar_right_tv.setText(LanguageUtil.getString(activity, R.string.app_cancle));
        }
    }

    /**
     * 设置删除按钮文案
     * @param isChoose
     * @param size
     */
    private void setDeleteBtn(boolean isChoose, int size) {
        if (isChoose) {
            publicRecycleviewButtom1.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
            publicRecycleviewButtom2.setText(LanguageUtil.getString(activity, R.string.app_delete) + "(" + (size) + ")");
            publicRecycleviewButtom2.setTextColor(ContextCompat.getColor(activity, R.color.maincolor));
        } else {
            publicRecycleviewButtom1.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
            publicRecycleviewButtom2.setText(LanguageUtil.getString(activity, R.string.app_delete));
            publicRecycleviewButtom2.setTextColor(ContextCompat.getColor(activity, R.color.red));
        }
    }
}
