package com.lnyp.sexybeach.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class HttpUtil {

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(5 * 1000);
        client.setConnectTimeout(5 * 1000);
    }

    public static void getReq(Context context, String url, RequestParams requestParams, ResponseHandler responseHandler) {

        client.get(context, url, requestParams, responseHandler);
    }

    public static void postReq(Context context, String url, RequestParams requestParams, ResponseHandler responseHandler) {

        client.post(context, url, requestParams, responseHandler);
    }
}
