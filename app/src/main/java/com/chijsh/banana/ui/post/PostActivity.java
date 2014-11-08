package com.chijsh.banana.ui.post;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.data.PostContract;
import com.chijsh.banana.data.PostContract.AccountEntry;
import com.chijsh.banana.event.MessageEvent;
import com.chijsh.banana.model.User;
import com.chijsh.banana.service.PostWeiboService;
import com.chijsh.banana.widget.BezelImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PostActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int USER_LOADER = 0;

    public static final int PICK_OR_TAKE_PICTURE = 42;
    public static final String POST_WEIBO_EXTRA = "post_weibo_extra";

    @InjectView(R.id.toolbar_actionbar) Toolbar toolbar;
    @InjectView(R.id.my_avatar) BezelImageView mAvatar;
    @InjectView(R.id.my_name) TextView mNameTextView;

    @InjectView(R.id.post_edit) AutoCompleteTextView mPostEdit;

    @InjectView(R.id.post_camera) ImageButton mCameraAction;
    @InjectView(R.id.post_at) ImageButton mAtAction;
    @InjectView(R.id.post_emotion) ImageButton mEmotionAction;

    @InjectView(R.id.post_send) ImageButton mSendAction;

    private static final String[] USER_COLUMNS = {
            AccountEntry.TABLE_NAME + "." + AccountEntry._ID,
            AccountEntry.COLUMN_USER_ID,
            AccountEntry.COLUMN_SCREEN_NAME,
            AccountEntry.COLUMN_PROVINCE,
            AccountEntry.COLUMN_CITY,
            AccountEntry.COLUMN_LOCATION,
            AccountEntry.COLUMN_DESCRIPTION,
            AccountEntry.COLUMN_URL,
            AccountEntry.COLUMN_PROFILE_URL,
            AccountEntry.COLUMN_GENDER,
            AccountEntry.COLUMN_FOLLOWERS_COUNT,
            AccountEntry.COLUMN_FRIENDS_COUNT,
            AccountEntry.COLUMN_STATUSES_COUNT,
            AccountEntry.COLUMN_FAVOURITES_COUNT,
            AccountEntry.COLUMN_CREATED_AT,
            AccountEntry.COLUMN_FOLLOWING,
            AccountEntry.COLUMN_AVATAR_LARGE,
            AccountEntry.COLUMN_FOLLOW_ME,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.inject(this);

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

    public void onEventMainThread(MessageEvent event){
        Toast.makeText(this, event.getMessage().toString(), Toast.LENGTH_SHORT).show();
        finish();
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

    }

    @OnClick(R.id.post_send)
    public void postWeibo() {
        Intent intent = new Intent(this, PostWeiboService.class);
        intent.putExtra(POST_WEIBO_EXTRA, mPostEdit.getText().toString());
        startService(intent);
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
            ViewHolder holder;
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
