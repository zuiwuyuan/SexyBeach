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
 * 最新的性感美图
 */
public class FragmentNewBeauty extends Fragment {

    @Bind(R.id.listViewNewBeauty)
    public PullToRefreshListView listViewNewBeauty;

    private BeautyGrilListAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private boolean isRefresh = false;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_new_beauty, container, false);

            ButterKnife.bind(this, view);
        }

        initView();

        getnewBeauties();

        return view;
    }

    private void initView() {

        listViewNewBeauty.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                isRefresh = true;
                getnewBeauties();
            }
        });

        ListView actualListView = listViewNewBeauty.getRefreshableView();
        actualListView.setOnScrollListener(new PauseOnScrollListener(ImageLoaderUtil.getInstance().getImageLoader(), false, true));
        actualListView.setOnItemClickListener(new OnItemClickHander());

        mDatas = new ArrayList<>();

        // 设置适配器
        mAdapter = new BeautyGrilListAdapter(getActivity(), mDatas);
        listViewNewBeauty.setAdapter(mAdapter);

    }


    /**
     * 获取最新的美女结合列表
     */
    private void getnewBeauties() {

        RequestParams params = new RequestParams();
        params.put("rows", 100);

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
                listViewNewBeauty.onRefreshComplete();
            }

            @Override
            public void onFinish() {
                listViewNewBeauty.onRefreshComplete();
            }
        });
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
