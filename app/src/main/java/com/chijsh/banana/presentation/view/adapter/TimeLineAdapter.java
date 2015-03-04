package com.chijsh.banana.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.chijsh.banana.domain.PicUrl;
import com.chijsh.banana.presentation.model.PicUrlModel;
import com.chijsh.banana.presentation.model.PostModel;
import com.chijsh.banana.presentation.model.UserModel;
import com.chijsh.banana.presentation.view.widget.LinkEnabledTextView;
import com.chijsh.banana.utils.DateUtil;
import com.chijsh.banana.utils.ScreenUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by chijsh on 10/28/14.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> implements LinkEnabledTextView.TextLinkClickListener {

//    public static final int COL_CREATED_AT = 1;
//    public static final int COL_POST_ID = 2;
//    public static final int COL_POST_TEXT = 3;
//    public static final int COL_POST_SOURCE = 4;
//    public static final int COL_POST_FAVORITED = 5;
//    public static final int COL_POST_PICURLS = 6;
//    public static final int COL_POST_GEO = 7;
//    public static final int COL_USER_ID = 8;
//    public static final int COL_USER_SCREENNAME = 9;
//    public static final int COL_USER_AVATAR = 10;
//    public static final int COL_RETWEETED_ID = 11;
//    public static final int COL_RETWEETED_USER_SCREENNAME = 12;
//    public static final int COL_RETWEETED_TEXT = 13;
//    public static final int COL_RETWEETED_PICURLS = 14;
//    public static final int COL_REPOST_COUNT = 15;
//    public static final int COL_COMMENT_COUNT = 16;
//    public static final int COL_ATTITUDE_COUNT = 17;

    private static final float CONTENT_THUMBNAIL_SIZE = 0.9f;
    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int mLastAnimatedPosition = -1;

    public interface PostItemClickListener {
        public void onItemClicked(View itemView, String postId);
        public void onAvatarClicked(String userId);
        public void onFavouriteActionClicked(String postId, boolean isFavourited);
        public void onCommentActionClicked(String postId);
        public void onForwardActionClicked(String postId);
        public void onImageClicked(View view, int position, List<PicUrlModel> pics);
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
        @InjectView(R.id.user_text)
        LinkEnabledTextView mTextView;
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

    private Context mContext;
    private List<PostModel> mPosts;

    public TimeLineAdapter(Context context, List<PostModel> postModels) {
        mContext = context;
        mPosts = postModels;
        mRequestBuilder = Glide.with(context)
                .from(String.class)
                .thumbnail(CONTENT_THUMBNAIL_SIZE)
                .fitCenter()
                .crossFade();

    }

    public void setData(List<PostModel> postModels) {
        mPosts = postModels;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, null, false);
        vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PostModel postModel = mPosts.get(position);
        final UserModel userModel = postModel.getUser();
        runEnterAnimation(holder.itemView, position);
        Glide.with(mContext)
                .load(userModel.getAvatarLarge())
                .thumbnail(0.1f)
                .placeholder(R.drawable.user_avatar_empty)
                .into(holder.mAvatarView);
        holder.mNameView.setText(userModel.getScreenName());
        holder.mSubHeadView.setText(DateUtil.getFriendlyDate(postModel.getCreatedAt()) + " " + Html.fromHtml(postModel.getSource()));

        holder.mTextView.setOnTextLinkClickListener(this);
        holder.mTextView.gatherLinksForText(postModel.getText());
        holder.mTextView.setMovementMethod(LinkMovementMethod.getInstance());

        List<PicUrlModel> pics = postModel.getPicUrls();

        handlePics(mContext, holder, false, pics);

        PostModel reTweet = postModel.getRetweetedStatus();

        if(reTweet != null) {
            String retwittText =  reTweet.getText();
            List<PicUrlModel> retweetPics = postModel.getRetweetedStatus().getPicUrls();
            String retwittUserName = postModel.getRetweetedStatus().getUser().getScreenName();
            holder.mRetweetStub.setVisibility(View.VISIBLE);
            holder.mRetwittTextView.setVisibility(View.VISIBLE);
            retwittUserName  = "@" + retwittUserName;
            holder.mRetwittTextView.setOnTextLinkClickListener(this);
            holder.mRetwittTextView.gatherLinksForText(retwittUserName + ":" + retwittText);
            holder.mRetwittTextView.setMovementMethod(LinkMovementMethod.getInstance());

            handlePics(mContext, holder, true, retweetPics);

        } else {
            holder.mRetweetStub.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(v, postModel.getIdstr());
            }
        });

        holder.mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAvatarClicked(userModel.getIdstr());
            }
        });

        holder.mFavouriteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFavouriteActionClicked(postModel.getIdstr(), postModel.isFavorited() ? false : true);
            }
        });
        holder.mCommentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCommentActionClicked(postModel.getIdstr());
            }
        });
        holder.mForwardAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onForwardActionClicked(postModel.getIdstr());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mPosts == null) {
            return 0;
        }
        return mPosts.size();
    }

//    @Override
//    public void bindView(final RecyclerView.ViewHolder viewHolder, Context context, final Cursor cursor, int position) {
//
//            ViewHolder vh = (ViewHolder)viewHolder;
//            runEnterAnimation(viewHolder.itemView, position);
//            Glide.with(context)
//                    .load(cursor.getString(COL_USER_AVATAR))
//                    .thumbnail(0.1f)
//                    .placeholder(R.drawable.user_avatar_empty)
//                    .into(vh.mAvatarView);
//            vh.getItemViewType();
//            vh.mNameView.setText(cursor.getString(COL_USER_SCREENNAME));
//            vh.mSubHeadView.setText(DateUtil.getFriendlyDate(cursor.getString(COL_CREATED_AT)) + " " + Html.fromHtml(cursor.getString(COL_POST_SOURCE)));
//
//            vh.mTextView.setOnTextLinkClickListener(this);
//            vh.mTextView.gatherLinksForText(cursor.getString(COL_POST_TEXT));
//            vh.mTextView.setMovementMethod(LinkMovementMethod.getInstance());
//
//            String pics = cursor.getString(COL_POST_PICURLS);
//
//            handlePics(context, vh, false, pics);
//
//            String retwittUserName =  cursor.getString(COL_RETWEETED_USER_SCREENNAME);
//            String retwittText =  cursor.getString(COL_RETWEETED_TEXT);
//            String retweetPics = cursor.getString(COL_RETWEETED_PICURLS);
//
//            if(retwittUserName != null && retwittText != null) {
//                vh.mRetweetStub.setVisibility(View.VISIBLE);
//                vh.mRetwittTextView.setVisibility(View.VISIBLE);
//                retwittUserName  = "@" + retwittUserName;
//                vh.mRetwittTextView.setOnTextLinkClickListener(this);
//                vh.mRetwittTextView.gatherLinksForText(retwittUserName + ":" + retwittText);
//                vh.mRetwittTextView.setMovementMethod(LinkMovementMethod.getInstance());
//
//                handlePics(context, vh, true, retweetPics);
//
//            } else {
//                vh.mRetweetStub.setVisibility(View.GONE);
//            }
//
//            final int cursorPosition = cursor.getPosition();
//
//            vh.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    cursor.moveToPosition(cursorPosition);
//                    mListener.onItemClicked(viewHolder.itemView, cursor.getString(COL_POST_ID));
//                }
//            });
//
//            vh.mAvatarView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    cursor.moveToPosition(cursorPosition);
//                    mListener.onAvatarClicked(cursor.getString(COL_USER_ID));
//                }
//            });
//
//            vh.mFavouriteAction.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    cursor.moveToPosition(cursorPosition);
//                    mListener.onFavouriteActionClicked(cursor.getString(COL_POST_ID), cursor.getInt(COL_POST_FAVORITED) == 0 ? false : true);
//                }
//            });
//            vh.mCommentAction.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    cursor.moveToPosition(cursorPosition);
//                    mListener.onCommentActionClicked(cursor.getString(COL_POST_ID));
//                }
//            });
//            vh.mForwardAction.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    cursor.moveToPosition(cursorPosition);
//                    mListener.onForwardActionClicked(cursor.getString(COL_POST_ID));
//                }
//            });
//
//
//    }


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

    private void handlePics(Context context, ViewHolder viewHolder, boolean isRetwitt, final List<PicUrlModel> pics) {

        if(isRetwitt) {
            if (!pics.isEmpty()) {

                viewHolder.mRetweetPicsStub.setVisibility(View.VISIBLE);
                final int size = pics.size();
                if(size > 1) {
                    viewHolder.mRetwittThumbImageView.setVisibility(View.GONE);
                    viewHolder.mRetwittPicsGrid.setVisibility(View.VISIBLE);
                    ImageView view;
                    for(int i = 0; i < size; ++i) {
                        final int position = i;
                        view  = (ImageView)viewHolder.mRetwittPicsGrid.getChildAt(i);
                        mRequestBuilder.load(pics.get(i).getThumbnailPic()).into(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListener.onImageClicked(v, position, pics);
                            }
                        });

                    }

                    handlePicsGrid(size, viewHolder.mRetwittPicsGrid);

                } else {
                    viewHolder.mRetwittThumbImageView.setVisibility(View.VISIBLE);
                    viewHolder.mRetwittPicsGrid.setVisibility(View.GONE);

                    mRequestBuilder.load(pics.get(0).getThumbnailPic()).into(viewHolder.mRetwittThumbImageView);
                    viewHolder.mRetwittThumbImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onImageClicked(v, 0, pics);
                        }
                    });

                }

            } else {
                viewHolder.mRetweetPicsStub.setVisibility(View.GONE);

            }
        } else {
            if (!pics.isEmpty()) {
                viewHolder.mTweetPicsStub.setVisibility(View.VISIBLE);
                int size = pics.size();
                if(size > 1) {
                    viewHolder.mThumbImageView.setVisibility(View.GONE);
                    viewHolder.mPicsGrid.setVisibility(View.VISIBLE);
                    ImageView view;
                    for(int i = 0; i < size; ++i) {
                        final int position = i;
                        view  = (ImageView)viewHolder.mPicsGrid.getChildAt(i);
                        mRequestBuilder.load(pics.get(i)).into(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListener.onImageClicked(v, position, pics);
                            }
                        });
                    }

                    handlePicsGrid(size, viewHolder.mPicsGrid);

                } else {
                    viewHolder.mThumbImageView.setVisibility(View.VISIBLE);
                    viewHolder.mPicsGrid.setVisibility(View.GONE);

                    mRequestBuilder.load(pics.get(0).getThumbnailPic()).into(viewHolder.mThumbImageView);

                    viewHolder.mThumbImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onImageClicked(v, 0, pics);
                        }
                    });

                }

            } else {
                viewHolder.mTweetPicsStub.setVisibility(View.GONE);

            }
        }
    }
}
