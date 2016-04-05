package com.lnyp.sexybeach.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.common.Const;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class BeautyListAdapter extends RecyclerView.Adapter {

    private LayoutInflater mLayoutInflater;
    private List<BeautySimple> beautySimples;

    private View.OnClickListener beautySimplesItemOnClick;

    public BeautyListAdapter(Context context, List<BeautySimple> beautySimples, View.OnClickListener beautySimplesItemOnClick) {
        mLayoutInflater = LayoutInflater.from(context);
        this.beautySimples = beautySimples;

        this.beautySimplesItemOnClick = beautySimplesItemOnClick;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_beauty_gril, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        BeautySimple beautySimple = beautySimples.get(position);

        if (beautySimple != null) {

            viewHolder.textClasifyTitle.setText(beautySimple.getTitle());

            String imgUrl = Const.BASE_IMG_URL2 + beautySimple.getImg();

            final ViewHolder finalHolder = viewHolder;
            ImageLoaderUtil.getInstance().displayListItemImage(imgUrl, finalHolder.imgBeautyGril, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    finalHolder.loading.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    finalHolder.loading.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    finalHolder.loading.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beautySimples.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBeautyGril;

        private TextView textClasifyTitle;

        private ProgressBar loading;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBeautyGril = (ImageView) itemView.findViewById(R.id.imgBeautyGril);
            textClasifyTitle = (TextView) itemView.findViewById(R.id.textClasifyTitle);
            loading = (ProgressBar) itemView.findViewById(R.id.loading);
        }
    }
}