package com.lnyp.sexybeach.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.lnyp.sexybeach.R;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {

    /**
     * 最短启动时间
     */
    private static final int SHOW_TIME_MIN = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        // 如果在启动APP时，不做任何网络请求，可设置app最短启动时间
        // 如果进行了网络请求，则无需设置
        new InitTask().execute();
    }

    public class InitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // 最短启动时间
            long startTime = System.currentTimeMillis();

            long loadingTime = System.currentTimeMillis() - startTime;

            if (loadingTime < SHOW_TIME_MIN) {
                try {
                    Thread.sleep(SHOW_TIME_MIN - loadingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent intent = new Intent();

            intent.setClass(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
