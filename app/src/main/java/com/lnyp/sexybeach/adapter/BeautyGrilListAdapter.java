package com.lnyp.sexybeach.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnyp.sexybeach.R;
import com.lnyp.sexybeach.common.Const;
import com.lnyp.sexybeach.entry.BeautySimple;
import com.lnyp.sexybeach.util.ImageLoaderUtil;

import java.util.List;

/**
 * 图片列表
 */
public class BeautyGrilListAdapter extends RecyclerView.Adapter<BeautyGrilListAdapter.ImgInfoHolder> {

    private Activity context;

    private List<BeautySimple> datas;

    private LayoutInflater mInflater;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public BeautyGrilListAdapter(Activity context, List<BeautySimple> datas) {

        this.context = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ImgInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImgInfoHolder holder = new ImgInfoHolder(mInflater.inflate(
                R.layout.list_item_beauty_gril, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImgInfoHolder holder, int position) {

        holder.textClasifyTitle.setText(datas.get(position).getTitle());
//        String imgUrl = Const.BASE_IMG_URL1 + datas.get(position).getImg() + "_800x600";
        String imgUrl = Const.BASE_IMG_URL2 + datas.get(position).getImg();
        ImageLoaderUtil.getInstance().displayListItemImage(imgUrl, holder.imgBeautyGril, null);

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

        ImageView imgBeautyGril;
        TextView textClasifyTitle;

        public ImgInfoHolder(View itemView) {
            super(itemView);
            imgBeautyGril = (ImageView) itemView.findViewById(R.id.imgBeautyGril);
            textClasifyTitle = (TextView) itemView.findViewById(R.id.textClasifyTitle);
        }
    }
}
