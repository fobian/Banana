package com.chijsh.banana.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.chijsh.banana.R;
import com.chijsh.banana.ui.recycursoradapter.RecyclerViewCursorAdapter;
import com.chijsh.banana.utils.DateUtil;
import com.chijsh.banana.utils.ScreenUtil;
import com.chijsh.banana.utils.StringUtil;
import com.chijsh.banana.widget.LinkEnabledTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by chijsh on 10/28/14.
 */

public class TimeLineCursorAdapter extends RecyclerViewCursorAdapter<RecyclerView.ViewHolder> implements LinkEnabledTextView.TextLinkClickListener {

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

    private static final float CONTENT_THUMBNAIL_SIZE = 0.9f;
    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int mLastAnimatedPosition = -1;

    public interface PostItemClickListener {
        public void onItemClicked(View itemView, String postId);
        public void onAvatarClicked(String userId);
        public void onFavouriteActionClicked(String postId, boolean isFavourited);
        public void onCommentActionClicked(String postId);
        public void onForwardActionClicked(String postId);
        public void onImageClicked(View view, int position, String[] pics);
    }

    private PostItemClickListener mListener;

    public void setPostItemClickListener(PostItemClickListener listener) {
        mListener = listener;
    }

    DrawableRequestBuilder mRequestBuilder;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.user_avatar) ImageView mAvatarView;
        @InjectView(R.id.user_name) TextView mNameView;
        @InjectView(R.id.user_subhead) TextView mSubHeadView;
        @InjectView(R.id.user_text) LinkEnabledTextView mTextView;
        @InjectView(R.id.favourite_action) ImageButton mFavouriteAction;
        @InjectView(R.id.comment_action) ImageButton mCommentAction;
        @InjectView(R.id.forward_action) ImageButton mForwardAction;

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
        mRequestBuilder = Glide.with(context)
                .from(String.class)
                .thumbnail(CONTENT_THUMBNAIL_SIZE)
                .fitCenter()
                .crossFade();

    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > mLastAnimatedPosition) {
            mLastAnimatedPosition = position;
            view.setTranslationY(ScreenUtil.getScreenSize(mContext).y);
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    @Override
    public RecyclerView.ViewHolder newViewHolder(Context context, Cursor cursor, int position) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(context)
                .inflate(R.layout.card_item, null, false);
        vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void bindView(final RecyclerView.ViewHolder viewHolder, Context context, final Cursor cursor, int position) {

            ViewHolder vh = (ViewHolder)viewHolder;
            runEnterAnimation(viewHolder.itemView, position);
            Glide.with(context)
                    .load(cursor.getString(COL_USER_AVATAR))
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.user_avatar_empty)
                    .into(vh.mAvatarView);
            vh.getItemViewType();
            vh.mNameView.setText(cursor.getString(COL_USER_SCREENNAME));
            vh.mSubHeadView.setText(DateUtil.getFriendlyDate(cursor.getString(COL_CREATED_AT)) + " " + Html.fromHtml(cursor.getString(COL_POST_SOURCE)));

            vh.mTextView.setOnTextLinkClickListener(this);
            vh.mTextView.gatherLinksForText(cursor.getString(COL_POST_TEXT));
            vh.mTextView.setMovementMethod(LinkMovementMethod.getInstance());

            String pics = cursor.getString(COL_POST_PICURLS);

            handlePics(context, vh, false, pics);

            String retwittUserName =  cursor.getString(COL_RETWEETED_USER_SCREENNAME);
            String retwittText =  cursor.getString(COL_RETWEETED_TEXT);
            String retweetPics = cursor.getString(COL_RETWEETED_PICURLS);

            if(retwittUserName != null && retwittText != null) {
                vh.mRetweetStub.setVisibility(View.VISIBLE);
                vh.mRetwittTextView.setVisibility(View.VISIBLE);
                retwittUserName  = "@" + retwittUserName;
                vh.mRetwittTextView.setOnTextLinkClickListener(this);
                vh.mRetwittTextView.gatherLinksForText(retwittUserName + ":" + retwittText);
                vh.mRetwittTextView.setMovementMethod(LinkMovementMethod.getInstance());

                handlePics(context, vh, true, retweetPics);

            } else {
                vh.mRetweetStub.setVisibility(View.GONE);
            }

            final int cursorPosition = cursor.getPosition();

            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(cursorPosition);
                    mListener.onItemClicked(viewHolder.itemView, cursor.getString(COL_POST_ID));
                }
            });

            vh.mAvatarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(cursorPosition);
                    mListener.onAvatarClicked(cursor.getString(COL_USER_ID));
                }
            });

            vh.mFavouriteAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(cursorPosition);
                    mListener.onFavouriteActionClicked(cursor.getString(COL_POST_ID), cursor.getInt(COL_POST_FAVORITED) == 0 ? false : true);
                }
            });
            vh.mCommentAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(cursorPosition);
                    mListener.onCommentActionClicked(cursor.getString(COL_POST_ID));
                }
            });
            vh.mForwardAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(cursorPosition);
                    mListener.onForwardActionClicked(cursor.getString(COL_POST_ID));
                }
            });


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
                    pic.setVisibility(View.GONE);
                    break;
                case 7:
                    for (int i = 8; i > 6; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    break;
                case 6:
                    for (int i = 8; i > 5; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }

                    break;
                case 5:
                    for (int i = 8; i > 4; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }

                    break;
                case 4:
                    for (int i = 8; i > 3; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    for (int i = 8; i > 2; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    for (int i = 8; i > 1; i--) {
                        pic = (ImageView) gridLayout.getChildAt(i);
                        pic.setVisibility(View.GONE);
                    }

                    break;


            }

        }
    }

    private void handlePics(Context context, ViewHolder viewHolder, boolean isRetwitt, String pics) {

        if(isRetwitt) {
            if (!TextUtils.isEmpty(pics)) {

                viewHolder.mRetweetPicsStub.setVisibility(View.VISIBLE);
                final String[] picArray = StringUtil.fastSplit(pics, ',');
                final int size = picArray.length;
                if(size > 1) {
                    viewHolder.mRetwittThumbImageView.setVisibility(View.GONE);
                    viewHolder.mRetwittPicsGrid.setVisibility(View.VISIBLE);
                    ImageView view;
                    for(int i = 0; i < size; ++i) {
                        final int position = i;
                        view  = (ImageView)viewHolder.mRetwittPicsGrid.getChildAt(i);
                        mRequestBuilder.load(picArray[i]).into(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListener.onImageClicked(v, position, picArray);
                            }
                        });

                    }

                    handlePicsGrid(size, viewHolder.mRetwittPicsGrid);

                } else {
                    viewHolder.mRetwittThumbImageView.setVisibility(View.VISIBLE);
                    viewHolder.mRetwittPicsGrid.setVisibility(View.GONE);

                    mRequestBuilder.load(pics).into(viewHolder.mRetwittThumbImageView);
                    viewHolder.mRetwittThumbImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onImageClicked(v, 0, picArray);
                        }
                    });

                }

            } else {
                viewHolder.mRetweetPicsStub.setVisibility(View.GONE);

            }
        } else {
            if (!TextUtils.isEmpty(pics)) {
                viewHolder.mTweetPicsStub.setVisibility(View.VISIBLE);
                final String[] picArray = StringUtil.fastSplit(pics, ',');
                int size = picArray.length;
                if(size > 1) {
                    viewHolder.mThumbImageView.setVisibility(View.GONE);
                    viewHolder.mPicsGrid.setVisibility(View.VISIBLE);
                    ImageView view;
                    for(int i = 0; i < size; ++i) {
                        final int position = i;
                        view  = (ImageView)viewHolder.mPicsGrid.getChildAt(i);
                        mRequestBuilder.load(picArray[i]).into(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListener.onImageClicked(v, position, picArray);
                            }
                        });
                    }

                    handlePicsGrid(size, viewHolder.mPicsGrid);

                } else {
                    viewHolder.mThumbImageView.setVisibility(View.VISIBLE);
                    viewHolder.mPicsGrid.setVisibility(View.GONE);

                    mRequestBuilder.load(pics).into(viewHolder.mThumbImageView);

                    viewHolder.mThumbImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onImageClicked(v, 0, picArray);
                        }
                    });

                }

            } else {
                viewHolder.mTweetPicsStub.setVisibility(View.GONE);

            }
        }
    }
}
