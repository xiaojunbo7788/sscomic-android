package com.ssreader.novel.utils;

import android.app.Activity;
import com.ssreader.novel.ui.dialog.BottomMenuDialog;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;

public class AppCommon {

    /**
    * 选择弹窗
    * */
    public static void showSelectedDialog(Activity activity, String[]sels,SCOnItemClickListener scOnItemClickListener) {
        new BottomMenuDialog().showBottomMenuDialog(activity, sels, scOnItemClickListener);
    }

}
