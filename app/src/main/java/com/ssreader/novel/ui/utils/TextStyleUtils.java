package com.ssreader.novel.ui.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristics;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.ui.view.VerticalAlignTextSpan;

public class TextStyleUtils {

    public Activity activity;
    public TextView textView;
    private final SpannableStringBuilder spannableStringBuilder;

    public TextStyleUtils(Activity activity, String str, TextView textView) {
        this.activity = activity;
        this.textView = textView;
        spannableStringBuilder = new SpannableStringBuilder(str);
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     * @param start
     * @param end
     * @return
     */
    public TextStyleUtils setSizeSpan(int textSize, int start, int end) {
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(ImageUtil.dp2px(activity, textSize));
        spannableStringBuilder.setSpan(absoluteSizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param textcolor
     * @param start
     * @param end
     * @return
     */
    public TextStyleUtils setColorSpan(int textcolor, int start, int end) {
        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(textcolor);
        spannableStringBuilder.setSpan(foregroundColorSpan2, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 设置字体相对大小
     *
     * @param textRelativeSize
     * @param start
     * @param end
     * @return
     */
    public TextStyleUtils setRelativeSizeSpan(float textRelativeSize, int style, int start, int end) {
        StyleSpan styleSpan_B = new StyleSpan(style);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(textRelativeSize);
        spannableStringBuilder.setSpan(styleSpan_B, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(relativeSizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体点击
     *
     * @param flag
     * @param start
     * @param end
     * @return
     */
    public TextStyleUtils setCallBackSpan(int flag, int start, int end) {
        PublicCallBackSpan opinionSpan = new PublicCallBackSpan(activity, flag);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        spannableStringBuilder.setSpan(opinionSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体样式
     */
    public void setSpanner() {
        textView.setText(spannableStringBuilder);
    }

    /**
     * @param textView
     * @param textViewWidth
     * @return 获取文字行数
     */
    public static int getTextViewLines(TextView textView, int textViewWidth) {
        int width = textViewWidth - textView.getCompoundPaddingLeft() - textView.getCompoundPaddingRight();
        StaticLayout staticLayout;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            staticLayout = getStaticLayout23(textView, width);
        } else {
            staticLayout = getStaticLayout(textView, width);
        }
        int lines = staticLayout.getLineCount();
        int maxLines = textView.getMaxLines();
        if (maxLines > lines) {
            return lines;
        }
        return maxLines;
    }

    /**
     * @param textView
     * @param width
     * @return sdk >= 23
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static StaticLayout getStaticLayout23(TextView textView, int width) {
        StaticLayout.Builder builder = StaticLayout.Builder.obtain(textView.getText(),
                0, textView.getText().length(), textView.getPaint(), width)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setTextDirection(TextDirectionHeuristics.FIRSTSTRONG_LTR)
                .setLineSpacing(textView.getLineSpacingExtra(), textView.getLineSpacingMultiplier())
                .setIncludePad(textView.getIncludeFontPadding())
                .setBreakStrategy(textView.getBreakStrategy())
                .setHyphenationFrequency(textView.getHyphenationFrequency())
                .setMaxLines(textView.getMaxLines() == -1 ? Integer.MAX_VALUE : textView.getMaxLines());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setJustificationMode(textView.getJustificationMode());
        }
        if (textView.getEllipsize() != null && textView.getKeyListener() == null) {
            builder.setEllipsize(textView.getEllipsize())
                    .setEllipsizedWidth(width);
        }
        return builder.build();
    }

    /**
     * @param textView
     * @param width
     * @return sdk < 23
     */
    private static StaticLayout getStaticLayout(TextView textView, int width) {
        return new StaticLayout(textView.getText(),
                0, textView.getText().length(),
                textView.getPaint(), width, Layout.Alignment.ALIGN_NORMAL,
                textView.getLineSpacingMultiplier(),
                textView.getLineSpacingExtra(), textView.getIncludeFontPadding(), textView.getEllipsize(),
                width);
    }
}
