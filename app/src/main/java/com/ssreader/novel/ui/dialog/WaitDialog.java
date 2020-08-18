package com.ssreader.novel.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.MyShape;

public class WaitDialog extends Dialog {

    private Dialog waitDialog = null;
    private TextView tv;
    private View dialog_httping_layout;

    public WaitDialog(Context context, int flag) {
        super(context);
        if (waitDialog == null) {
            waitDialog = new Dialog(context, R.style.progress_dialog);
        }
        waitDialog.setContentView(R.layout.dialog_httping);
        waitDialog.setCancelable(true);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = waitDialog.findViewById(R.id.id_tv_loadingmsg);
        dialog_httping_layout = waitDialog.findViewById(R.id.dialog_httping_layout);
        dialog_httping_layout.setBackground(MyShape.setMyshape(30, ContextCompat.getColor(context, R.color.black1)));
        if (flag == 0) {
            tv.setVisibility(View.VISIBLE);
        }
        AVLoadingIndicatorView avLoadingIndicatorView = waitDialog.findViewById(R.id.dialog_httping_AVLoadingIndicatorView);
        avLoadingIndicatorView.setIndicator(new BallSpinFadeLoaderIndicator());
    }

    public WaitDialog(Context context, boolean flag) {
        super(context);
        if (waitDialog == null) {
            waitDialog = new Dialog(context, R.style.progress_dialog);
        }
        waitDialog.setContentView(R.layout.dialog_httping);

        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = waitDialog.findViewById(R.id.id_tv_loadingmsg);
        waitDialog.setCancelable(flag);
    }

    public void setMessage(String message) {
        if (tv == null) {
            tv = waitDialog.findViewById(R.id.id_tv_loadingmsg);
        }
        tv.setText(message);
    }

    public void showDailog() {
        try {
            waitDialog.show();
        } catch (Exception e) {
        }

    }

    public void dismissDialog() {
        if (null != waitDialog && waitDialog.isShowing()) {
            try {//如果该对话框依附的Activity已经消失 调用dismiss(); 会参数异常
                waitDialog.dismiss();

            } catch (Exception E) {
            }
        }
        waitDialog = null;
    }

    public WaitDialog ShowDialog(boolean isShow) {
        if (isShow) {
            this.showDailog();
        } else {
            if (waitDialog != null) {
                this.dismissDialog();
            }
        }
        return this;
    }

    public void setCancleable(boolean enable) {
        waitDialog.setCancelable(enable);
    }

}
