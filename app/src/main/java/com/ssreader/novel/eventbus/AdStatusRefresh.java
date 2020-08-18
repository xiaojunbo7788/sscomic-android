package com.ssreader.novel.eventbus;

/**
 * 用于刷新广告状态
 * 关闭、开启
 */
public class AdStatusRefresh {

    public boolean StartAutoBuy;

    public AdStatusRefresh(boolean startAutoBuy) {
        StartAutoBuy = startAutoBuy;
    }

    public AdStatusRefresh() {
    }
}
