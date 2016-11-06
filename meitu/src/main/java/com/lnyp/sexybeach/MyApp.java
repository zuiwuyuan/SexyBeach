package com.lnyp.sexybeach;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.common.Const;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MyApp extends Application {

    // IWXAPI 是第三方app和微信通信的openapi接口
    public static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Const.APP_ID, true);
        // 将该app注册到微信
        api.registerApp(Const.APP_ID);

        LogUtils.configAllowLog = true;
        LogUtils.configTagPrefix = "sexybeach-";

    }
}
