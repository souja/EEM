package com.byt.eem.frag;

import android.view.LayoutInflater;

import com.byt.eem.R;
import com.byt.eem.base.MBaseLazyFragmentBHor;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragHistoryDataChart extends MBaseLazyFragmentBHor {

    Unbinder unbinder;

    private int pageType;

    @Override
    public void onFirstUserVisible() {
        pageType = getArguments().getInt("type");
        int layoutRes;
        switch (pageType) {
            case 1:
                layoutRes = R.layout.frag_his_data_chart1;
                break;
            case 2:
                layoutRes = R.layout.frag_his_data_chart2;
                break;
            case 3:
                layoutRes = R.layout.frag_his_data_chart3;
                break;
            default:
                layoutRes = R.layout.frag_his_data_chart4;
        }
        _contentView = LayoutInflater.from(mBaseActivity).inflate(layoutRes, null, false);
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
