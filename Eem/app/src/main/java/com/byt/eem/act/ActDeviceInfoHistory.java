package com.byt.eem.act;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.frag.FragHistoryDataChart;
import com.byt.eem.frag.FragHistoryDataText;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.MTool;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//设备状态历史记录
public class ActDeviceInfoHistory extends BaseAct {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tb_menu)
    TabLayout tbMenu;
    @BindView(R.id.vp_data)
    ViewPager vpData;
    @BindView(R.id.tv_date1)
    TextView tvDate1;
    @BindView(R.id.ll_chooseDate1)
    LinearLayout llChooseDate1;
    @BindView(R.id.tv_date2)
    TextView tvDate2;
    @BindView(R.id.ll_chooseDate2)
    LinearLayout llChooseDate2;

    private int deviceId;
    private static final String[] menus = new String[]{"列表", "漏电流", "电流", "电压", "温度"};
    FragHistoryDataText fragText;
    FragHistoryDataChart fragChart1,//漏电流
            fragChart2,//电流
            fragChart3,//电压
            fragChart4;//温度

    @Override
    protected int setupViewRes() {
        return R.layout.act_device_info_history;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        deviceId = getIntent().getIntExtra("id", 0);
        ibBack.setOnClickListener(view -> finish());
        fragText = new FragHistoryDataText();
        fragChart1 = new FragHistoryDataChart();
        fragChart2 = new FragHistoryDataChart();
        fragChart3 = new FragHistoryDataChart();
        fragChart4 = new FragHistoryDataChart();
        MTool.reflex(tbMenu, 0, 69);
        vpData.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 1:
                        return fragChart1;
                    case 2:
                        return fragChart2;
                    case 3:
                        return fragChart3;
                    case 4:
                        return fragChart4;
                    default:
                        return fragText;
                }
            }

            @Override
            public int getCount() {
                return menus.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return menus[position];
            }
        });
        vpData.setOffscreenPageLimit(menus.length - 1);
        tbMenu.setupWithViewPager(vpData);
//        getHistory();
    }

    private void getHistory() {
        Post(MConstants.URL.GET_DEVICES_HISTORY_STATE,
                HttpUtil.formatParams(new Param(deviceId, "2019-01-01 11:01", "2019-01-02 11:01").toString()),
                History.class, new IHttpCallBack<History>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<History> data) {

                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }

    class History extends BaseModel {

        /**
         * DeviceName : 1楼总配电箱
         * State : 在线
         * OperateTime : 2019-01-01T11:01:52
         * FirstLeakage : 36.1
         * Axelectricity : 0
         * Bxelectricity : 0
         * Cxelectricity : 0
         * CommonAxVoltage : 232.1
         * CommonBxVoltage : 232.2
         * CommonCxVoltage : 232.2
         * FirstChannelTemperature : 9.1
         * SecondChannelTemperature : 10.3
         * ThirdChannelTemperature : 10.3
         * FourthChannelTemperature : 9.5
         */

        private String DeviceName;
        private String State;
        private String OperateTime;
        private String FirstLeakage;
        private String Axelectricity;
        private String Bxelectricity;
        private String Cxelectricity;
        private String CommonAxVoltage;
        private String CommonBxVoltage;
        private String CommonCxVoltage;
        private String FirstChannelTemperature;
        private String SecondChannelTemperature;
        private String ThirdChannelTemperature;
        private String FourthChannelTemperature;

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

        public String getAxelectricity() {
            return Axelectricity;
        }

        public void setAxelectricity(String Axelectricity) {
            this.Axelectricity = Axelectricity;
        }

        public String getBxelectricity() {
            return Bxelectricity;
        }

        public void setBxelectricity(String Bxelectricity) {
            this.Bxelectricity = Bxelectricity;
        }

        public String getCxelectricity() {
            return Cxelectricity;
        }

        public void setCxelectricity(String Cxelectricity) {
            this.Cxelectricity = Cxelectricity;
        }

        public String getCommonAxVoltage() {
            return CommonAxVoltage;
        }

        public void setCommonAxVoltage(String CommonAxVoltage) {
            this.CommonAxVoltage = CommonAxVoltage;
        }

        public String getCommonBxVoltage() {
            return CommonBxVoltage;
        }

        public void setCommonBxVoltage(String CommonBxVoltage) {
            this.CommonBxVoltage = CommonBxVoltage;
        }

        public String getCommonCxVoltage() {
            return CommonCxVoltage;
        }

        public void setCommonCxVoltage(String CommonCxVoltage) {
            this.CommonCxVoltage = CommonCxVoltage;
        }

        public String getFirstChannelTemperature() {
            return FirstChannelTemperature;
        }

        public void setFirstChannelTemperature(String FirstChannelTemperature) {
            this.FirstChannelTemperature = FirstChannelTemperature;
        }

        public String getSecondChannelTemperature() {
            return SecondChannelTemperature;
        }

        public void setSecondChannelTemperature(String SecondChannelTemperature) {
            this.SecondChannelTemperature = SecondChannelTemperature;
        }

        public String getThirdChannelTemperature() {
            return ThirdChannelTemperature;
        }

        public void setThirdChannelTemperature(String ThirdChannelTemperature) {
            this.ThirdChannelTemperature = ThirdChannelTemperature;
        }

        public String getFourthChannelTemperature() {
            return FourthChannelTemperature;
        }

        public void setFourthChannelTemperature(String FourthChannelTemperature) {
            this.FourthChannelTemperature = FourthChannelTemperature;
        }
    }

    class Param extends BaseModel {
        int Id;
        String beginTime, endTime;

        public Param(int id, String beginTime, String endTime) {
            Id = id;
            this.beginTime = beginTime;
            this.endTime = endTime;
        }
    }
}
