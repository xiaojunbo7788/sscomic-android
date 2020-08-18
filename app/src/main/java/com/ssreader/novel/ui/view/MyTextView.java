package com.ssreader.novel.ui.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;

public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void SetText(String s) {
        setText(s+"");
    }
    public void SetText(int s) {
        setText(s+"");
    }


    int position;
    public void setMyText(String text, int position) {
        if(text==null){
            return;
        }
        this.position = position;
        String[] strings=null;
        if(position!=8) {
            strings = text.split("###");
        }else {

            if(text.contains("###")){
                text=text.replace("###","");
                setTextColor(Color.RED);
            }else {
                setTextColor(Color.GRAY);
            }
            setText(text);
            return;
        }
        if (strings.length >= 2) {
            SpannableStringBuilder spannableBuilder = null;
            if (position == 1) {
                // 单独设置字体颜色
                String str=strings[0]+strings[1];
                spannableBuilder = new SpannableStringBuilder(str);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.maincolor));
                spannableBuilder.setSpan(colorSpan, strings[0].length(), str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            } else if ((position == 2||position == 3)&&strings.length >= 3) {
                String str=strings[1]+strings[2];
                spannableBuilder = new SpannableStringBuilder(str);
                StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
                RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan((float) 2);
                spannableBuilder.setSpan(sizeSpan1, 0, strings[1].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableBuilder.setSpan(styleSpan_B, 0, strings[1].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            }
            if(spannableBuilder!=null) {
                setText(spannableBuilder);
            }else {
                setText(text);
            }
        } else {
            setText(text);
        }

    }


    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setMyTextColor(String text, int color) {
        if(text==null){
            return;
        }


        this.position = position;
        String[] strings=null;
        if(position!=8) {
            strings = text.split("###");
        }else {

            if(text.contains("###")){
                text=text.replace("###","");
                setTextColor(Color.RED);
            }else {
                setTextColor(Color.GRAY);
            }
            setText(text);
            return;
        }
        if (strings.length >= 2) {
            SpannableStringBuilder spannableBuilder = null;
            if (position == 1) {
                String str=strings[0]+strings[1];
                spannableBuilder = new SpannableStringBuilder(str);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(),color));
                spannableBuilder.setSpan(colorSpan, strings[0].length(), str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
                /*// 单独设置字体颜色
                String str=strings[0]+strings[1];
                spannableBuilder = new SpannableStringBuilder(str);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.maincolor));
                spannableBuilder.setSpan(colorSpan, strings[0].length(), str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            } else if ((position == 2||position == 3)&&strings.length >= 3) {
                String str=strings[1]+strings[2];
                spannableBuilder = new SpannableStringBuilder(str);
                StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
                RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan((float) 2);
                spannableBuilder.setSpan(sizeSpan1, 0, strings[1].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableBuilder.setSpan(styleSpan_B, 0, strings[1].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            }
            if(spannableBuilder!=null) {
                setText(spannableBuilder);
            }else {
                setText(text);
            }*/
        } else {
            setText(text);
        }
    }
}
