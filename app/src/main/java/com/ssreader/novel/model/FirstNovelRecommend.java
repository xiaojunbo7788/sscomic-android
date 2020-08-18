package com.ssreader.novel.model;

import java.util.List;

public class FirstNovelRecommend {

    private List<Book> book;
    private List<Comic> comic;
    private List<Audio> audio;

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    public List<Comic> getComic() {
        return comic;
    }

    public void setComic(List<Comic> comic) {
        this.comic = comic;
    }

    public List<Audio> getAudio() {
        return audio;
    }

    public void setAudio(List<Audio> audio) {
        this.audio = audio;
    }
}
