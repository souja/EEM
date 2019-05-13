package com.byt.eem.act;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
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
import com.souja.lib.utils.DialogFactory;
import com.souja.lib.utils.MDateUtils;
import com.souja.lib.utils.MTool;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//设备状态历史记录
//测试数据获取：2019-1-1 19:50 2019-1-1 19:52
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
    @BindView(R.id.btn_confirm)
    View btnConfirm;
    @BindView(R.id.layout_empty)
    LinearLayout mPlaceHolder;

    private int deviceId;
    private static final String[] menus = new String[]{"列表", "漏电流", "电流", "电压", "温度"};
    FragHistoryDataText fragText;
    FragHistoryDataChart fragChart1,//漏电流
            fragChart2,//电流
            fragChart3,//电压
            fragChart4;//温度

    private String beginDateTime, endDateTime;
    private String dateStr1, timeStr1,//起始日期，时间
            dateStr2, timeStr2;//截至日期，时间
    private final String tag1 = "起始日期：", tag2 = "截至日期：";

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
        Bundle bd1 = new Bundle();
        bd1.putInt("type",1);
        fragChart1.setArguments(bd1);
        fragChart2 = new FragHistoryDataChart();
        Bundle bd2 = new Bundle();
        bd2.putInt("type",2);
        fragChart2.setArguments(bd2);
        fragChart3 = new FragHistoryDataChart();
        Bundle bd3 = new Bundle();
        bd3.putInt("type",3);
        fragChart3.setArguments(bd3);
        fragChart4 = new FragHistoryDataChart();
        Bundle bd4 = new Bundle();
        bd4.putInt("type",4);
        fragChart4.setArguments(bd4);
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
        //默认查找昨天~今天的数据
        beginDateTime = MDateUtils.getStringDate(-1);//昨天
        endDateTime = MDateUtils.getStringDate(0);//今天
        LogUtil.e(beginDateTime + "   " + endDateTime);
        tvDate1.setText(String.valueOf(tag1 + beginDateTime));
        tvDate2.setText(String.valueOf(tag2 + endDateTime));
        //选择起始时间
        TimePickerDialog dialogTime1 = new TimePickerDialog(_this, (view, hourOfDay, minute) -> {
            timeStr1 = getTimeStr(hourOfDay, minute);
            setupTime(tvDate1, timeStr1);
        }, 0, 0, true);
        DatePickerDialog dialogDate1 = DialogFactory.getDatePickerDialog(_this, (view, year, monthOfYear, dayOfMonth) -> {
            dateStr1 = getDateStr(year, monthOfYear, dayOfMonth);
            setupDate(tvDate1, tag1, dateStr1);
            dialogTime1.show();
        });
        llChooseDate1.setOnClickListener(v -> dialogDate1.show());
        //选择截至时间
        TimePickerDialog dialogTime2 = new TimePickerDialog(_this, (view, hourOfDay, minute) -> {
            timeStr2 = getTimeStr(hourOfDay, minute);
            setupTime(tvDate2, timeStr2);
        }, 0, 0, true);
        DatePickerDialog dialogDate2 = DialogFactory.getDatePickerDialog(_this, (view, year, monthOfYear, dayOfMonth) -> {
            dateStr2 = getDateStr(year, monthOfYear, dayOfMonth);
            setupDate(tvDate2, tag2, dateStr2);
            dialogTime2.show();
        });
        llChooseDate2.setOnClickListener(v -> dialogDate2.show());
        //确定日期选择，获取数据
        btnConfirm.setOnClickListener(v -> {
            beginDateTime = dateStr1 + " " + timeStr1;
            endDateTime = dateStr2 + " " + timeStr2;
            LogUtil.e(beginDateTime + "   " + endDateTime);
            long start = MDateUtils.stringToDateLong(beginDateTime);
            long end = MDateUtils.stringToDateLong(endDateTime);
            if (MDateUtils.calcDate(start, end) > 7) {
                showToast("数据周期不能超过7天");
                return;
            }
            getHistory();
        });
        getHistory();
    }

    private String getTimeStr(int hourOfDay, int minute) {
        String hour = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String min = minute < 10 ? "0" + minute : "" + minute;
        return hour + ":" + min;
    }

    private String getDateStr(int year, int monthOfYear, int dayOfMonth) {
        String month, day;
        monthOfYear++;
        month = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;
        day = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        return year + "-" + month + "-" + day;
    }

    private void setupDate(TextView tvDate, String tag, String newDate) {
        String oriText = tvDate.getText().toString();
        LogUtil.e("ori text:" + oriText + ",newDate:" + newDate);
        String ss = oriText.substring(oriText.indexOf(tag) + tag.length(), oriText.indexOf(" "));
        LogUtil.e("date ss:" + ss);
        oriText = oriText.replace(ss, newDate);
        tvDate.setText(oriText);
        LogUtil.e(oriText);
    }

    private void setupTime(TextView tvDate, String newTime) {
        String oriText = tvDate.getText().toString();
        String ss = oriText.substring(oriText.indexOf(" ") + 1);
        oriText = oriText.replace(ss, newTime);
        tvDate.setText(oriText);
        LogUtil.e(oriText);
    }

    private void getHistory() {
        Post(getDialog(), MConstants.URL.GET_DEVICES_HISTORY_STATE,
                HttpUtil.formatParams(new Param(deviceId, beginDateTime, endDateTime).toString()),
                History.class, new IHttpCallBack<History>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<History> data) {
                        if (data == null || data.size() < 1) {
                            mPlaceHolder.setVisibility(View.VISIBLE);
                            vpData.setVisibility(View.INVISIBLE);
                            vpData.setEnabled(false);
                        } else  {
                            vpData.setVisibility(View.VISIBLE);
                            mPlaceHolder.setVisibility(View.INVISIBLE);
                            vpData.setEnabled(true);
                            fragText.setData(data);
                            fragChart1.setData(data);
                            fragChart2.setData(data);
                            fragChart3.setData(data);
                            fragChart4.setData(data);
                        }
                    }

                    @Override
                    public void OnFailure(String msg) {
                        mPlaceHolder.setVisibility(View.VISIBLE);
                        vpData.setVisibility(View.INVISIBLE);
                        vpData.setEnabled(false);
                        showToast(msg);
                    }
                });
    }

    public class History extends BaseModel {

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
