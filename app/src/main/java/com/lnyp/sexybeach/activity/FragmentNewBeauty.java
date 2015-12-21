package com.lnyp.sexybeach.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 最新的性感美图
 */
public class FragmentNewBeauty extends Fragment {

    @Bind(R.id.listViewNewBeauty)
    public SuperRecyclerView listViewNewBeauty;

    private BeautyGrilListAdapter mAdapter;

    private List<BeautySimple> mDatas;

    private ProgressDialog dialog;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_new_beauty, (ViewGroup) getActivity().findViewById(R.id.viewPagerProjects), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            getnewBeauties();
        }
    }

    /**
     * 获取最新的美女结合列表
     */
    private void getnewBeauties() {

        RequestParams params = new RequestParams();
        params.put("rows", 500);
//        params.put("classify", "1");
//        params.put("id", "0");

        HttpUtil.getReq(getActivity(), "http://www.tngou.net/tnfs/api/news", params, new ResponseHandler(getActivity()) {

            @Override
            public void onStart() {
                super.onStart();
                // 开启弹框
//                dialog = ProgressDialog.show(getActivity(), "", "加载中...");
            }

            @Override
            public void onSuccess(String result) {

//                LogUtils.e(result);

                RspBeautySimple rspBeautySimple = FastJsonUtil.json2T(result, RspBeautySimple.class);

                List<BeautySimple> tngous = rspBeautySimple.getTngou();

                mDatas.addAll(tngous);
                updateData();
            }

            @Override
            public void onFailure(Throwable throwable) {
                //关闭弹框
//                dialog.dismiss();
            }

            @Override
            public void onFinish() {
//                dialog.dismiss();
            }
        });
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

                LogUtils.e("position : " + position);
                LogUtils.e("beautySimple : " + beautySimple);

                intent.putExtra("beautySimple", beautySimple);
                startActivity(intent);
            }
        });
    }
}
