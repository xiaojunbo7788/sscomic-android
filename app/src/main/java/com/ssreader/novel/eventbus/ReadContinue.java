package com.ssreader.novel.eventbus;

import com.ssreader.novel.model.Book;

public class ReadContinue {
    public Book book;

    public ReadContinue(Book book) {
        this.book = book;
    }
}
