package com.ssreader.novel.eventbus;

import com.ssreader.novel.model.BookChapter;

/**
 * 用于传递ai的数据
 */
public class AudioAiServiceRefresh {

    private BookChapter bookChapter;

    public AudioAiServiceRefresh(BookChapter bookChapter) {
        this.bookChapter = bookChapter;
    }

    public BookChapter getBookChapter() {
        return bookChapter;
    }

    public void setBookChapter(BookChapter bookChapter) {
        this.bookChapter = bookChapter;
    }
}
