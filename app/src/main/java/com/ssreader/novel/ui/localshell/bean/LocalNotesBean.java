package com.ssreader.novel.ui.localshell.bean;

import androidx.annotation.Nullable;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

@Entity
public class LocalNotesBean implements Serializable {

    //id字段是必要的字段，不可忽略，不可用修饰符修饰
    @Id(assignable = true)
    public long book_id;
    public String frombookTitle;
    public String notesTime;
    public String notesContent;
    public String notesTitle;
    public int day_id;
    @Transient
    public boolean isUpdate;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public LocalNotesBean() {
    }

    @Override
    public int hashCode() {
        return (int) book_id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return book_id==((LocalNotesBean)obj).book_id;
    }

    public LocalNotesBean(String frombookTitle, String notesTime, String notesContent, String notesTitle) {
        this.frombookTitle = frombookTitle;
        this.notesTime = notesTime;
        this.notesContent = notesContent;
        this.notesTitle = notesTitle;
    }
    public void setUpdate(String frombookTitle, String notesTime, String notesContent, String notesTitle) {
        this.frombookTitle = frombookTitle;
        this.notesTime = notesTime;
        this.notesContent = notesContent;
        this.notesTitle = notesTitle;
    }
    public LocalNotesBean(long book_id, String frombookTitle, String notesTime, String notesContent, String notesTitle) {
        this.book_id = book_id;
        this.frombookTitle = frombookTitle;
        this.notesTime = notesTime;
        this.notesContent = notesContent;
        this.notesTitle = notesTitle;
    }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getFrombookTitle() {
        return frombookTitle;
    }

    public void setFrombookTitle(String frombookTitle) {
        this.frombookTitle = frombookTitle;
    }

    public String getNotesTime() {
        return notesTime;
    }

    public void setNotesTime(String notesTime) {
        this.notesTime = notesTime;
    }

    public String getNotesContent() {
        return notesContent;
    }

    public void setNotesContent(String notesContent) {
        this.notesContent = notesContent;
    }

    public String getNotesTitle() {
        return notesTitle;
    }

    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }
}
