package com.ssreader.novel.ui.read.animation;

import android.graphics.Canvas;
import android.view.View;

/**
 * 无动画，不能取消翻页
 */
public class NonePageAnim extends HorizonPageAnim {

    public NonePageAnim(int w, int h, View view, OnPageChangeListener listener) {
        super(w, h, view, listener);
    }

    @Override
    public void drawStatic(Canvas canvas) {
//        if (isCancel) {
//            canvas.drawBitmap(mCurBitmap, 0, 0, null);
//        } else {
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
//        }
    }

    @Override
    public void drawMove(Canvas canvas) {
//        if (isCancel) {
//            canvas.drawBitmap(mCurBitmap, 0, 0, null);
//        } else {
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
//        }
    }

    @Override
    public void startAnim() {
        onAnimationStopped.Stop(mStartX > mScreenWidth / 2, isCancel);
//        onAnimationStopped.Stop(true, isCancel);
//        if (onAnimationStopped != null) {
//            MyToash.setDelayedHandle(100, new MyToash.DelayedHandle() {
//                @Override
//                public void handle() {
//                    onAnimationStopped.Stop(mStartX > mScreenWidth / 2, isCancel);
//                }
//            });
//        }
    }
}
