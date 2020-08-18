package com.ssreader.novel.model;

public class BaseReadHistory extends BaseAd{
    public String log_id;
    public long chapter_id;
    public long audio_id;
    public String chapter_title;
    public int chapter_index;
    public String description;
    public long comic_id;//": 1,  // 漫画ID
    public String name;//": "女神的诱惑",    // 漫画名称
    public String vertical_cover;//": "http://ssreader.oss-cn-beijing.aliyuncs.com/comic/cover/ccwmiw4peep35.jpg?x-oss-process=image%2Fresize%2Cw_300%2Ch_400%2Cm_lfit",   // 竖图
    public String total_chapters;//": 128,  // 总章节数
    public String last_chapter_time;//": "更新于3个月前", // 更新时间
    public long book_id;
    public String cover;
   // public String total_chapters;

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public long getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(long audio_id) {
        this.audio_id = audio_id;
    }

    public String getTotal_chapters() {
        return total_chapters;
    }

    public void setTotal_chapters(String total_chapters) {
        this.total_chapters = total_chapters;
    }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public long getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public int getChapter_index() {
        return chapter_index;
    }

    public void setChapter_index(int chapter_index) {
        this.chapter_index = chapter_index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getComic_id() {
        return comic_id;
    }

    public void setComic_id(long comic_id) {
        this.comic_id = comic_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVertical_cover() {
        return vertical_cover;
    }

    public void setVertical_cover(String vertical_cover) {
        this.vertical_cover = vertical_cover;
    }


    public String getLast_chapter_time() {
        return last_chapter_time;
    }

    public void setLast_chapter_time(String last_chapter_time) {
        this.last_chapter_time = last_chapter_time;
    }

    public long getbook_id() {
        return book_id;
    }

    public void setbook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

}
