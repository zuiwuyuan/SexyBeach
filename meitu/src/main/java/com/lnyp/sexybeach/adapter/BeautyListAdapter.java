package com.lnyp.sexybeach.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.common.Const;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.weight.ShowMaxImageView;

import java.util.List;

public class BeautyListAdapter extends RecyclerView.Adapter {

    private LayoutInflater mLayoutInflater;

    private List<BeautySimple> beautySimples;

    private View.OnClickListener beautySimplesItemOnClick;

    private Fragment mContext;

    public BeautyListAdapter(Fragment context, List<BeautySimple> beautySimples, View.OnClickListener beautySimplesItemOnClick) {
        mLayoutInflater = LayoutInflater.from(context.getActivity());

        mContext = context;
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

            String imgUrl = Const.BASE_IMG_URL2 + beautySimple.getImg();

            LogUtils.e(imgUrl);

            Glide.with(mContext).load(imgUrl).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.imgBeautyGril);

            viewHolder.textClasifyTitle.setText(beautySimple.getTitle());


            viewHolder.itemView.setTag(position);
            viewHolder.itemView.setOnClickListener(beautySimplesItemOnClick);
        }
    }

    @Override
    public int getItemCount() {
        return beautySimples.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private ShowMaxImageView imgBeautyGril;

        private TextView textClasifyTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBeautyGril = (ShowMaxImageView) itemView.findViewById(R.id.imgBeautyGril);
            textClasifyTitle = (TextView) itemView.findViewById(R.id.textClasifyTitle);
        }
    }

}