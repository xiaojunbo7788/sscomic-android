package com.ssreader.novel.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.MyToash;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtlis {

    private static Matcher mat;

    /**
     * 验证银行卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 验证输入的名字是否为“中文”或者是否包含“.”
     * @param name
     * @param activity
     * @return
     */
    public static boolean isLegalName(String name, Activity activity) {
        if (name.contains("·") || name.contains("•")) {
            if (name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$")) {
                return true;
            } else {
                MyToash.Toash(activity, name);
                return false;
            }
        } else {
            if (name.matches("^[\\u4e00-\\u9fa5]+$")) {
                return true;
            } else {
                MyToash.Toash(activity, name);
                return false;
            }
        }
    }

    /**
     * 验证输入的邮箱是否正确
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证输入的手机号是否正确
     * @param mobileNums
     * @return
     */
    public static boolean isMobile(String mobileNums) {

        if (TextUtils.isEmpty(mobileNums)) {

            return false;
        } else if (mobileNums.length() != 11) {
            return false;
        } /*else {
            Pattern pat = Pattern.compile("^[1][3578][0-9]{9}$");
            mat = pat.matcher(mobileNums);
        }*/
        return mobileNums.startsWith("1");
    }


    /**
     * 验证输入的身份证号是否正确
     * @param id
     * @param activity
     * @return
     */
    public static boolean isLegalId(String id, Activity activity) {
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断一个字符串是否是数字。
     *
     * @param string
     * @return
     */
    public static boolean isNumber(String string) {
        if (string == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d+$");
        return pattern.matcher(string).matches();
    }
}
