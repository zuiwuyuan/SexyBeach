package com.lnyp.sexybeach.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lnyp.flexibledivider.GridSpacingItemDecoration;
import com.lnyp.recyclerview.EndlessRecyclerOnScrollListener;
import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lnyp.recyclerview.HeaderSpanSizeLookup;
import com.lnyp.recyclerview.RecyclerViewLoadingFooter;
import com.lnyp.recyclerview.RecyclerViewStateUtils;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.activity.BeautyDetailActivity;
import com.lnyp.sexybeach.activity.MainActivity;
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

/**
 * 美女列表
 */
public class FragmentBeautyList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 20;

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

    protected MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getInt("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null == view) {
            view = inflater.inflate(R.layout.fragment_beauty_list, container, false);
        }

        ButterKnife.bind(this, view);

        initView();

        rotateLoading.start();
        onRefresh();

        return view;
    }

    private void initView() {

        refreshLayout.setOnRefreshListener(this);
        listViewBeauties.setHasFixedSize(true); // 设置固定大小
        mDatas = new ArrayList<>();

        BeautyListAdapter beautyListAdapter = new BeautyListAdapter(this, mDatas, onItemClick);
        mAdapter = new HeaderAndFooterRecyclerViewAdapter(beautyListAdapter);
        listViewBeauties.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(listViewBeauties.getAdapter(), gridLayoutManager.getSpanCount()));
        listViewBeauties.setLayoutManager(gridLayoutManager);

        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration.Builder(getActivity(), gridLayoutManager.getSpanCount())
                .setH_spacing(1)
                .setV_spacing(1)
                .setDividerColor(Color.parseColor("#FFFFFF"))
                .build();

        listViewBeauties.addItemDecoration(itemDecoration);

        listViewBeauties.addOnScrollListener(mOnScrollListener);
    }


    /**
     * 获取性感美女列表
     */
    private void getBeauties() {

        OkHttpClient client = new OkHttpClient();

        String url = "http://www.tngou.net/tnfs/api/list?" + "page=" + page + "&rows=" + REQUEST_COUNT + "&id=" + id;
//        LogUtils.e(url);
        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, RecyclerViewLoadingFooter.State.NetWorkError, mFooterClick);

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

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            updateData();

                            RecyclerViewStateUtils.setFooterViewState(listViewBeauties, RecyclerViewLoadingFooter.State.Normal);

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
            RecyclerViewLoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(listViewBeauties);

            if (state == RecyclerViewLoadingFooter.State.Loading) {
                return;
            }

            if (hasMore) {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, RecyclerViewLoadingFooter.State.Loading, null);
                getBeauties();
            } else {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, RecyclerViewLoadingFooter.State.TheEnd, null);
            }
        }
    };

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), listViewBeauties, REQUEST_COUNT, RecyclerViewLoadingFooter.State.Loading, null);
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
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        getBeauties();
    }
}
