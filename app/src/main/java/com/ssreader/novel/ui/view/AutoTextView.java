package com.ssreader.novel.ui.view;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

/**
 * TextView的最后一行，不完全显示文本。
 */
public class AutoTextView extends AppCompatTextView {

    private int mEmptyWidth = 150;//空白文本宽度
    private int mMinLine = 5;

    public AutoTextView(Context context) {
        this(context, null);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int maxLines, textSize, lineWidth;

    public void setAutoText(final String text, SetAutoTextOver setAutoTextOver) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        textSize = ImageUtil.dp2px(getContext(), 13);
        setText(text);
        post(new Runnable() {
            @Override
            public void run() {
                int LineCount = getLineCount();
                if (setAutoTextOver != null) {
                    maxLines = Math.min(LineCount, 3);
                    lineWidth = (ScreenSizeUtils.getInstance(getContext()).getScreenWidth() - ImageUtil.dp2px(getContext(), 50));
                } else {
                    lineWidth = (ScreenSizeUtils.getInstance(getContext()).getScreenWidth() - ImageUtil.dp2px(getContext(), 40));
                    maxLines = 5;
                }
                if (LineCount >= maxLines) {
                    Layout layout = getLayout();
                    int start = 0;
                    int end;
                    String newText = "";
                    for (int i = 0; i < maxLines; i++) {
                        end = layout.getLineEnd(i);
                        String line = text.substring(start, end); //指定行的内容
                        if (i < maxLines - 1) {
                            newText += line;
                        } else {
                            int LinesTextSize = lineWidth / textSize;
                            if (line.length() <= LinesTextSize / 2) {
                                newText += line;
                                if (LineCount > maxLines) {
                                    newText += "...";
                                }
                            } else {
                                String lastText = line.substring(0, LinesTextSize / 2) + "...";
                                newText += lastText;
                            }

                        }
                        start = end;
                    }
                    setText(newText);
                    if (setAutoTextOver != null) {
                        setAutoTextOver.setAutoTextOver();
                    }
                }
            }
        });
    }

    public interface SetAutoTextOver {

        void setAutoTextOver();
    }
}