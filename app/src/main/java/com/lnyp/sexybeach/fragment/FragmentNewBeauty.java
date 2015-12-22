package com.lnyp.sexybeach.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.activity.BeautyDetailActivity;
import com.lnyp.sexybeach.adapter.BeautyGrilListAdapter;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.rspdata.RspBeautySimple;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.loopj.android.http.RequestParams;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.manager.AnimRFGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 最新的性感美图
 */
public class FragmentNewBeauty extends Fragment {

    @Bind(R.id.listViewNewBeauty)
    public AnimRFRecyclerView listViewNewBeauty;

    private BeautyGrilListAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private boolean isRefresh = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_beauty, container, false);

        ButterKnife.bind(this, view);

        mDatas = new ArrayList<BeautySimple>();

        View headerView = inflater.inflate(R.layout.header_view, container, false);
        View footerView = inflater.inflate(R.layout.footer_view, container, false);

        // 使用重写后的格子布局管理器
        listViewNewBeauty.setLayoutManager(new AnimRFGridLayoutManager(getActivity(), 2));
        // 添加头部和脚部，如果不添加就使用默认的头部和脚部
        listViewNewBeauty.addHeaderView(headerView);
        // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
        listViewNewBeauty.setScaleRatio(1.7f);
        // 设置下拉时拉伸的图片，不设置就使用默认的
        listViewNewBeauty.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
        listViewNewBeauty.addFootView(footerView);
        // 设置刷新动画的颜色
        listViewNewBeauty.setColor(Color.BLUE, Color.GREEN);
        // 设置头部恢复动画的执行时间，默认500毫秒
        listViewNewBeauty.setHeaderImageDurationMillis(300);
        // 设置拉伸到最高时头部的透明度，默认0.5f
        listViewNewBeauty.setHeaderImageMinAlpha(0.6f);

        // 设置适配器
        mAdapter = new BeautyGrilListAdapter(getActivity(), mDatas);
        listViewNewBeauty.setAdapter(mAdapter);

        onItemClick();
        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        listViewNewBeauty.setLoadDataListener(new AnimRFRecyclerView.LoadDataListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getnewBeauties();
            }

            @Override
            public void onLoadMore() {

            }
        });

        getnewBeauties();

        return view;
    }

    /**
     * 获取最新的美女结合列表
     */
    private void getnewBeauties() {

        RequestParams params = new RequestParams();
        params.put("rows", 500);

        HttpUtil.getReq(getActivity(), "http://www.tngou.net/tnfs/api/news", params, new ResponseHandler(getActivity()) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);

                RspBeautySimple rspBeautySimple = FastJsonUtil.json2T(result, RspBeautySimple.class);
                List<BeautySimple> tngous = rspBeautySimple.getTngou();

                if (isRefresh) {
                    mDatas.clear();
                }
                mDatas.addAll(tngous);
                updateData();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onFinish() {
                if (isRefresh) {
                    refreshComplate();
                    listViewNewBeauty.refreshComplate();
                }
            }
        });
    }

    private void updateData() {
        LogUtils.e("----updateData----");
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 点击事件
     */
    private void onItemClick() {

        mAdapter.setOnItemClickLitener(new BeautyGrilListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                BeautySimple beautySimple = mDatas.get(position);
                Intent intent = new Intent(getActivity(), BeautyDetailActivity.class);

//                LogUtils.e("position : " + position);
//                LogUtils.e("beautySimple : " + beautySimple);

                intent.putExtra("beautySimple", beautySimple);
                startActivity(intent);
            }
        });
    }


    public void refreshComplate() {
        listViewNewBeauty.getAdapter().notifyDataSetChanged();
    }

    public void loadMoreComplate() {
        listViewNewBeauty.getAdapter().notifyDataSetChanged();
    }

}
