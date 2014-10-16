package com.example.myapplication;

import android.content.Context;
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

    public LongShadowTextView(Context context) {
        super(context);
    }

    public LongShadowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LongShadowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        }

        mPaint.setColor(mTextColor);
        canvas.drawText(mText, offsetX, offsetY - mTextBounds.top, mPaint);
    }
}
