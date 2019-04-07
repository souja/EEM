package com.byt.eem.act;

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
import com.souja.lib.widget.LoadingDialog;
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

    private static final String[] simCardTypeStr = new String[]{"中国移动", "中国电信", "中国联通"};

    List<String> mListProjectStr;
    private List<Project> mListProject;
    List<String> mListDeviceTypeStr;
    private List<DeviceType> mListDeviceType;


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

    private String selectedProjectStr, selectedDeviceTypeStr,selectedSimTypeStr;

    private void initProjectWheel() {
        if (mListProjectStr == null) mListProjectStr = new ArrayList<>();
        else mListProjectStr.clear();

        for (Project model : mListProject) {
            mListProjectStr.add(model.TProjectName);
        }
        wv.setItems(mListProjectStr);
        wv.setListener(index -> selectedProjectStr = mListProjectStr.get(index));
        projects.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_cancel1).setOnClickListener(view -> projects.setVisibility(View.GONE));
        findViewById(R.id.tv_confirm1).setOnClickListener(view -> {
            if (TextUtils.isEmpty(selectedProjectStr)) selectedProjectStr = mListProjectStr.get(0);
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
        wv2.setListener(index -> selectedDeviceTypeStr = mListDeviceTypeStr.get(index));
        deviceType.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_cancel2).setOnClickListener(view -> deviceType.setVisibility(View.GONE));
        findViewById(R.id.tv_confirm2).setOnClickListener(view -> {
            if (TextUtils.isEmpty(selectedDeviceTypeStr))
                selectedDeviceTypeStr = mListDeviceTypeStr.get(0);
            deviceType.setVisibility(View.GONE);
            tvDeviceType.setText(selectedDeviceTypeStr);
        });
    }

    private void initSimTypeWheel() {
        wv3.setItems(Arrays.asList(simCardTypeStr));
        wv3.setListener(index -> selectedSimTypeStr = simCardTypeStr[index]);
        findViewById(R.id.tv_cancel3).setOnClickListener(view -> simCardType.setVisibility(View.GONE));
        findViewById(R.id.tv_confirm3).setOnClickListener(view -> {
            if (TextUtils.isEmpty(selectedSimTypeStr))
                selectedSimTypeStr = simCardTypeStr[0];
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

}
