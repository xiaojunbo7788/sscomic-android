package com.ssreader.novel.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 带有边框的TextView
 */
public class BorderTextView extends AppCompatTextView {

    public BorderTextView(Context context) {
        this(context, null);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param borderColor  border颜色
     * @param borderWidths border 宽度
     * @param borderRadius border 圆角半径
     */
    public void setBorder(final int borderColor, final int[] borderWidths, final int[] borderRadius) {
        setTextColor(borderColor);
        Drawable drawable = new BitmapDrawable() {
            @Override
            public void draw(Canvas canvas) {
                super.draw(canvas);
                drawBorder(canvas, borderColor, borderWidths, borderRadius);
            }
        };
        setBackground(drawable);
    }

    /**
     * 绘制border
     */
    public void drawBorder(Canvas canvas, final int borderColor, final int[] borderWidths, final int[] borderRadius) {
        Rect rect = canvas.getClipBounds();
        final int width = rect.width();
        final int height = rect.height();

        int borderWidthLeft;
        int borderWidthTop;
        int borderWidthRight;
        int borderWidthBottom;

        if (borderWidths != null && borderWidths.length == 4) {
            borderWidthLeft = Math.min(width / 2, borderWidths[0]);
            borderWidthTop = Math.min(height / 2, borderWidths[1]);
            borderWidthRight = Math.min(width / 2, borderWidths[2]);
            borderWidthBottom = Math.min(height / 2, borderWidths[3]);
        } else {
            return;
        }

        // 设置画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidthLeft);

        // 当边框宽度均相等
        if ((borderWidthLeft == borderWidthTop) && (borderWidthLeft == borderWidthRight) && (borderWidthLeft == borderWidthBottom)) {
            if (borderWidthLeft == 0) {
                return;
            }
            // borderRadius != null且borderWidth!-0;计算并画出圆角边框，否则为直角边框
            if (borderRadius != null && borderRadius.length == 4) {
                int sum = 0;
                for (int i = 0; i < borderRadius.length; i++) {
                    if (borderRadius[i] < 0) {
                        return;
                    }
                    sum += borderRadius[i];
                }
                if (sum == 0) {
                    canvas.drawRect(rect, paint);
                }

                int borderWidth = borderWidthLeft;

                int mMaxRadiusX = width / 2 - borderWidth / 2;
                int mMaxRadiusY = height / 2 - borderWidth / 2;

                int topLeftRadiusX = Math.min(mMaxRadiusX, borderRadius[0]);
                int topLeftRadiusY = Math.min(mMaxRadiusY, borderRadius[0]);
                int topRightRadiusX = Math.min(mMaxRadiusX, borderRadius[1]);
                int topRightRadiusY = Math.min(mMaxRadiusY, borderRadius[1]);
                int bottomRightRadiusX = Math.min(mMaxRadiusX, borderRadius[2]);
                int bottomRightRadiusY = Math.min(mMaxRadiusY, borderRadius[2]);
                int bottomLeftRadiusX = Math.min(mMaxRadiusX, borderRadius[3]);
                int bottomLeftRadiusY = Math.min(mMaxRadiusY, borderRadius[3]);

                if (topLeftRadiusX < borderWidth || topLeftRadiusY < borderWidth) {
                    RectF arc1 = new RectF(0, 0, topLeftRadiusX * 2, topLeftRadiusY * 2);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawArc(arc1, 180, 90, true, paint);
                } else {
                    RectF arc1 = new RectF(borderWidth / 2, borderWidth / 2, topLeftRadiusX * 2 - borderWidth / 2, topLeftRadiusY * 2 - borderWidth / 2);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(arc1, 180, 90, false, paint);
                }

                canvas.drawLine(topLeftRadiusX, borderWidth / 2, width - topRightRadiusX, borderWidth / 2, paint);

                if (topRightRadiusX < borderWidth || topRightRadiusY < borderWidth) {
                    RectF arc2 = new RectF(width - topRightRadiusX * 2, 0, width, topRightRadiusY * 2);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawArc(arc2, 270, 90, true, paint);
                } else {
                    RectF arc2 = new RectF(width - topRightRadiusX * 2 + borderWidth / 2, borderWidth / 2, width - borderWidth / 2, topRightRadiusY * 2 - borderWidth / 2);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(arc2, 270, 90, false, paint);
                }

                canvas.drawLine(width - borderWidth / 2, topRightRadiusY, width - borderWidth / 2, height - bottomRightRadiusY, paint);

                if (bottomRightRadiusX < borderWidth || bottomRightRadiusY < borderWidth) {
                    RectF arc3 = new RectF(width - bottomRightRadiusX * 2, height - bottomRightRadiusY * 2, width, height);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawArc(arc3, 0, 90, true, paint);
                } else {
                    RectF arc3 = new RectF(width - bottomRightRadiusX * 2 + borderWidth / 2, height - bottomRightRadiusY * 2 + borderWidth / 2, width - borderWidth / 2, height - borderWidth / 2);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(arc3, 0, 90, false, paint);
                }

                canvas.drawLine(bottomLeftRadiusX, height - borderWidth / 2, width - bottomRightRadiusX, height - borderWidth / 2, paint);

                if (bottomLeftRadiusX < borderWidth || bottomLeftRadiusY < borderWidth) {
                    RectF arc4 = new RectF(0, height - bottomLeftRadiusY * 2, bottomLeftRadiusX * 2, height);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawArc(arc4, 90, 90, true, paint);
                } else {
                    RectF arc4 = new RectF(borderWidth / 2, height - bottomLeftRadiusY * 2 + borderWidth / 2, bottomLeftRadiusX * 2 - borderWidth / 2, height - borderWidth / 2);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(arc4, 90, 90, false, paint);
                }

                canvas.drawLine(borderWidth / 2, topLeftRadiusY, borderWidth / 2, height - bottomLeftRadiusY, paint);
            } else {
                canvas.drawRect(rect, paint);
            }
        } else {
            if (borderWidthLeft > 0) {
                paint.setStrokeWidth(borderWidthLeft);
                canvas.drawLine(borderWidthLeft / 2, rect.top, borderWidthLeft / 2, rect.bottom, paint);
            }
            if (borderWidthTop > 0) {
                paint.setStrokeWidth(borderWidthTop);
                canvas.drawLine(rect.left, borderWidthTop / 2, rect.right, borderWidthTop / 2, paint);
            }
            if (borderWidthRight > 0) {
                paint.setStrokeWidth(borderWidthRight);
                canvas.drawLine(rect.right - borderWidthRight / 2, rect.top, rect.right - borderWidthRight / 2, rect.bottom, paint);
            }
            if (borderWidthBottom > 0) {
                paint.setStrokeWidth(borderWidthBottom);
                canvas.drawLine(rect.left, rect.bottom - borderWidthBottom / 2, width, rect.bottom - borderWidthBottom / 2, paint);
            }
        }
    }
}
