package com.lnyp.sexybeach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.activity.BeautyDetailActivity;
import com.lnyp.sexybeach.adapter.BeautyListAdapter;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.rspdata.RspBeautySimple;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.loopj.android.http.RequestParams;
import com.victor.loading.rotate.RotateLoading;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import weight.LoadingFooter;
import weight.RecyclerViewStateUtils;

/**
 * 美女列表
 */
public class FragmentBeautyList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    @Bind(R.id.rotateLoading)
    public RotateLoading rotateLoading;

    @Bind(R.id.listViewBeauties)
    public RecyclerView listViewBeauties;

    @Bind(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;

    private BeautyListAdapter beautyListAdapter;

    private List<BeautySimple> mDatas;

    private int page = 1;

    private int id;

    private boolean hasMore = false;

    private boolean isRefresh = false;

    private boolean isNetError = false;

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getInt("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null == view) {
            view = inflater.inflate(R.layout.fragment_beauty_list, container, false);
            ButterKnife.bind(this, view);
        }

        initView();

        rotateLoading.start();
        onRefresh();

        return view;
    }

    private void initView() {

        refreshLayout.setOnRefreshListener(this);

        mDatas = new ArrayList<>();

        beautyListAdapter = new BeautyListAdapter(this, mDatas, onItemClick);

        listViewBeauties.setAdapter(beautyListAdapter);

        listViewBeauties.setLayoutManager(new LinearLayoutManager(getActivity()));

        listViewBeauties.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .sizeResId(R.dimen.list_divider_left_margin)
                        .marginResId(R.dimen.list_divider_left_margin, R.dimen.list_divider_right_margin)
                        .build());

        listViewBeauties.addOnScrollListener(mOnScrollListener);
    }


    /**
     * 获取性感美女列表
     */
    private void getBeauties() {

        RequestParams params = new RequestParams();
        params.put("page", this.page);
        params.put("rows", REQUEST_COUNT);
        params.put("id", id);

        LogUtils.e(params);

        HttpUtil.getReq(getActivity(), "http://www.tngou.net/tnfs/api/list", params, new ResponseHandler(getActivity()) {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(String result) {
                        LogUtils.e(result);
                        RspBeautySimple rspBeautySimple = null;
                        try {
                            rspBeautySimple = FastJsonUtil.json2T(result, RspBeautySimple.class);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (rspBeautySimple != null) {
                            int total = rspBeautySimple.getTotal();
                            List<BeautySimple> tngous = rspBeautySimple.getTngou();
                            if (tngous != null) {

                                if (isRefresh) {
                                    mDatas.clear();
                                    isRefresh = false;
                                }
                                mDatas.addAll(tngous);
                                updateData();

                                if (total > mDatas.size()) {
                                    hasMore = true;
                                } else {
                                    hasMore = false;
                                }
                                page++;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        isNetError = true;
                    }

                    @Override
                    public void onFinish() {

                        if (isNetError) {
                            RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);
                            isNetError = false;
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(listViewBeauties, LoadingFooter.State.Normal);
                        }

                        rotateLoading.stop();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void updateData() {

        beautyListAdapter.notifyDataSetChanged();
    }


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(listViewBeauties);

            if (state == LoadingFooter.State.Loading) {
                return;
            }

            if (hasMore) {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                getBeauties();
            } else {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            getBeauties();
        }
    };

    private View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                int pos = (int) v.getTag();
                BeautySimple beautySimple = mDatas.get(pos);

                if (beautySimple != null) {
                    Intent intent = new Intent(getActivity(), BeautyDetailActivity.class);
                    intent.putExtra("beautySimple", beautySimple);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        getBeauties();
    }
}
