package com.qjm3662.cloud_u_pan.Widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.R;

/**
 * Created by qjm3662 on 2016/9/20 0020.
 */
public class EasyButton extends TextView {
    private float mRadius;
    private float mRecWidth;
    private float mLinePosX;
    private float mSweepAng = 180;
    private float mLineWidth;
    private float mLineRadius;
    private float mRippleWidth;
    private float mInitRippleWidth;
    private float mMaxRippleWidth;
    private float mSweepAngMin = 20f;
    private float mTickLen;
    private float cxLeft;
    private float cyLeft;
    private float cxRight;
    private float cyRight;
    private int mInitTextColor;
    private long mRippleDuration;
    private Paint mButtonPaint;
    private Paint mLinePaint;
    private Paint mShadowPaint;
    private Paint mRipplePaint;
    private Paint mTickPaint;
    private int mRippleAlpha;
    private String mStatus = "INIT";
    private final String INIT = "INIT";
    private final String RIPPLE = "RIPPLE";
    private final String LINE_MOVE = "LINE_MOVE";
    private final String TICK = "TICK";
    private ValueAnimator rippleAnimator;
    private ValueAnimator rippleAlphaAnimator;
    private int mInitBtnColor;
    private int mLineColor;
    private int mTickColor;

    public EasyButton(Context context) {
        super(context);
        init(null);
    }

    public EasyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EasyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mStatus) {
            case INIT:
                this.setLayerType(View.LAYER_TYPE_SOFTWARE, mLinePaint);
//                this.setLayerType(View.LAYER_TYPE_SOFTWARE, mButtonPaint);
                mLinePaint.setShadowLayer(10, 1, 1, getResources().getColor(R.color.deepBlue));//偏移度很小时则变成发光字体
//                mButtonPaint.setShadowLayer(10,1,1, getResources().getColor(R.color.blue));//偏移度很小时则变成发光字体;
                drawLine(canvas, 90, 180, 0);
                drawButton(canvas);
                break;
            case RIPPLE:
                drawRipple(canvas);
                drawLine(canvas, 90, 180, 0);
                drawButton(canvas);
                break;
            case LINE_MOVE:
                drawLine(canvas, 90, mSweepAng, mLinePosX);
                drawButton(canvas);
                break;
        }
        super.onDraw(canvas);
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

                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float textSize = getTextSize();
        float textLen = getText().length();
        mRadius = Math.round(2 * textSize);
        mTickLen = Math.round(mRadius / 180 * 3.14 * mSweepAngMin);
        mMaxRippleWidth = mRadius / 2;
        mLineWidth = Math.round(mRadius / 100);

//        Log.e("mLineWidth", mLineWidth + "");
        mRippleWidth = mLineWidth;
        mInitRippleWidth = mRippleWidth;
        mRecWidth = textLen * textSize;
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
        super.onMeasure(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setGravity(Gravity.CENTER);
        super.onLayout(changed, left, top, right, bottom);
    }

    void init(AttributeSet attrs) {

        TypedArray typeArray = getContext().obtainStyledAttributes(attrs,
                com.spark.submitbutton.R.styleable.SubmitButton);
        mButtonPaint = new Paint();
        mInitBtnColor = typeArray.getColor(com.spark.submitbutton.R.styleable.SubmitButton_sub_btn_background,
                ContextCompat.getColor(getContext(), com.spark.submitbutton.R.color.sub_btn_background));
        mButtonPaint.setColor(mInitBtnColor);
        mButtonPaint.setAntiAlias(true);
        mLinePaint = new Paint();
        mLineColor = typeArray.getColor(com.spark.submitbutton.R.styleable.SubmitButton_sub_btn_line_color,
                ContextCompat.getColor(getContext(), com.spark.submitbutton.R.color.sub_btn_line));
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);

        mShadowPaint = new Paint();
        int color = getResources().getColor(R.color.shadow);
        mShadowPaint.setColor(color);
        mShadowPaint.setAntiAlias(true);


        mRipplePaint = new Paint();
        int mRippleColor = typeArray.getColor(com.spark.submitbutton.R.styleable.SubmitButton_sub_btn_ripple_color,
                ContextCompat.getColor(getContext(), com.spark.submitbutton.R.color.sub_btn_ripple));
        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setAntiAlias(true);
        mRippleDuration = typeArray.getInt(com.spark.submitbutton.R.styleable.SubmitButton_sub_btn_duration, 500) / 6;
        mInitTextColor = getCurrentTextColor();
        typeArray.recycle();
    }

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
        canvas.drawArc(new RectF(cxRight - mRadius, cyRight - mRadius, cxRight + mRadius, cyRight +
                mRadius), -90, 360, false, mButtonPaint);
    }

    void drawLine(Canvas canvas, float startAng, float sweepAng, float startPosX) {
        float radius = mLineRadius - mLineWidth / 2;
        canvas.drawArc(new RectF(cxLeft - radius, cyLeft - radius, cxLeft + radius,
                cyLeft + radius), -startAng, -sweepAng, false, mLinePaint);
        canvas.drawLine(cxLeft + startPosX, cyLeft - radius,
                cxRight, cyLeft - radius, mLinePaint);
        canvas.drawLine(cxLeft, cyLeft + radius,
                cxRight - startPosX, cyLeft + radius, mLinePaint);
        canvas.drawArc(new RectF(cxRight - radius, cyRight - radius, cxRight + radius,
                cyRight + radius), startAng, -sweepAng, false, mLinePaint);
    }

    void drawShadow(Canvas canvas, float startAng, float sweepAng, float startPosX) {
        float radius = mLineRadius - mLineWidth / 2;
        canvas.drawArc(new RectF(cxLeft - radius, cyLeft - radius, cxLeft + radius,
                cyLeft + radius), -startAng, -sweepAng, false, mShadowPaint);
        canvas.drawLine(cxLeft + startPosX, cyLeft - radius,
                cxRight, cyLeft - radius, mShadowPaint);
        canvas.drawLine(cxLeft, cyLeft + radius,
                cxRight - startPosX, cyLeft + radius, mShadowPaint);
        canvas.drawArc(new RectF(cxRight - radius, cyRight - radius, cxRight + radius,
                cyRight + radius), startAng, -sweepAng, false, mShadowPaint);
    }

    void drawRipple(Canvas canvas) {
        //TODO precise problem
        mRipplePaint.setAlpha(mRippleAlpha);
        float mRippleRadius = mLineRadius + mRippleWidth;
        canvas.drawArc(new RectF(cxLeft - mRippleRadius, cyLeft - mRippleRadius,
                cxLeft + mRippleRadius, cyLeft + mRippleRadius), 90, 180, false, mRipplePaint);
        canvas.drawRect(cxLeft, cyLeft - mLineRadius - mRippleWidth,
                cxRight, cyRight + mLineRadius + mRippleWidth, mRipplePaint);
        canvas.drawArc(new RectF(cxRight - mRippleRadius, cyRight - mRippleRadius,
                cxRight + mRippleRadius, cyRight + mRippleRadius), -90, 180, false, mRipplePaint);
    }

    public void startAnimation() {
        startRippleAnim();
    }

    private void startRippleAnim() {
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
                mRippleAlpha = 255;
//                startLineAnim();
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
}