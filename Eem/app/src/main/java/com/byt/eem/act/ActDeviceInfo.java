package com.byt.eem.act;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.widget.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActDeviceInfo extends BaseAct {

    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rv_status)
    RecyclerView rvStatus;

    private int deviceId;
    private DeviceInfo mDeviceInfo;

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
        getDeviceInfo();
    }

    private void getDeviceInfo() {
        Post(MConstants.URL.GET_DEVICES_STATE_BY_DEVICEID + deviceId, HttpUtil.defaultParam(),
                DeviceInfo.class, new IHttpCallBack<DeviceInfo>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<DeviceInfo> data) {
                        if (data.size() > 0) {
                            mDeviceInfo = data.get(0);
                        }
                        initPage();
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }

    private void initPage() {
        if (mDeviceInfo == null) return;

    }


    class DeviceInfo extends BaseModel {

        /**
         * DeviceName : 1楼总配电箱
         * State : 在线
         * OperateTime : 2019-03-07T15:14:15
         * FirstLeakage : 34.8
         * FirstLeakageState : 正常
         * Axelectricity : 15
         * AxelectricityState : 告警
         * Bxelectricity : 16
         * BxelectricityState : 告警
         * Cxelectricity : 0
         * CxelectricityState : 正常
         * CommonAxVoltage : 230.8
         * CommonAxVoltageState : 正常
         * CommonBxVoltage : 230.9
         * CommonBxVoltageState : 正常
         * CommonCxVoltage : 230.9
         * CommonCxVoltageState : 正常
         * FirstChannelTemperature : 14.6
         * FirstChannelTemperatureState : 告警
         * SecondChannelTemperature : 15
         * SecondChannelTemperatureState : 正常
         * ThirdChannelTemperature : 14.4
         * ThirdChannelTemperatureState : 正常
         * FourthChannelTemperature : 15
         * FourthChannelTemperatureState : 正常
         */

        private String DeviceName;
        private String State;
        private String OperateTime;
        private String FirstLeakage;
        private String FirstLeakageState;
        private String Axelectricity;
        private String AxelectricityState;
        private String Bxelectricity;
        private String BxelectricityState;
        private String Cxelectricity;
        private String CxelectricityState;
        private String CommonAxVoltage;
        private String CommonAxVoltageState;
        private String CommonBxVoltage;
        private String CommonBxVoltageState;
        private String CommonCxVoltage;
        private String CommonCxVoltageState;
        private String FirstChannelTemperature;
        private String FirstChannelTemperatureState;
        private String SecondChannelTemperature;
        private String SecondChannelTemperatureState;
        private String ThirdChannelTemperature;
        private String ThirdChannelTemperatureState;
        private String FourthChannelTemperature;
        private String FourthChannelTemperatureState;

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

        public String getFirstLeakage() {
            return FirstLeakage;
        }

        public void setFirstLeakage(String FirstLeakage) {
            this.FirstLeakage = FirstLeakage;
        }

        public String getFirstLeakageState() {
            return FirstLeakageState;
        }

        public void setFirstLeakageState(String FirstLeakageState) {
            this.FirstLeakageState = FirstLeakageState;
        }

        public String getAxelectricity() {
            return Axelectricity;
        }

        public void setAxelectricity(String Axelectricity) {
            this.Axelectricity = Axelectricity;
        }

        public String getAxelectricityState() {
            return AxelectricityState;
        }

        public void setAxelectricityState(String AxelectricityState) {
            this.AxelectricityState = AxelectricityState;
        }

        public String getBxelectricity() {
            return Bxelectricity;
        }

        public void setBxelectricity(String Bxelectricity) {
            this.Bxelectricity = Bxelectricity;
        }

        public String getBxelectricityState() {
            return BxelectricityState;
        }

        public void setBxelectricityState(String BxelectricityState) {
            this.BxelectricityState = BxelectricityState;
        }

        public String getCxelectricity() {
            return Cxelectricity;
        }

        public void setCxelectricity(String Cxelectricity) {
            this.Cxelectricity = Cxelectricity;
        }

        public String getCxelectricityState() {
            return CxelectricityState;
        }

        public void setCxelectricityState(String CxelectricityState) {
            this.CxelectricityState = CxelectricityState;
        }

        public String getCommonAxVoltage() {
            return CommonAxVoltage;
        }

        public void setCommonAxVoltage(String CommonAxVoltage) {
            this.CommonAxVoltage = CommonAxVoltage;
        }

        public String getCommonAxVoltageState() {
            return CommonAxVoltageState;
        }

        public void setCommonAxVoltageState(String CommonAxVoltageState) {
            this.CommonAxVoltageState = CommonAxVoltageState;
        }

        public String getCommonBxVoltage() {
            return CommonBxVoltage;
        }

        public void setCommonBxVoltage(String CommonBxVoltage) {
            this.CommonBxVoltage = CommonBxVoltage;
        }

        public String getCommonBxVoltageState() {
            return CommonBxVoltageState;
        }

        public void setCommonBxVoltageState(String CommonBxVoltageState) {
            this.CommonBxVoltageState = CommonBxVoltageState;
        }

        public String getCommonCxVoltage() {
            return CommonCxVoltage;
        }

        public void setCommonCxVoltage(String CommonCxVoltage) {
            this.CommonCxVoltage = CommonCxVoltage;
        }

        public String getCommonCxVoltageState() {
            return CommonCxVoltageState;
        }

        public void setCommonCxVoltageState(String CommonCxVoltageState) {
            this.CommonCxVoltageState = CommonCxVoltageState;
        }

        public String getFirstChannelTemperature() {
            return FirstChannelTemperature;
        }

        public void setFirstChannelTemperature(String FirstChannelTemperature) {
            this.FirstChannelTemperature = FirstChannelTemperature;
        }

        public String getFirstChannelTemperatureState() {
            return FirstChannelTemperatureState;
        }

        public void setFirstChannelTemperatureState(String FirstChannelTemperatureState) {
            this.FirstChannelTemperatureState = FirstChannelTemperatureState;
        }

        public String getSecondChannelTemperature() {
            return SecondChannelTemperature;
        }

        public void setSecondChannelTemperature(String SecondChannelTemperature) {
            this.SecondChannelTemperature = SecondChannelTemperature;
        }

        public String getSecondChannelTemperatureState() {
            return SecondChannelTemperatureState;
        }

        public void setSecondChannelTemperatureState(String SecondChannelTemperatureState) {
            this.SecondChannelTemperatureState = SecondChannelTemperatureState;
        }

        public String getThirdChannelTemperature() {
            return ThirdChannelTemperature;
        }

        public void setThirdChannelTemperature(String ThirdChannelTemperature) {
            this.ThirdChannelTemperature = ThirdChannelTemperature;
        }

        public String getThirdChannelTemperatureState() {
            return ThirdChannelTemperatureState;
        }

        public void setThirdChannelTemperatureState(String ThirdChannelTemperatureState) {
            this.ThirdChannelTemperatureState = ThirdChannelTemperatureState;
        }

        public String getFourthChannelTemperature() {
            return FourthChannelTemperature;
        }

        public void setFourthChannelTemperature(String FourthChannelTemperature) {
            this.FourthChannelTemperature = FourthChannelTemperature;
        }

        public String getFourthChannelTemperatureState() {
            return FourthChannelTemperatureState;
        }

        public void setFourthChannelTemperatureState(String FourthChannelTemperatureState) {
            this.FourthChannelTemperatureState = FourthChannelTemperatureState;
        }
    }
}
