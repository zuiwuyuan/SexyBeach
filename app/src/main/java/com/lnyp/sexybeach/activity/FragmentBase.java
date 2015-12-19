package com.lnyp.sexybeach.activity;

import android.support.v4.app.Fragment;
import android.view.View;

public class FragmentBase extends Fragment {

    @Override
    // 添加该函数，为了在fragmentActivity中可以用fragmentPagerAdapter切换，否则没有效果，详情请看MainActivity
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }
}
