package com.byt.eem.act;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.MTool;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//设置
public class ActSet extends BaseAct {
    @BindView(R.id.tv_device)
    TextView tvDevice;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ed_value1)
    EditText edValue1;
    @BindView(R.id.ed_value2)
    EditText edValue2;
    @BindView(R.id.ed_value3)
    EditText edValue3;
    @BindView(R.id.ed_value4)
    EditText edValue4;
    @BindView(R.id.ed_value5)
    EditText edValue5;
    @BindView(R.id.btn_read)
    Button btnRead;
    @BindView(R.id.btn_set)
    Button btnSet;

    @Override
    protected int setupViewRes() {
        return R.layout.act_set;
    }

    private String deviceCode;
    private DeviceParam mDeviceParam;

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        deviceCode = getIntent().getStringExtra("code");
        String status = getIntent().getStringExtra("status");
        tvDevice.setText(deviceCode);
        tvStatus.setText(status);
        btnRead.setOnClickListener(view -> getDeviceParam());
        btnSet.setOnClickListener(view -> setupParams());
    }

    private void getDeviceParam() {
        Post(getDialog(), MTool.formatStr(MConstants.URL.GET_DEVICE_PARAM, deviceCode), DeviceParam.class,
                new IHttpCallBack<DeviceParam>() {
                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<DeviceParam> data) {
                        if (data != null && data.size() > 0) {
                            mDeviceParam = data.get(0);
                            initParams();
                        }
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }

    private void initParams() {
        edValue1.setText("" + mDeviceParam.getLeakageThreshold());//漏电xx值
        edValue2.setText("" + mDeviceParam.getTemperatureThreshold());//温度xx值
        edValue3.setText("" + mDeviceParam.getElectricityThreshold());//电流xx值
        edValue4.setText("" + mDeviceParam.getVoltageMaxThreshold());//过电xx值
        edValue5.setText("" + mDeviceParam.getVoltageMinThreshold());//欠电xx值
    }

    private void setupParams() {
        String valStr1 = edValue1.getText().toString().trim();
        if (valStr1.isEmpty()) {
            showToast("请输入\"漏电报警值\"");
            return;
        }
        int val1 = Integer.parseInt(valStr1);
        if (val1 <= 0) {
            showToast("\"漏电报警值\"设置需要大于0");
            return;
        }

        String valStr2 = edValue1.getText().toString().trim();
        if (valStr2.isEmpty()) {
            showToast("请输入\"温度报警值\"");
            return;
        }
        int val2 = Integer.parseInt(valStr2);
        if (val2 <= 0) {
            showToast("\"温度报警值\"设置需要大于0");
            return;
        }

        String valStr3 = edValue1.getText().toString().trim();
        if (valStr3.isEmpty()) {
            showToast("请输入\"电流报警值\"");
            return;
        }
        int val3 = Integer.parseInt(valStr3);
        if (val3 <= 0) {
            showToast("\"电流报警值\"设置需要大于0");
            return;
        }


        String valStr4 = edValue1.getText().toString().trim();
        if (valStr4.isEmpty()) {
            showToast("请输入\"过电压报警值\"");
            return;
        }
        int val4 = Integer.parseInt(valStr4);
        if (val4 <= 0) {
            showToast("\"过电压报警值\"设置需要大于0");
            return;
        }


        String valStr5 = edValue1.getText().toString().trim();
        if (valStr5.isEmpty()) {
            showToast("请输入\"欠电压报警值\"");
            return;
        }
        int val5 = Integer.parseInt(valStr5);
        if (val5 <= 0) {
            showToast("\"欠电压报警值\"设置需要大于0");
            return;
        }


        SetParam param = new SetParam();
        param.LeakageThreshold = val1;
        param.TemperatureThreshold = val2;
        param.ElectricityThreshold = val3;
        param.VoltageMaxThreshold = val4;
        param.VoltageMinThreshold = val5;
        Post(getDialog("设置中..."), MConstants.URL.SET_DEVICE_PARAM,
                HttpUtil.formatParams(param.toString()), new IHttpCallBack<Object>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<Object> data) {
                        showToast("设置成功");
                        addSubscription(MConstants.RX_UPDATE_DEVICE_INFO, "");
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });

    }

    class SetParam extends BaseModel {
        int LeakageThreshold;//漏电
        int TemperatureThreshold;//温度
        int ElectricityThreshold;//电流
        int VoltageMaxThreshold;//过电
        int VoltageMinThreshold;//欠电
    }

    class DeviceParam extends BaseModel {

        /**
         * Id : 1
         * DeviceCode : testwd2018
         * DeviceName : 1楼总配电箱
         * TDeviceTypeId : 3
         * TProjectId : 1
         * DeployAdress : 神仙树
         * DeviceEnableDate : 2018-12-29T05:31:59
         * DeviceDisableDate : 2019-12-28T21:32:18
         * IotCardNo : 15682075691
         * IotCardType : 移动
         * IotCardEnableDate : 2018-12-29T05:34:29
         * IotCardDisableDate : 2019-06-30T05:34:38
         * IotCardValidDays : 60
         * DTypeName : 智慧用电
         * TProjectName : 军区总医院
         * ElectricityThreshold : 100
         * VoltageMaxThreshold : 242
         * VoltageMinThreshold : 110
         * LeakageThreshold : 500
         * TemperatureThreshold : 60
         * IsClose : true
         * ContactsMan : null
         * ContactsManPhoneNumber : null
         * IsSendDangerMsg : null
         * IsReceiveVoiceNotify : null
         */

        private int Id;
        private String DeviceCode;
        private String DeviceName;
        private int TDeviceTypeId;
        private int TProjectId;
        private String DeployAdress;
        private String DeviceEnableDate;
        private String DeviceDisableDate;
        private String IotCardNo;
        private String IotCardType;
        private String IotCardEnableDate;
        private String IotCardDisableDate;
        private int IotCardValidDays;
        private String DTypeName;
        private String TProjectName;
        private int ElectricityThreshold;
        private int VoltageMaxThreshold;
        private int VoltageMinThreshold;
        private int LeakageThreshold;
        private int TemperatureThreshold;
        private boolean IsClose;
        private Object ContactsMan;
        private Object ContactsManPhoneNumber;
        private Object IsSendDangerMsg;
        private Object IsReceiveVoiceNotify;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
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

        public int getTDeviceTypeId() {
            return TDeviceTypeId;
        }

        public void setTDeviceTypeId(int TDeviceTypeId) {
            this.TDeviceTypeId = TDeviceTypeId;
        }

        public int getTProjectId() {
            return TProjectId;
        }

        public void setTProjectId(int TProjectId) {
            this.TProjectId = TProjectId;
        }

        public String getDeployAdress() {
            return DeployAdress;
        }

        public void setDeployAdress(String DeployAdress) {
            this.DeployAdress = DeployAdress;
        }

        public String getDeviceEnableDate() {
            return DeviceEnableDate;
        }

        public void setDeviceEnableDate(String DeviceEnableDate) {
            this.DeviceEnableDate = DeviceEnableDate;
        }

        public String getDeviceDisableDate() {
            return DeviceDisableDate;
        }

        public void setDeviceDisableDate(String DeviceDisableDate) {
            this.DeviceDisableDate = DeviceDisableDate;
        }

        public String getIotCardNo() {
            return IotCardNo;
        }

        public void setIotCardNo(String IotCardNo) {
            this.IotCardNo = IotCardNo;
        }

        public String getIotCardType() {
            return IotCardType;
        }

        public void setIotCardType(String IotCardType) {
            this.IotCardType = IotCardType;
        }

        public String getIotCardEnableDate() {
            return IotCardEnableDate;
        }

        public void setIotCardEnableDate(String IotCardEnableDate) {
            this.IotCardEnableDate = IotCardEnableDate;
        }

        public String getIotCardDisableDate() {
            return IotCardDisableDate;
        }

        public void setIotCardDisableDate(String IotCardDisableDate) {
            this.IotCardDisableDate = IotCardDisableDate;
        }

        public int getIotCardValidDays() {
            return IotCardValidDays;
        }

        public void setIotCardValidDays(int IotCardValidDays) {
            this.IotCardValidDays = IotCardValidDays;
        }

        public String getDTypeName() {
            return DTypeName;
        }

        public void setDTypeName(String DTypeName) {
            this.DTypeName = DTypeName;
        }

        public String getTProjectName() {
            return TProjectName;
        }

        public void setTProjectName(String TProjectName) {
            this.TProjectName = TProjectName;
        }

        public int getElectricityThreshold() {
            return ElectricityThreshold;
        }

        public void setElectricityThreshold(int ElectricityThreshold) {
            this.ElectricityThreshold = ElectricityThreshold;
        }

        public int getVoltageMaxThreshold() {
            return VoltageMaxThreshold;
        }

        public void setVoltageMaxThreshold(int VoltageMaxThreshold) {
            this.VoltageMaxThreshold = VoltageMaxThreshold;
        }

        public int getVoltageMinThreshold() {
            return VoltageMinThreshold;
        }

        public void setVoltageMinThreshold(int VoltageMinThreshold) {
            this.VoltageMinThreshold = VoltageMinThreshold;
        }

        public int getLeakageThreshold() {
            return LeakageThreshold;
        }

        public void setLeakageThreshold(int LeakageThreshold) {
            this.LeakageThreshold = LeakageThreshold;
        }

        public int getTemperatureThreshold() {
            return TemperatureThreshold;
        }

        public void setTemperatureThreshold(int TemperatureThreshold) {
            this.TemperatureThreshold = TemperatureThreshold;
        }

        public boolean isIsClose() {
            return IsClose;
        }

        public void setIsClose(boolean IsClose) {
            this.IsClose = IsClose;
        }

        public Object getContactsMan() {
            return ContactsMan;
        }

        public void setContactsMan(Object ContactsMan) {
            this.ContactsMan = ContactsMan;
        }

        public Object getContactsManPhoneNumber() {
            return ContactsManPhoneNumber;
        }

        public void setContactsManPhoneNumber(Object ContactsManPhoneNumber) {
            this.ContactsManPhoneNumber = ContactsManPhoneNumber;
        }

        public Object getIsSendDangerMsg() {
            return IsSendDangerMsg;
        }

        public void setIsSendDangerMsg(Object IsSendDangerMsg) {
            this.IsSendDangerMsg = IsSendDangerMsg;
        }

        public Object getIsReceiveVoiceNotify() {
            return IsReceiveVoiceNotify;
        }

        public void setIsReceiveVoiceNotify(Object IsReceiveVoiceNotify) {
            this.IsReceiveVoiceNotify = IsReceiveVoiceNotify;
        }
    }
}
