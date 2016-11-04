package com.qjm3662.cloud_u_pan.Loading;

/**
 * Created by qjm3662 on 2016/11/4 0004.
 */
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;



public class LoadingDrawable extends Drawable implements Animatable {
    private final LoadingRenderer mLoadingRender;

    /**
     * 实现子Drawable的接口
     */
    private final Callback mCallback = new Callback() {

        //当Drawable需要被重回的时候触发
        @Override
        public void invalidateDrawable(Drawable d) {
            invalidateSelf();
        }

        @Override
        public void scheduleDrawable(Drawable d, Runnable what, long when) {
            scheduleSelf(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable d, Runnable what) {
            unscheduleSelf(what);
        }
    };


    public LoadingDrawable(LoadingRenderer loadingRender) {
        this.mLoadingRender = loadingRender;
        this.mLoadingRender.setCallback(mCallback);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mLoadingRender.setBounds(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!getBounds().isEmpty()) {
            this.mLoadingRender.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        this.mLoadingRender.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        this.mLoadingRender.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    /**
     * 以下三个实现自Animatable的接口
     */
    @Override
    public void start() {
        this.mLoadingRender.start();
    }

    @Override
    public void stop() {
        this.mLoadingRender.stop();
    }

    @Override
    public boolean isRunning() {
        return this.mLoadingRender.isRunning();
    }



    @Override
    public int getIntrinsicHeight() {
        return (int) this.mLoadingRender.mHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) this.mLoadingRender.mWidth;
    }
}