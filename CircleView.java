package com.example.administrator.canvastest.canvas;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.style.LineHeightSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

import com.example.administrator.canvastest.R;

/**
 * Created by Administrator on 2015/12/22 0022.
 */
public class CircleView extends View {

    private  int mMinHeight = 300;
    private  int mMinWidth = 300;

    private Paint mPtInnerCircle;
    private Paint mPtOutCircle;
    private Paint mPtCenterCirCle;
    private Paint mPtText;

    private int mInnerRadus;
    private int mCenterWidth;
    private int mOutWidth;

    private int mInnerColor;
    private int mOutColor;
    private int mCenterColor;
    private int mTextColor;

    private String mCenterText = "Start ENGING";

    private int mViewWidth;
    private int mViewHeight;
    private int mCenterX;
    private int mCenterY;

    private int mArcAngle;
    private int mArcStartAngle;

    private boolean mStartAnination;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int designWidth = 0;
        int designHeight = 0;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mMinHeight = (int) (2.5*(mInnerRadus+mCenterWidth+mOutWidth));
        mMinWidth = (int) (2.5*(mInnerRadus+mCenterWidth+mOutWidth));
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED){
            designWidth = Math.min(width, mMinWidth);
        }else {
            designWidth = width;
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED){
            designHeight = Math.min(height, mMinHeight);
        }else {
            designHeight = height;
        }

        setMeasuredDimension(designWidth, designHeight);
    }

    public void initAttr(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mInnerRadus = typedArray.getInt(R.styleable.CircleView_inner_raduis, 150);
        mOutWidth = typedArray.getInt(R.styleable.CircleView_outer_raduis, 10);
        mCenterWidth = typedArray.getInt(R.styleable.CircleView_center_raduis, 20);

        mArcAngle = typedArray.getInt(R.styleable.CircleView_arc_angle, 30);
        mArcStartAngle = typedArray.getInt(R.styleable.CircleView_start_angle, 0);

        mInnerColor = typedArray.getColor(R.styleable.CircleView_inner_color, Color.GRAY);
        mOutColor = typedArray.getColor(R.styleable.CircleView_outer_color, Color.GRAY);
        mCenterColor = typedArray.getColor(R.styleable.CircleView_center_color, Color.BLUE);
        mTextColor = typedArray.getColor(R.styleable.CircleView_text_color, Color.WHITE);

        typedArray.recycle();
    }

    public void init(){

        mStartAnination = false;
        mPtInnerCircle = new Paint();
        mPtInnerCircle.setAntiAlias(true);
        mPtInnerCircle.setColor(mInnerColor);
        mPtInnerCircle.setStyle(Paint.Style.FILL);

        mPtOutCircle = new Paint();
        mPtOutCircle.setAntiAlias(true);
        mPtOutCircle.setColor(mOutColor);
        mPtOutCircle.setStyle(Paint.Style.STROKE);
        mPtOutCircle.setStrokeWidth(mOutWidth);

        mPtCenterCirCle = new Paint();
        mPtCenterCirCle.setColor(mCenterColor);
        mPtCenterCirCle.setAntiAlias(true);
        mPtCenterCirCle.setStyle(Paint.Style.STROKE);
        mPtCenterCirCle.setStrokeWidth(mCenterWidth);

        mPtText = new Paint();
        mPtText.setAntiAlias(true);
        mPtText.setColor(mTextColor);
        mPtText.setTextSize(32);

        this.post(new Runnable() {
            @Override
            public void run() {
                mViewWidth = getMeasuredWidth();
                mViewHeight = getMeasuredHeight();
                mCenterX = mViewWidth/2;
                mCenterY = mViewHeight/2;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInnerCircle(canvas);
        drawText(canvas);
        drawOutCircle(canvas);
        if (!mStartAnination){
            mStartAnination = true;
            startDrawArc();
        }else {
            drawCenterArc(canvas);
        }
    }

    public void drawInnerCircle(Canvas canvas){
        canvas.drawCircle(mCenterX, mCenterY, mInnerRadus, mPtInnerCircle);
    }

    public void drawText(Canvas canvas){
        int textWidth = (int) mPtText.measureText(mCenterText);
        Paint.FontMetrics metrics = mPtText.getFontMetrics();
        float startX = mCenterX - textWidth/2;
        float startY = mCenterY - metrics.descent + (metrics.bottom - metrics.top) / 2;
        canvas.drawText(mCenterText, startX, startY, mPtText);
    }

    public void drawOutCircle(Canvas canvas){
        int outRadus = mInnerRadus + mCenterWidth + mOutWidth/2;
        canvas.drawCircle(mCenterX, mCenterY, outRadus, mPtOutCircle);
    }

    public void drawCenterArc(Canvas canvas){
        canvas.save();
        RectF rectF = new RectF(mCenterX-(mInnerRadus + mCenterWidth/2), mCenterY - (mInnerRadus + mCenterWidth/2),
                mCenterX+(mInnerRadus + mCenterWidth/2), mCenterY+(mInnerRadus + mCenterWidth/2));
        canvas.drawArc(rectF, mArcStartAngle, mArcAngle, false, mPtCenterCirCle);
        canvas.restore();
    }

    public void startDrawArc(){
        ValueAnimator animator = ValueAnimator.ofInt(0, 360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcStartAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setRepeatMode(Animation.RESTART);
        //animator.setInterpolator();
        //
        animator.setRepeatCount(Animation.INFINITE);
        animator.setDuration(1500);
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_MOVE:
                startDrawArc();
                break;
        }
        return true;
    }
}
