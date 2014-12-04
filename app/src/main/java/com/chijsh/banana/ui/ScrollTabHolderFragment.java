package com.chijsh.banana.ui;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;

/**
 * Created by chijsh on 12/3/14.
 */
public abstract class ScrollTabHolderFragment extends Fragment implements ScrollTabHolderListener {

    protected ScrollTabHolderListener mScrollTabHolderListener;

    public void setScrollTabHolder(ScrollTabHolderListener scrollTabHolderListener) {
        mScrollTabHolderListener = scrollTabHolderListener;
    }

    @Override
    public void onScroll(RecyclerView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        // nothing
    }

}
