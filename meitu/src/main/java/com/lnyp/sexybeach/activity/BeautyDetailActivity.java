package com.lnyp.sexybeach.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lnyp.sexybeach.MyApp;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.common.Const;
import com.lnyp.sexybeach.entry.BeautyDetail;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.lnyp.sexybeach.util.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 美女图片详情
 */
public class BeautyDetailActivity extends BaseActivity {

    @Bind(R.id.rotateLoading)
    public RotateLoading rotateLoading;

    @Bind(R.id.scrollContent)
    public ScrollView scrollContent;

    @Bind(R.id.imgCover)
    public ImageView imgCover;

    @Bind(R.id.textCount)
    public TextView textCount;

    @Bind(R.id.textTitle)
    public TextView textTitle;

    private BeautyDetail beautyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);

        ButterKnife.bind(this);

        scrollContent.setVisibility(View.INVISIBLE);

        BeautySimple beautySimple = (BeautySimple) getIntent().getSerializableExtra("beautySimple");

        rotateLoading.start();
        getBeautyDetail(beautySimple.getId());
    }

    private void getBeautyDetail(int id) {


        OkHttpClient client = new OkHttpClient();

        String url = "http://www.tngou.net/tnfs/api/show?" + "id=" + id;
        LogUtils.e(url);
        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                BeautyDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BeautyDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

                        rotateLoading.stop();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                beautyDetail = FastJsonUtil.json2T(result, BeautyDetail.class);
                if (beautyDetail != null) {
                    BeautyDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateData();
                        }
                    });
                }
            }
        });

    }

    /**
     * 更新页面UI
     */
    private void updateData() {

        String imgUrl = Const.BASE_IMG_URL2 + beautyDetail.getImg();

        Glide.with(this)
                .load(imgUrl)
                .override(720, 1280)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(new GlideDrawableImageViewTarget(imgCover) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        //在这里添加一些图片加载完成的操作

                        textCount.setText("共有" + beautyDetail.getSize() + "张");

                        textTitle.setText(beautyDetail.getTitle());

                        scrollContent.setVisibility(View.VISIBLE);

                        rotateLoading.stop();
                    }
                });

    }

    @OnClick({R.id.layoutImgs, R.id.imgBack, R.id.imgShare})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.layoutImgs:
                if (beautyDetail != null) {
                    Intent intent = new Intent(this, ImageBrowseActivity.class);
                    intent.putParcelableArrayListExtra("imgs", beautyDetail.getList());
                    startActivity(intent);
                }

                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgShare:

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
