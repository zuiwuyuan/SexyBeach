package com.lnyp.sexybeach.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

/**
 * 图片列表
 */
public class BeautyGrilListAdapter extends BaseAdapter {

    private Activity mContext;

    private LayoutInflater mInflater;

    private List<BeautySimple> mDatas;

    public BeautyGrilListAdapter(Activity context, List<BeautySimple> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDatas = datas;

    }

    @Override
    public int getCount() {
        return (mDatas != null ? mDatas.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return (mDatas != null ? mDatas.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.list_item_beauty_gril, null);

            holder = new ViewHolder();

            holder.imgBeautyGril = (ImageView) convertView.findViewById(R.id.imgBeautyGril);
            holder.loading = (ProgressBar) convertView.findViewById(R.id.loading);
            holder.textClasifyTitle = (TextView) convertView.findViewById(R.id.textClasifyTitle);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BeautySimple beautySimple = mDatas.get(position);

        if (beautySimple != null) {
            holder.textClasifyTitle.setText(beautySimple.getTitle());
            String imgUrl = Const.BASE_IMG_URL2 + beautySimple.getImg();

            final ViewHolder finalHolder = holder;
            ImageLoaderUtil.getInstance().displayListItemImage(imgUrl, holder.imgBeautyGril, new ImageLoadingListener() {
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

        return convertView;
    }

    class ViewHolder {

        ImageView imgBeautyGril;

        TextView textClasifyTitle;

        ProgressBar loading;
    }
}
