package com.ssreader.novel.eventbus;

/**
 * 用于刷新书架的数据
 */
public class RefreshShelf {

    public int productType;
    public RefreshBookSelf refreshBookSelf;
    public RefreshComicShelf refreshComicShelf;
    public RefreshAudioShelf refreshAudioShelf;

    public RefreshShelf(int productType, RefreshBookSelf refreshBookSelf) {
        this.productType = productType;
        this.refreshBookSelf = refreshBookSelf;
    }

    public RefreshShelf(int productType, RefreshComicShelf refreshComicShelf) {
        this.productType = productType;
        this.refreshComicShelf = refreshComicShelf;
    }

    public RefreshShelf(int productType, RefreshAudioShelf refreshAudioShelf) {
        this.productType = productType;
        this.refreshAudioShelf = refreshAudioShelf;
    }

    public RefreshShelf(int productType, RefreshBookSelf refreshBookSelf, RefreshComicShelf refreshComicShelf, RefreshAudioShelf refreshAudioShelf) {
        this.productType = productType;
        this.refreshBookSelf = refreshBookSelf;
        this.refreshComicShelf = refreshComicShelf;
        this.refreshAudioShelf = refreshAudioShelf;
    }

    public int getProductType() {
        return productType;
    }

    public RefreshBookSelf getRefreshBookSelf() {
        return refreshBookSelf;
    }

    public void setRefreshBookSelf(RefreshBookSelf refreshBookSelf) {
        this.refreshBookSelf = refreshBookSelf;
    }

    public RefreshComicShelf getRefreshComicShelf() {
        return refreshComicShelf;
    }

    public void setRefreshComicShelf(RefreshComicShelf refreshComicShelf) {
        this.refreshComicShelf = refreshComicShelf;
    }

    public RefreshAudioShelf getRefreshAudioShelf() {
        return refreshAudioShelf;
    }

    public void setRefreshAudioShelf(RefreshAudioShelf refreshAudioShelf) {
        this.refreshAudioShelf = refreshAudioShelf;
    }

    @Override
    public String toString() {
        return "RefreshShelf{" +
                "refreshBookSelf=" + refreshBookSelf +
                ", refreshComicShelf=" + refreshComicShelf +
                '}';
    }
}
