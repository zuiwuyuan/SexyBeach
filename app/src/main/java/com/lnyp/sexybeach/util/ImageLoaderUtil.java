package com.lnyp.sexybeach.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lnyp.sexybeach.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 配置全局的 Android-Universal-Image-Loader
 */
public class ImageLoaderUtil {
    private static ImageLoaderUtil instance = null;

    private ImageLoader mImageLoader;

    // 列表中默认的图片
    private DisplayImageOptions mListItemOptions;

    private ImageLoaderUtil(Context context) {
        mImageLoader = ImageLoader.getInstance();
        mListItemOptions = new DisplayImageOptions.Builder()
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.default_empty_bg)
                .showImageOnLoading(R.drawable.default_empty_bg)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.drawable.default_empty_bg)
                // 加载图片时会在内存、磁盘中加载缓存
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .delayBeforeLoading(200)
//                .displayer(new FadeInBitmapDisplayer(500))
                .build();
    }

    public static ImageLoaderUtil getInstance() {
        return instance;
    }

    public synchronized static ImageLoaderUtil init(Context context) {
        if (instance == null) {
            instance = new ImageLoaderUtil(context);
        }

        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(10 * 1024 * 1024))
                .diskCacheSize(200 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);

        return instance;
    }

    /**
     * 列表图片
     *
     * @param uri
     * @param imageView
     */
    public void displayListItemImage(String uri, ImageView imageView) {
        String strUri = (isEmpty(uri) ? "" : uri);
        mImageLoader.displayImage(strUri, imageView, mListItemOptions);
    }

    /**
     * 列表图片
     *
     * @param uri
     * @param imageView
     */
    public void displayListItemImage(String uri, ImageView imageView, ImageLoadingListener listener) {
        String strUri = (isEmpty(uri) ? "" : uri);
        mImageLoader.displayImage(strUri, imageView, mListItemOptions, listener);
    }

    /**
     * 列表图片
     *
     * @param uri
     * @param imageView
     */
    public void displayListItemImage(String uri, ImageView imageView, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        String strUri = (isEmpty(uri) ? "" : uri);
        mImageLoader.displayImage(strUri, imageView, mListItemOptions, listener, progressListener);
    }


    public String getFileName(String url) {
        return mImageLoader.getDiskCache().get(url).getName();
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    private boolean isEmpty(String str) {
        if (str != null && str.trim().length() > 0 && !str.equalsIgnoreCase("null")) {
            return false;
        }

        return true;
    }
}
