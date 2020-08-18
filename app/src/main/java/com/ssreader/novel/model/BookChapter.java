package com.ssreader.novel.model;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * 章节目录bean
 */

@Entity
public class BookChapter implements Serializable {

    @Id(assignable =true)
    public long chapter_id;
    public String chapter_title;
    public int is_vip;
    public int display_order;
    public int is_preview;
    public int is_read;  // 0--> 没有阅读  1--> 已阅读
    public String update_time;
    public long next_chapter;
    public long last_chapter;
    //存数据库所需字段
    public long book_id;
    public long chapteritem_begin;
    public String chapter_path, chapter_text;
    public int PagePos;
    // 作者的话
    public String author_note, comment_num, ticket_num, reward_num;

    public BookChapter() {

    }

    public boolean equals(Object object) {
        if (object instanceof BookChapter) {
            BookChapter p = (BookChapter) object;
            return chapter_id==(p.chapter_id);
        }
        return false;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public int hashCode() {
        return (int)chapter_id;
    }

    public long getChapteritem_begin() {
        return chapteritem_begin;
    }

    public void setChapteritem_begin(long chapteritem_begin) {
        this.chapteritem_begin = chapteritem_begin;
    }

    public BookChapter(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter_path() {
        return chapter_path;
    }

    public long getNext_chapter() {
        return next_chapter;
    }

    public void setNext_chapter(long next_chapter) {
        this.next_chapter = next_chapter;
    }

    public long getLast_chapter() {
        return last_chapter;
    }

    public void setLast_chapter(long last_chapter) {
        this.last_chapter = last_chapter;
    }

    public void setChapter_path(String chapter_path) {
        this.chapter_path = chapter_path;
    }

    public long getbook_id() {
        return book_id;
    }

    public void setbook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getIs_preview() {
        return is_preview;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public long getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(long chapter_id) {
        this.chapter_id = chapter_id;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public void setIs_preview(int is_preview) {
        this.is_preview = is_preview;
    }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public int getDisplay_order() {
        return display_order;
    }

    @Override
    public String toString() {
        return "BookChapter{" +
                "chapter_id=" + chapter_id +
                ", chapter_title='" + chapter_title + '\'' +
                ", is_vip=" + is_vip +
                ", display_order=" + display_order +
                ", is_preview=" + is_preview +
                ", is_read=" + is_read +
                ", update_time='" + update_time + '\'' +
                ", next_chapter=" + next_chapter +
                ", last_chapter=" + last_chapter +
                ", book_id=" + book_id +
                ", chapteritem_begin=" + chapteritem_begin +
                ", chapter_path='" + chapter_path + '\'' +
                '}';
    }

    public int getPagePos() {
        return PagePos;
    }

    public void setPagePos(int pagePos) {
        PagePos = pagePos;
    }

    public String getAuthor_note() {
        return author_note;
    }

    public void setAuthor_note(String author_note) {
        this.author_note = author_note;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getTicket_num() {
        return ticket_num;
    }

    public void setTicket_num(String ticket_num) {
        this.ticket_num = ticket_num;
    }

    public String getReward_num() {
        return reward_num;
    }

    public void setReward_num(String reward_num) {
        this.reward_num = reward_num;
    }
}
