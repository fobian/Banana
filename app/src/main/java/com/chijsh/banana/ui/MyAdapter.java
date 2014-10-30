package com.chijsh.banana.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chijsh.banana.R;
import com.chijsh.banana.Utility;
import com.chijsh.banana.ui.widget.LinkEnabledTextView;
import com.squareup.picasso.Picasso;


/**
 * Created by chijsh on 10/28/14.
 */

public class MyAdapter extends CursorRecyclerViewAdapter<MyAdapter.ViewHolder> implements LinkEnabledTextView.TextLinkClickListener {

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
        public LinkEnabledTextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView)itemView.findViewById(R.id.user_avatar);
            mNameView = (TextView)itemView.findViewById(R.id.user_name);
            mSubHeadView = (TextView)itemView.findViewById(R.id.user_subhead);
            mTextView = (LinkEnabledTextView)itemView.findViewById(R.id.user_text);
        }

    }

    private Context mContext;
    public MyAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
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
        viewHolder.mSubHeadView.setText(Utility.getFriendlyDate(cursor.getString(COL_CREATED_AT)) + " " + context.getString(R.string.weibo_source) + Html.fromHtml(cursor.getString(COL_POST_SOURCE)));

        viewHolder.mTextView.setOnTextLinkClickListener(this);
        viewHolder.mTextView.gatherLinksForText(cursor.getString(COL_POST_TEXT));
        viewHolder.mTextView.setLinkTextColor(context.getResources().getColor(R.color.txt_link));

        MovementMethod m = viewHolder.mTextView.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (viewHolder.mTextView.getLinksClickable()) {
                viewHolder.mTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

    }


    @Override
    public void onTextLinkClick(View textView, String clickedString) {
        Toast.makeText(mContext, clickedString, Toast.LENGTH_SHORT).show();
    }

}
