package com.byt.eem.act;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseActEd;
import com.byt.eem.base.BaseHolder;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.inter.CommonItemClickListener;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.widget.CircularImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//设备列表
public class ActDeviceList extends BaseActEd {

    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.rv_devices)
    RecyclerView rvDevices;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private List<Device> mList;
    private AdapterDevice mAdapter;
    private int projectId;

    @Override
    protected int setupViewRes() {
        return R.layout.act_device_list;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        projectId = getIntent().getIntExtra("id", 0);
        mList = new ArrayList<>();
        mAdapter = new AdapterDevice(_this, new ArrayList<>(), position ->
                NEXT(new Intent(_this, ActDeviceInfo.class)
                        .putExtra("name", mList.get(position).getDeviceName())
                        .putExtra("id", mList.get(position).getDeviceId())));
        rvDevices.setAdapter(mAdapter);
        smartRefresh.setEnableLoadMore(false);
        smartRefresh.setOnRefreshListener(smartRefresh -> getList());
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
                List<Device> tempList = new ArrayList<>();
                String key = editable.toString();
                for (Device model : mList) {
                    if (model.getDeviceName().contains(key)
                            || model.getDeviceCode().contains(key)) {
                        tempList.add(model);
                    }
                }
                mAdapter.setDataList(tempList);
            }
        });

        getList();
    }

    private void getList() {
        Post(MConstants.URL.GET_DEVICES_BY_PROJECT + projectId, HttpUtil.defaultParam(),
                Device.class, new IHttpCallBack<Device>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<Device> data) {
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


    class AdapterDevice extends MBaseAdapter<Device> {

        private int colorProper, colorOffline;

        public AdapterDevice(Context context, List<Device> list, CommonItemClickListener listener) {
            super(context, list, listener);
            Resources res = context.getResources();
            colorProper = res.getColor(android.R.color.holo_green_dark);
            colorOffline = res.getColor(android.R.color.holo_orange_dark);
        }

        @Override
        public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
            return new HolderDevice(mInflater.inflate(R.layout.item_device, parent, false));
        }

        @Override
        public void onBindView(RecyclerView.ViewHolder holder, int position) {
            HolderDevice mHolder = (HolderDevice) holder;
            Device model = getItem(position);
            mHolder.tvNumber.setText(String.valueOf("设备编号  " + model.getDeviceCode()));
            mHolder.tvDeviceName.setText(model.getDeviceName());
            String status = model.getState();
            mHolder.tvStatus.setText(status);
            if (status.contains("正常")) {
                mHolder.tvStatus.setTextColor(colorProper);
            } else {
                mHolder.tvStatus.setTextColor(colorOffline);
            }
//            mHolder.tvDeviceNo.setText(model.getDeviceCode());
            String time = model.getOperateTime();
            if (TextUtils.isEmpty(time)) {
                mHolder.tvTime.setVisibility(View.INVISIBLE);
            } else {
                mHolder.tvTime.setVisibility(View.VISIBLE);
                mHolder.tvTime.setText(time.replace("T", " "));
            }
            mHolder.tvAddress.setText(String.valueOf("地址  " + model.getDeployAdress()));
            mHolder.itemView.setOnClickListener(view -> mItemClickListener.onClick(position));
        }

        public void setDataList(List<Device> tempList) {
            mList.clear();
            mList.addAll(tempList);
            notifyDataSetChanged();
        }
    }

    static class HolderDevice extends BaseHolder {

        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.civ_icon)
        CircularImageView civIcon;
        @BindView(R.id.tv_deviceName)
        TextView tvDeviceName;
        @BindView(R.id.tv_deviceNo)
        TextView tvDeviceNo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_address)
        TextView tvAddress;

        public HolderDevice(View itemView) {
            super(itemView);
        }
    }

    class Device extends BaseModel {

        /**
         * ProjectId : 1
         * ProjectName : 军区总医院
         * DeviceId : 1
         * DeviceCode : testwd2018
         * DeviceName : 1楼总配电箱
         * State : 正常
         * OperateTime : 2019-03-07T15:14:15
         * DeployAdress : 神仙树
         */

        private int ProjectId;
        private String ProjectName;
        private int DeviceId;
        private String DeviceCode;
        private String DeviceName;
        private String State;
        private String OperateTime;
        private String DeployAdress;

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

        public int getDeviceId() {
            return DeviceId;
        }

        public void setDeviceId(int DeviceId) {
            this.DeviceId = DeviceId;
        }

        public String getDeviceCode() {
            return DeviceCode;
        }

        public void setDeviceCode(String DeviceCode) {
            this.DeviceCode = DeviceCode;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String DeviceName) {
            this.DeviceName = DeviceName;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getOperateTime() {
            return OperateTime;
        }

        public void setOperateTime(String OperateTime) {
            this.OperateTime = OperateTime;
        }

        public String getDeployAdress() {
            return DeployAdress;
        }

        public void setDeployAdress(String DeployAdress) {
            this.DeployAdress = DeployAdress;
        }
    }
}
