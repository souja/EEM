package com.byt.eem.act;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.base.BaseHolder;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.widget.LoadingDialog;
import com.souja.lib.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//设备参数信息
public class ActDeviceInfo extends BaseAct {

    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.rv_status)
    RecyclerView rvStatus;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout mRefreshLayout;

    private int deviceId;
    private DeviceInfo mDeviceInfo;
    private AdapterStatus mAdapterStatus;

    @Override
    protected int setupViewRes() {
        return R.layout.act_device_info;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        deviceId = getIntent().getIntExtra("id", 0);
        ((TitleBar) findViewById(R.id.m_title)).setRightClick(view ->
                NEXT(new Intent(_this, ActDeviceInfoHistory.class)
                        .putExtra("id", deviceId)));
        mAdapterStatus = new AdapterStatus(_this, new ArrayList<>());
        rvStatus.setAdapter(mAdapterStatus);
        rvStatus.setNestedScrollingEnabled(false);
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(s -> getDeviceInfo(true));
        getDeviceInfo(false);
    }

    private void getDeviceInfo(boolean refresh) {
        Post(refresh ? null : new LoadingDialog(_this), MConstants.URL.GET_DEVICES_STATE_BY_DEVICEID + deviceId,
                HttpUtil.defaultParam(), DeviceInfo.class, new IHttpCallBack<DeviceInfo>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<DeviceInfo> data) {
                        mRefreshLayout.finishRefresh();
                        if (data.size() > 0) {
                            mDeviceInfo = data.get(0);
                        }
                        initPage();
                    }

                    @Override
                    public void OnFailure(String msg) {
                        mRefreshLayout.finishRefresh();
                        showToast(msg);
                    }
                });
    }

    private void initPage() {
        if (mDeviceInfo == null || mDeviceInfo.getItems() == null || mDeviceInfo.getItems().size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            tvStatus.setText(String.valueOf("状态：" + mDeviceInfo.getState()));
            tvDate.setText(String.valueOf("时间：" + mDeviceInfo.getOperateTime()));
            mAdapterStatus.setDataList((ArrayList<ItemsBean>) mDeviceInfo.getItems());
        }
    }

    class AdapterStatus extends MBaseAdapter<ItemsBean> {

        private int colorProper, colorWarn;


        public AdapterStatus(Context context, List<ItemsBean> list) {
            super(context, list);
            Resources res = mContext.getResources();
            colorProper = res.getColor(android.R.color.holo_green_dark);
            colorWarn = res.getColor(android.R.color.holo_red_dark);
        }

        @Override
        public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
            return new HolderStatus(mInflater.inflate(R.layout.item_device_status, parent, false));
        }

        @Override
        public void onBindView(RecyclerView.ViewHolder holder, int position) {
            HolderStatus mHolder = (HolderStatus) holder;
            ItemsBean model = getItem(position);
            mHolder.tvStatusName.setText(model.getParamName());
            mHolder.tvStatusValue.setText(model.getParamValue());
            String desc = model.getParamState();
            mHolder.tvStatusDesc.setText(desc);
            if (desc.contains("正常")) {
                mHolder.tvStatusDesc.setTextColor(colorProper);
            } else {
                mHolder.tvStatusDesc.setTextColor(colorWarn);
            }
        }
    }

    static class HolderStatus extends BaseHolder {

        @BindView(R.id.tv_statusName)
        TextView tvStatusName;
        @BindView(R.id.tv_statusValue)
        TextView tvStatusValue;
        @BindView(R.id.tv_statusDesc)
        TextView tvStatusDesc;

        public HolderStatus(View itemView) {
            super(itemView);
        }
    }

    public class DeviceInfo extends BaseModel {

        /**
         * Items : [{"ParamName":"漏电流","ParamValue":"34.8","ParamState":"正常"},{"ParamName":"A相电流","ParamValue":"15","ParamState":"告警"},{"ParamName":"B相电流","ParamValue":"16","ParamState":"告警"},{"ParamName":"C相电流","ParamValue":"0","ParamState":"正常"},{"ParamName":"A相电压","ParamValue":"230.8","ParamState":"正常"},{"ParamName":"B相电压","ParamValue":"230.9","ParamState":"正常"},{"ParamName":"C相电压","ParamValue":"230.9","ParamState":"正常"},{"ParamName":"1路温度","ParamValue":"14.6","ParamState":"告警"},{"ParamName":"2路温度","ParamValue":"15","ParamState":"正常"},{"ParamName":"3路温度","ParamValue":"14.4","ParamState":"正常"},{"ParamName":"4路温度","ParamValue":"15","ParamState":"正常"}]
         * DeviceName : 1楼总配电箱
         * State : 在线
         * OperateTime : 03/07/2019 15:14:15
         */

        private String DeviceName;
        private String State;
        private String OperateTime;
        private List<ItemsBean> Items;

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

        public List<ItemsBean> getItems() {
            return Items;
        }

        public void setItems(List<ItemsBean> Items) {
            this.Items = Items;
        }

    }

    public class ItemsBean extends BaseModel {
        /**
         * ParamName : 漏电流
         * ParamValue : 34.8
         * ParamState : 正常
         */

        private String ParamName;
        private String ParamValue;
        private String ParamState;

        public String getParamName() {
            return ParamName;
        }

        public void setParamName(String ParamName) {
            this.ParamName = ParamName;
        }

        public String getParamValue() {
            return ParamValue;
        }

        public void setParamValue(String ParamValue) {
            this.ParamValue = ParamValue;
        }

        public String getParamState() {
            return ParamState;
        }

        public void setParamState(String ParamState) {
            this.ParamState = ParamState;
        }
    }

}
