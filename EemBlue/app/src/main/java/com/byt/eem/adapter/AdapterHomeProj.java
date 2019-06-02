package com.byt.eem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseHolder;
import com.byt.eem.model.OHomeProj;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.inter.CommonItemClickListener;

import java.util.List;

import butterknife.BindView;

public class AdapterHomeProj extends MBaseAdapter<OHomeProj> {


    public AdapterHomeProj(Context context, List<OHomeProj> list, CommonItemClickListener listener) {
        super(context, list, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        return new HolderProj(mInflater.inflate(R.layout.item_home_proj, parent, false));
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, int position) {
        HolderProj mHolder = (HolderProj) holder;
        OHomeProj model = getItem(position);
        int sortNumber = position + 1;
        String sortStr;
        if (sortNumber < 10) {
            sortStr = "0" + sortNumber;
        } else sortStr = String.valueOf(sortNumber);
        mHolder.tvSortNumber.setText(sortStr);

        mHolder.tvAreaName.setText(model.getProvinceName());
        mHolder.tvTotalProjects.setText(String.valueOf("共" + model.getProjectCount() + "个项目"));
        mHolder.tvOnlineCount.setText(String.valueOf("在线  " + model.getProperlyCount()));
        mHolder.tvOfflineCount.setText(String.valueOf("离线  " + model.getOffLineCount()));
        mHolder.tvWarnCount.setText(String.valueOf("报警  " + model.getWarnCount()));


        if (isLastItem(position)) mHolder.vBot.setVisibility(View.GONE);
        else mHolder.vBot.setVisibility(View.VISIBLE);

        mHolder.itemView.setOnClickListener(view -> mItemClickListener.onClick(position));
    }

    static class HolderProj extends BaseHolder {

        @BindView(R.id.tv_sortNumber)
        TextView tvSortNumber;
        @BindView(R.id.tv_areaName)
        TextView tvAreaName;
        @BindView(R.id.tv_totalProjects)
        TextView tvTotalProjects;
        @BindView(R.id.tv_onlineCount)
        TextView tvOnlineCount;
        @BindView(R.id.tv_offlineCount)
        TextView tvOfflineCount;
        @BindView(R.id.tv_warnCount)
        TextView tvWarnCount;
        @BindView(R.id.v_bot)
        View vBot;

        public HolderProj(View itemView) {
            super(itemView);
        }
    }
}
