package com.lnyp.sexybeach.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.adapter.ImgBrowsePagerAdapter;
import com.lnyp.sexybeach.entry.ListEntity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 图片浏览
 */
public class ImageBrowseActivity extends BaseActivity {

    private ViewPager viewPagerImgs;

    private List<ListEntity> imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_browse);

        ButterKnife.bind(this);

        applyKitKatTranslucency();

        this.imgs = getIntent().getParcelableArrayListExtra("imgs");

        viewPagerImgs = (ViewPager) this.findViewById(R.id.viewPagerImgs);
        viewPagerImgs.setOffscreenPageLimit(4);

        PagerAdapter adapter = new ImgBrowsePagerAdapter(this, imgs);

        viewPagerImgs.setAdapter(adapter);
    }
}
