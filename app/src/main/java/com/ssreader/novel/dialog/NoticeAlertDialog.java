package com.ssreader.novel.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ssreader.novel.R;

public class NoticeAlertDialog extends Dialog {

    private Activity activity;
    public NoticeAlertDialog(Context context) {
        super(context);
    }

    public NoticeAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.CENTER;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public static class Builder {

        private View layout;
        private String content;
        private NoticeAlertDialog dialog;

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }


        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new NoticeAlertDialog(context, R.style.MyDialog);
            dialog.setCancelable(false);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_notice_alert, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        public NoticeAlertDialog create() {
            TextView contentView = layout.findViewById(R.id.notice_alert_content);
            if (this.content != null && this.content.length() > 0) {
                contentView.setText(this.content);
            }
            Button button = layout.findViewById(R.id.commit_btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.setContentView(layout);
            dialog.setCancelable(false);
            return  dialog;
        }
    }


}
