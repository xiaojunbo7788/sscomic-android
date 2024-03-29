package com.ssreader.novel.ui.view.comiclookview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wang.avi.AVLoadingIndicatorView;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.view.screcyclerview.SCProgressStyle;
import com.ssreader.novel.ui.view.screcyclerview.SCSimpleViewSwitcher;


public class ComicLoadingMoreFooter extends LinearLayout {

    private SCSimpleViewSwitcher progressCon;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    public int   DP10 ;
    private TextView mText;
    private String loadingHint;
    private String noMoreHint;
    private String loadingDoneHint;

    private AVLoadingIndicatorView progressView;

    public ComicLoadingMoreFooter(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public ComicLoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        DP10= ImageUtil.dp2px(context,10);
        initView();
    }

    public void destroy(){
        progressCon = null;
        if(progressView != null){
            progressView=null;
            progressView = null;
        }
    }

    public void setLoadingHint(String hint) {
        loadingHint = hint;
    }

    public void setNoMoreHint(String hint) {
        noMoreHint = hint;
    }

    public void setLoadingDoneHint(String hint) {
        loadingDoneHint = hint;
    }

    public void initView(){
        setGravity(Gravity.CENTER);
        setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        progressCon = new SCSimpleViewSwitcher(getContext());
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        progressView = new AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
       // progressView.setIndicatorId(SCProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(getContext());
//        mText.setText(getContext().getString(R.string.listview_loading));

        if(loadingHint == null || loadingHint.equals("")){
//            loadingHint = (String)getContext().getText(R.string.listview_loading);
        }
        if(noMoreHint == null || noMoreHint.equals("")){
//            noMoreHint = (String)getContext().getText(R.string.nomore_loading);
        }
        if(loadingDoneHint == null || loadingDoneHint.equals("")){
//            loadingDoneHint = (String)getContext().getText(R.string.loading_done);
        }

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(DP10,0,0,0 );

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    public void setProgressStyle(int style) {
        if(style == SCProgressStyle.SysProgress){
            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        }else{
            progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
           // progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    public void  setState(int state) {
        switch(state) {
            case STATE_LOADING:
                progressCon.setVisibility(View.VISIBLE);
                mText.setText(loadingHint);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(loadingDoneHint);
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(noMoreHint);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}