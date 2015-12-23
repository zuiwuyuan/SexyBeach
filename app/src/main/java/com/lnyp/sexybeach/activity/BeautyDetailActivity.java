package com.lnyp.sexybeach.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.MyApp;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.common.Const;
import com.lnyp.sexybeach.entry.BeautyDetail;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.lnyp.sexybeach.util.ImageLoaderUtil;
import com.lnyp.sexybeach.util.ImageUtil;
import com.lnyp.sexybeach.util.Util;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

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
//                dialog = ProgressDialog.show(BeautyDetailActivity.this, "", "加载中..");
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
//                dialog.dismiss();
            }

            @Override
            public void onFinish() {
//                dialog.dismiss();
            }
        });
    }

    /**
     * 更新页面UI
     */
    private void updateData() {

        String imgUrl = Const.BASE_IMG_URL2 + beautyDetail.getImg();
        LogUtils.e("imgUrl : " + imgUrl);
        LogUtils.e("imgCover : " + imgCover);
        ImageLoaderUtil.getInstance().displayListItemImage(imgUrl, imgCover, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                // 缩放图片
//                Bitmap zoomBitmap = ImageUtil.zoomBitmap(bitmap, 480, 300);

                // 获取倒影图片
                Bitmap reflectBitmap = ImageUtil.createReflectionImageWithOrigin(bitmap);

                imgCover.setImageBitmap(reflectBitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        }, null);

        LogUtils.e(beautyDetail.getList().size());
        textCount.setText("共有" + beautyDetail.getSize() + "张");

        textTitle.setText(beautyDetail.getTitle());

    }

    @OnClick({R.id.layoutImgs, R.id.imgBack, R.id.imgShare})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.layoutImgs:
                Intent intent = new Intent(this, ImageBrowseActivity.class);

                intent.putParcelableArrayListExtra("imgs", beautyDetail.getList());
                startActivity(intent);
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgShare:
                ;
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "http://www.tngou.net/tnfs/show/" + beautyDetail.getId();
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = beautyDetail.getTitle();
                msg.description = beautyDetail.getTitle();
                Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                msg.thumbData = Util.bmpToByteArray(thumb, true);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                MyApp.api.sendReq(req);

                break;
        }
    }
}
