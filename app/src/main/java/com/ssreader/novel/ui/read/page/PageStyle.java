package com.ssreader.novel.ui.read.page;

import androidx.annotation.ColorRes;

import com.ssreader.novel.R;

/**
 * 作用：页面的展示风格。
 */
public enum PageStyle {

    BG_0(R.color.read_bg_default, R.color.read_font_default),
    BG_1(R.color.read_bg_1, R.color.read_font_1),
    BG_2(R.color.read_bg_2, R.color.read_font_2),
    BG_3(R.color.read_bg_3, R.color.read_font_3),
    BG_4(R.color.read_bg_4, R.color.read_font_4),
    BG_7(R.color.read_bg_7, R.color.read_font_7),
    BG_8(R.color.read_bg_8, R.color.nb_read_font_night),
    NIGHT(R.color.nb_read_bg_night, R.color.nb_read_font_night);

    private int fontColor;
    private int bgColor;

    PageStyle(@ColorRes int fontColor, @ColorRes int bgColor) {
        this.fontColor = fontColor;
        this.bgColor = bgColor;
    }

    public int getFontColor() {
        return fontColor;
    }

    public int getBgColor() {
        return bgColor;
    }
}
