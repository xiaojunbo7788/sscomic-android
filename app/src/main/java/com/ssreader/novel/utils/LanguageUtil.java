package com.ssreader.novel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.eventbus.FinaShActivity;
import com.ssreader.novel.ui.activity.MainActivity;
import com.ssreader.novel.ui.activity.SettingActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

/**
 * 切换语言的工具类
 */
public class LanguageUtil {

    private static String LANGUAGE;

    public static String getLANGUAGE(Context activity) {
        if (LANGUAGE == null) {
            LANGUAGE = ShareUitls.getString(activity, "Language", "");
            if (LANGUAGE.equals("")) {
                LANGUAGE = ShareUitls.getString(activity, "Languagenull", "");
            }
        }
        return LANGUAGE;
    }

    public static String getString(Context context, int id) {
        if (context != null) {
            String str = "";
            try {
                str = context.getString(id);
            } catch (Exception e) {
                str = context.getResources().getString(id);
            }
            return str;
        }
        return "";
    }

    public static void reFreshLanguage(Locale locale, Activity activity, Class<?> homeClass) {
        if (homeClass == null) {
            String Langaupage = com.ssreader.novel.utils.ShareUitls.getString(activity, "Langaupage", "");
            switch (Langaupage) {
                case "":
                    Locale locale_cu;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        locale_cu = activity.getResources().getConfiguration().getLocales().get(0);
                    } else {
                        locale_cu = activity.getResources().getConfiguration().locale;
                    }
                    try {
                        if (locale_cu.getLanguage().equals("zh")) {
                            if (locale_cu.getCountry().equals("TW")) {
                                LANGUAGE = "tw";
                            } else {
                                LANGUAGE = "zh";
                            }
                        } else {
                            LANGUAGE = "zh";
                        }
                    } catch (Exception w) {
                        LANGUAGE = "zh";
                    }
                    ShareUitls.putString(activity, "Languagenull", LANGUAGE);
                    if (LANGUAGE.equals("zh")) {
                        locale = Locale.SIMPLIFIED_CHINESE;

                    } else {
                        locale = Locale.TRADITIONAL_CHINESE;
                    }
                    Resources resources = activity.getResources();
                    Configuration configuration = resources.getConfiguration();
                    DisplayMetrics metrics = resources.getDisplayMetrics();
                    configuration.setLocale(locale);
                    resources.updateConfiguration(configuration, metrics);
                    Locale.setDefault(locale);
                    return;
                case "CN":
                    locale = Locale.SIMPLIFIED_CHINESE;
                    break;
                case "in":
                    locale = new Locale("in", "in");
                    break;
            }
        }
        if (locale == null) {
            return;
        }
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        chengeLanguage(locale, activity, homeClass, resources, configuration);
    }

    private static void chengeLanguage(Locale locale, Activity activity, Class<?> homeClass, Resources resources, Configuration configuration) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, metrics);
        Locale.setDefault(locale);
        if (locale.getLanguage().equals("zh")) {
            LANGUAGE = "zh";
        } else {
            ShareUitls.putString(activity, "Language", locale.getLanguage());
            LANGUAGE = locale.getLanguage();
        }
        if (homeClass != null)
            if (homeClass == SettingActivity.class) {
                EventBus.getDefault().post(new FinaShActivity(true));
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            }
    }

    /**
     * 截取字符串
     *
     * @param activity
     * @param content
     * @param flag
     * @return
     */
    public static String getSubString(Activity activity, String content, int flag) {
        try {
            int length = content.length();
            if (flag == 0) {
                float tvWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 50);
                float LineCount = (tvWidth / ImageUtil.dp2px(activity, 13)) / 2;
                //float lines = length / LineCount;
                if (length <= LineCount) {
                } else if (length <= 2 * LineCount) {
                    content = content.substring(0, (int) LineCount) + "...";
                } else if (length <= LineCount * 3) {
                } else if (length <= LineCount * 4) {
                    content = content.substring(0, (int) (LineCount * 3)) + "...";
                } else if (length <= LineCount * 5) {
                } else {
                    content = content.substring(0, (int) (LineCount * 5)) + "...";
                }
            } else {
                float tvWidth = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 40);
                float LineCount = (tvWidth / ImageUtil.dp2px(activity, 13)) / 2;
                int Num9 = (int) (LineCount * 9);
                // float lines = length / LineCount;
                MyToash.Log("tvWidth", length+ "  " + LineCount + "  " + Num9+"  "+(content.contains("\\n")));
                MyToash.Log("tvWidth2", content);

                if (length>= Num9) {
                    content = content.substring(0, Num9) + "...";
                }
            }
        } catch (Exception e) {
        }
        return content;
    }
}
