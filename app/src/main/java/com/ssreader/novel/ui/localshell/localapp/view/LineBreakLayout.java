package com.ssreader.novel.ui.localshell.localapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.localshell.bean.LineBreakLayoutBeen;

import java.util.ArrayList;
import java.util.List;

public class LineBreakLayout extends ViewGroup {

    private final static String TAG = "LineBreakLayout";
    /**
     * 所有标签
     */
    private ArrayList<String> lables;
    private ArrayList<LineBreakLayoutBeen> lineBreakLayoutBeens;
    /**
     * 选中标签
     */
    private List<String> lableSelected = new ArrayList<>();

    //自定义属性
    private int LEFT_RIGHT_SPACE; //dip
    private int FIRST_LEFT_RIGHT_SPACE; //dip
    private int ROW_SPACE;
    Drawable drawable;
    public boolean noonLayout = true;

    public LineBreakLayout(Context context) {
        this(context, null);
    }

    public LineBreakLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineBreakLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lables = new ArrayList<>();
        lineBreakLayoutBeens = new ArrayList<>();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        LEFT_RIGHT_SPACE = ta.getDimensionPixelSize(R.styleable.FlowLayout_leftAndRightSpace, 10);

        FIRST_LEFT_RIGHT_SPACE = ta.getDimensionPixelSize(R.styleable.FlowLayout_firstleftAndRightSpace, 0);


        ROW_SPACE = ta.getDimensionPixelSize(R.styleable.FlowLayout_rowSpace, 10);

        ta.recycle(); //回收

    }

    int itemlayout, flag;

    public void initItem(int itemlayout, int flag) {
        this.itemlayout = itemlayout;
        this.flag = flag;
    }


    boolean b = false;

    public void setLablesTieZiTags(Activity activity, List<LineBreakLayoutBeen> list) {
        if (list != null) {
            noonLayout = true;
            removeAllViews();
            if (!list.isEmpty()) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                int i = 0;
                for (final LineBreakLayoutBeen lable : list) {
                    //获取标签布局
                    final TextView tv = (TextView) inflater.inflate(R.layout.item_line_break_lable, null);
                    tv.setText(lable.getTitle());
                    tv.setBackground(ContextCompat.getDrawable(activity, R.color.lightgray1));
                    tv.setTextColor(ContextCompat.getColor(activity, R.color.gray));
                    //点击标签后，重置选中效果
                    int finalI = i;
                    tv.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (onItemOnclickListener != null) {
                                onItemOnclickListener.OnItemOnclick(tv, list.get(finalI).title, finalI);
                            }
                        }
                    });
                    //将标签添加到容器中
                    addView(tv);
                    ++i;
                }
            }
        }
    }


    OnItemOnclickListener onItemOnclickListener;

    public interface OnItemOnclickListener {
        void OnItemOnclick(TextView textView, String text, int position);
    }

    public void setOnItemOnclickListener(OnItemOnclickListener onItemOnclickListener) {
        this.onItemOnclickListener = onItemOnclickListener;
    }

    onClickListener onClickListener;

    public void setOnClickListener(LineBreakLayout.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface onClickListener {
        void OnItemOnclick(TextView textView, String text, int position, ImageView img);
    }


    /**
     * 获取选中标签
     */
    public List<String> getSelectedLables() {
        return lableSelected;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (true) {
            //为所有的标签childView计算宽和高
            measureChildren(widthMeasureSpec, heightMeasureSpec);

            //获取高的模式
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            //建议的高度
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            //布局的宽度采用建议宽度（match_parent或者size），如果设置wrap_content也是match_parent的效果
            // int width = MeasureSpec.getSize(widthMeasureSpec);
            int Width = MeasureSpec.getSize(widthMeasureSpec);
            int width = Width - FIRST_LEFT_RIGHT_SPACE;
            int height;
            if (heightMode == MeasureSpec.EXACTLY) {
                //如果高度模式为EXACTLY（match_perent或者size），则使用建议高度
                height = heightSize;
            } else {
                //其他情况下（AT_MOST、UNSPECIFIED）需要计算计算高度
                int childCount = getChildCount();
                if (childCount <= 0) {
                    height = 0;   //没有标签时，高度为0
                } else {
                    int row = 1;  // 标签行数
                    int widthSpace = width;// 当前行右侧剩余的宽度


                    Log.i("widthSpace", "  " + Width + " " + width + "  ：" + FIRST_LEFT_RIGHT_SPACE + " " + widthSpace);

                    for (int i = 0; i < childCount; i++) {
                        if (row != 1) {
                            width = Width;
                        }
                        View view = getChildAt(i);
                        //获取标签宽度
                        int childW = view.getMeasuredWidth();
                        //
                        if (widthSpace >= childW) {
                            //如果剩余的宽度大于此标签的宽度，那就将此标签放到本行
                            widthSpace -= childW;
                        } else {
                            row++;    //增加一行
                            //如果剩余的宽度不能摆放此标签，那就将此标签放入一行
                            widthSpace = width - childW;
                        }
                        //减去标签左右间距
                        widthSpace -= LEFT_RIGHT_SPACE;
                    }
                    //由于每个标签的高度是相同的，所以直接获取第一个标签的高度即可
                    int childH = getChildAt(0).getMeasuredHeight();
                    //最终布局的高度=标签高度*行数+行距*(行数-1)
                    height = (childH * row) + ROW_SPACE * (row - 1);

                }
            }

            //设置测量宽度和测量高度
            setMeasuredDimension(Width, height);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (noonLayout) {
            int row = 0;
            int right = 0;   // 标签相对于布局的右侧位置
            int botom;       // 标签相对于布局的底部位置
            for (int i = 0; i < getChildCount(); i++) {
                if (i == 0) {
                    right += FIRST_LEFT_RIGHT_SPACE;
                }
                View childView = getChildAt(i);
                int childW = childView.getMeasuredWidth();
                int childH = childView.getMeasuredHeight();
                //右侧位置=本行已经占有的位置+当前标签的宽度
                right += childW;
                //底部位置=已经摆放的行数*（标签高度+行距）+当前标签高度
                botom = row * (childH + ROW_SPACE) + childH;
                // 如果右侧位置已经超出布局右边缘，跳到下一行
                // if it can't drawing on a same line , skip to next line
                if (right > (r - LEFT_RIGHT_SPACE)) {
                    row++;
                    right = childW;
                    botom = row * (childH + ROW_SPACE) + childH;
                }
                Log.i(TAG, "left = " + (right - childW) + " top = " + (botom - childH) +
                        " right = " + right + " botom = " + botom);
                childView.layout(right - childW, botom - childH, right, botom);

                right += LEFT_RIGHT_SPACE;
            }
            noonLayout = false;
        }
    }

}
