package com.lnyp.sexybeach.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.FastJsonUtil;
import com.lnyp.sexybeach.HttpUtil;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.ResponseHandler;
import com.lnyp.sexybeach.entry.BeautyClassify;
import com.lnyp.sexybeach.entry.BeautyDetail;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.rspdata.RspBeautySimple;
import com.loopj.android.http.RequestParams;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getBeautyClasify();
//        getBeauties();
//        getBeautyDetail();
        getnewBeauties();
    }

    /**
     * 获取美女分类
     */
    public void getBeautyClasify() {

        RequestParams params = new RequestParams();

        HttpUtil.getReq(this, "http://www.tngou.net/tnfs/api/classify", params, new ResponseHandler(this) {

            @Override
            public void onStart() {
                super.onStart();
                // 开启弹框
            }

            @Override
            public void onSuccess(String result) {

                LogUtils.e(result);

                List<BeautyClassify> datas = FastJsonUtil.json2Collection(result, BeautyClassify.class);

                for (BeautyClassify data : datas) {
                    LogUtils.e(data);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                //关闭弹框
            }
        });
    }

    /**
     * 获取美女结合列表
     */
    private void getBeauties() {

        RequestParams params = new RequestParams();
        params.put("page", "1");
        params.put("rows", "2");
        params.put("id", "1");
        HttpUtil.getReq(this, "http://www.tngou.net/tnfs/api/list", params, new ResponseHandler(this) {

            @Override
            public void onStart() {
                super.onStart();
                // 开启弹框
            }

            @Override
            public void onSuccess(String result) {

                LogUtils.e(result);

                RspBeautySimple rspBeautySimple = FastJsonUtil.json2T(result, RspBeautySimple.class);
                int totle = rspBeautySimple.getTotal();
                List<BeautySimple> tngous = rspBeautySimple.getTngou();

                for (BeautySimple data : tngous) {
                    LogUtils.e(data);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                //关闭弹框
            }
        });
    }

    private void getBeautyDetail() {
        //
        RequestParams params = new RequestParams();
        params.put("id", "521");
        HttpUtil.getReq(this, "http://www.tngou.net/tnfs/api/show", params, new ResponseHandler(this) {

            @Override
            public void onStart() {
                super.onStart();
                // 开启弹框
            }

            @Override
            public void onSuccess(String result) {

                LogUtils.e(result);

                BeautyDetail rspBeautySimple = FastJsonUtil.json2T(result, BeautyDetail.class);
                List<BeautyDetail.ListEntity> listEntities = rspBeautySimple.getList();
                for (BeautyDetail.ListEntity data : listEntities) {
                    LogUtils.e(data);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                //关闭弹框
            }
        });
    }

    /**
     * 获取最新的美女结合列表
     */
    private void getnewBeauties() {


        RequestParams params = new RequestParams();
//        params.put("classify", "1");
//        params.put("rows", "2");
        params.put("id", "0");

        HttpUtil.getReq(this, "http://www.tngou.net/tnfs/api/news", params, new ResponseHandler(this) {

            @Override
            public void onStart() {
                super.onStart();
                // 开启弹框
            }

            @Override
            public void onSuccess(String result) {

                LogUtils.e(result);

                RspBeautySimple rspBeautySimple = FastJsonUtil.json2T(result, RspBeautySimple.class);
                int totle = rspBeautySimple.getTotal();
                List<BeautySimple> tngous = rspBeautySimple.getTngou();

                for (BeautySimple data : tngous) {
                    LogUtils.e(data);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                //关闭弹框
            }
        });
    }
}
