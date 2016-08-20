package com.lnyp.sexybeach.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lnyp.sexybeach.fragment.FragmentBeautyList;

public class ProjectPagerAdapter extends FragmentPagerAdapter {

    private static String[] titles = {"性感美女", "韩日美女", "丝袜美腿"
            , "美女照片", "美女写真", "清纯美女", "性感车模"};

    public ProjectPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle;
        switch (position) {
            case 0:
                fragment = new FragmentBeautyList();
                bundle = new Bundle();
                bundle.putInt("id", 1);
                fragment.setArguments(bundle);
                break;

            case 1:
                fragment = new FragmentBeautyList();
                bundle = new Bundle();
                bundle.putInt("id", 2);
                fragment.setArguments(bundle);
                break;

            case 2:
                fragment = new FragmentBeautyList();
                bundle = new Bundle();
                bundle.putInt("id", 3);
                fragment.setArguments(bundle);
                break;

            case 3:
                fragment = new FragmentBeautyList();
                bundle = new Bundle();
                bundle.putInt("id", 4);
                fragment.setArguments(bundle);
                break;

            case 4:
                fragment = new FragmentBeautyList();
                bundle = new Bundle();
                bundle.putInt("id", 5);
                fragment.setArguments(bundle);
                break;

            case 5:
                fragment = new FragmentBeautyList();
                bundle = new Bundle();
                bundle.putInt("id", 6);
                fragment.setArguments(bundle);
                break;

            case 6:
                fragment = new FragmentBeautyList();
                bundle = new Bundle();
                bundle.putInt("id", 7);
                fragment.setArguments(bundle);
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
