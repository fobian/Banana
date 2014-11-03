package com.chijsh.banana.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chijsh.banana.R;
import com.chijsh.banana.Utility;
import com.chijsh.banana.widget.LinkEnabledTextView;


/**
 * Created by chijsh on 10/28/14.
 */

public class TimeLineAdapter extends CursorRecyclerViewAdapter<TimeLineAdapter.ViewHolder> implements LinkEnabledTextView.TextLinkClickListener {

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
    public static final int COL_RETWEETED_USER_SCREENNAME = 12;
    public static final int COL_RETWEETED_TEXT = 13;
    public static final int COL_RETWEETED_PICURLS = 14;
    public static final int COL_REPOST_COUNT = 15;
    public static final int COL_COMMENT_COUNT = 16;
    public static final int COL_ATTITUDE_COUNT = 17;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mAvatarView;
        public TextView mNameView;
        public TextView mSubHeadView;
        public LinkEnabledTextView mTextView;
        public ImageView mThumbImageView;

        public RecyclerView mPicGridView;

        public View mRetwittView;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mAvatarView = (ImageView)itemView.findViewById(R.id.user_avatar);
            mNameView = (TextView)itemView.findViewById(R.id.user_name);
            mSubHeadView = (TextView)itemView.findViewById(R.id.user_subhead);
            mTextView = (LinkEnabledTextView)itemView.findViewById(R.id.user_text);
            mThumbImageView = (ImageView)itemView.findViewById(R.id.thumbnail_pic);

            mRetwittView = itemView.findViewById(R.id.retwitt_layout);
        }

    }


    public TimeLineAdapter(Context context, Cursor cursor) {
        super(context, cursor);

    }

    @Override
    public ViewHolder newView(Context context, Cursor cursor) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.card_item, null, false);
        ViewHolder vh = new ViewHolder(v, context);
        return vh;
    }

    @Override
    public void bindView(ViewHolder viewHolder, Context context, Cursor cursor) {
        Glide.with(context).
                load(cursor.getString(COL_USER_AVATAR))
                .into(viewHolder.mAvatarView);
        viewHolder.mNameView.setText(cursor.getString(COL_USER_SCREENNAME));
        viewHolder.mSubHeadView.setText(Utility.getFriendlyDate(cursor.getString(COL_CREATED_AT)) + " " + Html.fromHtml(cursor.getString(COL_POST_SOURCE)));

        viewHolder.mTextView.setOnTextLinkClickListener(this);
        viewHolder.mTextView.gatherLinksForText(cursor.getString(COL_POST_TEXT));

        MovementMethod m = viewHolder.mTextView.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (viewHolder.mTextView.getLinksClickable()) {
                viewHolder.mTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

        String pics = cursor.getString(COL_POST_PICURLS);

        if (pics != null) {
            if(Utility.strToArray(pics).length > 1) {

                viewHolder.mThumbImageView.setVisibility(View.GONE);
            } else {
                viewHolder.mThumbImageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(pics)
                        .into(viewHolder.mThumbImageView);

            }

        } else {
            viewHolder.mThumbImageView.setVisibility(View.GONE);
        }

        String retwittUserName =  cursor.getString(COL_RETWEETED_USER_SCREENNAME);
        String retwittText =  cursor.getString(COL_RETWEETED_TEXT);

        if(retwittUserName != null && retwittText != null) {
            retwittUserName  = "@" + retwittUserName;
            ((LinkEnabledTextView)viewHolder.mRetwittView.findViewById(R.id.retwitt_content)).setOnTextLinkClickListener(this);
            ((LinkEnabledTextView)viewHolder.mRetwittView.findViewById(R.id.retwitt_content)).gatherLinksForText(retwittUserName + ":" + retwittText);
            MovementMethod mm = ((LinkEnabledTextView)viewHolder.mRetwittView.findViewById(R.id.retwitt_content)).getMovementMethod();
            if ((mm == null) || !(mm instanceof LinkMovementMethod)) {
                if (((LinkEnabledTextView)viewHolder.mRetwittView.findViewById(R.id.retwitt_content)).getLinksClickable()) {
                    ((LinkEnabledTextView)viewHolder.mRetwittView.findViewById(R.id.retwitt_content)).setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
            viewHolder.mRetwittView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mRetwittView.setVisibility(View.GONE);
        }


    }


    @Override
    public void onTextLinkClick(View textView, String clickedString) {
        Toast.makeText(mContext, clickedString, Toast.LENGTH_SHORT).show();
    }

}
