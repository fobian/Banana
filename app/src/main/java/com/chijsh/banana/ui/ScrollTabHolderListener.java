package com.chijsh.banana.ui;

import android.support.v7.widget.RecyclerView;

/**
 * Created by chijsh on 12/3/14.
 */
public interface ScrollTabHolderListener {

    void adjustScroll(int scrollHeight);

    void onScroll(RecyclerView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
