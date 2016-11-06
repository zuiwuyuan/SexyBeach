package com.lnyp.sexybeach.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.common.Const;
import com.lnyp.sexybeach.entry.ListEntity;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImgBrowsePagerAdapter extends PagerAdapter {

    List<ListEntity> imgs;

    List<View> views;

    Activity mContext;

    public ImgBrowsePagerAdapter(Activity context, List<ListEntity> imgs) {

        this.mContext = context;
        this.imgs = imgs;

        this.views = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

    }

    @Override
    public int getCount() { // 获得size
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String imgUrl = Const.BASE_IMG_URL2 + imgs.get(position).getSrc();

        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.img_browse, null);
        final PhotoView img = (PhotoView) view.findViewById(R.id.photoViewImg);

        img.setTag(R.string.app_name, imgUrl);

        Glide.with(mContext)
                .load(imgUrl)
                .asBitmap()
                .centerCrop()
                .into(img);

        img.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {

                mContext.finish();
            }
        });
        img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                mContext.finish();
            }
        });

        ((ViewPager) container).addView(view);

        return view;
    }
}