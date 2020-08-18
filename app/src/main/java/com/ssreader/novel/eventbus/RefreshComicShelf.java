package com.ssreader.novel.eventbus;

import com.ssreader.novel.model.Comic;

import java.util.List;

public class RefreshComicShelf {

    public List<Comic> baseComics;
    public Comic baseComic;
    public boolean flag;

    public int ADD = -1;

    public RefreshComicShelf(List<Comic> Books) {
        this.baseComics = Books;
    }

    /**
     * 添加或删除
     * @param baseComic
     * @param ADD
     */
    public RefreshComicShelf(Comic baseComic, int ADD) {
        this.baseComic = baseComic;
        this.ADD = ADD;
    }

    public RefreshComicShelf(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "RefreshComicShelf{" +
                "baseComics=" + baseComics +
                ", baseComic=" + baseComic +
                ", flag=" + flag +
                ", ADD=" + ADD +
                '}';
    }
}
