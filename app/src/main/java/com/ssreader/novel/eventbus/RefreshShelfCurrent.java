package com.ssreader.novel.eventbus;

import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.Comic;

public class RefreshShelfCurrent {

    public Book book;
    public Comic comic;
    public Audio audio;
    public int productType;

    public RefreshShelfCurrent(int productType, Book book) {
        this.productType = productType;
        this.book = book;
    }

    public RefreshShelfCurrent(int productType, Comic comic) {
        this.productType = productType;
        this.comic = comic;
    }

    public RefreshShelfCurrent(int productType, Audio book) {
        this.productType = productType;
        this.audio = book;
    }
}
