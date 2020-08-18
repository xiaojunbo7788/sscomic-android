package com.ssreader.novel.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.utils.TextStyleUtils;

public class SizeAnmotionTextview extends AppCompatTextView {

    public SizeAnmotionTextview(Context context) {
        super(context);
    }

    public SizeAnmotionTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    int position;

    public void setMyText(Activity activity, String text, int position, int color, int flag) {
        if (text == null) {
            return;
        }
        this.position = position;
        String[] strings = null;
        if (position != 8) {
            strings = text.split("###");
        } else {
            if (text.contains("###")) {
                text = text.replace("###", "");
                setTextColor(Color.RED);
            } else {
                setTextColor(Color.GRAY);
            }
            setText(text);
            return;
        }
        if (strings.length >= 2) {
            if (position == 1) {
                //MyToash.Log("SpannableStringBuilder", strings.length + text);
                String str = "";
                // 单独设置字体颜色
                if (strings.length == 2) {
                    str = (strings[0]+" ") + strings[1];
                    new TextStyleUtils(activity, str, this)
                            .setColorSpan(color, strings[0].length(), str.length())
                            .setSpanner();
                    if (flag == 1) {
                        new TextStyleUtils(activity, str, this)
                                .setSizeSpan(16, strings[0].length(), str.length())
                                .setColorSpan(color,strings[0].length(), str.length())
                                .setSpanner();
                    }
                } else {
                    str = strings[0] + strings[1] + strings[2];
                    new TextStyleUtils(activity, str, this)
                            .setColorSpan(color, strings[0].length(), strings[0].length() + strings[1].length())
                            .setSpanner();
                    if (flag == 1) {
                        new TextStyleUtils(activity, str, this)
                                .setSizeSpan(16, strings[0].length(), strings[0].length() + strings[1].length())
                                .setColorSpan(color, strings[0].length(), strings[0].length() + strings[1].length())
                                .setSpanner();
                    }
                }
            } else if ((position == 2 || position == 3) && strings.length >= 3) {
                String str = strings[1] + strings[2];
                new TextStyleUtils(activity, str, this)
                        .setRelativeSizeSpan(2, Typeface.BOLD, 0, strings[1].length())
                        .setSpanner();
            } else if (position == 4) {
                if (strings.length == 3) {
                    String str = strings[0] + strings[1] + strings[2];
                    new TextStyleUtils(activity, str, this)
                            .setColorSpan(color, strings[0].length(), strings[0].length() + strings[1].length())
                            .setSpanner();
                }
            }
        } else {
            setText(text);
        }

    }
}
