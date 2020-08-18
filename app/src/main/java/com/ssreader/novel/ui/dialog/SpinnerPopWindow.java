package com.ssreader.novel.ui.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.adapter.SpinnerAdapter;
import com.ssreader.novel.ui.view.MySpinner;

import java.util.List;

public class SpinnerPopWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private SpinnerAdapter mAdapter;
    private MySpinner.OnItemSelectedListener mItemSelectListener;

    public SpinnerPopWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public void setItemListener(MySpinner.OnItemSelectedListener listener){
        mItemSelectListener = listener;
    }

    public void setAdatper(SpinnerAdapter adapter){
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.piblic_listview, null);
        setContentView(view);
        setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.public_listviwe);
        mListView.setOnItemClickListener(this);
    }

    public <T> void refreshData(List<T> list, int selIndex) {
        if (list != null && selIndex  != -1) {
            if (mAdapter != null){
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null){
            mItemSelectListener.onItemSelected(pos);
        }
    }
}
