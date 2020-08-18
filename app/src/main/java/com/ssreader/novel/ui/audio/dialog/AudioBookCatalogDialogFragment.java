package com.ssreader.novel.ui.audio.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.util.ColorParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseDialogFragment;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.GetBookChapterList;
import com.ssreader.novel.ui.audio.adapter.AudioBookCataLogAdapter;
import com.ssreader.novel.ui.audio.fragment.AudioInfoCatalogFragment;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.RecycleViewMessageUtil;
import com.ssreader.novel.utils.LanguageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AgainTime;

/**
 * 用于播放时显示章节目录
 */
public class AudioBookCatalogDialogFragment extends BaseDialogFragment {

    @BindView(R.id.dialog_audio_catalog_layout)
    LinearLayout linearLayout;
    @BindView(R.id.dialog_audio_catalog_smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.dialog_audio_catalog_recyclerView)
    RecyclerView recyclerView;
    @BindViews({R.id.dialog_audio_catalog_chapter_tv, R.id.dialog_audio_catalog_order_tv})
    List<TextView> catalogTexts;
    @BindView(R.id.dialog_audio_catalog_order_image)
    ImageView catalogOrderImg;

    @BindView(R.id.dialog_audio_catalog_result_layout)
    RelativeLayout noResultLayout;

    // 正序or倒序
    private boolean isOrder = true;
    // 是否有声
    private boolean isSound;
    // 数据
    private Book book;
    private Audio audio;
    // 事件
    private OnAudioBookCatalogClickListener clickListener;

    private LinearLayoutManager linearLayoutManager;
    // 适配器
    private AudioBookCataLogAdapter audioBookCataLogAdapter;
    private List<Object> catalogList = new ArrayList<>();
    // 2 = 正序  1 = 倒序
    private int order_by;
    private long ClickTime;

    public AudioBookCatalogDialogFragment() {

    }

    public AudioBookCatalogDialogFragment(Activity activity, boolean isSound,
                Book book, Audio audio, OnAudioBookCatalogClickListener clickListener) {
        super(true);
        this.activity = activity;
        this.isSound = isSound;
        this.book = book;
        this.audio = audio;
        this.clickListener = clickListener;
    }

    @Override
    public int initContentView() {
        return R.layout.dialog_audio_catalog;
    }

    @Override
    public void initView() {
        linearLayoutManager = new LinearLayoutManager(activity);
        audioBookCataLogAdapter = new AudioBookCataLogAdapter(activity, catalogList);
        linearLayout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 10),
                ImageUtil.dp2px(activity, 10), 0, 0,
                ColorParser.parseCssColor("#f8f8f8")));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(audioBookCataLogAdapter);
        ClassicsHeader classicsHeader = new ClassicsHeader(activity);
        classicsHeader.setEnableLastTime(false);
        smartRefreshLayout.setRefreshHeader(classicsHeader);
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(activity));
        if (isSound) {
            smartRefreshLayout.setEnableLoadMore(false);
            smartRefreshLayout.setEnableRefresh(false);
            if (audio != null) {
                audioBookCataLogAdapter.currentListenerChapterId = audio.current_listen_chapter_id;
                setCatalogCount(audio.total_chapter);
            }
        } else {
            if (book != null) {
                audioBookCataLogAdapter.currentListenerChapterId = book.current_listen_chapter_id;
                setCatalogCount(book.total_chapter);
            }
        }
        initListener();
        noResultLayout.setVisibility(View.GONE);
        smartRefreshLayout.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.dialog_audio_catalog_order_layout, R.id.dialog_audio_close_layout})
    public void onCatalogClick(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.dialog_audio_close_layout:
                    dismissAllowingStateLoss();
                    break;
                case R.id.dialog_audio_catalog_order_layout:
                    if (!catalogList.isEmpty()) {
                        isOrder = !isOrder;
                        if (!isOrder) {
                            order_by = 2;
                            catalogTexts.get(1).setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoxu));
                            catalogOrderImg.setImageResource(R.mipmap.reverse_order);
                        } else {
                            order_by = 1;
                            catalogTexts.get(1).setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_zhengxu));
                            catalogOrderImg.setImageResource(R.mipmap.positive_order);
                        }
                        if (book != null) {
                            catalogList.clear();
                            getCatalog(0, 2, 1);
                        } else {
                            Collections.reverse(catalogList);
                            audioBookCataLogAdapter.notifyDataSetChanged();
                            setRecyclerViewLoad();
                        }
                    }
                    break;
            }
        }
    }

    private void initListener() {
        // 适配器点击
        audioBookCataLogAdapter.setOnCatalogClickListener(new AudioBookCataLogAdapter.OnCatalogClickListener() {
            @Override
            public void onBookChapter(int index, BookChapter bookChapter) {
                if (!isSound && book != null) {
                    audioBookCataLogAdapter.currentListenerChapterId = bookChapter.getChapter_id();
                    audioBookCataLogAdapter.notifyDataSetChanged();
                    if (clickListener != null) {
                        dismissAllowingStateLoss();
                        if (!isOrder) {
                            Collections.reverse(catalogList);
                        }
                        clickListener.onChapter(book.book_id, bookChapter.chapter_id, catalogList);
                    }
                }
            }

            @Override
            public void onAudioChapter(int index, AudioChapter audioChapter) {
                if (isSound && audio != null) {
                    audioBookCataLogAdapter.currentListenerChapterId = audioChapter.getChapter_id();
                    audioBookCataLogAdapter.notifyDataSetChanged();
                    if (clickListener != null) {
                        dismissAllowingStateLoss();
                        if (!isOrder) {
                            Collections.reverse(catalogList);
                        }
                        clickListener.onChapter(audio.audio_id, audioChapter.getChapter_id(), catalogList);
                    }
                }
            }
        });
        // 加载监听
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                BookChapter bookChapter = (BookChapter) catalogList.get(catalogList.size() - 1);
                if (isOrder) {
                    // 正序
                    if (bookChapter.next_chapter == 0) {
                        refreshLayout.finishLoadMore();
                        return;
                    }
                    getCatalog(bookChapter.next_chapter, 1, 1);
                } else {
                    if (bookChapter.last_chapter == 0) {
                        refreshLayout.finishLoadMore();
                        return;
                    }
                    getCatalog(bookChapter.last_chapter, 2, 1);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                BookChapter bookChapter = (BookChapter) catalogList.get(0);
                if (isOrder) {
                    // 正序
                    if (bookChapter.last_chapter == 0) {
                        refreshLayout.finishRefresh();
                        return;
                    }
                    getCatalog(bookChapter.last_chapter, 1, 2);
                } else {
                    if (bookChapter.next_chapter == 0) {
                        refreshLayout.finishRefresh();
                        return;
                    }
                    getCatalog(bookChapter.next_chapter, 2, 2);
                }
            }
        });
    }

    /**
     * 获取内容
     */
    private void getCatalog(long chapter_id, int order_byy, int scroll_type) {

        if (!catalogList.isEmpty()) {
            if (scroll_type == 2) {
                if (order_by != 2) {
                    chapter_id = ((BookChapter) catalogList.get(0)).getLast_chapter();
                } else {
                    chapter_id = ((BookChapter) catalogList.get(0)).getNext_chapter();
                }
            } else {
                if (order_by != 2) {
                    chapter_id = ((BookChapter) catalogList.get(catalogList.size() - 1)).getNext_chapter();
                } else {
                    chapter_id = ((BookChapter) catalogList.get(catalogList.size() - 1)).getLast_chapter();
                }
            }
            if (chapter_id == 0) {
                if (scroll_type == 2) {
                    scRecyclerView.refreshComplete();
                } else if (scroll_type == 1) {
                    scRecyclerView.loadMoreComplete();
                }
                return;
            }
        } else {
            scroll_type = 0;
            if (order_by != 0) {
                chapter_id = 0;
                smartRefreshLayout.setEnableRefresh(false);
            }
        }
        int finalScroll_type = scroll_type;
        book.getBookChapterList(activity, chapter_id, scroll_type, order_by, new GetBookChapterList() {
            @Override
            public void getBookChapterList(List<BookChapter> bookChapterlist) {

                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                if (!bookChapterlist.isEmpty()) {
                    if (finalScroll_type == 1 || finalScroll_type == 0) {
                        catalogList.addAll(bookChapterlist);
                        audioBookCataLogAdapter.notifyDataSetChanged();
                    } else if (finalScroll_type == 2) {
                        catalogList.addAll(0, bookChapterlist);
                        audioBookCataLogAdapter.notifyDataSetChanged();
                        RecycleViewMessageUtil.MoveToPosition(linearLayoutManager, recyclerView, bookChapterlist.size());
                    }
                }
                setRecyclerViewLoad();
            }
        });
    }

    @Override
    public void initData() {
        if (isSound && audio != null) {
            // 有声
            audio.getAudioChapterList(activity, new AudioInfoCatalogFragment.GetAudioChapterList() {
                @Override
                public void getAudioChapterList(List<AudioChapter> audioChapters) {
                    if (audioChapters != null && audioChapters.size() > 4) {
                        setBgLayoutHeight();
                    }
                    List<Object> objects = new ArrayList<>();
                    objects.addAll(audioChapters);
                    setCatalogList(objects, true);
                }
            });
        } else if (!isSound && book != null) {
            book.getBookChapterList(activity, book.current_listen_chapter_id, 0, 0, new GetBookChapterList() {
                @Override
                public void getBookChapterList(List<BookChapter> bookChapters) {
                    List<Object> objects = new ArrayList<>();
                    objects.addAll(bookChapters);
                    setCatalogList(objects, false);
                }
            });
        }
    }

    private void setCatalogList(List<Object> objectList, boolean isAudio) {
        if (!objectList.isEmpty()) {
            noResultLayout.setVisibility(View.GONE);
            smartRefreshLayout.setVisibility(View.VISIBLE);
            catalogList.clear();
            catalogList.addAll(objectList);
            if ((audio != null && audio.current_listen_chapter_id == 0) ||
                    (book != null && book.current_listen_chapter_id == 0)) {
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
            } else {
                for (int i = 0; i < catalogList.size(); i++) {
                    if (isAudio) {
                        AudioChapter audioChapter = (AudioChapter) catalogList.get(i);
                        if (audioChapter.getChapter_id() == audio.current_listen_chapter_id) {
                            linearLayoutManager.scrollToPositionWithOffset(i, 0);
                            break;
                        }
                    } else {
                        BookChapter bookChapter = (BookChapter) catalogList.get(i);
                        if (bookChapter.getChapter_id() == book.current_listen_chapter_id) {
                            linearLayoutManager.scrollToPositionWithOffset(i, 0);
                            break;
                        }
                    }
                }
            }
            linearLayoutManager.setStackFromEnd(true);
            audioBookCataLogAdapter.notifyDataSetChanged();
            // 设置是否需要下拉、上滑
            setRecyclerViewLoad();
        } else {
            smartRefreshLayout.setVisibility(View.GONE);
            noResultLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initInfo(String json) {

    }

    /**
     * 判断是否需要下拉、上滑
     */
    private void setRecyclerViewLoad() {
        if (isSound || catalogList.isEmpty()) {
            return;
        }
        if (catalogList.size() > 4) {
            setBgLayoutHeight();
        }
        BookChapter firstBookChapter = (BookChapter) catalogList.get(0);
        BookChapter LastBookChapter = (BookChapter) catalogList.get(catalogList.size() - 1);
        if (firstBookChapter.last_chapter == 0) {
            smartRefreshLayout.setEnableRefresh(false);
        } else {
            smartRefreshLayout.setEnableRefresh(true);
        }
        if (LastBookChapter.next_chapter == 0) {
            smartRefreshLayout.setEnableLoadMore(false);
        } else {
            smartRefreshLayout.setEnableLoadMore(true);
        }
    }

    /**
     * 设置多少章
     *
     * @param size
     */
    private void setCatalogCount(int size) {
        catalogTexts.get(0).setText(String.format(LanguageUtil.getString(activity, R.string.audio_info_chapter_num), size));
    }

    /**
     * 更改高度
     */
    private void setBgLayoutHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) smartRefreshLayout.getLayoutParams();
        params.height = ImageUtil.dp2px(activity, 200);
        smartRefreshLayout.setLayoutParams(params);
    }

    /**
     * 点击事件
     */
    public interface OnAudioBookCatalogClickListener {

        void onChapter(long id, long chapterId, List<Object> objects);
    }
}
