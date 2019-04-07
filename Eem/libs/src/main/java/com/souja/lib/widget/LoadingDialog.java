package com.souja.lib.widget;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.souja.lib.R;
import com.souja.lib.utils.ScreenUtil;


/**
 * 菊花...
 * Created by Yangdz on 2015/2/4.
 */
public class LoadingDialog extends ProgressDialog {

    private Context mContext;
    private TextView tvMsg;
    private String msg;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.mContext = context;
    }

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.loading_dialog);
        this.mContext = context;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.progress_dialog, null);
        ScreenUtil.initScale(contentView);
        tvMsg = contentView.findViewById(R.id.content);
        if (msg != null) tvMsg.setText(msg);
        setCanceledOnTouchOutside(false);
        setContentView(contentView);
    }

    public void setMsg(String msg) {
        if(!TextUtils.isEmpty(msg)){
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(msg);
        }else tvMsg.setVisibility(View.GONE);
    }
}