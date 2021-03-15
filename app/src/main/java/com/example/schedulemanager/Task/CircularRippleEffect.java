package com.example.schedulemanager.Task;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.PorterDuff.Mode.CLEAR;

import static androidx.core.content.ContextCompat.getColor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

import com.example.schedulemanager.R;

import butterknife.ButterKnife;

public class CircularRippleEffect extends View {
    private static final int ADDITIONAL_SIZE_TO_CLEAR_ANTIALIASING = 1;
    private Paint circlePaint = new Paint();
    private Paint maskPaint = new Paint();

    private Bitmap tempBitmap;
    private Canvas tempCanvas;

    private float outerCircleRadiusProgress = 0f;
    private float innerCircleRadiusProgress = 0f;

    private int maxCircleSize;

    private int rippleSize;

    public CircularRippleEffect(Context context) {
        super(context);
        init();
    }

    public CircularRippleEffect(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircularRippleEffect,
                0, 0);

        try {
            rippleSize = typedArray.getDimensionPixelSize(
                    R.styleable.CircularRippleEffect_rippleSize, 500);
        } finally {
            typedArray.recycle();
        }
        init();
    }

    public CircularRippleEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        int dotColor = getColor(getContext(), R.color.colorPrimaryDark);
        circlePaint.setStyle(FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(dotColor);
        maskPaint.setXfermode(new PorterDuffXfermode(CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxCircleSize = w / 2;
        tempBitmap = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);
        tempCanvas = new Canvas(tempBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tempCanvas.drawColor(Color.WHITE, CLEAR);
        tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2,
                outerCircleRadiusProgress * maxCircleSize, circlePaint);
        tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, innerCircleRadiusProgress
                * (maxCircleSize + ADDITIONAL_SIZE_TO_CLEAR_ANTIALIASING), maskPaint);
        canvas.drawBitmap(tempBitmap, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(rippleSize, rippleSize);
    }

    public void setInnerCircleRadiusProgress(float innerCircleRadiusProgress) {
        this.innerCircleRadiusProgress = innerCircleRadiusProgress;
        postInvalidate();
    }

    public float getInnerCircleRadiusProgress() {
        return innerCircleRadiusProgress;
    }

    public void setOuterCircleRadiusProgress(float outerCircleRadiusProgress) {
        this.outerCircleRadiusProgress = outerCircleRadiusProgress;
        postInvalidate();
    }

    public float getOuterCircleRadiusProgress() {
        return outerCircleRadiusProgress;
    }

    public static final Property<CircularRippleEffect, Float> INNER_CIRCLE_RADIUS_PROGRESS =
            new Property<CircularRippleEffect, Float>(Float.class, "innerCircleRadiusProgress") {
                @Override
                public Float get(CircularRippleEffect object) {
                    return object.getInnerCircleRadiusProgress();
                }

                @Override
                public void set(CircularRippleEffect object, Float value) {
                    object.setInnerCircleRadiusProgress(value);
                }
            };

    public static final Property<CircularRippleEffect, Float> OUTER_CIRCLE_RADIUS_PROGRESS =
            new Property<CircularRippleEffect, Float>(Float.class, "outerCircleRadiusProgress") {
                @Override
                public Float get(CircularRippleEffect object) {
                    return object.getOuterCircleRadiusProgress();
                }

                @Override
                public void set(CircularRippleEffect object, Float value) {
                    object.setOuterCircleRadiusProgress(value);
                }
            };
}