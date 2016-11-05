package com.lnyp.sexybeach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.HeaderSpanSizeLookup;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.activity.BeautyDetailActivity;
import com.lnyp.sexybeach.adapter.BeautyListAdapter;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.rspdata.RspBeautySimple;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    private HeaderAndFooterRecyclerViewAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private int page = 1;

    private int id;

    private boolean hasMore = false;

    private boolean isRefresh = false;

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

        BeautyListAdapter beautyListAdapter = new BeautyListAdapter(this, mDatas, onItemClick);
        mAdapter = new HeaderAndFooterRecyclerViewAdapter(beautyListAdapter);
        listViewBeauties.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) listViewBeauties.getAdapter(), gridLayoutManager.getSpanCount()));
        listViewBeauties.setLayoutManager(gridLayoutManager);

        listViewBeauties.addOnScrollListener(mOnScrollListener);
    }


    /**
     * 获取性感美女列表
     */
    private void getBeauties() {

//        RequestParams params = new RequestParams();
//        params.put("page", this.page);
//        params.put("rows", REQUEST_COUNT);
//        params.put("id", id);
//        LogUtils.e(params);

        OkHttpClient client = new OkHttpClient();

        String url = "http://www.tngou.net/tnfs/api/list?" + "page=" + page + "&rows=" + REQUEST_COUNT + "&id=" + id;
        LogUtils.e(url);
        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);

                        rotateLoading.stop();
                        refreshLayout.setRefreshing(false);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

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


                        if (total > mDatas.size()) {
                            hasMore = true;
                        } else {
                            hasMore = false;
                        }
                        page++;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            updateData();

                            RecyclerViewStateUtils.setFooterViewState(listViewBeauties, LoadingFooter.State.Normal);

                            rotateLoading.stop();
                            refreshLayout.setRefreshing(false);

                        }
                    });
                }
            }
        });

    }

    private void updateData() {

        mAdapter.notifyDataSetChanged();
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
