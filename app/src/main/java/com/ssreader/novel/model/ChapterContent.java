package com.ssreader.novel.model;

/**
 * 章节内容
 */
public class ChapterContent {

    private String chapter_title;
    private String content, content2, content3, content4;
    private int is_preview;
    private long last_chapter;
    private long next_chapter;
    private long chapter_id;
    private long audio_id;
    private int words;
    private String update_time;

    public String getUpdate_time() {
        return update_time;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public String getContent4() {
        return content4;
    }

    public void setContent4(String content4) {
        this.content4 = content4;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public long getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(long audio_id) {
        this.audio_id = audio_id;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIs_preview() {
        return is_preview;
    }

    public void setIs_preview(int is_preview) {
        this.is_preview = is_preview;
    }

    public long getLast_chapter() {
        return last_chapter;
    }

    public void setLast_chapter(long last_chapter) {
        this.last_chapter = last_chapter;
    }

    public long getNext_chapter() {
        return next_chapter;
    }

    public void setNext_chapter(long next_chapter) {
        this.next_chapter = next_chapter;
    }

    public long getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "ChapterContent{" +
                "chapter_title='" + chapter_title + '\'' +
                ", is_preview='" + is_preview + '\'' +
                ", last_chapter='" + last_chapter + '\'' +
                ", next_chapter='" + next_chapter + '\'' +
                ", chapter_id='" + chapter_id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
