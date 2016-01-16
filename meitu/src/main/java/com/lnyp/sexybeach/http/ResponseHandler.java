package com.lnyp.sexybeach.http;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public abstract class ResponseHandler  extends TextHttpResponseHandler {

    public abstract void onSuccess(String result);

    public abstract void onFailure(Throwable throwable);

    private Context mContext;

    public ResponseHandler(Context context) {

        this.mContext = context;
    }

    @Override
    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

        Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();

        onFailure(throwable);
    }

    @Override
    public void onSuccess(int i, Header[] headers, String s) {

        onSuccess(s);
    }
}
