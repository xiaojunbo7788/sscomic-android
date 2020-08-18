package com.ssreader.novel.eventbus;

import com.ssreader.novel.model.Audio;

import java.util.List;

public class RefreshAudioShelf {

    public List<Audio> audioList;
    public Audio audio;
    public boolean flag;

    public int ADD = -1;

    public RefreshAudioShelf(List<Audio> Books) {
        this.audioList = Books;
    }

    /**
     * 添加或者删除
     * @param baseComic
     * @param ADD
     */
    public RefreshAudioShelf(Audio baseComic, int ADD) {
        this.audio = baseComic;
        this.ADD = ADD;
    }

    public RefreshAudioShelf(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "RefreshComicShelf{" +
                "baseComics=" + audioList +
                ", baseComic=" + audio +
                ", flag=" + flag +
                ", ADD=" + ADD +
                '}';
    }
}
