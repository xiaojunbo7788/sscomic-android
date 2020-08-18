package com.ssreader.novel.model;

public class ComicChapterEventbus {

    public int Flag;
    public ComicChapter comicChapter;

    public ComicChapterEventbus(int flag, ComicChapter comicChapter) {
        Flag = flag;
        this.comicChapter = comicChapter;
    }
}
