package com.chijsh.banana.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chijsh.banana.R;

/**
 * Created by chijsh on 10/31/14.
 */
public class PicGridAdapter extends RecyclerView.Adapter<PicGridAdapter.ViewHolder> {
    private Context mContext;
    private String[] mPicUrls;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.grid_img);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PicGridAdapter(Context context, String[] dataset) {
        mContext = context;
        mPicUrls = dataset;
    }

    public void setDataset(String[] dataset) {
        mPicUrls = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_pic_img, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mPicUrls[position]).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mPicUrls.length;
    }
}
