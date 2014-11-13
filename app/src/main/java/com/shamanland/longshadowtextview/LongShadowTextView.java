package com.shamanland.longshadowtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class LongShadowTextView extends View {
    public static final float DEFAULT_TEXT_SIZE = 20;
    public static final int DEFAULT_SHADOW_COLOR = Color.BLACK;
    public static final int DEFAULT_TEXT_COLOR = Color.GRAY;

    // configurable fields
    private String mText;
    private float mTextSize = DEFAULT_TEXT_SIZE;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mShadowColor = DEFAULT_SHADOW_COLOR;

    // internal data
    private Bitmap mBitmap;
    private Paint mPaint;
    private Rect mTextBounds;
    private Rect mSrc;
    private RectF mDst;

    public LongShadowTextView(Context context) {
        this(context, null, 0);
    }

    public LongShadowTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LongShadowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LongShadowTextView);
            mTextSize = (int) a.getDimension(R.styleable.LongShadowTextView_text_size, DEFAULT_TEXT_SIZE);
            mTextColor = a.getColor(R.styleable.LongShadowTextView_text_color, DEFAULT_TEXT_COLOR);
            mShadowColor = a.getColor(R.styleable.LongShadowTextView_shadow_color, DEFAULT_SHADOW_COLOR);
            mText = a.getString(R.styleable.LongShadowTextView_text);
            a.recycle();
        }
        refresh();
    }

    public String getText() {
        return mText;
    }

    public void setText(String value) {
        boolean changed = mText == null && value != null || mText != null && !mText.equals(value);

        mText = value;

        if (changed) {
            refresh();
        }
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float value) {
        boolean changed = mTextSize != value;

        mTextSize = value;

        if (changed) {
            refresh();
        }
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int value) {
        boolean changed = mTextColor != value;

        mTextColor = value;

        if (changed) {
            refresh();
        }
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public void setShadowColor(int value) {
        boolean changed = mShadowColor != value;

        mShadowColor = value;

        if (changed) {
            refresh();
        }
    }

    public void refresh() {
        if (mText == null) {
            return;
        }

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);

            mSrc = new Rect();
            mDst = new RectF();
        }

        mPaint.setColor(mShadowColor);
        mPaint.setTextSize(mTextSize);

        if (mTextBounds == null) {
            mTextBounds = new Rect();
        }

        mPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);

        Bitmap bitmap = Bitmap.createBitmap(mTextBounds.width() + 2 * mTextBounds.height(), mTextBounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(mText, 0, mTextBounds.height(), mPaint);

        Rect src = new Rect();
        RectF dst = new RectF();

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        src.left = 0;
        src.right = w;

        for (int i = 0; i < h; ++i) {
            src.top = i;
            src.bottom = i + 1;

            dst.left = 1;
            dst.top = i + 1;
            dst.right = 1 + w;
            dst.bottom = i + 2;

            canvas.drawBitmap(bitmap, src, dst, null);
        }

        mBitmap = bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mText == null) {
            return;
        }

        float offsetX = (canvas.getWidth() - mTextBounds.width()) / 2f;
        float offsetY = (canvas.getHeight() - mTextBounds.height()) / 2f;

        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, offsetX, offsetY, null);

            mSrc.right = mBitmap.getWidth();
            mSrc.top = mBitmap.getHeight() - 1;
            mSrc.bottom = mBitmap.getHeight();

            for (int x = (int) offsetX + 1, y = (int) (offsetY + mBitmap.getHeight()), h = canvas.getHeight(); y < h; ++x, ++y) {
                mDst.left = x;
                mDst.right = x + mBitmap.getWidth();
                mDst.top = y;
                mDst.bottom = y + 1;

                canvas.drawBitmap(mBitmap, mSrc, mDst, null);
            }
        }

        mPaint.setColor(mTextColor);
        canvas.drawText(mText, offsetX, offsetY - mTextBounds.top, mPaint);
    }
}
