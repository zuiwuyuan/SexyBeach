package com.lnyp.sexybeach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 图片浏览
 */
public class GrilImgAdapter extends RecyclerView.Adapter<GrilImgAdapter.ImgInfoHolder> {

    @Override
    public ImgInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ImgInfoHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ImgInfoHolder extends RecyclerView.ViewHolder {
        public ImgInfoHolder(View itemView) {
            super(itemView);
        }
    }
}
