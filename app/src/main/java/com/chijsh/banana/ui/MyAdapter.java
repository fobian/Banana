package com.chijsh.banana.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chijsh.banana.R;


/**
 * Created by chijsh on 10/28/14.
 */

public class MyAdapter extends CursorRecyclerViewAdapter<MyAdapter.ViewHolder> {

    public static final int COL_CREATED_AT = 1;
    public static final int COL_USER_ID = 8;
    public static final int COL_POST_TEXT = 3;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mUserView;
        public TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mUserView = (TextView)itemView.findViewById(R.id.user);
            mTextView = (TextView)itemView.findViewById(R.id.text);
        }
    }

    public MyAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public ViewHolder newView(Context context, Cursor cursor) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item, null, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void bindView(ViewHolder viewHolder, Context context, Cursor cursor) {
        viewHolder.mUserView.setText(cursor.getString(COL_USER_ID));
        viewHolder.mTextView.setText(cursor.getString(COL_POST_TEXT));
    }
}
