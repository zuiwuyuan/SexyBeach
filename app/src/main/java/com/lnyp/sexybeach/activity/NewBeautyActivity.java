package com.lnyp.sexybeach.activity;

import android.os.Bundle;

import com.lnyp.sexybeach.R;

/**
 * 最新的美女
 */
public class NewBeautyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beauty);

        applyKitKatTranslucency();
    }

}
