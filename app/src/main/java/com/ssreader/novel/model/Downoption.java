package com.ssreader.novel.model;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

@Entity
public class Downoption implements Serializable, Comparable<Downoption> {

    @Id
    public long id;
    public String label;
    @Transient
    public String tag;//": "免费", //标签 值为空时不展示
    public long s_chapter;//'": "50238", //开始章节id 调用下载接口时传参使用
    public int down_num;//": 18 //下载章节数 调用下载接口时传参使用
    public int down_cunrrent_num;//当前以及下载的章节

    @Index
    public String file_name;//唯一ID

    public boolean isdown;//是否下载

    //关联的书籍
    public long book_id;
    public String cover;
    public String bookname;
    public String description;
    public String downoption_size;
    public long downoption_date;
    public int start_order;
    public int end_order;
    public boolean showHead;

    public long downTime;

    @Transient
    public boolean isDowning;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            return (file_name.equals(((Downoption) obj).file_name));
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return file_name.hashCode();
    }

    @Override
    public int compareTo(Downoption downoption) {
        int flag1 = (int) (this.book_id - downoption.book_id);
        int flag2 = this.start_order - downoption.start_order;
        int flag3 = this.end_order - downoption.end_order;
        int flag4 = (int) (downoption.downoption_date-this.downoption_date);
        return (flag1 != 0) ? flag4 : ((flag2 != 0) ? flag2 : flag3);
    }

    public Downoption() {
    }

    public Downoption(long id, String label, String tag, long s_chapter, int down_num, int down_cunrrent_num, String file_name, boolean isdown, long book_id, String cover, String bookname, String description, String downoption_size, long downoption_date, int start_order, int end_order, boolean showHead) {
        this.id = id;
        this.label = label;
        this.tag = tag;
        this.s_chapter = s_chapter;
        this.down_num = down_num;
        this.down_cunrrent_num = down_cunrrent_num;
        this.file_name = file_name;
        this.isdown = isdown;
        this.book_id = book_id;
        this.cover = cover;
        this.bookname = bookname;
        this.description = description;
        this.downoption_size = downoption_size;
        this.downoption_date = downoption_date;
        this.start_order = start_order;
        this.end_order = end_order;
        this.showHead = showHead;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getS_chapter() {
        return s_chapter;
    }

    public void setS_chapter(long s_chapter) {
        this.s_chapter = s_chapter;
    }

    public int getDown_num() {
        return down_num;
    }

    public void setDown_num(int down_num) {
        this.down_num = down_num;
    }

    public int getDown_cunrrent_num() {
        return down_cunrrent_num;
    }

    public void setDown_cunrrent_num(int down_cunrrent_num) {
        this.down_cunrrent_num = down_cunrrent_num;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public boolean isIsdown() {
        return isdown;
    }

    public void setIsdown(boolean isdown) {
        this.isdown = isdown;
    }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownoption_size() {
        return downoption_size;
    }

    public void setDownoption_size(String downoption_size) {
        this.downoption_size = downoption_size;
    }

    public long getDownoption_date() {
        return downoption_date;
    }

    public void setDownoption_date(long downoption_date) {
        this.downoption_date = downoption_date;
    }

    public int getStart_order() {
        return start_order;
    }

    public void setStart_order(int start_order) {
        this.start_order = start_order;
    }

    public int getEnd_order() {
        return end_order;
    }

    public void setEnd_order(int end_order) {
        this.end_order = end_order;
    }

    public boolean isShowHead() {
        return showHead;
    }

    public void setShowHead(boolean showHead) {
        this.showHead = showHead;
    }
}
