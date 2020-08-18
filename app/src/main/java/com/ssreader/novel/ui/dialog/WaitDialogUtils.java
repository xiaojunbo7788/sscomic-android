package com.ssreader.novel.ui.dialog;

import android.app.Activity;

public class WaitDialogUtils {

    public static WaitDialog waitDialog;

    public static void showDialog(Activity activity) {
        dismissDialog();
        waitDialog = new WaitDialog(activity, 1).ShowDialog(true);
    }

    public static void dismissDialog() {
        if (waitDialog != null) {
            waitDialog.dismissDialog();
            waitDialog = null;
        }
    }
}
