package com.chijsh.banana.ui;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.chijsh.banana.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends ActionBarActivity {

    public static final String EXTRA_PHOTO_POSITION = "extra_photo_position";
    public static final String EXTRA_PHOTO_ARRAY = "extra_photo_array";

    @InjectView(R.id.photo_pager)
    ViewPager mPhotoPager;

    PhotoPagerAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        if (intent != null) {
            int position = intent.getIntExtra(EXTRA_PHOTO_POSITION, -1);
            String[] pics = intent.getStringArrayExtra(EXTRA_PHOTO_ARRAY);

            mPhotoAdapter = new PhotoPagerAdapter(pics);
            mPhotoPager.setAdapter(mPhotoAdapter);
            mPhotoPager.setCurrentItem(position);
        }

    }

    static class PhotoPagerAdapter extends PagerAdapter {

        private String[] mPics;

        PhotoPagerAdapter(String[] pics) {
            mPics = pics;
        }

        @Override
        public int getCount() {
            return mPics.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            Glide.with(container.getContext())
                    .load(mPics[position])
                    .into(photoView);

            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
