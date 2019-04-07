package com.byt.eem.act;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.byt.eem.EApp;
import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.DialogFactory;
import com.souja.lib.widget.LoadingDialog;
import com.souja.lib.widget.TitleBar;
import com.weigan.loopview.LoopView;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

//添加设备
public class ActCreateDevice extends BaseAct {


    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_deviceType)
    TextView tvDeviceType;
    @BindView(R.id.ed_deviceCode)
    EditText edDeviceCode;
    @BindView(R.id.ib_scan)
    ImageButton ibScan;
    @BindView(R.id.ed_deviceName)
    EditText edDeviceName;
    @BindView(R.id.ed_deployAddress)
    EditText edDeployAddress;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.ed_simNo)
    EditText edSimNo;
    @BindView(R.id.tv_simType)
    TextView tvSimType;
    @BindView(R.id.tv_simStartTime)
    TextView tvSimStartTime;
    @BindView(R.id.ed_simEffPeriodTime)
    EditText edSimEffPeriodTime;
    @BindView(R.id.tv_simEndTime)
    TextView tvSimEndTime;
    @BindView(R.id.wheelView1)
    LoopView wv;
    @BindView(R.id.projects)
    View projects;
    @BindView(R.id.wheelView2)
    LoopView wv2;
    @BindView(R.id.deviceType)
    View deviceType;
    @BindView(R.id.wheelView3)
    LoopView wv3;
    @BindView(R.id.simCardType)
    View simCardType;

    @Override
    protected int setupViewRes() {
        return R.layout.act_new_device;
    }

    private final String[] simCardTypeStr = new String[]{"中国移动", "中国电信", "中国联通"};

    List<String> mListProjectStr;
    private List<Project> mListProject;
    List<String> mListDeviceTypeStr;
    private List<DeviceType> mListDeviceType;

    private String selectedProjectStr, selectedDeviceTypeStr, selectedSimTypeStr;

    private DatePickerDialog startDialog, simStartDialog, endDialog, simEndDialog;

    private String deviceCode;

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        deviceCode = getIntent().getStringExtra("code");
        LogUtil.e("Code:" + deviceCode);
        if (!TextUtils.isEmpty(deviceCode)) {
            edDeviceCode.setText(deviceCode);
        }
        //项目
        tvProject.setOnClickListener(view -> {
            if (mListProjectStr == null) {
                getUserProjects();
            } else projects.setVisibility(View.VISIBLE);
        });
        //设备类型
        tvDeviceType.setOnClickListener(view -> {
            if (mListDeviceTypeStr == null) getDeviceType();
            else deviceType.setVisibility(View.VISIBLE);
        });

        //扫描二维码
        ibScan.setOnClickListener(view -> {
            Consumer onScanBack = (Consumer<ActScanQRCode.RxScanResult>) rxResult -> {
                String result = rxResult.result;
                LogUtil.e("扫描结果：" + result);
                edDeviceCode.setText(result);
                rxResult.act.finish();
                delAction(MConstants.RX_SCAN_QR_CODE);
            };
            addAction(MConstants.RX_SCAN_QR_CODE, onScanBack);
            GO(ActScanQRCode.class);
        });
        //卡类型
        initSimTypeWheel();
        tvSimType.setOnClickListener(view -> simCardType.setVisibility(View.VISIBLE));
        initDatePickers();
        //启用时间
        tvStartTime.setOnClickListener(view -> startDialog.show());
        //截止时间
        tvEndTime.setOnClickListener(view -> endDialog.show());
        //sim卡激活日期
        tvSimStartTime.setOnClickListener(view -> simStartDialog.show());
        //sim卡截止日期
        tvSimEndTime.setOnClickListener(view -> simEndDialog.show());
        //保存数据
        ((TitleBar) findViewById(R.id.m_title)).setRightClick(view -> saveDeviceInfo());
    }

    private void saveDeviceInfo() {
        if (tvProject.getText().toString().isEmpty()) {
            showToast("请选择项目");
            return;
        }
        if (tvDeviceType.getText().toString().isEmpty()) {
            showToast("请选择设备类型");
            return;
        }
        deviceCode = edDeviceCode.getText().toString().trim();
        if (deviceCode.isEmpty()) {
            showToast("请输入设备编码");
            return;
        }
        String deviceName = edDeviceName.getText().toString().trim();
        if (deviceName.isEmpty()) {
            showToast("请输入设备名称");
            return;
        }
        String address = edDeployAddress.getText().toString().trim();
        if (address.isEmpty()) {
            showToast("请输入部署地址");
            return;
        }
        String sTime = tvStartTime.getText().toString();
        if (sTime.isEmpty()) {
            showToast("请选择启用时间");
            return;
        }
        String eTime = tvEndTime.getText().toString();
        if (eTime.isEmpty()) {
            showToast("请选择截止时间");
            return;
        }
        String simNo = edSimNo.getText().toString().trim();
        if (simNo.isEmpty()) {
            showToast("请输入卡号");
            return;
        }
        if (tvSimType.getText().toString().isEmpty()) {
            showToast("请选择卡类型");
            return;
        }
        String ssTime = tvSimStartTime.getText().toString();
        if (ssTime.isEmpty()) {
            showToast("请选择卡激活日期");
            return;
        }
        String effPeriod = edSimEffPeriodTime.getText().toString().trim();
        if (effPeriod.isEmpty()) {
            showToast("请输入有效期");
            return;
        }
        String seTime = tvSimEndTime.getText().toString();
        if (seTime.isEmpty()) {
            showToast("请选择卡截止日期");
            return;
        }
        Param param = new Param();
        param.setDeviceCode(deviceCode);
        param.setDeviceName(deviceName);
        int typeId = 0;
        for (DeviceType type : mListDeviceType) {
            if (type.getDTypeName().equals(selectedDeviceTypeStr)) {
                typeId = type.getId();
                break;
            }
        }
        param.setTDeviceTypeId(typeId);
        int projectId = 0;
        for (Project pro : mListProject) {
            if (pro.TProjectName.equals(selectedProjectStr)) {
                projectId = pro.Id;
                break;
            }
        }
        param.setTProjectId(projectId);
        param.setDeployAdress(address);
        param.setDeviceEnableDate(sTime);
        param.setDeviceDisableDate(eTime);
        param.setIotCardNo(simNo);
        param.setIotCardType(selectedSimTypeStr);
        param.setIotCardEnableDate(ssTime);
        param.setIotCardValidDays(Integer.parseInt(effPeriod));
        param.setIotCardDisableDate(seTime);

        Post(new LoadingDialog(_this, "数据保存中..."), MConstants.URL.GET_SAVE_DEVICE_INFO,
                HttpUtil.formatParams(param.toString()), new IHttpCallBack() {

                    @Override
                    public <T> void OnSuccess(String msg, ODataPage page, ArrayList<T> data) {
                        showToast("保存成功");
                        finish();
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }


    private void initDatePickers() {
        startDialog = DialogFactory.getDatePickerDialog(_this, tvStartTime);
        simStartDialog = DialogFactory.getDatePickerDialog(_this, tvSimStartTime);
        endDialog = DialogFactory.getDatePickerDialog(_this, tvEndTime);
        simEndDialog = DialogFactory.getDatePickerDialog(_this, tvSimEndTime);
    }

    private void getUserProjects() {
        Post(new LoadingDialog(_this, ""),
                MConstants.URL.GET_MY_AND_MYCHILD_PROJECTS + EApp.getUserInfo().getId(),
                HttpUtil.defaultParam(), Project.class, new IHttpCallBack() {
                    @Override
                    public <T> void OnSuccess(String msg, ODataPage page, ArrayList<T> data) {
                        if (mListProject == null) mListProject = new ArrayList<>();
                        else mListProject.clear();
                        if (data.size() > 0) {
                            mListProject.addAll((Collection<? extends Project>) data);
                            initProjectWheel();
                        } else showToast("暂无可选项目");
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }

    private void getDeviceType() {
        Post(new LoadingDialog(_this, ""), MConstants.URL.GET_ALL_DEVICE_TYPE,
                HttpUtil.defaultParam(), DeviceType.class, new IHttpCallBack() {
                    @Override
                    public <T> void OnSuccess(String msg, ODataPage page, ArrayList<T> data) {
                        if (mListDeviceType == null) mListDeviceType = new ArrayList<>();
                        else mListDeviceType.clear();
                        if (data.size() > 0) {
                            mListDeviceType.addAll((Collection<? extends DeviceType>) data);
                            initDeviceTypeWheel();
                        } else showToast("暂无可选设备类型");
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }

    private void initProjectWheel() {
        if (mListProjectStr == null) mListProjectStr = new ArrayList<>();
        else mListProjectStr.clear();

        for (Project model : mListProject) {
            mListProjectStr.add(model.TProjectName);
        }
        wv.setItems(mListProjectStr);
        projects.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_cancel1).setOnClickListener(view -> projects.setVisibility(View.GONE));
        findViewById(R.id.tv_confirm1).setOnClickListener(view -> {
            selectedProjectStr = mListProjectStr.get(wv.getSelectedItem());
            projects.setVisibility(View.GONE);
            tvProject.setText(selectedProjectStr);
        });

    }

    private void initDeviceTypeWheel() {
        if (mListDeviceTypeStr == null) mListDeviceTypeStr = new ArrayList<>();
        else mListDeviceTypeStr.clear();

        for (DeviceType model : mListDeviceType) {
            mListDeviceTypeStr.add(model.getDTypeName());
        }
        wv2.setItems(mListDeviceTypeStr);
        deviceType.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_cancel2).setOnClickListener(view -> deviceType.setVisibility(View.GONE));
        findViewById(R.id.tv_confirm2).setOnClickListener(view -> {
            selectedDeviceTypeStr = mListDeviceTypeStr.get(wv2.getSelectedItem());
            deviceType.setVisibility(View.GONE);
            tvDeviceType.setText(selectedDeviceTypeStr);
        });
    }

    private void initSimTypeWheel() {
        wv3.setItems(Arrays.asList(simCardTypeStr));
        findViewById(R.id.tv_cancel3).setOnClickListener(view -> simCardType.setVisibility(View.GONE));
        findViewById(R.id.tv_confirm3).setOnClickListener(view -> {
            selectedSimTypeStr = simCardTypeStr[wv3.getSelectedItem()];
            simCardType.setVisibility(View.GONE);
            tvSimType.setText(selectedSimTypeStr);
        });
    }

    //项目
    class Project extends BaseModel {
        public int Id;
        public String TProjectName;
    }

    //设备类型
    class DeviceType extends BaseModel {

        /**
         * Id : 1
         * DTypeCode : 001
         * DTypeName : 火灾监控
         */

        private int Id;
        private String DTypeCode;
        private String DTypeName;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getDTypeCode() {
            return DTypeCode;
        }

        public void setDTypeCode(String DTypeCode) {
            this.DTypeCode = DTypeCode;
        }

        public String getDTypeName() {
            return DTypeName;
        }

        public void setDTypeName(String DTypeName) {
            this.DTypeName = DTypeName;
        }
    }

    //接口参数
    class Param extends BaseModel {

        /**
         * DeviceCode : testd123
         * DeviceName : 测试设备
         * TDeviceTypeId : 1
         * TProjectId : 1
         * DeployAdress : 神仙树2号
         * DeviceEnableDate : 2018-1-3 17:51
         * DeviceDisableDate : 2019-1-3 17:51
         * IotCardNo : 13882636665
         * IotCardType : 中国联通
         * IotCardEnableDate : 2019-1-4 09:00
         * IotCardValidDays : 60
         * IotCardDisableDate : 2019-3-4 09:00
         */

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
        private int IotCardValidDays;
        private String IotCardDisableDate;

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

        public int getIotCardValidDays() {
            return IotCardValidDays;
        }

        public void setIotCardValidDays(int IotCardValidDays) {
            this.IotCardValidDays = IotCardValidDays;
        }

        public String getIotCardDisableDate() {
            return IotCardDisableDate;
        }

        public void setIotCardDisableDate(String IotCardDisableDate) {
            this.IotCardDisableDate = IotCardDisableDate;
        }
    }

}
