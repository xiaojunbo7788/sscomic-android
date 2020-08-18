package com.ssreader.novel.model;

import java.util.List;

public class ShelfBookBeen {

    public List<Book> list;
    public List<Announce> announcement;
    public List<BaseBookComic> recommend_list;

    @Override
    public String toString() {
        return "ShelfBookBeen{" +
                "announce=" + announcement +
                ", recommend=" + recommend_list +
                '}';
    }
}
