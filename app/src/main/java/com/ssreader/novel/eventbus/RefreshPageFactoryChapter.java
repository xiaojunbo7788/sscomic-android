package com.ssreader.novel.eventbus;

import android.app.Activity;

import com.ssreader.novel.model.BookChapter;

import java.util.List;

public class RefreshPageFactoryChapter {

    public BookChapter bookChapter;
    public Activity activity;
    public List<BookChapter> bookChapterList;

    public RefreshPageFactoryChapter(BookChapter bookChapter, Activity activity) {
        this.bookChapter = bookChapter;
        this.activity = activity;
    }

    /**
     * 章节目录使用
     * @param bookChapterList
     * @param bookChapter
     */
    public RefreshPageFactoryChapter(List<BookChapter> bookChapterList, BookChapter bookChapter) {
        this.bookChapter = bookChapter;
        this.bookChapterList = bookChapterList;
    }
}
