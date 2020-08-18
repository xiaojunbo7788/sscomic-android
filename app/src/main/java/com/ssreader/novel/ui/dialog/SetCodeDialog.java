package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetCodeDialog {

    private Activity activity;
    private ViewHolder viewHolder;
    private PopupWindow popupWindow;

    private onVerificationSuccess onVerificationSuccess;

    public void setOnVerificationSuccess(onVerificationSuccess onVerificationSuccess) {
        this.onVerificationSuccess = onVerificationSuccess;
    }

    public void showSetCodeDialog(Activity activity, String codeName, String edHintName) {
        this.activity = activity;
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_set_code, null);
        viewHolder = new ViewHolder(view);
        popupWindow = new PopupWindow(view, ScreenSizeUtils.getInstance(activity).getScreenWidth() -
                ImageUtil.dp2px(activity, 80), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        viewHolder.mSetEditCode.setFocusable(true);
        viewHolder.mDialogSetTitle.setText(codeName);
        viewHolder.mSetEditCode.setText(edHintName);

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.sign_pop_anim);
        popupWindow.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);
        popupWindow.update();
        showSoftInputFromWindow(viewHolder.mSetEditCode);
        if (!SystemUtil.getSystemModel().contains("MI 6X")) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 0.4f;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            activity.getWindow().setAttributes(lp);
            popupWindow.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);
        } else {
            popupWindow.showAtLocation(new View(activity), Gravity.CENTER, 0, -100);
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            public void onDismiss() {
                if (!SystemUtil.getSystemModel().contains("MI 6X")) {
                    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                    lp.alpha = 1f;
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    activity.getWindow().setAttributes(lp);
                }
            }
        });
    }

    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public class ViewHolder {

        @BindView(R.id.dialog_set_code_layout)
        FrameLayout dialog_set_code_layout;
        @BindView(R.id.dialog_set_title)
        TextView mDialogSetTitle;
        @BindView(R.id.set_edit_code)
        EditText mSetEditCode;
        @BindView(R.id.dialog_set_code_sure)
        TextView mDialogSetCodeSure;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mSetEditCode.setBackground(MyShape.setMyshapeStrokeMyBg(activity,4, 2,ContextCompat.getColor(activity, R.color.grayline), ContextCompat.getColor(activity, R.color.white)));
            dialog_set_code_layout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 10), Color.WHITE));
        }

        @OnClick({R.id.dialog_set_code_finish, R.id.dialog_set_code_sure, R.id.dialog_set_code_cancle})
        public void onClick(View v) {
            String mEditT = viewHolder.mSetEditCode.getText().toString();
            switch (v.getId()) {
                case R.id.dialog_set_code_sure:
                    onVerificationSuccess.success(mEditT);
                    popupWindow.dismiss();
                    break;
                case R.id.dialog_set_code_finish:
                case R.id.dialog_set_code_cancle:
                    popupWindow.dismiss();
                    break;
            }
        }
    }

    public interface onVerificationSuccess {

        void success(String editText);
    }
}

