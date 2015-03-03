package com.chijsh.banana.presentation.view.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chijsh on 12/1/14.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int padding;

    public DividerItemDecoration(int padding) {
        this.padding = padding;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top += padding;
        outRect.left += padding;
        outRect.right += padding;
    }

}
