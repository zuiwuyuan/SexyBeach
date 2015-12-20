package com.lnyp.sexybeach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.adapter.BeautyGrilListAdapter;
import com.lnyp.sexybeach.common.DividerGridItemDecoration;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.rspdata.RspBeautySimple;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.loopj.android.http.RequestParams;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 美女列表
 */
public class FragmentBeautyList extends FragmentBase {

    @Bind(R.id.listViewNewBeauty)
    public SuperRecyclerView listViewNewBeauty;

    private BeautyGrilListAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private View view;

    private int page = 1;

    private static final int ROWS = 10;

    private int id;

    private boolean hasMore = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getInt("id");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_beauty_list, (ViewGroup) getActivity().findViewById(R.id.viewPagerProjects), false);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        ButterKnife.bind(this, view);

        mDatas = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listViewNewBeauty.setLayoutManager(layoutManager);
        listViewNewBeauty.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        // 加载更多
        listViewNewBeauty.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

                if (hasMore) {
                    getBeauties();
                } else {
                    listViewNewBeauty.hideMoreProgress();
                }
            }
        }, 1);

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
//                LogUtils.e(result);

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
                    }
                }

        );
    }

    private void updateData() {
        if (mAdapter == null) {
            mAdapter = new BeautyGrilListAdapter(getActivity(), mDatas);
            listViewNewBeauty.setAdapter(mAdapter);

            onItemClick();
        } else {
            mAdapter.notifyDataSetChanged();
        }
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
}
