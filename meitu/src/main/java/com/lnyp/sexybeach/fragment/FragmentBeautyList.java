package com.lnyp.sexybeach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apkfuns.logutils.LogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.activity.BeautyDetailActivity;
import com.lnyp.sexybeach.adapter.BeautyGrilListAdapter;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.http.HttpUtil;
import com.lnyp.sexybeach.http.ResponseHandler;
import com.lnyp.sexybeach.rspdata.RspBeautySimple;
import com.lnyp.sexybeach.util.FastJsonUtil;
import com.lnyp.sexybeach.util.ImageLoaderUtil;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 美女列表
 */
public class FragmentBeautyList extends Fragment {

    @Bind(R.id.listViewBeauties)
    public PullToRefreshListView listViewBeauties;

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
        }

        id = getArguments().getInt("id");

        initView();

        getBeauties();

        return view;
    }

    private void initView() {

        listViewBeauties.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                page = 1;
                getBeauties();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (hasMore) {
                    isRefresh = false;
                    getBeauties();
                } else {
                    listViewBeauties.onRefreshComplete();
                }
            }

        });

        ListView actualListView = listViewBeauties.getRefreshableView();
        actualListView.setOnScrollListener(new PauseOnScrollListener(ImageLoaderUtil.getInstance().getImageLoader(), false, true));
        actualListView.setOnItemClickListener(new OnItemClickHander());

        mDatas = new ArrayList<>();
        // 设置适配器
        mAdapter = new BeautyGrilListAdapter(getActivity(), mDatas);
        listViewBeauties.setAdapter(mAdapter);

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
                        listViewBeauties.onRefreshComplete();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        listViewBeauties.onRefreshComplete();
                    }
                }
        );
    }

    private void updateData() {
        mAdapter.notifyDataSetChanged();
    }


    class OnItemClickHander implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BeautySimple beautySimple = (BeautySimple) parent.getAdapter().getItem(
                    position);
            if (beautySimple != null) {
                Intent intent = new Intent(getActivity(), BeautyDetailActivity.class);
                intent.putExtra("beautySimple", beautySimple);
                startActivity(intent);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

}
