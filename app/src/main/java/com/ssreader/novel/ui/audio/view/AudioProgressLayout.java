package com.ssreader.novel.ui.audio.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.sxu.shadowdrawable.ShadowDrawable;
import com.ssreader.novel.R;
import com.ssreader.novel.ui.utils.ImageUtil;

/**
 * 用于在特定界面展示播放进度
 *
 * @author admin
 * @version 2020/02/11
 */
public class AudioProgressLayout extends LinearLayout {

    private int parentType = 0;
    // 父布局
    private LinearLayout linearLayout;
    // 圆形进度条
    private CircularProgressBar progressBar;
    // 旋转图片
    private ImageView roundImageView;
    // 取消
    private RelativeLayout close;
    // 状态
    private ImageView statusImage;
    private boolean isPlayer = false;

    // 设置旋转动画
    private ObjectAnimator objectAnimator;
    // 设置属性动画的值
    private float startValues = 0f;

    // 父布局的宽高
    private int maxWidth, maxHeight;
    // 上次点击位置
    private int lastX, lastY;
    // 用于判断是拖动还是点击
    private boolean isDrag = false;

    private OnProgressLayoutClickListener layoutClickListener;

    public void setLayoutClickListener(OnProgressLayoutClickListener layoutClickListener) {
        this.layoutClickListener = layoutClickListener;
    }

    public AudioProgressLayout(Context context) {
        this(context, null);
    }

    public AudioProgressLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioProgressLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            initView(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     *
     * @param context
     * @throws Exception
     */
    private void initView(Context context) throws Exception {
        if (Build.VERSION.SDK_INT <= 22) {
            this.addView(LayoutInflater.from(context).inflate(R.layout.audio_progress_layout, this));
        } else {
            LayoutInflater.from(context).inflate(R.layout.audio_progress_layout, this);
        }
        linearLayout = (LinearLayout) findViewById(R.id.audio_progress_bg);
        progressBar = (CircularProgressBar) findViewById(R.id.audio_progress_progressBar);
        roundImageView = (ImageView) findViewById(R.id.audio_progress_image);
        statusImage = (ImageView) findViewById(R.id.audio_progress_status);
        close = (RelativeLayout) findViewById(R.id.audio_progress_close_layout);
        // 设置布局样式
        ShadowDrawable.setShadowDrawable(linearLayout, ContextCompat.getColor(getContext(), R.color.alpha_white),
                ImageUtil.dp2px(getContext(), 23), ContextCompat.getColor(getContext(), R.color.gray_3),
                ImageUtil.dp2px(getContext(), 2), 2, 2);
        setCircleShape(roundImageView, ImageUtil.dp2px(context, 19));
        initListener();
    }

    private void initListener() {
        progressBar.setProgressListener(new CircularProgressBar.OnProgressListener() {
            @Override
            public void onEnd() {
                setImageAnimator(false);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        long time = System.currentTimeMillis();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 默认不拖动
                isDrag = false;
                // 屏蔽父布局的点击事件
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    maxWidth = parent.getWidth();
                    maxHeight = parent.getHeight();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (maxWidth <= 0 || maxHeight <= 0) {
                    // 如果不存在父类的宽高则无法拖动，默认直接返回false
                    isDrag = false;
                    break;
                }
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                // 这里修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                // 此处稍微增加一些移动的偏移量，防止手指抖动，误判为移动无法触发点击时间
                if (distance == 0) {
                    isDrag = false;
                    break;
                }
                if (parentType == 0) {
                    // 程序到达此处一定是正在拖动了
                    isDrag = true;
                    float x = getX() + dx;
                    float y = getY() + dy;
                    // 检测是否到达边缘 左上右下
                    y = getY() < 200 ? 200 : getY() + getHeight() > maxHeight - 250 ? maxHeight - getHeight() - 250 : y;
                    setX(x);
                    setY(y);
                    lastX = rawX;
                    lastY = rawY;
                } else {
                    isDrag = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isDrag && (System.currentTimeMillis() - time < 50)) {
                    if ((event.getX() < roundImageView.getRight()) && (event.getY() < roundImageView.getBottom())) {
                        if (layoutClickListener != null) {
                            layoutClickListener.onIntoAudio();
                        }
                    } else if (event.getY() > statusImage.getTop() && event.getX() > statusImage.getLeft() &&
                            event.getX() < statusImage.getRight() && event.getY() < statusImage.getBottom()) {
                        if (layoutClickListener != null) {
                            layoutClickListener.onPlayer();
                        }
                    } else if (event.getY() > close.getTop() && event.getX() > close.getLeft() &&
                            event.getX() < close.getRight() && event.getY() < close.getBottom()) {
                        if (layoutClickListener != null) {
                            if (objectAnimator != null) {
                                objectAnimator.pause();
                            }
                            layoutClickListener.onCancel();
                        }
                    }
                }
                welt(rawX);
                break;
        }
        return true;
    }

    private boolean isLeftSide() {
        return getX() == 0;
    }

    private boolean isRightSide() {
        return getX() == maxWidth - getWidth();
    }

    private void welt(int currentX) {
        if (!isLeftSide() || !isRightSide()) {
            if (currentX >= maxWidth / 2) {
                //靠右吸附
                animate().setInterpolator(new DecelerateInterpolator())
                        .setDuration(400)
                        .xBy(maxWidth - getWidth() - getX())
                        .start();
            } else {
                //靠左吸附
                ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                oa.setInterpolator(new DecelerateInterpolator());
                oa.setDuration(400);
                oa.start();
            }
        }
    }

    /**
     * 设置最大值
     *
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress) {
        progressBar.setMaxProgress(maxProgress);
    }

    /**
     * 获取最大值
     *
     * @return
     */
    public int getMaxProgress() {
        return progressBar.getMaxProgress();
    }

    /**
     * 设置值
     *
     * @param progress
     */
    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    /**
     * 获取最大值
     *
     * @return
     */
    public int getProgress() {
        return progressBar.getProgress();
    }

    /**
     * 设置图片地址
     *
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .error(R.mipmap.appic)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(roundImageView);
    }

    /**
     * 设置是否播放
     *
     * @param isPlayer
     */
    public void setPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
        if (isPlayer) {
            statusImage.setImageResource(R.mipmap.audio_layout_pause);
            setImageAnimator(true);
        } else {
            statusImage.setImageResource(R.mipmap.audio_layout_player);
            setImageAnimator(false);
        }
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    /**
     * 设置裁剪为圆形
     *
     * @param view
     * @param padding 这个是设置间距是长或宽的几分之一
     */
    public static void setCircleShape(View view, final int padding) {
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int margin = Math.min(view.getWidth(), view.getHeight()) / padding;
                outline.setOval(margin, margin, view.getWidth() - margin, view.getHeight() - margin);
            }
        });
    }

    /**
     * 设置图片的动画
     *
     * @param isAnimator
     */
    public void setImageAnimator(boolean isAnimator) {
        if (isAnimator) {
            if (objectAnimator == null) {
                initAnimator();
            } else {
                objectAnimator.resume();
            }
        } else {
            if (objectAnimator != null) {
                objectAnimator.pause();
            }
        }
    }

    /**
     * 设置动画
     */
    private void initAnimator() {
        objectAnimator = ObjectAnimator.ofFloat(roundImageView, "rotation", startValues, 360f);
        objectAnimator.setDuration(15 * 1000);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(-1);
        objectAnimator.start();
    }

    /**
     * 用于判断父界面
     * @param parentType
     */
    public void setParentType(int parentType) {
        this.parentType = parentType;
    }

    public interface OnProgressLayoutClickListener {

        void onIntoAudio();

        void onPlayer();

        void onCancel();
    }
}
