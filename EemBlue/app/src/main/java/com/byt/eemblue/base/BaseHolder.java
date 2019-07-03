package com.byt.eemblue.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.souja.lib.utils.ScreenUtil;

import butterknife.ButterKnife;

/**
 * Created by Ydz on 2017/3/29 0029.
 */

public class BaseHolder extends RecyclerView.ViewHolder {
    public BaseHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        ScreenUtil.initScale(itemView);
    }
}
