package com.byt.eem.frag;

import android.view.LayoutInflater;

import com.byt.eem.R;
import com.byt.eem.base.MBaseLazyFragmentB;

public class FragMsg extends MBaseLazyFragmentB {

    @Override
    public void onFirstUserVisible() {
        _contentView = LayoutInflater.from(mBaseActivity).inflate(R.layout.frag_msg, null, false);
        setContentView(_contentView);

    }
}
