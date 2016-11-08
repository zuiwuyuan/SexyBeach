package com.lnyp.sexybeach.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.common.Const;
import com.lnyp.sexybeach.entry.ListEntity;
import com.victor.loading.rotate.RotateLoading;

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
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((View) obj);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {


        RelativeLayout view = (RelativeLayout) object;
        PhotoView img = (PhotoView) view.findViewById(R.id.photoViewImg);

        Glide.clear(img);

        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String imgUrl = Const.BASE_IMG_URL2 + imgs.get(position).getSrc();

        RelativeLayout view = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.img_browse, null);
        final PhotoView img = (PhotoView) view.findViewById(R.id.photoViewImg);
        final RotateLoading rotateLoading = (RotateLoading) view.findViewById(R.id.rotateLoading);

        img.setTag(R.string.app_name, imgUrl);

        Glide.with(mContext)
                .load(imgUrl)
//                .override(720, 1280)
                .error(R.drawable.default_empty_bg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .skipMemoryCache(true)
                .into(new GlideDrawableImageViewTarget(img) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        //在这里添加一些图片加载完成的操作

                        LogUtils.e("*********onResourceReady**********");
                        rotateLoading.stop();
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        rotateLoading.start();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        LogUtils.e("***********onLoadFailed**************");
                        rotateLoading.stop();
                    }
                });

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

        container.addView(view);

        return view;
    }
}