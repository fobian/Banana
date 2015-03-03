package com.chijsh.banana.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chijsh.banana.R;
import com.chijsh.banana.presentation.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chijsh on 12/2/14.
 */
public class FollowsAdapter extends RecyclerView.Adapter<FollowsAdapter.ViewHolder>{

    private Context mContext;
    private List<UserModel> mFollows;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.follows_avatar) ImageView mAvatar;
        @InjectView(R.id.follows_name) TextView mName;
        @InjectView(R.id.follows_description) TextView mDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public FollowsAdapter(Context context) {
        super();
        mContext = context;
        mFollows = new ArrayList<>();
    }

    public void addFollows(List<UserModel> follows) {
        mFollows.addAll(follows);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follows_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModel user = mFollows.get(position);
        Glide.with(mContext)
                .load(user.avatarLarge)
                .thumbnail(0.1f)
                .placeholder(R.drawable.user_avatar_empty)
                .into(holder.mAvatar);
        holder.mName.setText(user.screenName);
        holder.mDescription.setText(user.description);
    }

    @Override
    public int getItemCount() {
        return mFollows == null ? 0 : mFollows.size();
    }


}
