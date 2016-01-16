package com.lnyp.sexybeach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView;
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
 * 最新的性感美图
 */
public class FragmentNewBeauty extends Fragment {

    private PullToRefreshLayout ptrl;

    @Bind(R.id.listViewNewBeauty)
    public PullableRecyclerView listViewNewBeauty;

    private BeautyGrilListAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private boolean isRefresh = false;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_new_beauty, container, false);
            ButterKnife.bind(this, view);

            ptrl = (PullToRefreshLayout)view.listViewNewBeauty;
            mDatas = new ArrayList<BeautySimple>();

            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            listViewNewBeauty.setLayoutManager(layoutManager);

//            listViewNewBeauty.setLoadingMoreEnabled(false);
//            listViewNewBeauty.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//            listViewNewBeauty.setLaodingMoreProgressStyle(ProgressStyle.BallRotate);
////            listViewBeauties.setArrowImageView(R.drawable.iconfont_downgrey);
//
////            listViewNewBeauty.addItemDecoration(new DividerGridItemDecoration(getActivity()));
//
//            listViewNewBeauty.setLoadingListener(new XRecyclerView.LoadingListener() {
//                @Override
//                public void onRefresh() {
//                    isRefresh = true;
//                    getnewBeauties();
//                }
//
//                @Override
//                public void onLoadMore() {
//
//                }
//            });


            // 设置适配器
            mAdapter = new BeautyGrilListAdapter(getActivity(), mDatas);
            listViewNewBeauty.setAdapter(mAdapter);

            onItemClick();

            getnewBeauties();

        }
        return view;
    }

    /**
     * 获取最新的美女结合列表
     */
    private void getnewBeauties() {

        RequestParams params = new RequestParams();
        params.put("rows", 20);

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
//                listViewNewBeauty.refreshComplete();
            }

            @Override
            public void onFinish() {
//                listViewNewBeauty.refreshComplete();
            }
        });
    }

    private void updateData() {
        mAdapter.notifyDataSetChanged();
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
//                LogUtils.e("position : " + position + "      beautySimple : " + beautySimple);
                intent.putExtra("beautySimple", beautySimple);
                startActivity(intent);
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
