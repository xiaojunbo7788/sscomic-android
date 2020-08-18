package com.ssreader.novel.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.ssreader.novel.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 判断是否为今天
     * @param time
     * @return
     */
    public static boolean isToday(long time) {
        // 传入的时间
        Date lastDate = new Date(time);
        Calendar lastCal = Calendar.getInstance();
        lastCal.setTime(lastDate);
        // 当前的时间
        Date nowDa = new Date(System.currentTimeMillis());
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(nowDa);
        return lastCal.get(Calendar.ERA) == nowCal.get(Calendar.ERA)
                && lastCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR)
                && lastCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 根据传入的范围，获取特定的内容
     * 只判断今天
     *
     * @param context
     * @param time
     * @return
     */
    public static String getRange(Context context, long time) {
        if (context == null) {
            return "";
        }
        long nowTime = System.currentTimeMillis();
        long chaTime = nowTime - time;
//        if (chaTime <= 0) {
//            return "";
//        }
        if (chaTime >= 60 * 60 * 1000) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm");
            String date = simpleDateFormat.format(time);
            int index = date.lastIndexOf("-");
            return LanguageUtil.getString(context, R.string.BookInfoActivity_today) + " "
                    + date.substring(index + 1, date.length());
        } else if (chaTime >= 60 * 1000) {
            int minute = (int) (chaTime / 1000 / 60);
            return java.lang.String.format(LanguageUtil.getString(context, R.string.BookInfoActivity_minute_ago), minute);
        } else {
            return LanguageUtil.getString(context, R.string.BookInfoActivity_now);
        }
    }

    /**
     * 获取特定格式的时间字符串
     * @param time
     * @param type
     * @return
     */
    public static String getDateString(long time, String type) {
        if (time == 0 || TextUtils.isEmpty(type)) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        String date = simpleDateFormat.format(time);
        return date;
    }
}
