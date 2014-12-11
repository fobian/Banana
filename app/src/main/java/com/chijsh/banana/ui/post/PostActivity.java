package com.chijsh.banana.ui.post;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.data.PostContract;
import com.chijsh.banana.data.PostContract.AccountEntry;
import com.chijsh.banana.event.MessageEvent;
import com.chijsh.banana.model.User;
import com.chijsh.banana.service.PostWeiboService;
import com.chijsh.banana.ui.ProfileActivity;
import com.chijsh.banana.utils.ScreenUtil;
import com.chijsh.banana.utils.Utility;
import com.chijsh.banana.widget.BezelImageView;
import com.chijsh.banana.widget.SizeNotifierRelativeLayout;
import com.chijsh.banana.widget.emoji.Emoji;
import com.chijsh.banana.widget.emoji.EmojiView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PostActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, SizeNotifierRelativeLayout.SizeNotifierRelativeLayoutListener{

    private static final int USER_LOADER = 0;

    public static final int PICK_OR_TAKE_PICTURE = 42;
    public static final String POST_WEIBO_EXTRA = "post_weibo_extra";
    public static final String NAME_EXTRA = "name_extra";

    @InjectView(R.id.toolbar_actionbar) Toolbar mToolbar;
    @InjectView(R.id.avatar_name) View mAvatarName;
    @InjectView(R.id.my_avatar) BezelImageView mAvatar;
    @InjectView(R.id.my_name) TextView mNameTextView;

    @InjectView(R.id.post_edit) AutoCompleteTextView mPostEdit;

    @InjectView(R.id.post_camera) ImageButton mCameraAction;
    @InjectView(R.id.post_at) ImageButton mAtAction;
    @InjectView(R.id.post_emotion) ImageButton mEmotionAction;

    @InjectView(R.id.post_send) ImageButton mSendAction;

    @InjectView(R.id.container) SizeNotifierRelativeLayout mSizeRelativeLayout;
    private PopupWindow mEmojiPopup;
    private EmojiView mEmojiView;
    private int keyboardHeight = 0;
    private boolean keyboardVisible;
    private View contentView;

    private static final String[] USER_COLUMNS = {
            AccountEntry.TABLE_NAME + "." + AccountEntry._ID,
            AccountEntry.COLUMN_USER_ID,
            AccountEntry.COLUMN_SCREEN_NAME,
//            AccountEntry.COLUMN_PROVINCE,
//            AccountEntry.COLUMN_CITY,
//            AccountEntry.COLUMN_LOCATION,
//            AccountEntry.COLUMN_DESCRIPTION,
//            AccountEntry.COLUMN_URL,
//            AccountEntry.COLUMN_PROFILE_URL,
//            AccountEntry.COLUMN_GENDER,
//            AccountEntry.COLUMN_FOLLOWERS_COUNT,
//            AccountEntry.COLUMN_FRIENDS_COUNT,
//            AccountEntry.COLUMN_STATUSES_COUNT,
//            AccountEntry.COLUMN_FAVOURITES_COUNT,
//            AccountEntry.COLUMN_CREATED_AT,
//            AccountEntry.COLUMN_FOLLOWING,
            AccountEntry.COLUMN_AVATAR_LARGE,
 //           AccountEntry.COLUMN_FOLLOW_ME,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.inject(this);

        mSizeRelativeLayout.listener = this;
        contentView = mSizeRelativeLayout;

        MyAdapter adapter = new MyAdapter(this);
        mPostEdit.setAdapter(adapter);

        getLoaderManager().initLoader(USER_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mEmojiPopup != null && mEmojiPopup.isShowing()) {
            hideEmojiPopup();
        } else {
            super.onBackPressed();
        }
    }

    public void onEventMainThread(MessageEvent event){
        Utility.toast(this, event.getMessage());
        finish();
    }

    @OnClick(R.id.avatar_name)
    public void viewProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(NAME_EXTRA, mNameTextView.getText());
        startActivity(intent);
    }

    @OnClick(R.id.post_camera)
    public void addPicture() {
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickIntent,
                getString(R.string.title_add_picture));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                new Intent[] { takePhotoIntent });

        startActivityForResult(chooserIntent, PICK_OR_TAKE_PICTURE);
    }

    @OnClick(R.id.post_at)
    public void atSomebody() {
        mPostEdit.getText().append("@");
    }

    @OnClick(R.id.post_emotion)
    public void addEmotion() {
        if (mEmojiPopup == null) {
            showEmojiPopup(true);
        } else {
            showEmojiPopup(!mEmojiPopup.isShowing());
        }
    }

    @OnClick(R.id.post_send)
    public void postWeibo() {
        Intent intent = new Intent(this, PostWeiboService.class);
        intent.putExtra(POST_WEIBO_EXTRA, mPostEdit.getText().toString());
        startService(intent);
    }

    @Override
    public void onSizeChanged(int height) {
        Rect localRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);

        WindowManager manager = (WindowManager)getSystemService(Activity.WINDOW_SERVICE);
        if (manager == null || manager.getDefaultDisplay() == null) {
            return;
        }
        //int rotation = manager.getDefaultDisplay().getRotation();

        height -= ScreenUtil.getStatusBarHeight(this);
        if (height > Emoji.scale(50)) {
            keyboardHeight = height;
            getSharedPreferences("emoji", 0).edit().putInt("kbd_height", keyboardHeight).commit();
        }

        if (mEmojiPopup != null && mEmojiPopup.isShowing()) {
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            final WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams)mEmojiPopup.getContentView().getLayoutParams();
            layoutParams.width = contentView.getWidth();

            layoutParams.height = keyboardHeight;

            wm.updateViewLayout(mEmojiPopup.getContentView(), layoutParams);
            if (!keyboardVisible) {
                contentView.post(new Runnable() {
                    @Override
                    public void run() {
                        contentView.setPadding(0, 0, 0, layoutParams.height);
                        contentView.requestLayout();
                    }
                });
            }
        }

        boolean oldValue = keyboardVisible;
        keyboardVisible = height > 0;
        if (keyboardVisible && contentView.getPaddingBottom() > 0) {
            showEmojiPopup(false);
        } else if (!keyboardVisible && keyboardVisible != oldValue && mEmojiPopup != null && mEmojiPopup.isShowing()) {
            showEmojiPopup(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = AccountEntry.CONTENT_URI;
        String selection = AccountEntry.COLUMN_USER_ID + " = '" + AccessTokenKeeper.readAccessToken(this).getUid() + "'";

        return new CursorLoader(
                this,
                uri,
                USER_COLUMNS,
                selection,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor != null && cursor.moveToFirst()) {
            String avatarUrl = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_AVATAR_LARGE));
            String name = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_SCREEN_NAME));
            Glide.with(this)
                    .load(avatarUrl)
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.user_avatar_empty)
                    .into(mAvatar);
            mNameTextView.setText(name);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO
    }


    private void createEmojiPopup() {
        mEmojiView = new EmojiView(this);
        mEmojiView.setListener(new EmojiView.Listener() {
            public void onBackspace() {
                mPostEdit.dispatchKeyEvent(new KeyEvent(0, 67));
            }

            public void onEmojiSelected(String paramAnonymousString) {
                int i = mPostEdit.getSelectionEnd();
                CharSequence localCharSequence = Emoji.replaceEmoji(paramAnonymousString);
                mPostEdit.setText(mPostEdit.getText().insert(i, localCharSequence));
                int j = i + localCharSequence.length();
                mPostEdit.setSelection(j, j);
            }
        });
        mEmojiPopup = new PopupWindow(mEmojiView);
    }

    public void hideEmojiPopup() {
        if (mEmojiPopup != null && mEmojiPopup.isShowing()) {
            showEmojiPopup(false);
        }
    }

    private void showEmojiPopup(boolean show) {

        if (show) {
            if (mEmojiPopup == null) {
                createEmojiPopup();
            }
            int currentHeight;
            if (keyboardHeight <= 0) {
                keyboardHeight = getSharedPreferences("emoji", 0).getInt("kbd_height", Emoji.scale(200.0f));
            }
            currentHeight = keyboardHeight;
            mEmojiPopup.setHeight(View.MeasureSpec.makeMeasureSpec(currentHeight, View.MeasureSpec.EXACTLY));
            mEmojiPopup.setWidth(View.MeasureSpec.makeMeasureSpec(contentView.getWidth(), View.MeasureSpec.EXACTLY));

            mEmojiPopup.showAtLocation(getWindow().getDecorView(), 83, 0, 0);
            if (!keyboardVisible) {
                contentView.setPadding(0, 0, 0, currentHeight);
                //emojiButton.setImageResource(R.drawable.ic_msg_panel_hide);
                return;
            }
            //emojiButton.setImageResource(R.drawable.ic_msg_panel_kb);
            return;
        }
        //if (emojiButton != null) {
        //    emojiButton.setImageResource(R.drawable.ic_msg_panel_smiles);
        //}
        if (mEmojiPopup != null) {
            mEmojiPopup.dismiss();
        }
        if (contentView != null) {
            contentView.post(new Runnable() {
                public void run() {
                    if (contentView != null) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            });
        }
    }

    public class MyAdapter extends BaseAdapter
            implements Filterable {

        Context mContext;
        List<User> mUserList;

        class ViewHolder {

            @InjectView(R.id.at_user_avatar)
            BezelImageView mAvatar;
            @InjectView(R.id.at_user_name)
            TextView mName;

            ViewHolder(View itemView) {
                ButterKnife.inject(this, itemView);
            }
        }

        public MyAdapter(Context context) {
            super();
            mContext = context;
        }

        public void setUserList(List<User> userList) {
            mUserList = userList;
        }

        @Override
        public User getItem(int position) {
            return mUserList.get(position);
        }

        @Override
        public int getCount() {
            return mUserList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            User user = mUserList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.sugesstion_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            if ( user != null) {
                Glide.with(mContext)
                        .load(user.avatarLarge)
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.user_avatar_empty)
                        .into(holder.mAvatar);
                holder.mName.setText(user.screenName);
            }
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        mUserList = findUser(mContext, constraint.toString());

                        filterResults.values = mUserList;
                        filterResults.count = mUserList.size();
                        for(User user : mUserList) {
                            Log.d("ssssssssss", user.screenName);
                        }

                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }

        private List<User> findUser(Context context, String n) {
            Cursor cursor = context.getContentResolver().query(
                    PostContract.UserEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            List<User> users = new ArrayList<User>();
            User user = new User();
            if(cursor != null) {
                while (cursor.moveToFirst()) {
                    user.avatarLarge = cursor.getString(cursor.getColumnIndex(PostContract.UserEntry.COLUMN_AVATAR_LARGE));
                    user.screenName = cursor.getString(cursor.getColumnIndex(PostContract.UserEntry.COLUMN_SCREEN_NAME));
                    if (user.screenName.contains(n)) {
                        users.add(user);
                    }
                }
                cursor.close();
            }
            return users;
        }
    }
}
