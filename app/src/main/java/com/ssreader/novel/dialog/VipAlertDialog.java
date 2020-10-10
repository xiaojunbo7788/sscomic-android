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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssreader.novel.R;

public class VipAlertDialog extends Dialog {

    private Activity activity;
    public VipAlertDialog(Context context) {
        super(context);
    }

    public VipAlertDialog(Context context, int theme) {
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

        private VipAlertDialogListener iVipAlertDialogListener;
        private View layout;
        private String content;
        private VipAlertDialog dialog;
        private boolean isHiddenRecharge = false;

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setVipAlertDialogListener(VipAlertDialogListener iVipAlertDialogListener) {
            this.iVipAlertDialogListener = iVipAlertDialogListener;
            return this;
        }

        public Builder setHiddenRecharge(boolean hiddenRecharge) {
            isHiddenRecharge = hiddenRecharge;
            return this;
        }

        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new VipAlertDialog(context, R.style.MyDialog);
            dialog.setCancelable(false);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_vip_alert, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        public VipAlertDialog create() {
            TextView contentView = layout.findViewById(R.id.vip_alert_content);
            if (this.content != null && this.content.length() > 0) {
                contentView.setText(this.content);
            }

            RelativeLayout cancelBtn = layout.findViewById(R.id.cancel_btn);
            Button commitButton = layout.findViewById(R.id.commit_btn);
            LinearLayout vipBtn = layout.findViewById(R.id.vip_btn);
            LinearLayout rechargeBtn = layout.findViewById(R.id.recharge_btn);
            if (isHiddenRecharge) {
                rechargeBtn.setVisibility(View.GONE);
            }

            //TODO:事件
            vipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (iVipAlertDialogListener != null) {
                        iVipAlertDialogListener.onClickGoToVip();
                    }
                }
            });

            rechargeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (iVipAlertDialogListener != null) {
                        iVipAlertDialogListener.onClickGoToRecharge();
                    }
                }
            });

            commitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (iVipAlertDialogListener != null) {
                        iVipAlertDialogListener.onClickGoToShare();
                    }
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(layout);
            dialog.setCancelable(false);
            return  dialog;
        }
    }

    public interface VipAlertDialogListener {
        void onClickGoToVip();
        void onClickGoToRecharge();
        void onClickGoToShare();
    }

}
