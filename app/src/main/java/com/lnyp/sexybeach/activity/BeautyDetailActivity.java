package com.lnyp.sexybeach.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.common.Const;
import com.lnyp.sexybeach.entry.BeautyDetail;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.lnyp.sexybeach.util.ImageLoaderUtil;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 美女图片详情
 */
public class BeautyDetailActivity extends BaseActivity {

    @Bind(R.id.imgCover)
    public ImageView imgCover;

    @Bind(R.id.textCount)
    public TextView textCount;

    @Bind(R.id.textTitle)
    public TextView textTitle;

    private BeautyDetail beautyDetail;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);

        ButterKnife.bind(this);

        applyKitKatTranslucency();

        BeautySimple beautySimple = (BeautySimple) getIntent().getSerializableExtra("beautySimple");

        getBeautyDetail(beautySimple.getId());
    }

    private void getBeautyDetail(int id) {

        RequestParams params = new RequestParams();
        params.put("id", id);
        HttpUtil.getReq(this, "http://www.tngou.net/tnfs/api/show", params, new ResponseHandler(this) {

            @Override
            public void onStart() {
                super.onStart();
                // 开启弹框
                dialog = ProgressDialog.show(BeautyDetailActivity.this, "", "加载中..");
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);

                beautyDetail = FastJsonUtil.json2T(result, BeautyDetail.class);
                updateData();
            }

            @Override
            public void onFailure(Throwable throwable) {
                //关闭弹框
                dialog.dismiss();
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }

    /**
     * 更新页面UI
     */
    private void updateData() {

        ImageLoaderUtil.getInstance().displayListItemImage(Const.BASE_IMG_URL2 + beautyDetail.getImg(), imgCover);

        LogUtils.e(beautyDetail.getList().size());
        textCount.setText("共有" + beautyDetail.getSize() + "张");

        textTitle.setText(beautyDetail.getTitle());
    }

    @OnClick(R.id.layoutImgs)
    public void onClick(View view) {

        Intent intent = new Intent(this, ImageBrowseActivity.class);

        intent.putParcelableArrayListExtra("imgs", beautyDetail.getList());
        startActivity(intent);
    }

}
