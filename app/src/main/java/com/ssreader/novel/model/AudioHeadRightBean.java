package com.ssreader.novel.model;

public class AudioHeadRightBean {

    public String imaName;
    public int img;
    public String markDate;

    public AudioHeadRightBean(String imaName, int img, String markDate) {
        this.imaName = imaName;
        this.img = img;
        this.markDate = markDate;
    }

    public String getMarkDate() {
        return markDate;
    }

    public void setMarkDate(String markDate) {
        this.markDate = markDate;
    }

    public String getImaName() {
        return imaName;
    }

    public void setImaName(String imaName) {
        this.imaName = imaName;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
