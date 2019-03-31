package com.byt.eem.frag;

import android.view.LayoutInflater;

import com.byt.eem.R;
import com.byt.eem.base.MBaseLazyFragmentB;

public class FragMine extends MBaseLazyFragmentB {

    @Override
    public void onFirstUserVisible() {
        _contentView = LayoutInflater.from(mBaseActivity).inflate(R.layout.frag_mine, null, false);
        setContentView(_contentView);

        //扫一扫
//        _contentView.findViewById(R.id.scan).setOnClickListener(view -> );
        //我的项目
//        _contentView.findViewById(R.id.scan).setOnClickListener(view -> );
        //我的资料
//        _contentView.findViewById(R.id.scan).setOnClickListener(view -> );
        //关于系统
//        _contentView.findViewById(R.id.scan).setOnClickListener(view -> );
    }
}
