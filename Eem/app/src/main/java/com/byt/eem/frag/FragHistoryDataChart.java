package com.byt.eem.frag;

import android.view.LayoutInflater;

import com.byt.eem.R;
import com.byt.eem.base.MBaseLazyFragmentBHor;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragHistoryDataChart extends MBaseLazyFragmentBHor {

    Unbinder unbinder;


    @Override
    public void onFirstUserVisible() {
        _contentView = LayoutInflater.from(mBaseActivity).inflate(R.layout.frag_his_data_chart, null, false);
        setContentView(_contentView);
        unbinder = ButterKnife.bind(this, _contentView);

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

}
