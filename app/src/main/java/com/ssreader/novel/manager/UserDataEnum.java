package com.ssreader.novel.manager;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class UserDataEnum {



    /**
     * 线路
     * */
    @IntDef({UserLineData.UserLineNormal,
            UserLineData.UserLineVip})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserLineData {
        //普通
        int UserLineNormal = 0;
        //vip
        int UserLineVip = 1;
    }

    /**
     * 清晰度
     * */
    @IntDef({UserClearData.UserClearNormal,
            UserClearData.UserClearVip})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserClearData {
        //普通
        int UserClearNormal = 0;
        //vip
        int UserClearVip = 1;
    }
}
