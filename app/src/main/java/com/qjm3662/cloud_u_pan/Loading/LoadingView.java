package com.qjm3662.cloud_u_pan.Loading;

/**
 * Created by qjm3662 on 2016/11/4 0004.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.qjm3662.cloud_u_pan.R;


public class LoadingView extends ImageView {
    private LoadingDrawable mLoadingDrawable;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        try {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
            int loadingRendererId = ta.getInt(R.styleable.LoadingView_loading_renderer, 0);
            LoadingRenderer loadingRenderer = LoadingRendererFactory.createLoadingRenderer(context, loadingRendererId);
            setLoadingRenderer(loadingRenderer);
            ta.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoadingRenderer(LoadingRenderer loadingRenderer) {
        mLoadingDrawable = new LoadingDrawable(loadingRenderer);
        setImageDrawable(mLoadingDrawable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    public void startAnimation() {
        if (mLoadingDrawable != null) {
            mLoadingDrawable.start();
        }
    }

    public void stopAnimation() {
        if (mLoadingDrawable != null) {
            mLoadingDrawable.stop();
        }
    }

    public void dismiss(){
        stopAnimation();
        this.setVisibility(INVISIBLE);
    }

    public void show(){
        startAnimation();
        this.setVisibility(VISIBLE);
    }
}