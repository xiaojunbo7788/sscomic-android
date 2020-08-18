package com.ssreader.novel.model;


import java.util.List;

public class RefreshComic {
    public List<Comic> baseComics;
    public Comic  baseComic;


    public boolean flag;

    public int  ADD=-1;
    public RefreshComic(List<Comic> baseBooks) {
        this.baseComics = baseBooks;
    }

    public RefreshComic(Comic baseComic, int  ADD) {//添加 或  取消收藏
        this.baseComic = baseComic;
        this.ADD = ADD;
    }

    public RefreshComic(boolean flag) {
        this.flag = flag;
    }
}
