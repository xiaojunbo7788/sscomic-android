package com.ssreader.novel.eventbus;

/**
 * 用于刷新底部选择器
 */
public class ToStore {
    public int PRODUCT;
    public boolean SHELF_DELETE_OPEN;
    public boolean oneScroll, IsOneScroll;
    public ToStore(int PRODUCT) {
        this.PRODUCT = PRODUCT;
    }
    public ToStore(boolean oneScroll, boolean IsOneScroll) {
        this.oneScroll = oneScroll;
        this.IsOneScroll = IsOneScroll;
    }
    public ToStore(int PRODUCT, boolean SHELF_DELETE_OPEN) {
        this.PRODUCT = PRODUCT;
        this.SHELF_DELETE_OPEN = SHELF_DELETE_OPEN;
    }
}
