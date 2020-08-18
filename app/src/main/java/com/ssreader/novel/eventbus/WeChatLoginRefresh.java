package com.ssreader.novel.eventbus;

public class WeChatLoginRefresh {

    public boolean isLogin, isBind;

    public String code;

    public WeChatLoginRefresh(boolean isLogin, String code, boolean isBind) {
        this.isLogin = isLogin;
        this.code = code;
        this.isBind = isBind;
    }
}
