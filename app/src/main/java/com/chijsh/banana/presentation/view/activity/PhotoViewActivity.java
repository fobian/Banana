package com.chijsh.banana.presentation.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.chijsh.banana.R;
import com.chijsh.banana.presentation.view.widget.PhotoViewPager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends ActionBarActivity {

    public static final String EXTRA_PHOTO_POSITION = "extra_photo_position";
    public static final String EXTRA_PHOTO_ARRAY = "extra_photo_array";
    public static final String EXTRA_PHOTO_LEFT = "extra_photo_left";
    public static final String EXTRA_PHOTO_TOP = "extra_photo_top";
    public static final String EXTRA_PHOTO_WIDTH = "extra_photo_width";
    public static final String EXTRA_PHOTO_HEIGHT = "extra_photo_height";

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
    private static final int ANIM_DURATION = 500;

    ColorDrawable mBackground;

    @InjectView(R.id.photo_layout)
    FrameLayout mPhotoLayout;
    @InjectView(R.id.photo_pager)
    PhotoViewPager mPhotoPager;

    PhotoPagerAdapter mPhotoAdapter;

    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt(EXTRA_PHOTO_POSITION);
        String[] pics = bundle.getStringArray(EXTRA_PHOTO_ARRAY);

        final int thumbnailTop = bundle.getInt(EXTRA_PHOTO_TOP);
        final int thumbnailLeft = bundle.getInt(EXTRA_PHOTO_LEFT);
        final int thumbnailWidth = bundle.getInt(EXTRA_PHOTO_WIDTH);
        final int thumbnailHeight = bundle.getInt(EXTRA_PHOTO_HEIGHT);

        mPhotoAdapter = new PhotoPagerAdapter(pics);
        mPhotoPager.setAdapter(mPhotoAdapter);
        mPhotoPager.setCurrentItem(position);

        mBackground = new ColorDrawable(Color.BLACK);
        mPhotoLayout.setBackground(mBackground);


        if (savedInstanceState == null) {

            ViewTreeObserver observer = mPhotoPager.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    mPhotoPager.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    mPhotoPager.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) thumbnailWidth / mPhotoPager.getWidth();
                    mHeightScale = (float) thumbnailHeight / mPhotoPager.getHeight();

                    runEnterAnimation(mPhotoPager);

                    return true;
                }
            });
        }

    }

    public void runEnterAnimation(View view) {
        final long duration = ANIM_DURATION;

        view.setPivotX(0);
        view.setPivotY(0);
        view.setScaleX(mWidthScale);
        view.setScaleY(mHeightScale);
        view.setTranslationX(mLeftDelta);
        view.setTranslationY(mTopDelta);

        view.animate().setDuration(duration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator);

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
        bgAnim.setDuration(duration);
        bgAnim.start();

    }

    public void runExitAnimation(final Runnable endAction) {

        final long duration = ANIM_DURATION;

        mPhotoPager.animate().setDuration(duration).
                scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        endAction.run();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0);
        bgAnim.setDuration(duration);
        bgAnim.start();


    }

    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public static class PicData implements Parcelable {

        public String mUrl;
        public int mLeft;
        public int mRight;
        public int mWidth;
        public int mHeight;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mUrl);
            dest.writeInt(this.mLeft);
            dest.writeInt(this.mRight);
            dest.writeInt(this.mWidth);
            dest.writeInt(this.mHeight);
        }

        public PicData() {
        }

        private PicData(Parcel in) {
            this.mUrl = in.readString();
            this.mLeft = in.readInt();
            this.mRight = in.readInt();
            this.mWidth = in.readInt();
            this.mHeight = in.readInt();
        }

        public static final Parcelable.Creator<PicData> CREATOR = new Parcelable.Creator<PicData>() {
            public PicData createFromParcel(Parcel source) {
                return new PicData(source);
            }

            public PicData[] newArray(int size) {
                return new PicData[size];
            }
        };
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
                    .thumbnail(0.1f)
                    .into(photoView);

            container.addView(photoView);

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
