package com.ssreader.novel.eventbus;

public class BookCatalogMarkRefresh {

    private boolean isFinish;

    public BookCatalogMarkRefresh(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
