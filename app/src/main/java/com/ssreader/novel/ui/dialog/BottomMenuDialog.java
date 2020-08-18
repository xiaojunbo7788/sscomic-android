package com.ssreader.novel.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;


import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.ScreenSizeUtils;

public class BottomMenuDialog {
    
    public Dialog bottomDialog;

    public void showBottomMenuDialog(android.app.Activity activity, String[] strings, SCOnItemClickListener scOnItemClickListener) {
        bottomDialog = new Dialog(activity, R.style.BottomDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_bottommenu, null);
        final LinearLayout dialogLayout = view.findViewById(R.id.dialog_bottommenu_layout);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        for (int i = 0; i < strings.length; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ImageUtil.dp2px(activity, 45));
            View view1 = layoutInflater.inflate(R.layout.dialog_bottommenu_text, null);
            TextView dialog_bottommenu_text = view1.findViewById(R.id.dialog_bottommenu_text);
            View dialog_bottommenu_view = view1.findViewById(R.id.dialog_bottommenu_view);

            if (i == strings.length - 1) {
                dialog_bottommenu_view.setVisibility(View.GONE);
            }
            dialog_bottommenu_text.setText(strings[i]);
            dialogLayout.addView(view1, layoutParams);
            int finalI = i;
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scOnItemClickListener.OnItemClickListener(0, finalI, strings[finalI]);
                    bottomDialog.dismiss();
                }
            });
        }
        dialogLayout.setBackground(MyShape.setMyshape(30, Color.WHITE));

        final TextView dialog_bottommenu_cancle = view.findViewById(R.id.dialog_bottommenu_cancle);
        dialog_bottommenu_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });
        dialog_bottommenu_cancle.setBackground(MyShape.setMyshape(30, Color.WHITE));
        bottomDialog.setContentView(view);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        view.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.AnimBottom);
        bottomDialog.show();
        bottomDialog.onWindowFocusChanged(false);
        bottomDialog.setCanceledOnTouchOutside(true);
    }
}
