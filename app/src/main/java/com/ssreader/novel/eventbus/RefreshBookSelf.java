package com.ssreader.novel.eventbus;

import com.ssreader.novel.model.Book;

import java.util.List;

public class RefreshBookSelf {

    public List<Book> Books;
    public boolean flag;
    public Book Book;

    public int ADD = -1;

    public RefreshBookSelf(List<Book> Books) {
        this.Books = Books;
    }

    /**
     * 添加或者删除
     * @param Book
     * @param ADD
     */
    public RefreshBookSelf(Book Book, int ADD) {
        this.Book = Book;
        this.ADD = ADD;
    }

    public RefreshBookSelf(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "RefreshBookSelf{" +
                "Books=" + Books +
                ", flag=" + flag +
                ", Book=" + Book +
                ", ADD=" + ADD +
                '}';
    }

}
