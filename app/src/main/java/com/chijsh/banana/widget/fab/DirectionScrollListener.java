package com.chijsh.banana.widget.fab;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chijsh on 12/05/14
 */
class DirectionScrollListener extends RecyclerView.OnScrollListener {

    private static final int DIRECTION_CHANGE_THRESHOLD = 1;
    private final FloatingActionButton mFloatingActionButton;
    private int mPrevPosition;
    private int mPrevTop;
    private boolean mUpdated;

    DirectionScrollListener(FloatingActionButton floatingActionButton) {
        mFloatingActionButton = floatingActionButton;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        final View topChild = recyclerView.getChildAt(0);
        int firstViewTop = 0;
        if (topChild != null) {
            firstViewTop = topChild.getTop();
        }
        boolean goingDown;
        boolean changed = true;
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        if (mPrevPosition == firstVisibleItem) {
            final int topDelta = mPrevTop - firstViewTop;
            goingDown = firstViewTop < mPrevTop;
            changed = Math.abs(topDelta) > DIRECTION_CHANGE_THRESHOLD;
        } else {
            goingDown = firstVisibleItem > mPrevPosition;
        }
        if (changed && mUpdated) {
            mFloatingActionButton.hide(goingDown);
        }
        mPrevPosition = firstVisibleItem;
        mPrevTop = firstViewTop;
        mUpdated = true;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

}