package com.lnyp.sexybeach.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.entry.BeautyClassify;

import java.util.List;

/**
 * 图片分类
 */
public class BeautyCiasifyAdapter extends RecyclerView.Adapter<BeautyCiasifyAdapter.ImgInfoHolder> {

    private Activity context;

    private List<BeautyClassify> datas;

    private LayoutInflater mInflater;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public BeautyCiasifyAdapter(Activity context, List<BeautyClassify> datas) {

        this.context = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ImgInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImgInfoHolder holder = new ImgInfoHolder(mInflater.inflate(
                R.layout.list_item_beauty_clasify, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImgInfoHolder holder, int position) {

        holder.textClasifyTitle.setText(datas.get(position).getTitle());

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ImgInfoHolder extends RecyclerView.ViewHolder {

        ImageView imgClasifyUrl;
        TextView textClasifyTitle;

        public ImgInfoHolder(View itemView) {
            super(itemView);

            imgClasifyUrl = (ImageView) itemView.findViewById(R.id.imgClasifyUrl);
            textClasifyTitle = (TextView) itemView.findViewById(R.id.textClasifyTitle);
        }
    }
}
