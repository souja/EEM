package com.souja.lib.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.souja.lib.R;
import com.souja.lib.inter.IBaseLazyFragmentListener;
import com.souja.lib.utils.ScreenUtil;


/**
 * Created by ydz on 2015/5/11.
 */
public abstract class BaseLazyFragment extends BaseFragment implements IBaseLazyFragmentListener {


    public void setTip(String tip) {
        setMClick(null);
        if (mProgressBar.getVisibility() != View.VISIBLE) mProgressBar.setVisibility(View.VISIBLE);
        mTvTip.setText(tip);
    }

    public void setErrMsg(String msg) {
        hideProgress();
        mTvTip.setVisibility(View.VISIBLE);
        mTvTip.setText(msg);
    }

    public void setErrMsg(int msgRes) {
        if (progressView.getVisibility() != View.VISIBLE)
            progressView.setVisibility(View.VISIBLE);
        hideProgress();
        mTvTip.setVisibility(View.VISIBLE);
        mTvTip.setText(msgRes);
    }

    public void setErrMsgRetry(String msg) {
        if (progressView.getVisibility() != View.VISIBLE)
            progressView.setVisibility(View.VISIBLE);
        hideProgress();
        mTvTip.setVisibility(View.VISIBLE);
        mTvTip.setText(msg + "\n\n点击重试");
    }

    public void setRetryDefaultTip() {
        setMClick(null);
        if (progressView != null && progressView.getVisibility() != View.VISIBLE)
            progressView.setVisibility(View.VISIBLE);
        if (mProgressBar.getVisibility() != View.VISIBLE) mProgressBar.setVisibility(View.VISIBLE);
        if (mTvTip != null) {
            if (mTvTip.getVisibility() != View.VISIBLE) mTvTip.setVisibility(View.VISIBLE);
            mTvTip.setText("请稍候...");
        }
    }

    protected boolean isProgressing() {
        return progressView.getVisibility() == View.VISIBLE;
    }

    public void ShowEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void setMClick(MLoadingClick listener) {
        mClick = listener;
    }


    public void setContentView(View v) {
        ScreenUtil.initScale(v);
        baseContentView.addView(v, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public void ShowProgress() {
        if (progressView.getVisibility() != View.VISIBLE)
            progressView.setVisibility(View.VISIBLE);
        if (baseContentView.getVisibility() != View.GONE)
            baseContentView.setVisibility(View.GONE);
    }

    @Override
    public void ShowContentView() {
        if (progressView.getVisibility() != View.GONE)
            progressView.setVisibility(View.GONE);
        if (baseContentView.getVisibility() != View.VISIBLE)
            baseContentView.setVisibility(View.VISIBLE);
    }

    private FrameLayout baseContentView;
    private View progressView, emptyView;
    private TextView mTvTip;
    private ProgressBar mProgressBar;
    protected View _contentView;
    private MLoadingClick mClick;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    public interface MLoadingClick {
        void onLoadingClick();
    }

    @Override
    public int setupLayoutRes() {
        return R.layout.base_f;
    }

    @Override
    public void initMain() {
        baseContentView = _rootView.findViewById(R.id.base_f_frame);
        progressView = _rootView.findViewById(R.id.rl_progress);
        emptyView = _rootView.findViewById(R.id.layout_empty);
        mProgressBar = _rootView.findViewById(R.id.progress_bar);
        mTvTip = _rootView.findViewById(R.id.content);

        progressView.setOnClickListener(vv -> {
            if (mClick != null)
                mClick.onLoadingClick();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
