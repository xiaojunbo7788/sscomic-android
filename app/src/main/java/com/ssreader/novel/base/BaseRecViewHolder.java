package com.ssreader.novel.base;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

public class BaseRecViewHolder extends RecyclerView.ViewHolder {

    public BaseRecViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}