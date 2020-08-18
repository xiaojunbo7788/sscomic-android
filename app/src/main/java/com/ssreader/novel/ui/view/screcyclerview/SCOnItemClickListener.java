package com.ssreader.novel.ui.view.screcyclerview;

public interface SCOnItemClickListener<T> {

    void  OnItemClickListener(int flag,int position, T O);

    void  OnItemLongClickListener(int flag, int position, T O);
}
