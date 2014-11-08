package com.chijsh.banana.ui.post;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chijsh.banana.R;
import com.chijsh.banana.model.User;
import com.chijsh.banana.widget.BezelImageView;

import java.util.ArrayList;
import java.util.List;
import com.chijsh.banana.data.PostContract.UserEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chijsh on 11/8/14.
 */
public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder>
        implements Filterable {

    Context mContext;
    List<User> mUserList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.at_user_avatar)
        BezelImageView mAvatar;
        @InjectView(R.id.at_user_name)
        TextView mName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public AutoCompleteAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
    }

    public void setUserList(List<User> userList) {
        mUserList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sugesstion_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .load(mUserList.get(i).avatarLarge)
                .thumbnail(0.1f)
                .placeholder(R.drawable.user_avatar_empty)
                .into(viewHolder.mAvatar);
        viewHolder.mName.setText(mUserList.get(i).screenName);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
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
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    //notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private List<User> findUser(Context context, String n) {
        Cursor cursor = context.getContentResolver().query(
                UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        List<User> users = new ArrayList<User>();
        User user = new User();
        if(cursor != null) {
            while (cursor.moveToFirst()) {
                user.avatarLarge = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_AVATAR_LARGE));
                user.screenName = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_SCREEN_NAME));
                if (user.screenName.contains(n)) {
                    users.add(user);
                }
            }
            cursor.close();
            return users;
        }
        return null;
    }
}
