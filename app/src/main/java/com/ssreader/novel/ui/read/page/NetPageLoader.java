package com.ssreader.novel.ui.read.page;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshShelfCurrent;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.ui.dialog.WaitDialogUtils;
import com.ssreader.novel.ui.read.ReadActivity;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络页面加载器
 */
public class NetPageLoader extends PageLoader {

    public NetPageLoader(ReadActivity activity, PageView pageView, Book collBook) {
        super(activity, pageView, collBook);
    }

    private List<BookChapter> convertTxtChapter(List<BookChapter> bookChapters) {
        return bookChapters;
    }

    @Override
    public void openChapter(BookChapter bookChapter) {
        this.bookChapter = bookChapter;
        mChapterList = ChapterManager.getInstance().mChapterList;
        skipToChapter(bookChapter.chapter_id);
    }

    @Override
    public void refreshChapterList() {
        // 将 BookChapter 转换成当前可用的 Chapter
        isChapterListPrepare = true;
        // 目录加载完成，执行回调操作/。
        if (mPageChangeListener != null) {
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
        // 如果章节未打开
        if (!isChapterOpen()) {
            // 打开章节
            openChapter();
        }
        if (mCollBook.total_chapter > mChapterList.size()) {//章节不完整
            ChapterManager.downChapter(mContext, mCollBook.book_id, new ChapterManager.DownChapter() {
                @Override
                public void success(List<BookChapter> bookChapterList) {
                    if (!bookChapterList.isEmpty()) {
                        if (!ChapterManager.getInstance().IsGetChapter) {
                            mChapterList.clear();
                            mChapterList.addAll(bookChapterList);
                        }else {
                            ChapterManager.getInstance().setGetChapterInterface(new ChapterManager.GetChapterInterface() {
                                @Override
                                public void getChapterOver(BookChapter bookChapter) {
                                    ChapterManager.getInstance().setGetChapterInterface(null);
                                    mChapterList.clear();
                                    mChapterList.addAll(bookChapterList);
                                }
                            });
                        }
                    }
                }

                @Override
                public void fail() {

                }
            });
        }
        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onChapterChange(BookChapter bookChapter) {
                preLoadNextChapter();
            }

            @Override
            public void requestChapters(List<BookChapter> requestChapters) {

            }

            @Override
            public void onCategoryFinish(List<BookChapter> chapters) {

            }

            @Override
            public void onPageCountChange(int count) {

            }

            @Override
            public void onPageChange(long pos) {

            }
        });
    }

    @Override
    protected BufferedReader getChapterReader(BookChapter chapter) throws Exception {
        if (hasChapterData(chapter)) {
            Reader reader = new FileReader(chapter.getChapter_path());
            BufferedReader br = new BufferedReader(reader);
            return br;
        }
        return null;
    }

    @Override
    protected boolean hasChapterData(BookChapter chapter) {
        return FileManager.isExistLocalBookTxtPath(chapter);
    }

    // 装载上一章节的内容
    @Override
    boolean parsePrevChapter() {
        boolean isRight = super.parsePrevChapter();

        if (mStatus == STATUS_FINISH) {
            loadPrevChapter();
        } else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载当前章内容。
    @Override
    boolean parseCurChapter() {
        boolean isRight = super.parseCurChapter();
        if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载下一章节的内容
    @Override
    boolean parseNextChapter() {
        boolean isRight = super.parseNextChapter();

        if (mStatus == STATUS_FINISH) {
            loadNextChapter();
        } else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    /**
     * 加载当前页的前面两个章节
     */
    private void loadPrevChapter() {
        if (mPageChangeListener != null) {
            int end = mChapterList.indexOf(bookChapter);
            int begin = end - 1;
            if (begin < 0) {
                begin = 0;
            }

            requestChapters(begin, end);
        }
    }

    /**
     * 加载前一页，当前页，后一页。
     */
    private void loadCurrentChapter() {
        if (mPageChangeListener != null) {
            int begin = mChapterList.indexOf(bookChapter);
            ;
            int end = begin;

            // 是否当前不是最后一章
            if (end < mChapterList.size()) {
                end = end + 1;
                if (end >= mChapterList.size()) {
                    end = mChapterList.size() - 1;
                }
            }

            // 如果当前不是第一章
            if (begin != 0) {
                begin = begin - 1;
                if (begin < 0) {
                    begin = 0;
                }
            }

            requestChapters(begin, end);
        }
    }

    /**
     * 加载当前页的后两个章节
     */
    private void loadNextChapter() {
        if (mPageChangeListener != null) {

            // 提示加载后两章
            int begin = mChapterList.indexOf(bookChapter) + 1;
            int end = begin;

            // 判断是否大于最后一章
            if (begin >= mChapterList.size()) {
                // 如果下一章超出目录了，就没有必要加载了
                return;
            }

            if (end > mChapterList.size()) {
                end = mChapterList.size() - 1;
            }

            requestChapters(begin, end);
        }
    }

    private void requestChapters(int start, int end) {
        // 检验输入值
        if (start < 0) {
            start = 0;
        }

        if (end >= mChapterList.size()) {
            end = mChapterList.size() - 1;
        }


        List<BookChapter> chapters = new ArrayList<>();

        // 过滤，哪些数据已经加载了
        for (int i = start; i <= end; ++i) {
            BookChapter txtChapter = mChapterList.get(i);
            if (!hasChapterData(txtChapter)) {
                chapters.add(txtChapter);
            }
        }

        if (!chapters.isEmpty()) {
            mPageChangeListener.requestChapters(chapters);
        }
    }

    @Override
    public void saveRecord() {
        super.saveRecord();
        if (mCollBook != null && isChapterListPrepare && bookChapter != null && mCurPage != null) {
            // 表示当前CollBook已经阅读
            mCollBook.is_read = 1;
            mCollBook.isRecommend = false;
            mCollBook.current_chapter_id = bookChapter.chapter_id;
            // 更新本地信息
            ObjectBoxUtils.addData(mCollBook, Book.class);
            EventBus.getDefault().post(new RefreshShelfCurrent(Constant.BOOK_CONSTANT, mCollBook));
            saveCurrentChapterPos(false);
            ChapterManager.getInstance().mCurrentChapter = bookChapter;
        }
    }


}

