package com.lnyp.sexybeach.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.adapter.ProjectPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tabPageProjects)
    public TabPageIndicator tabPageProjects;

    @Bind(R.id.viewPagerProjects)
    public ViewPager viewPagerProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ProjectPagerAdapter mAdapter = new ProjectPagerAdapter(getSupportFragmentManager());

        viewPagerProjects.setOffscreenPageLimit(2);

        viewPagerProjects.setAdapter(mAdapter);

        tabPageProjects.setViewPager(viewPagerProjects);
    }
}
