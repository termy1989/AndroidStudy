package com.example.loaderweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaderweather.R;

// класс разделителя
public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDivider;
    private final boolean mDrawTopDivider;

    // конструктор
    public SimpleDividerItemDecoration(@NonNull Context context, boolean drawTopDivider) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
        mDrawTopDivider = drawTopDivider;
    }

    // отрисовка разделителя
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top;
            if (mDrawTopDivider) {
                top = child.getTop();
            } else {
                top = child.getBottom() + params.bottomMargin;
            }
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}