package com.lnyp.sexybeach.activity;

import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.rspdata.RspBeautySimple;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * 最新的美女
 */
public class NewBeautyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beauty);
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
