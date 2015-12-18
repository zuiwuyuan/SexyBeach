package com.lnyp.sexybeach.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.adapter.BeautyCiasifyAdapter;
import com.lnyp.sexybeach.entry.BeautyClassify;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.rViewClassify)
    public RecyclerView rViewClassify;

    private BeautyCiasifyAdapter mAdapter;

    private List<BeautyClassify> mDatas;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getBeautyClasify();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        rViewClassify.setLayoutManager(layoutManager);
        mDatas = new ArrayList<>();
        mAdapter = new BeautyCiasifyAdapter(this, mDatas);
        // 设置item动画
        rViewClassify.setItemAnimator(new DefaultItemAnimator());

        rViewClassify.setAdapter(mAdapter);

        initEvent();
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
                dialog = ProgressDialog.show(MainActivity.this, "", "加载中");
            }

            @Override
            public void onSuccess(String result) {

                LogUtils.e(result);

                List<BeautyClassify> datas = FastJsonUtil.json2Collection(result, BeautyClassify.class);

                mDatas.addAll(datas);
                updateData();
            }

            @Override
            public void onFailure(Throwable throwable) {
                //关闭弹框
                dialog.dismiss();
            }

            @Override
            public void onFinish() {
                //关闭弹框
                dialog.dismiss();
            }
        });
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {

        mAdapter.setOnItemClickLitener(new BeautyCiasifyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                BeautyClassify beautyClassify = mDatas.get(position);
                Intent intent = new Intent(MainActivity.this, BeautyListActivity.class);
                intent.putExtra("beautyClassify", beautyClassify);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void updateData() {

        mAdapter.notifyDataSetChanged();
    }
}
