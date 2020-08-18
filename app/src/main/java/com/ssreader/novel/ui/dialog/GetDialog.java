package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.PublicCallBackSpan;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetDialog {

    public interface GetEditTextDialogInterface {
        void getText(String text);
    }

    public interface IsOperationInterface {
        void isOperation();
    }

    public static void IsOperation(final Activity activity, String title, String suretext, final IsOperationInterface isOperationInterface) {
        if (activity == null) {
            return;
        }
        Dialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(suretext)
                .setPositiveButton(LanguageUtil.getString(activity, R.string.app_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isOperationInterface.isOperation();
                    }
                })
                .setNegativeButton(LanguageUtil.getString(activity, R.string.app_cancle), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    public interface GetTimeDialogInterface {
        void getTimeDialogInterface(int year, int monthOfYear, int dayOfMonth);
    }

    public static void getTimeDialog(Activity activity, String minTime, Calendar calendar, final GetTimeDialogInterface getTimeDialogInterface) {
        int style = AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, style,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        getTimeDialogInterface.getTimeDialogInterface(year, monthOfYear, dayOfMonth);
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.setTitle("选择查询日期");
        try {
            //设置最小日期
            datePicker.setMinDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(minTime).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        } //设置最大日期
        datePicker.setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public interface OkCommit {
        void success();
    }

    public static void PraviteDialog(final Activity activity, OkCommit okCommit) {
        ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(0.4f, activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.diaolog_privte, null);
        final PopupWindow popupWindow = new PopupWindow(view);
        HolderSign holderSign = new HolderSign(view);
        holderSign.dialog_privte_layout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity,10), Color.WHITE));
        final SpannableStringBuilder style = new SpannableStringBuilder();
        String appname = LanguageUtil.getString(activity, R.string.app_name);
        String content = String.format(LanguageUtil.getString(activity, R.string.AboutActivity_user_private_content), appname, appname);
        //设置文字
        style.append(content);
        PublicCallBackSpan yonghu = new PublicCallBackSpan(activity, 1);
        yonghu.isSplashYinsi = true;
        PublicCallBackSpan yinsi = new PublicCallBackSpan(activity, 2);
        yinsi.isSplashYinsi = true;
        holderSign.dialogPrivteTitle.setText(LanguageUtil.getString(activity, R.string.AboutActivity_user_private_tips));
        holderSign.dialogPrivteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(1f, activity);
                okCommit.success();
                popupWindow.dismiss();
            }
        });
        holderSign.dialog_privte_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                activity.finish();
            }
        });
        int startYinPosition = content.indexOf("《");
        int endYinPosition = content.indexOf("》") + 1;
        int startYongPosition = content.lastIndexOf("《");
        int endYongPosition = content.lastIndexOf("》") + 1;
        style.setSpan(yinsi, startYinPosition, endYinPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(yonghu, startYongPosition, endYongPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.rgb(255, 153, 102));
        style.setSpan(foregroundColorSpan, startYinPosition, endYinPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(Color.rgb(255, 153, 102));
        style.setSpan(foregroundColorSpan2, startYongPosition, endYongPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //配置给TextView
        holderSign.dialogPrivteContent.setMovementMethod(LinkMovementMethod.getInstance());
        holderSign.dialogPrivteContent.setHighlightColor(ContextCompat.getColor(activity,R.color.transparent));
        holderSign.dialogPrivteContent.setText(style);

        popupWindow.setWidth(ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 50));
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.sign_pop_anim);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static void MovieTicketDialog(Activity activity, String title,String content) {
        ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(0.4f, activity);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.diaolog_movieticket, null);
        final PopupWindow popupWindow = new PopupWindow(inflate);
        View dialog_movieTicket_layout = inflate.findViewById(R.id.dialog_movieTicket_layout);
        View dialog_privte_layout = inflate.findViewById(R.id.dialog_privte_layout);
        ViewGroup.LayoutParams layoutParams = dialog_privte_layout.getLayoutParams();
        layoutParams.width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 100);
        layoutParams.height = layoutParams.width * 328 / 265;
        dialog_privte_layout.setLayoutParams(layoutParams);
        TextView dialog_privte_title = inflate.findViewById(R.id.dialog_privte_title);
        TextView dialog_privte_content = inflate.findViewById(R.id.dialog_privte_content);
        dialog_privte_title.setText(title);
        dialog_privte_content.setText(content);
        dialog_privte_layout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 10), Color.WHITE));

        popupWindow.setWidth(ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 100));
        popupWindow.setHeight(layoutParams.width * 328 / 265);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.sign_pop_anim);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog_movieTicket_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(1f, activity);
                popupWindow.dismiss();
            }
        });
    }



    public static class HolderSign {

        @BindView(R.id.dialog_privte_title)
        TextView dialogPrivteTitle;
        @BindView(R.id.dialog_privte_content)
        TextView dialogPrivteContent;
        @BindView(R.id.dialog_privte_ok)
        TextView dialogPrivteOk;

        @BindView(R.id.dialog_privte_layout)
        View dialog_privte_layout;
        @BindView(R.id.dialog_privte_cancle)
        View dialog_privte_cancle;


        public HolderSign(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
