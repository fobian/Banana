package com.chijsh.banana.presentation.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chijsh.banana.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chijsh on 2/28/15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_home,
            R.string.navdrawer_mention,
            R.string.navdrawer_comment,
            R.string.navdrawer_favourite,
            R.string.navdrawer_setting,
    };

//    private static final int[] NAVDRAWER_ICON_RES_ID = new int[] {
//            R.drawable.ic_drawer_home,
//            R.drawable.ic_drawer_mention,
//            R.drawable.ic_drawer_comment,
//            R.drawable.ic_drawer_favourate,
//            R.drawable.ic_drawer_settings,
//    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.nav_title)
        TextView mTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_normal_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(NAVDRAWER_TITLE_RES_ID[position]);
    }

    @Override
    public int getItemCount() {
        return NAVDRAWER_TITLE_RES_ID.length;
    }
}
