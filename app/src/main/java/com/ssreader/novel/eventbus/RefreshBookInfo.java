package com.ssreader.novel.eventbus;

import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.Book;

public class RefreshBookInfo {

    public boolean isSave;
    public Book book;
    public Audio audio;

    public RefreshBookInfo(Book book, boolean isSave) {
        this.isSave = isSave;
        this.book = book;
    }

    public RefreshBookInfo(Audio audio, boolean isSave) {
        this.isSave = isSave;
        this.audio = audio;
    }
}
