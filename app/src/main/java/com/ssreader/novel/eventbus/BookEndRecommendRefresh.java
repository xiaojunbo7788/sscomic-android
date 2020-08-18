package com.ssreader.novel.eventbus;

public class BookEndRecommendRefresh {

    private boolean isFinish, isFinishBookInfo;

    public BookEndRecommendRefresh(boolean isFinish, boolean isFinishBookInfo) {
        this.isFinish = isFinish;
        this.isFinishBookInfo = isFinishBookInfo;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isFinishBookInfo() {
        return isFinishBookInfo;
    }

    public void setFinishBookInfo(boolean finishBookInfo) {
        isFinishBookInfo = finishBookInfo;
    }
}
