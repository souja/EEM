package com.byt.eem.frag;

import android.view.LayoutInflater;

import com.byt.eem.R;
import com.byt.eem.act.ActDeviceInfoHistory;
import com.byt.eem.base.MBaseLazyFragmentBHor;
import com.byt.eem.widget.IChart;
import com.byt.eem.widget.LeakChart;
import com.byt.eem.widget.MultiBarChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragHistoryDataChart extends MBaseLazyFragmentBHor {

    Unbinder unbinder;

    private int pageType;

    private IChart mIChart;

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
        if (pageType == 1) {
            mIChart = new LeakChart(_contentView.findViewById(R.id.chart));
        } else {
            mIChart = new MultiBarChart(_contentView.findViewById(R.id.chart));
        }
        if (mData != null) {
            mIChart.setData(mData, pageType);
        }
        setContentView(_contentView);
        unbinder = ButterKnife.bind(this, _contentView);

    }

    private ArrayList<ActDeviceInfoHistory.History> mData;

    public void setData(List<ActDeviceInfoHistory.History> data) {
        mData = (ArrayList<ActDeviceInfoHistory.History>) data;
        if (mIChart != null) {
            mIChart.setData((ArrayList<ActDeviceInfoHistory.History>) data, pageType);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

}
