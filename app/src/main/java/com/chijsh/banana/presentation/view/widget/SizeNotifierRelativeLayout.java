/*
 * This is the source code of Telegram for Android v. 1.3.2.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013.
 */

package com.chijsh.banana.presentation.view.widget;

import android.content.Context;
import android.graphics.Rect;
import android.widget.RelativeLayout;

public class SizeNotifierRelativeLayout extends RelativeLayout {

    private Rect mRect = new Rect();
    public SizeNotifierRelativeLayoutListener listener;

    public abstract interface SizeNotifierRelativeLayoutListener {
        public abstract void onSizeChanged(int keyboardHeight);
    }

    public SizeNotifierRelativeLayout(Context context) {
        super(context);
    }

    public SizeNotifierRelativeLayout(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeNotifierRelativeLayout(Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (listener != null) {
            int usableViewHeight = this.getRootView().getHeight();
            this.getWindowVisibleDisplayFrame(mRect);
            int keyboardHeight = usableViewHeight - (mRect.bottom - mRect.top);
            listener.onSizeChanged(keyboardHeight);
        }
    }
}
