package com.qjm3662.cloud_u_pan.Widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qjm3662.cloud_u_pan.R;

/**
 * Created by qjm3662 on 2016/11/8 0008.
 */

public class EasyButton_RoundImage extends EasyButton {
    public EasyButton_RoundImage(Context context) {
        super(context);
    }

    public EasyButton_RoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyButton_RoundImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mRadius = Math.round(MeasureSpec.getSize(widthMeasureSpec) / 2);
        mTickLen = Math.round(mRadius / 180 * 3.14 * mSweepAngMin);
        mMaxRippleWidth = mRadius / 2;
        mLineWidth = Math.round(mRadius / 100);
        mRippleWidth = mLineWidth;
        mInitRippleWidth = mRippleWidth;
        mRecWidth = 0;
        float mWidth = mRecWidth + 2 * (mRadius + mLineWidth + mMaxRippleWidth);
        float mHeight = 2 * (mRadius + mLineWidth + mMaxRippleWidth);
        mLineRadius = mRadius + mLineWidth;
        cxLeft = mLineRadius + mMaxRippleWidth;
        cyLeft = mLineRadius + mMaxRippleWidth;
        cxRight = cxLeft + mRecWidth;
        cyRight = cyLeft;


        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mShadowPaint.setStrokeWidth(mLineWidth);
        mShadowPaint.setStrokeCap(Paint.Cap.ROUND);

        int width = MeasureSpec.makeMeasureSpec((int) mWidth, MeasureSpec.getMode(widthMeasureSpec));
        int height = MeasureSpec.makeMeasureSpec((int) mHeight, MeasureSpec.getMode(heightMeasureSpec));
        setMeasuredDimension(width, width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mStatus.equals(INIT)) {
                    startAnimation();
                } else {
                    mStatus = INIT;
                    rippleAnimator.cancel();
                    rippleAlphaAnimator.cancel();
                    startAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mRippleWidth = mInitRippleWidth;
//                mStatus = INIT;
                break;
        }
        super.onTouchEvent(event);
        return true;
    }


    @Override
    void drawButton(Canvas canvas) {
        if (mStatus.equals(TICK)) {
            setTextColor(Color.TRANSPARENT);
        } else {
            setTextColor(mInitTextColor);
            mButtonPaint.setColor(mInitBtnColor);
        }
        canvas.drawArc(new RectF(cxLeft - mRadius, cyLeft - mRadius, cxLeft + mRadius, cyLeft + mRadius),
                90, 360, false, mButtonPaint);
        canvas.drawRect(cxLeft, cyLeft - mRadius, cxRight, cyRight + mRadius, mButtonPaint);
        RectF rectF = new RectF(cxRight - mRadius, cyRight - mRadius, cxRight + mRadius, cyRight +
                mRadius);
        canvas.drawArc(rectF, -90, 360, false, mButtonPaint);
//        System.out.println(rectF.toString());
    }


    @Override
    protected void startRippleAnim() {
        mStatus = RIPPLE;
        rippleAnimator = ValueAnimator.ofFloat(mRippleWidth, mMaxRippleWidth);
        rippleAlphaAnimator = ValueAnimator.ofInt(255, 0);
        rippleAnimator.setDuration(mRippleDuration);
        rippleAlphaAnimator.setDuration(mRippleDuration);
        rippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRippleWidth = (Float) animation.getAnimatedValue();
            }
        });
        rippleAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRippleAlpha = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rippleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRippleWidth = mInitRippleWidth;
                mRippleAlpha = 160;
                mStatus = LINE_MOVE;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRippleWidth = mInitRippleWidth;
                mRippleAlpha = 255;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(rippleAnimator, rippleAlphaAnimator);
        animSet.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        switch (mStatus) {
            case INIT:
                this.setLayerType(View.LAYER_TYPE_SOFTWARE, mLinePaint);
                mLinePaint.setShadowLayer(10, 1, 1, getResources().getColor(R.color.deepBlue));//偏移度很小时则变成发光字体
                drawButton(canvas);
                break;
            case RIPPLE:
                drawRipple(canvas);
                drawButton(canvas);
                break;
            case LINE_MOVE:
                drawButton(canvas);
                break;
        }
        super.onDraw(canvas);
    }
}
