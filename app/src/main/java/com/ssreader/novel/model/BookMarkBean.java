package com.ssreader.novel.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 书签类
 */
@Entity
public class BookMarkBean implements Comparable<BookMarkBean> {

    @Id(assignable = true)
    private long mark_id;

    private long book_id, chapter_id, addTime;

    private String title, content;

    private int position;

    private int coordinate;

    public String getTitle() {
        return title;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getMark_id() {
        return mark_id;
    }

    public void setMark_id(long mark_id) {
        this.mark_id = mark_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "BookMarkBean{" +
                "mark_id=" + mark_id +
                ", book_id=" + book_id +
                ", chapter_id=" + chapter_id +
                ", content='" + content + '\'' +
                ", addTime=" + addTime +
                ", position=" + position +
                ", title=" + title + '\'' +
                ", coordinate=" + coordinate +
                '}';
    }

    @Override
    public int compareTo(BookMarkBean o) {
        int chapter = (int) (chapter_id - o.chapter_id);
        if (chapter != 0) {
            return chapter;
        } else {
            return (int) (o.addTime - addTime);
        }
    }
}
