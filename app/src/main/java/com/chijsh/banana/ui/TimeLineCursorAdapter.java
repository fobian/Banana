package com.chijsh.banana.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chijsh.banana.R;
import com.chijsh.banana.ui.recycursoradapter.RecyclerViewCursorAdapter;
import com.chijsh.banana.utils.DateUtil;
import com.chijsh.banana.utils.StringUtil;
import com.chijsh.banana.utils.Utility;
import com.chijsh.banana.widget.LinkEnabledTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by chijsh on 10/28/14.
 */

public class TimeLineCursorAdapter extends RecyclerViewCursorAdapter<TimeLineCursorAdapter.ViewHolder> implements LinkEnabledTextView.TextLinkClickListener {

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

    private enum VIEW_TYPE {
        NORMAL,
        NORMAL_WITH_PIC,
        RETWEET,
        RETWEET_WITH_PIC

    }

    public interface PostItemClickListener {
        public void onItemClicked(View itemView, String postId);
        public void onAvatarClicked(String userId);
        public void onFavouriteActionClicked(String postId, boolean isFavourited);
        public void onCommentActionClicked(String postId);
        public void onForwardActionClicked(String postId);
    }

    private PostItemClickListener mListener;

    public void setPostItemClickListener(PostItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.user_avatar) ImageView mAvatarView;
        @InjectView(R.id.user_name) TextView mNameView;
        @InjectView(R.id.user_subhead) TextView mSubHeadView;
        @InjectView(R.id.user_text) LinkEnabledTextView mTextView;
        @InjectView(R.id.favourite_action) ImageView mFavouriteAction;
        @InjectView(R.id.comment_action) ImageView mCommentAction;
        @InjectView(R.id.forward_action) ImageView mForwardAction;

        @InjectView(R.id.tweet_pics_stub) ViewStub mTweetPicsStub;
        View mTweetPics;
        ImageView mThumbImageView;
        GridLayout mPicsGrid;

        @InjectView(R.id.retweet_stub) ViewStub mRetweetStub;
        View mRetweetLayout;
        LinkEnabledTextView mRetwittTextView;
        ViewStub mRetweetPicsStub;
        View mRetweetPics;
        ImageView mRetwittThumbImageView;
        GridLayout mRetwittPicsGrid;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            mRetweetLayout = mRetweetStub.inflate();
            mRetwittTextView = (LinkEnabledTextView)mRetweetLayout.findViewById(R.id.retwitt_content);

            mRetweetPicsStub = (ViewStub)mRetweetLayout.findViewById(R.id.retweet_pics_stub);
            mRetweetPics = mRetweetPicsStub.inflate();
            mRetwittThumbImageView = (ImageView)mRetweetPics.findViewById(R.id.thumbnail_pic);
            mRetwittPicsGrid = (GridLayout)mRetweetPics.findViewById(R.id.pic_grid);


            mTweetPics = mTweetPicsStub.inflate();
            mThumbImageView = (ImageView)mTweetPics.findViewById(R.id.thumbnail_pic);
            mPicsGrid = (GridLayout)mTweetPics.findViewById(R.id.pic_grid);

        }

    }


    public TimeLineCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);

    }

    @Override
    public ViewHolder newViewHolder(Context context, Cursor cursor) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.card_item, null, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void bindView(final ViewHolder viewHolder, Context context, final Cursor cursor) {
        Glide.with(context)
                .load(cursor.getString(COL_USER_AVATAR))
                .thumbnail(0.1f)
                .placeholder(R.drawable.user_avatar_empty)
                .into(viewHolder.mAvatarView);
        viewHolder.mNameView.setText(cursor.getString(COL_USER_SCREENNAME));
        viewHolder.mSubHeadView.setText(DateUtil.getFriendlyDate(cursor.getString(COL_CREATED_AT)) + " " + Html.fromHtml(cursor.getString(COL_POST_SOURCE)));

        viewHolder.mTextView.setOnTextLinkClickListener(this);
        viewHolder.mTextView.gatherLinksForText(cursor.getString(COL_POST_TEXT));
        viewHolder.mTextView.setMovementMethod(LinkMovementMethod.getInstance());

        String pics = cursor.getString(COL_POST_PICURLS);

        handlePics(context, viewHolder, false, pics);

        String retwittUserName =  cursor.getString(COL_RETWEETED_USER_SCREENNAME);
        String retwittText =  cursor.getString(COL_RETWEETED_TEXT);
        String retweetPics = cursor.getString(COL_RETWEETED_PICURLS);

        if(retwittUserName != null && retwittText != null) {
            viewHolder.mRetweetStub.setVisibility(View.VISIBLE);
            viewHolder.mRetwittTextView.setVisibility(View.VISIBLE);
            retwittUserName  = "@" + retwittUserName;
            viewHolder.mRetwittTextView.setOnTextLinkClickListener(this);
            viewHolder.mRetwittTextView.gatherLinksForText(retwittUserName + ":" + retwittText);
            viewHolder.mRetwittTextView.setMovementMethod(LinkMovementMethod.getInstance());

            handlePics(context, viewHolder, true, retweetPics);

        } else {
            viewHolder.mRetweetStub.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(viewHolder.itemView, cursor.getString(COL_POST_ID));
            }
        });

        viewHolder.mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAvatarClicked(cursor.getString(COL_USER_SCREENNAME));
            }
        });

        viewHolder.mFavouriteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFavouriteActionClicked(cursor.getString(COL_POST_ID), cursor.getInt(COL_POST_FAVORITED) == 0 ? false : true);
            }
        });
        viewHolder.mCommentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCommentActionClicked(cursor.getString(COL_POST_ID));
            }
        });
        viewHolder.mForwardAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onForwardActionClicked(cursor.getString(COL_POST_ID));
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        if (cursor.getString(COL_RETWEETED_USER_SCREENNAME) == null) {
            if (cursor.getString(COL_POST_PICURLS) == null) {
                return VIEW_TYPE.NORMAL.ordinal();
            } else {
                return VIEW_TYPE.NORMAL_WITH_PIC.ordinal();
            }
        } else {
            if (cursor.getString(COL_RETWEETED_PICURLS) == null) {
                return VIEW_TYPE.RETWEET.ordinal();
            } else {
                return VIEW_TYPE.RETWEET_WITH_PIC.ordinal();
            }
        }

    }

    @Override
    public void onTextLinkClick(View textView, String clickedString) {
        Toast.makeText(mContext, clickedString, Toast.LENGTH_SHORT).show();
    }


    private void handlePicsGrid(int size, GridLayout gridLayout) {
        if (size < 9) {
            ImageView pic;
            switch (size) {
                case 8:
                    pic = (ImageView) gridLayout.getChildAt(8);
                    pic.setVisibility(View.INVISIBLE);
                    break;
                case 7:
                    for (int i = 8; i > 6; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 6:
                    for (int i = 8; i > 5; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }

                    break;
                case 5:
                    for (int i = 8; i > 5; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    pic = (ImageView) gridLayout.getChildAt(5);
                    pic.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    for (int i = 8; i > 5; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    pic = (ImageView) gridLayout.getChildAt(5);
                    pic.setVisibility(View.INVISIBLE);
                    pic = (ImageView) gridLayout.getChildAt(4);
                    pic.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    for (int i = 8; i > 2; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    for (int i = 8; i > 2; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    pic = (ImageView) gridLayout.getChildAt(2);
                    pic.setVisibility(View.INVISIBLE);
                    break;


            }

        }
    }

    private void handlePics(Context context, ViewHolder viewHolder, boolean isRetwitt, String pics) {

        if(isRetwitt) {
            if (pics != null) {
                viewHolder.mRetweetPicsStub.setVisibility(View.VISIBLE);
                String[] picArray = StringUtil.fastSplit(pics, ',');
                int size = picArray.length;
                if(size > 1) {
                    viewHolder.mRetwittPicsGrid.setVisibility(View.VISIBLE);
                    ImageView view;
                    for(int i = 0; i < size; ++i) {
                        view = (ImageView)viewHolder.mRetwittPicsGrid.getChildAt(i);
                        Glide.with(context)
                                .load(picArray[i])
                                .thumbnail(0.1f)
                                .into(view);
                    }

                    handlePicsGrid(size, viewHolder.mRetwittPicsGrid);

                    viewHolder.mRetwittThumbImageView.setVisibility(View.GONE);
                } else {
                    viewHolder.mRetwittThumbImageView.setVisibility(View.VISIBLE);
                    viewHolder.mRetwittPicsGrid.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(pics)
                            .thumbnail(0.1f)
                            .into(viewHolder.mRetwittThumbImageView);

                }

            } else {
                viewHolder.mRetweetPicsStub.setVisibility(View.GONE);

            }
        } else {
            if (pics != null) {
                viewHolder.mTweetPicsStub.setVisibility(View.VISIBLE);
                String[] picArray = StringUtil.fastSplit(pics, ',');
                int size = picArray.length;
                if(size > 1) {
                    viewHolder.mPicsGrid.setVisibility(View.VISIBLE);
                    ImageView view;
                    for(int i = 0; i < size; ++i) {
                        view = (ImageView)viewHolder.mPicsGrid.getChildAt(i);
                        Glide.with(context)
                                .load(picArray[i])
                                .thumbnail(0.1f)
                                .into(view);
                    }

                    handlePicsGrid(size, viewHolder.mPicsGrid);

                    viewHolder.mThumbImageView.setVisibility(View.GONE);
                } else {
                    viewHolder.mThumbImageView.setVisibility(View.VISIBLE);
                    viewHolder.mPicsGrid.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(pics)
                            .thumbnail(0.1f)
                            .into(viewHolder.mThumbImageView);

                }

            } else {
                viewHolder.mTweetPicsStub.setVisibility(View.GONE);

            }
        }
    }
}
