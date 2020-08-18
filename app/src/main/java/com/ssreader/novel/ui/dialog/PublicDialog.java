package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.ScreenSizeUtils;

public class PublicDialog {

    public static void publicDialogVoid(Activity activity, String tips, String viewTitle,
                                        String cancelTitle, String confirmTitle, OnPublicListener onPublicListener) {
        final Dialog dialog = new Dialog(activity, R.style.NormalDialogStyle);
        View view = View.inflate(activity, R.layout.dialog_add_shelf, null);
        view.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5), Color.WHITE));
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        TextView title = view.findViewById(R.id.title);
        TextView tip = view.findViewById(R.id.public_dialog_tips);
        View dialog_add_shelf_finish = view.findViewById(R.id.dialog_add_shelf_finish);
        if (!TextUtils.isEmpty(tips)) {
            tip.setText(tips);
        }
        title.setText(viewTitle);
        confirm.setText(confirmTitle);
        cancel.setText(cancelTitle);
        view.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5), Color.WHITE));
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        //设置对话框的大小a
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(activity).getScreenWidth() * 0.82f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPublicListener != null) {
                    onPublicListener.onClickConfirm(false);
                }
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPublicListener != null) {
                    onPublicListener.onClickConfirm(true);
                }
                dialog.dismiss();

            }
        });
        dialog_add_shelf_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface OnPublicListener {

        void onClickConfirm(boolean isConfirm);
    }
}
