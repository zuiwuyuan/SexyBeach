package com.lnyp.sexybeach.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.adapter.BeautyGrilListAdapter;
import com.lnyp.sexybeach.entry.BeautyClassify;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.rspdata.RspBeautySimple;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 美女图片列表
 */
public class BeautyListActivity extends BaseActivity {

    @Bind(R.id.rViewClassify)
    public RecyclerView rViewClassify;

    private BeautyGrilListAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private ProgressDialog dialog;

    private int page = 1;

    private int rows = 20;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_list);

        ButterKnife.bind(this);

        applyKitKatTranslucency();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rViewClassify.setLayoutManager(layoutManager);
        mDatas = new ArrayList<>();
        mAdapter = new BeautyGrilListAdapter(this, mDatas);
        // 设置item动画
        rViewClassify.setItemAnimator(new DefaultItemAnimator());

        BeautyClassify beautyClassify =
                (BeautyClassify) getIntent().getSerializableExtra("beautyClassify");
        id = beautyClassify.getId();
        getBeauties();

        initEvent();
    }

    private void initEvent() {

        mAdapter.setOnItemClickLitener(new BeautyGrilListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                BeautySimple beautySimple = mDatas.get(position);
                Intent intent = new Intent(BeautyListActivity.this, BeautyDetailActivity.class);
                intent.putExtra("beautySimple", beautySimple);
                BeautyListActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * 获取性感美女列表
     */
    private void getBeauties() {

        RequestParams params = new RequestParams();
        params.put("page", this.page);
        params.put("rows", this.rows);
        params.put("id", id);

        LogUtils.e("params : " + params.toString());

        HttpUtil.getReq(this, "http://www.tngou.net/tnfs/api/list", params, new ResponseHandler(this) {

            @Override
            public void onStart() {
                super.onStart();
                // 开启弹框
                // 开启弹框
                dialog = ProgressDialog.show(BeautyListActivity.this, "", "加载中");
            }

            @Override
            public void onSuccess(String result) {

                LogUtils.e(result);

                RspBeautySimple rspBeautySimple = FastJsonUtil.json2T(result, RspBeautySimple.class);
                int totle = rspBeautySimple.getTotal();
                List<BeautySimple> tngous = rspBeautySimple.getTngou();

                mDatas.addAll(tngous);
                updateData();
            }

            @Override
            public void onFailure(Throwable throwable) {
                //关闭弹框
                dialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //关闭弹框
                dialog.dismiss();
            }
        });
    }

    private void updateData() {

        rViewClassify.setAdapter(mAdapter);
    }
}
