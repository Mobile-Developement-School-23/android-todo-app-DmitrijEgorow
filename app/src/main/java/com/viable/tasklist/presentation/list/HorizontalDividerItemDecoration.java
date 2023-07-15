package com.viable.tasklist.presentation.list;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;

    public HorizontalDividerItemDecoration(@NonNull Drawable divider) {
        this.divider = divider.mutate();
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = - parent.getPaddingRight() + parent.getWidth();
        int childCount = parent.getChildCount();
        for (int i=0; i<childCount; i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
