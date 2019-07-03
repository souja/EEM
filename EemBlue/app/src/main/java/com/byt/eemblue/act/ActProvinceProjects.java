package com.byt.eemblue.act;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.byt.eemblue.R;
import com.byt.eemblue.base.BaseActEd;
import com.byt.eemblue.base.BaseHolder;
import com.byt.eemblue.util.HttpUtil;
import com.byt.eemblue.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.inter.CommonItemClickListener;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//xx省项目列表
public class ActProvinceProjects extends BaseActEd {
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.rv_projects)
    RecyclerView rvProjects;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    private int provinceId;

    private List<ProvProj> mList;
    private AdapterProvProj mAdapter;

    @Override
    protected int setupViewRes() {
        return R.layout.act_province_projects;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);

        provinceId = getIntent().getIntExtra("id", 0);
        String provinceStr = getIntent().getStringExtra("str");
        ((TitleBar) findViewById(R.id.m_title)).setTitle(provinceStr + "项目总览");
        mList = new ArrayList<>();
        mAdapter = new AdapterProvProj(_this, new ArrayList<>(), position ->
                NEXT(new Intent(_this, ActDeviceList.class)
                        .putExtra("id", mList.get(position).getProjectId())));
        rvProjects.setAdapter(mAdapter);
        smartRefresh.setEnableLoadMore(false);
        smartRefresh.setOnRefreshListener(refreshLayout -> getList());
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mList.size() == 0) return;
                if (editable.length() == 0) mAdapter.setDataList(mList);
                List<ProvProj> tempList = new ArrayList<>();
                String key = editable.toString();
                for (ProvProj model : mList) {
                    if (model.getProjectName().contains(key)) {
                        tempList.add(model);
                    }
                }
                mAdapter.setDataList(tempList);
            }
        });
        getList();
    }

    private void getList() {
        Post(getDialog(), MConstants.URL.GET_PROJECTS_BY_PROVINCE + provinceId,
                HttpUtil.defaultParam(), ProvProj.class, new IHttpCallBack<ProvProj>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<ProvProj> data) {
                        mList.clear();
                        if (data.size() > 0) {
                            mList.addAll(data);
                        }
                        mAdapter.setDataList(mList);
                        smartRefresh.finishRefresh();
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                        smartRefresh.finishRefresh();
                    }
                });
    }

    class AdapterProvProj extends MBaseAdapter<ProvProj> {


        public AdapterProvProj(Context context, List<ProvProj> list, CommonItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
            return new HolderProvProj(mInflater.inflate(R.layout.item_prov_proj, parent, false));
        }

        @Override
        public void onBindView(RecyclerView.ViewHolder holder, int position) {
            HolderProvProj mHolder = (HolderProvProj) holder;
            ProvProj model = getItem(position);
            mHolder.tvName.setText(model.getProjectName());
            mHolder.tvLoc.setText(model.getCityName());
            mHolder.tvCount.setText(String.valueOf("设备数  " + model.getDeviceCount()));
            mHolder.tvOnlineCount.setText(String.valueOf("在线  " + model.getProperlyCount()));
            mHolder.tvOfflineCount.setText(String.valueOf("离线  " + model.getOffLineCount()));
            mHolder.tvWarnCount.setText(String.valueOf("告警  " + model.getWarnCount()));

            mHolder.itemView.setOnClickListener(view -> mItemClickListener.onClick(position));
        }

        public void setDataList(List<ProvProj> tempList) {
            mList.clear();
            mList.addAll(tempList);
            notifyDataSetChanged();
        }
    }

    static class HolderProvProj extends BaseHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_loc)
        TextView tvLoc;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_onlineCount)
        TextView tvOnlineCount;
        @BindView(R.id.tv_offlineCount)
        TextView tvOfflineCount;
        @BindView(R.id.tv_warnCount)
        TextView tvWarnCount;

        public HolderProvProj(View itemView) {
            super(itemView);
        }
    }

    class ProvProj extends BaseModel {

        /**
         * ProjectId : 1
         * ProjectName : 军区总医院
         * ProvinceName : 四川
         * CityName : 成都市
         * DeviceCount : 10
         * WarnCount : 0
         * ProperlyCount : 1
         * OffLineCount : 9
         * OperateTime : 2019-03-07T15:14:15
         */

        private int ProjectId;
        private String ProjectName;
        private String ProvinceName;
        private String CityName;
        private String DeviceCount;
        private String WarnCount;
        private String ProperlyCount;
        private String OffLineCount;
        private String OperateTime;

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

        public String getProvinceName() {
            return ProvinceName;
        }

        public void setProvinceName(String ProvinceName) {
            this.ProvinceName = ProvinceName;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String CityName) {
            this.CityName = CityName;
        }

        public String getDeviceCount() {
            return DeviceCount;
        }

        public void setDeviceCount(String DeviceCount) {
            this.DeviceCount = DeviceCount;
        }

        public String getWarnCount() {
            return WarnCount;
        }

        public void setWarnCount(String WarnCount) {
            this.WarnCount = WarnCount;
        }

        public String getProperlyCount() {
            return ProperlyCount;
        }

        public void setProperlyCount(String ProperlyCount) {
            this.ProperlyCount = ProperlyCount;
        }

        public String getOffLineCount() {
            return OffLineCount;
        }

        public void setOffLineCount(String OffLineCount) {
            this.OffLineCount = OffLineCount;
        }

        public String getOperateTime() {
            return OperateTime;
        }

        public void setOperateTime(String OperateTime) {
            this.OperateTime = OperateTime;
        }
    }


}
