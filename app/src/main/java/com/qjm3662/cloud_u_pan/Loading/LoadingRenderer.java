package com.qjm3662.cloud_u_pan.Loading;

/**
 * Created by qjm3662 on 2016/11/4 0004.
 */

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.qjm3662.cloud_u_pan.Tool.DensityUtil;


/**
 * 抽象类（加载绘制）
 */
public abstract class LoadingRenderer {
    private static final long ANIMATION_DURATION = 1333;//动画持续时间
    private static final float DEFAULT_SIZE = 56.0f;    //默认大小

    /**
     * 监听器用于动画在播放过程中的重要动画事件，动画每播放一帧时调用
     * 在动画过程中，可侦听此事件来获取并使用 ValueAnimator 计算出来的属性值。
     * 利用传入事件的 ValueAnimator 对象，调用其 getAnimatedValue() 方法即可获取当前的属性值。
     * 如果使用 ValueAnimator来实现动画的话 ，则必需实现此侦听器。
     */
    private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener
            = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            computeRender((float) animation.getAnimatedValue());
            invalidateSelf();
        }
    };

    /**
     * 边界对象
     * Whenever {@link LoadingDrawable} boundary changes mBounds will be updated.
     * More details you can see {@link LoadingDrawable#onBoundsChange(Rect)}
     */
    protected final Rect mBounds = new Rect();

    //Drawable就是一个可画的对象
    //绘制对象的回调接口对象
    private Drawable.Callback mCallback;

    //ValueAnimator是属性动画（Property Animation）系统的核心类，它包含了配置Property Animation属性的大部分方法
    private ValueAnimator mRenderAnimator;

    protected long mDuration;       //持续时间
    protected float mWidth;
    protected float mHeight;

    /**
     * 构造函数
     * @param context
     */
    public LoadingRenderer(Context context) {
        initParams(context);//参数初始化
        setupAnimators();
    }

    @Deprecated
    protected void draw(Canvas canvas, Rect bounds) {
    }

    protected void draw(Canvas canvas) {
        draw(canvas, mBounds);
    }

    /**
     * 计算
     * @param renderProgress
     */
    protected abstract void computeRender(float renderProgress);

    /**
     * 设置透明度
     * @param alpha
     */
    protected abstract void setAlpha(int alpha);

    /**
     * 设置颜色过滤器
     * @param cf
     */
    protected abstract void setColorFilter(ColorFilter cf);

    /**
     * 重启
     */
    protected abstract void reset();

    /**
     * 添加监听器
     * @param animatorListener
     */
    protected void addRenderListener(Animator.AnimatorListener animatorListener) {
        mRenderAnimator.addListener(animatorListener);
    }


    /**
     * 启动绘制
     */
    void start() {
        reset();
        mRenderAnimator.addUpdateListener(mAnimatorUpdateListener);

        mRenderAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRenderAnimator.setDuration(mDuration);
        mRenderAnimator.start();
    }

    /**
     * 停止绘制操作
     */
    void stop() {
        // if I just call mRenderAnimator.end(),
        // it will always call the method onAnimationUpdate(ValueAnimator animation)
        // why ? if you know why please send email to me (dinus_developer@163.com)
        mRenderAnimator.removeUpdateListener(mAnimatorUpdateListener);
        mRenderAnimator.setRepeatCount(0);
        mRenderAnimator.setDuration(0);
        mRenderAnimator.end();
    }

    /**
     * 判断动画是否正在运行
     * @return
     */
    boolean isRunning() {
        return mRenderAnimator.isRunning();
    }


    /**
     * 设置回调接口
     * @param callback
     */
    void setCallback(Drawable.Callback callback) {
        this.mCallback = callback;
    }

    /**
     * 设置范围（界限）
     * @param bounds
     */
    void setBounds(Rect bounds) {
        mBounds.set(bounds);
    }

    /**
     * 参数初始化
     * @param context
     */
    private void initParams(Context context) {
        //将大小设置为默认的56dp*56dp,持续时间也设置为默认的
        mWidth = DensityUtil.dip2px(context, DEFAULT_SIZE);
        mHeight = DensityUtil.dip2px(context, DEFAULT_SIZE);
        mDuration = ANIMATION_DURATION;
    }

    /**
     * 安装动画
     * http://blog.csdn.net/yegongheng/article/details/38435553（ValueAnimator和ObjectAnimator实现动画实例）
     */
    private void setupAnimators() {
        mRenderAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);        //实例化ValueAnimator对象，并设置目标属性的属性名、初始值或结束值等值;
        mRenderAnimator.setRepeatCount(Animation.INFINITE);         //设置重复次数（该参数表示无限期的重复，直到finish）
        mRenderAnimator.setRepeatMode(ValueAnimator.RESTART);       /**这个地方做了修改，原来的参数是Animtion.RESTART*/
        mRenderAnimator.setDuration(mDuration);                     //设置持续时间
        //fuck you! the default interpolator（插值器） is AccelerateDecelerateInterpolator
        mRenderAnimator.setInterpolator(new LinearInterpolator());  //设置插值器（LinearInterpolator匀速播放）
        mRenderAnimator.addUpdateListener(mAnimatorUpdateListener);
    }

    /**
     * 使自己失效
     */
    private void invalidateSelf() {
        mCallback.invalidateDrawable(null);
    }

}
