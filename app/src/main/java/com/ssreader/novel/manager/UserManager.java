package com.ssreader.novel.manager;

import com.ssreader.novel.ui.read.util.SharedPreUtils;

public class UserManager {

    private static final String LINE_KEY = "line_key";
    private static final String Clear_KEY = "clear_key";

    public static UserManager instance;

    private int isVip;
    private @UserDataEnum.UserLineData int lineData;
    private @UserDataEnum.UserClearData int clearData;


    public static UserManager getInstance() {
        synchronized (UserManager.class) {
            if (instance == null) {
                instance = new UserManager();
            }
        }
        return instance;
    }

    private UserManager() {
        lineData = SharedPreUtils.getInstance().getInt(LINE_KEY,0);
        clearData = SharedPreUtils.getInstance().getInt(Clear_KEY,0);
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getLineData() {
        if (isVip == 0) {
            return 0;
        }
        return lineData;
    }

    public void setLineData(int lineData) {
        SharedPreUtils.getInstance().putInt(LINE_KEY,lineData);
        this.lineData = lineData;
    }

    public int getClearData() {
        if (isVip == 0) {
            return 0;
        }
        return clearData;
    }

    public void setClearData(int clearData) {
        SharedPreUtils.getInstance().putInt(Clear_KEY,clearData);
        this.clearData = clearData;
    }
}
