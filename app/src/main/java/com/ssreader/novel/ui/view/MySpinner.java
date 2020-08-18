package com.ssreader.novel.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssreader.novel.R;
import com.ssreader.novel.model.AudioSpeedBean;
import com.ssreader.novel.ui.adapter.SpinnerAdapter;
import com.ssreader.novel.ui.dialog.SpinnerPopWindow;
import com.ssreader.novel.ui.utils.PublicStaticMethod;

import java.util.List;

public class MySpinner extends LinearLayout {

    private TextView tv_value;
    private ImageView bt_dropdown;
    private Context mcontext;
    private List<String> mItems;
    OnItemSelectedListener listener;
    private SpinnerPopWindow mSpinerPopWindow;
    private SpinnerAdapter mAdapter;
    View myView;


    public MySpinner(Context context) {
        super(context);
        mcontext = context;
        init();
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        init();
    }

    private void init(){
        LayoutInflater mInflater = LayoutInflater.from(mcontext);
        myView = mInflater.inflate(R.layout.myspinner_layout, null);
        addView(myView);

        tv_value = (TextView) myView.findViewById(R.id.tv_value);
        bt_dropdown = (ImageView) myView.findViewById(R.id.bt_dropdown);
        tv_value.setOnClickListener(onClickListener);
        bt_dropdown.setOnClickListener(onClickListener);
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startPopWindow();
        }
    };

    public void setData(List<String> datas){
        mItems = datas;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener){
        this.listener = listener;
    }


    public void startPopWindow(){
        List<AudioSpeedBean> audioSpeedDate = PublicStaticMethod.getAudioSpeedDate((Activity) mcontext);
        mAdapter = new SpinnerAdapter((Activity) mcontext,audioSpeedDate,1);
        mSpinerPopWindow = new SpinnerPopWindow(mcontext);
        mSpinerPopWindow.setAdatper(mAdapter);
        mSpinerPopWindow.setItemListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(int pos) {
                // TODO Auto-generated method stub
                tv_value.setText(mItems.get(pos));
                listener.onItemSelected(pos);
            }
        });
        showSpinWindow();
    }

    private void showSpinWindow(){
        mSpinerPopWindow.setWidth(myView.getWidth());
        mSpinerPopWindow.showAsDropDown(myView);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int pos);
    }
}

