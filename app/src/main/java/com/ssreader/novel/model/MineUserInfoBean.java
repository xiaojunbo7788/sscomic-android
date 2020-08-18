package com.ssreader.novel.model;

public class MineUserInfoBean {

    public String title;
    public String itemName;
    private String avatar;
    public boolean isDialog;
    public boolean subscript;
    public boolean isAvatar;
    public boolean isTopLine;

    public MineUserInfoBean(String title, String itemName, boolean isDialog, boolean subscript, boolean isAvatar, String avatar,boolean isTopLine) {
        this.title = title;
        this.itemName = itemName;
        this.isDialog = isDialog;
        this.subscript = subscript;
        this.isAvatar = isAvatar;
        this.avatar = avatar;
        this.isTopLine = isTopLine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isDialog() {
        return isDialog;
    }

    public void setDialog(boolean dialog) {
        isDialog = dialog;
    }

    public boolean isSubscript() {
        return subscript;
    }

    public void setSubscript(boolean subscript) {
        this.subscript = subscript;
    }

    public boolean isAvatar() {
        return isAvatar;
    }

    public void setAvatar(boolean avatar) {
        isAvatar = avatar;
    }
}
