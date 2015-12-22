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
 * 美女列表
 */
public class FragmentBeautyList extends Fragment {

    @Bind(R.id.listViewBeauties)
    public AnimRFRecyclerView listViewBeauties;

    private BeautyGrilListAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private View view;

    private int page = 1;

    private static final int ROWS = 10;

    private int id;

    private boolean hasMore = false;

    private boolean isRefresh = false;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beauty_list, container, false);

        ButterKnife.bind(this, view);

        id = getArguments().getInt("id");

        mDatas = new ArrayList<>();

//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        listViewBeauties.setLayoutManager(layoutManager);
//        listViewBeauties.addItemDecoration(new DividerGridItemDecoration(getActivity()));

//        // 加载更多
//        listViewBeauties.setupMoreListener(new OnMoreListener() {
//            @Override
//            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
//
//                if (hasMore) {
//                    getBeauties();
//                } else {
//                    listViewBeauties.hideMoreProgress();
//                }
//            }
//        }, 1);

        View headerView = inflater.inflate(R.layout.header_view, container, false);
        View footerView = inflater.inflate(R.layout.footer_view, container, false);


        // 使用重写后的格子布局管理器
        listViewBeauties.setLayoutManager(new AnimRFGridLayoutManager(getActivity(), 2));
        // 添加头部和脚部，如果不添加就使用默认的头部和脚部
        listViewBeauties.addHeaderView(headerView);
        // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
        listViewBeauties.setScaleRatio(1.7f);
        // 设置下拉时拉伸的图片，不设置就使用默认的
        listViewBeauties.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
        listViewBeauties.addFootView(footerView);
        // 设置刷新动画的颜色
        listViewBeauties.setColor(Color.BLUE, Color.GREEN);
        // 设置头部恢复动画的执行时间，默认500毫秒
        listViewBeauties.setHeaderImageDurationMillis(300);
        // 设置拉伸到最高时头部的透明度，默认0.5f
        listViewBeauties.setHeaderImageMinAlpha(0.6f);

        // 设置适配器
        mAdapter = new BeautyGrilListAdapter(getActivity(), mDatas);
        listViewBeauties.setAdapter(mAdapter);
        onItemClick();

        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        listViewBeauties.setLoadDataListener(new AnimRFRecyclerView.LoadDataListener() {
            @Override
            public void onRefresh() {
//                refreshComplate();
//                listViewBeauties.refreshComplate();
                isRefresh = true;
            }

            @Override
            public void onLoadMore() {

                if (hasMore) {
                    getBeauties();
                } else {
                    loadMoreComplate();
                    listViewBeauties.loadMoreComplate();
                }
            }
        });


        getBeauties();

        return view;
    }


    /**
     * 获取性感美女列表
     */
    private void getBeauties() {

        RequestParams params = new RequestParams();
        params.put("page", this.page);
        params.put("rows", ROWS);
        params.put("id", id);

        HttpUtil.getReq(getActivity(), "http://www.tngou.net/tnfs/api/list", params, new ResponseHandler(getActivity()) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(String result) {
                        LogUtils.e(result);

                        RspBeautySimple rspBeautySimple = FastJsonUtil.json2T(result, RspBeautySimple.class);
                        int total = rspBeautySimple.getTotal();
                        List<BeautySimple> tngous = rspBeautySimple.getTngou();

                        if (tngous != null) {
                            mDatas.addAll(tngous);
                            updateData();

                            // 判断是否还有更多的数据
                            page++;
                            if (total > mDatas.size()) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (isRefresh) {
                            refreshComplate();
                            listViewBeauties.refreshComplate();
                        } else {
                            loadMoreComplate();
                            listViewBeauties.loadMoreComplate();
                        }
                    }
                }

        );
    }

    private void updateData() {
//        if (mAdapter == null) {
//            mAdapter = new BeautyGrilListAdapter(getActivity(), mDatas);
//            listViewBeauties.setAdapter(mAdapter);
//
//            onItemClick();
//        } else {
        mAdapter.notifyDataSetChanged();
//        }
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
                intent.putExtra("beautySimple", beautySimple);
                LogUtils.e("position : " + position);
                LogUtils.e("beautySimple : " + beautySimple);
                startActivity(intent);

//                ActivityTransitionLauncher.with(getActivity()).from(view).launch(intent);
            }
        });
    }

    public void refreshComplate() {
        listViewBeauties.getAdapter().notifyDataSetChanged();
    }

    public void loadMoreComplate() {
        listViewBeauties.getAdapter().notifyDataSetChanged();
    }

}
