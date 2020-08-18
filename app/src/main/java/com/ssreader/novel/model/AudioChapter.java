package com.ssreader.novel.model;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

@Entity
public class AudioChapter implements Serializable {

    @Id(assignable = true)
    public long chapter_id;

    public long audio_id;

    public int is_read;  // 0--> 没有阅读  1--> 已阅读

    public long read_progress;  // 记录（毫秒）

    private String chapter_title, content;

    private String duration_time, play_num, update_time;

    private int duration_second, display_order;

    private int is_vip, can_read, is_preview, size;

    private long last_chapter, next_chapter;

    // 是否正在下载
    public int status; //0 未下载 1 下载中  -1 已下载  2下载失败
    // 是否被选中
    @Transient
    private boolean isChoose;

    public String path;

    public boolean equals(Object object) {
        if (object instanceof AudioChapter) {
            AudioChapter p = (AudioChapter) object;
            return chapter_id == (p.chapter_id);
        }
        return false;
    }

    public AudioChapter() {
    }

    public AudioChapter(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    @Override
    public int hashCode() {
        return (int) chapter_id;
    }

    public long getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(long audio_id) {
        this.audio_id = audio_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDuration_time() {
        return duration_time;
    }

    public void setDuration_time(String duration_time) {
        this.duration_time = duration_time;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration_second() {
        return duration_second;
    }

    public void setDuration_second(int duration_second) {
        this.duration_second = duration_second;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getCan_read() {
        return can_read;
    }

    public void setCan_read(int can_read) {
        this.can_read = can_read;
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

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public long getRead_progress() {
        return read_progress;
    }

    public void setRead_progress(long read_progress) {
        this.read_progress = read_progress;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public boolean getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}
