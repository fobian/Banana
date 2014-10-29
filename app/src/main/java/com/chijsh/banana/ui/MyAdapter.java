package com.chijsh.banana.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chijsh.banana.R;
import com.squareup.picasso.Picasso;


/**
 * Created by chijsh on 10/28/14.
 */

public class MyAdapter extends CursorRecyclerViewAdapter<MyAdapter.ViewHolder> {

    public static final int COL_CREATED_AT = 1;
    public static final int COL_POST_ID = 2;
    public static final int COL_POST_TEXT = 3;
    public static final int COL_POST_SOURCE = 4;
    public static final int COL_POST_FAVORITED = 5;
    public static final int COL_POST_PICURLS = 6;
    public static final int COL_POST_GEO = 7;
    public static final int COL_USER_ID = 8;
    public static final int COL_USER_SCREENNAME = 9;
    public static final int COL_USER_AVATAR = 10;
    public static final int COL_RETWEETED_ID = 11;
    public static final int COL_RETWEETED_USER_S = 12;
    public static final int COL_RETWEETED_TEXT = 13;
    public static final int COL_REPOST_COUNT = 14;
    public static final int COL_COMMENT_COUNT = 15;
    public static final int COL_ATTITUDE_COUNT = 16;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mAvatarView;
        public TextView mNameView;
        public TextView mSubHeadView;
        public TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView)itemView.findViewById(R.id.user_avatar);
            mNameView = (TextView)itemView.findViewById(R.id.user_name);
            mSubHeadView = (TextView)itemView.findViewById(R.id.user_subhead);
            mTextView = (TextView)itemView.findViewById(R.id.user_text);
        }
    }

    public MyAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public ViewHolder newView(Context context, Cursor cursor) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.card_item, null, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void bindView(ViewHolder viewHolder, Context context, Cursor cursor) {
        Picasso.with(context).
                load(cursor.getString(COL_USER_AVATAR))
                .into(viewHolder.mAvatarView);
        viewHolder.mNameView.setText(cursor.getString(COL_USER_SCREENNAME));
        viewHolder.mSubHeadView.setText(cursor.getString(COL_CREATED_AT) + " " + cursor.getString(COL_POST_SOURCE));
        viewHolder.mTextView.setText(cursor.getString(COL_POST_TEXT));
    }
}
