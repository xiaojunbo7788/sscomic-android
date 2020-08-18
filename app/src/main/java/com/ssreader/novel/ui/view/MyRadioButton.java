package com.ssreader.novel.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import androidx.appcompat.widget.AppCompatRadioButton;


public class MyRadioButton extends AppCompatRadioButton {
    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String field;
    public int raw;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public void setfield(String field) {
        this.field = field;
    }


}
