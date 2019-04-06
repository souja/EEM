package com.byt.eem.frag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseHolder;
import com.byt.eem.base.MBaseLazyFragmentB;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.inter.CommonItemClickListener;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.MDateUtils;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragReport extends MBaseLazyFragmentB {

    @BindView(R.id.rv_msgList)
    RecyclerView rvMsgList;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    Unbinder unbinder;

    private String startTime, endTime;
    private List<Report> mList;
    private AdapterReport mAdapter;

    @Override
    public void onFirstUserVisible() {
        _contentView = LayoutInflater.from(mBaseActivity).inflate(R.layout.frag_report, null, false);
        setContentView(_contentView);
        unbinder = ButterKnife.bind(this, _contentView);
        smartRefresh.setEnableLoadMore(false);
        smartRefresh.setOnRefreshListener(refreshLayout -> getReports());
        mList = new ArrayList<>();
        mAdapter = new AdapterReport(mBaseActivity, mList, position -> {

        });
        rvMsgList.setAdapter(mAdapter);
        startTime = MDateUtils.getStringDate(-7);
        endTime = MDateUtils.getStringDate(0);
        LogUtil.e("Init StartTime:" + startTime + ",EndTime:" + endTime);
        startTime = "2018-01-03 11:01";
        endTime = "2019-01-03 11:01";
        getReports();
    }

    private void getReports() {
        Post(MConstants.URL.GET_PROJECT_STATISTICS,
                HttpUtil.formatParams(new Param(startTime, endTime).toString()),
                Report.class, new IHttpCallBack() {
                    @Override
                    public <T> void OnSuccess(String msg, ODataPage page, ArrayList<T> data) {
                        mList.clear();
                        if (data.size() > 0) {
                            mList.addAll((Collection<? extends Report>) data);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }


    class AdapterReport extends MBaseAdapter<Report> {

        public AdapterReport(Context context, List<Report> list, CommonItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
            return new HolderReport(mInflater.inflate(R.layout.item_report, parent, false));
        }

        @Override
        public void onBindView(RecyclerView.ViewHolder holder, int position) {
            HolderReport mHolder = (HolderReport) holder;
            Report model = getItem(position);
            mHolder.tvName.setText(model.getProjectName());
            mHolder.tvCount.setText(String.valueOf("报警次数：" + model.getCounts() + "次"));
            //todo 缺少"项目编号"
            if (isLastItem(position)) {
                mHolder.vBot.setVisibility(View.GONE);
                mHolder.vTemp.setVisibility(View.VISIBLE);
            }else{
                mHolder.vBot.setVisibility(View.VISIBLE);
                mHolder.vTemp.setVisibility(View.GONE);
            }
        }
    }

    static class HolderReport extends BaseHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.v_bot)
        View vBot;
        @BindView(R.id.v_temp)
        View vTemp;

        public HolderReport(View itemView) {
            super(itemView);
        }
    }

    class Report extends BaseModel {

        /**
         * ProjectId : 1
         * ProjectName : 军区总医院
         * Counts : 448
         */

        private int ProjectId;
        private String ProjectName;
        private int Counts;

        public int getProjectId() {
            return ProjectId;
        }

        public void setProjectId(int ProjectId) {
            this.ProjectId = ProjectId;
        }

        public String getProjectName() {
            return ProjectName;
        }

        public void setProjectName(String ProjectName) {
            this.ProjectName = ProjectName;
        }

        public int getCounts() {
            return Counts;
        }

        public void setCounts(int Counts) {
            this.Counts = Counts;
        }
    }

    class Param extends BaseModel {
        public String beginTime, endTime;

        public Param(String beginTime, String endTime) {
            this.beginTime = beginTime;
            this.endTime = endTime;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}
