package com.byt.eem.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.byt.eem.R;
import com.byt.eem.inter.IListPage;
import com.byt.eem.util.HttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.ODataPage;
import com.souja.lib.widget.MLoadingDialog;
import com.souja.lib.widget.TitleBar;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseListAct extends BaseAct implements IListPage {

    @BindView(R.id.m_title)
    TitleBar mTitleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.m_loading)
    MLoadingDialog mLoadingDialog;

    @Override
    protected int setupViewRes() {
        return R.layout.act_base_list_page;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        setupTitle(mTitleBar);
        setupRecyclerView(recyclerView);
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (pageIndex < pageAmount) {
                    pageIndex++;
                    getList(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                updateList();
            }
        });
        getList(false);
    }

    public abstract void setupTitle(TitleBar titleBar);

    public abstract boolean isGet();

    protected int pageIndex = 1, pageAmount = 1;
    private Callback.Cancelable request;

    protected void getList(boolean retry) {
        if (retry) mLoadingDialog.setRetryDefaultTip();
        request = HttpUtil.Request(null, getRequestUrl(pageIndex),
                isGet() ? HttpMethod.GET : HttpMethod.POST,
                getRequestParams(), getResultClass(), new IHttpCallBack() {
                    @Override
                    public <T> void OnSuccess(String msg, ODataPage page, ArrayList<T> data) {
                        smartRefresh.finishRefresh();
                        smartRefresh.finishLoadMore();
                        if (pageIndex == 1) {
                            clearList();
                            pageAmount = page.getTotalPages();
                        }

                        if (data.size() > 0) {
                            addItems(data);
                        }
                        smartRefresh.setEnableLoadMore(pageIndex < pageAmount);
                        notifyAdapter();
                    }

                    @Override
                    public void OnFailure(String msg) {
                        if (mLoadingDialog.isShowing()) {
                            mLoadingDialog.setErrMsgRetry(msg);
                            mLoadingDialog.setMClick(() -> getList(true));
                        } else {
                            smartRefresh.finishRefresh();
                            smartRefresh.finishLoadMore();
                            showToast(msg);
                            if (pageIndex > 1) pageIndex--;
                        }
                    }
                });
    }

    public void hideLoading() {
        mLoadingDialog.dismiss();
    }

    public void updateList() {
        pageIndex = 1;
        getList(false);
    }

    @Override
    protected void onDestroy() {
        if (request != null && !request.isCancelled()) request.cancel();
        super.onDestroy();
    }
}
