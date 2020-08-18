package com.ssreader.novel.ui.localshell.bean;

import java.io.Serializable;
import java.util.List;

public class LineBreakLayoutBeen implements Serializable {
    /**
     * title : 古装
     * description : 古装
     * color : #33a3dc
     */
    public String title;
    public String description;
    public String color;
    public String tag_id;
    public boolean ischose;

    public LineBreakLayoutBeen(String title) {
        this.title = title;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public boolean isIschose() {
        return ischose;
    }

    public void setIschose(boolean ischose) {
        this.ischose = ischose;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public class LineBreakBean{
        public List<LineBreakLayoutBeen> list;

        public List<LineBreakLayoutBeen> getList() {
            return list;
        }

        public void setList(List<LineBreakLayoutBeen> list) {
            this.list = list;
        }
    }
}
