package com.ssreader.novel.ui.read.manager;

import com.ssreader.novel.ui.read.util.ScreenUtils;
import com.ssreader.novel.ui.read.util.SharedPreUtils;
import com.ssreader.novel.ui.read.page.PageMode;
import com.ssreader.novel.ui.read.page.PageStyle;

/**
 * 阅读器的配置管理
 */
public class ReadSettingManager {

    private final static String AUTO_SPEED = "autospeed";

    public static final String SHARED_READ_BG = "shared_read_bg";
    public static final String SHARED_READ_BG_LAST = "shared_read_bg_last";
    public static final String SHARED_READ_BRIGHTNESS = "shared_read_brightness";
    public static final String SHARED_READ_IS_BRIGHTNESS_AUTO = "shared_read_is_brightness_auto";
    public static final String SHARED_READ_TEXT_SIZE = "shared_read_text_size";
    public static final String SHARED_READ_IS_TEXT_DEFAULT = "shared_read_text_default";
    public static final String SHARED_READ_PAGE_MODE = "shared_read_mode";
    public static final String SHARED_READ_NIGHT_MODE = "shared_night_mode";
    public static final String SHARED_READ_VOLUME_TURN_PAGE = "shared_read_volume_turn_page";
    public static final String SHARED_READ_FULL_SCREEN = "shared_read_full_screen";
    public static final String SHARED_READ_CONVERT_TYPE = "shared_read_convert_type";
    private final static String LINE_SPACING_KEY = "linespacingmode";

    private static volatile ReadSettingManager sInstance;

    public final static float LINE_SPACING_SMALL = 0.5f;
    public final static float LINE_SPACING_MEDIUM = 1.0f;
    public final static float LINE_SPACING_BIG = 2.0f;

    private SharedPreUtils sharedPreUtils;

    public static ReadSettingManager getInstance() {
        if (sInstance == null) {
            synchronized (ReadSettingManager.class) {
                if (sInstance == null) {
                    sInstance = new ReadSettingManager();
                }
            }
        }
        return sInstance;
    }

    private ReadSettingManager() {
        sharedPreUtils = SharedPreUtils.getInstance();
    }

    public void setBrightness(int progress) {
        sharedPreUtils.putInt(SHARED_READ_BRIGHTNESS, progress);
    }

    public int getBrightness() {
        return sharedPreUtils.getInt(SHARED_READ_BRIGHTNESS, 200);
    }

    public void setAutoBrightness(boolean isAuto) {
        sharedPreUtils.putBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, isAuto);
    }

    public void setDefaultTextSize(boolean isDefault) {
        sharedPreUtils.putBoolean(SHARED_READ_IS_TEXT_DEFAULT, isDefault);
    }

    public void setTextSize(int textSize) {
        sharedPreUtils.putInt(SHARED_READ_TEXT_SIZE, textSize);
    }

    public void setPageMode(PageMode mode) {
        sharedPreUtils.putInt(SHARED_READ_PAGE_MODE, mode.ordinal());
    }

    public void setNightMode(boolean isNight) {
        sharedPreUtils.putBoolean(SHARED_READ_NIGHT_MODE, isNight);
    }


    public boolean isBrightnessAuto() {
        return sharedPreUtils.getBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, false);
    }

    public int getTextSize() {
        return sharedPreUtils.getInt(SHARED_READ_TEXT_SIZE, ScreenUtils.spToPx(20));
    }

    public boolean isDefaultTextSize() {
        return sharedPreUtils.getBoolean(SHARED_READ_IS_TEXT_DEFAULT, false);
    }

    public PageMode getPageMode() {
        int mode = sharedPreUtils.getInt(SHARED_READ_PAGE_MODE, PageMode.SIMULATION.ordinal());
        return PageMode.values()[mode];
    }

    public PageStyle getPageStyle() {
        int style = sharedPreUtils.getInt(SHARED_READ_BG, PageStyle.BG_0.ordinal());
        return PageStyle.values()[style];
    }

    public void setPageStyle(PageStyle pageStyle) {
        sharedPreUtils.putInt(SHARED_READ_BG, pageStyle.ordinal());
    }
    public void setLastLightPageStyle(PageStyle pageStyle) {
        sharedPreUtils.putInt(SHARED_READ_BG_LAST, pageStyle.ordinal());
    }
    public PageStyle getLastLightPageStyle() {
        int style = sharedPreUtils.getInt(SHARED_READ_BG_LAST, PageStyle.BG_0.ordinal());
        return PageStyle.values()[style];
    }
    public boolean isNightMode() {
        return sharedPreUtils.getBoolean(SHARED_READ_NIGHT_MODE, false);
    }

    public void setVolumeTurnPage(boolean isTurn) {
        sharedPreUtils.putBoolean(SHARED_READ_VOLUME_TURN_PAGE, isTurn);
    }

    public boolean isVolumeTurnPage() {
        return sharedPreUtils.getBoolean(SHARED_READ_VOLUME_TURN_PAGE, false);
    }

    public void setFullScreen(boolean isFullScreen) {
        sharedPreUtils.putBoolean(SHARED_READ_FULL_SCREEN, isFullScreen);
    }

    public boolean isFullScreen() {
        return sharedPreUtils.getBoolean(SHARED_READ_FULL_SCREEN, false);
    }

    public void setConvertType(int convertType) {
        sharedPreUtils.putInt(SHARED_READ_CONVERT_TYPE, convertType);
    }

    public int getConvertType() {
        return sharedPreUtils.getInt(SHARED_READ_CONVERT_TYPE, 0);
    }

    public float getLineSpacingMode() {
        return sharedPreUtils.getFloat(LINE_SPACING_KEY, LINE_SPACING_BIG);
    }

    public void setLineSpacingMode(float mode) {
        sharedPreUtils.putFloat(LINE_SPACING_KEY, mode);
    }

    public int getAutoSpeed() {
        return sharedPreUtils.getInt(AUTO_SPEED, 5);
    }

    public void setAutoSpeed(int autoSpeed) {
        sharedPreUtils.putInt(AUTO_SPEED, autoSpeed);
    }
}
