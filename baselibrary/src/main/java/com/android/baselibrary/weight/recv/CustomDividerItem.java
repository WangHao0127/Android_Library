package com.android.baselibrary.weight.recv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Author: WangHao
 * Created On: 2018/10/11 0011 13:43
 * Description: 自定义DividerItemDecoration分割线
 * 自定义目的：去除最后一项的分割线
 */
public class CustomDividerItem extends DividerItemDecoration {

    private final Rect mBounds = new Rect();
    private static final int[] ATTRS = new int[]{16843284};
    private Drawable mDivider;

    public CustomDividerItem(Context context, int orientation) {
        super(context, orientation);
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = a.getDrawable(0);
        if (this.mDivider == null) {
            Log.w("DividerItem",
                "@android:attr/listDivider was not set in the theme used for this "
                + "DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        a.recycle();
        this.setOrientation(orientation);
    }

    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        } else {
            this.mDivider = drawable;
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() != null && this.mDivider != null) {
            this.drawVertical(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            // 获取当前子试图所在的位置
         /* int pos = parent.getChildLayoutPosition(parent.getChildAt(i));
            // 跳过这个item不画线
         if (i == 0 && pos == 0) {
                continue;
            }*/
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }

        canvas.restore();

    }
}
