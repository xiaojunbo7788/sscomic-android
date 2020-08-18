package com.ssreader.novel.ui.view.banner.holder;

import android.content.Context;
import android.view.View;

public interface Holder<T> {

    View createView(Context context, int size);

    void UpdateUI(Context context, int position, T data);
}