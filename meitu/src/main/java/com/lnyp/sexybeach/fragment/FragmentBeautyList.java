package com.lnyp.sexybeach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.activity.BeautyDetailActivity;
import com.lnyp.sexybeach.adapter.BeautyGrilListAdapter;
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
 * 美女列表
 */
public class FragmentBeautyList extends Fragment {

    @Bind(R.id.listViewBeauties)
    public XRecyclerView listViewBeauties;

    private BeautyGrilListAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private int page = 1;

    private static final int ROWS = 10;

    private int id;

    private boolean hasMore = false;

    private boolean isRefresh = false;

    private View view;

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null == view) {
            view = inflater.inflate(R.layout.fragment_beauty_list, container, false);
            ButterKnife.bind(this, view);

            id = getArguments().getInt("id");

            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            listViewBeauties.setLayoutManager(layoutManager);

            listViewBeauties.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            listViewBeauties.setLaodingMoreProgressStyle(ProgressStyle.BallRotate);
//            listViewBeauties.setArrowImageView(R.drawable.iconfont_downgrey);
//            listViewBeauties.addItemDecoration(new DividerGridItemDecoration(getActivity()));

            listViewBeauties.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {

                    isRefresh = true;
                    getBeauties();
                }

                @Override
                public void onLoadMore() {

                    if (hasMore) {
                        getBeauties();
                    } else {
                        listViewBeauties.loadMoreComplete();
                    }
                }
            });

            mDatas = new ArrayList<>();

            // 设置适配器
            mAdapter = new BeautyGrilListAdapter(getActivity(), mDatas);
            listViewBeauties.setAdapter(mAdapter);
            onItemClick();

            getBeauties();

        }
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

                            if (isRefresh) {
                                mDatas.clear();
                            }
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
                        listViewBeauties.refreshComplete();
                        listViewBeauties.loadMoreComplete();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                        listViewBeauties.refreshComplete();
                        listViewBeauties.loadMoreComplete();
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

                BeautySimple beautySimple = mDatas.get(position - 1);
                Intent intent = new Intent(getActivity(), BeautyDetailActivity.class);
                intent.putExtra("beautySimple", beautySimple);
                startActivity(intent);

//                ActivityTransitionLauncher.with(getActivity()).from(view).launch(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

}
