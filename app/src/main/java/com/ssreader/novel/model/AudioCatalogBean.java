package com.ssreader.novel.model;

import java.util.List;

/**
 * 有声目录
 * @author admin
 * @version 2020/02/23
 */
public class AudioCatalogBean {

    private int total_chapters;

    private List<AudioChapter> chapter_list;

    public int getTotal_chapter() {
        return total_chapters;
    }

    public void setTotal_chapter(int total_chapter) {
        this.total_chapters = total_chapter;
    }

    public List<AudioChapter> getChapter_list() {
        return chapter_list;
    }

    public void setChapter_list(List<AudioChapter> chapter_list) {
        this.chapter_list = chapter_list;
    }

}
