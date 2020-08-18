package com.ssreader.novel.eventbus;

public class ChapterBuyRefresh {

    public int productType;
    public int num;
    public long[] ids;
    public boolean IsRead;
    public long chapterId;

    public ChapterBuyRefresh(int productType, int num, long[] ids) {
        this.productType = productType;
        this.num = num;
        this.ids = ids;
    }

    public ChapterBuyRefresh(int productType, boolean isRead, long chapterId) {
        this.productType = productType;
        IsRead = isRead;
        this.chapterId = chapterId;
    }
}
