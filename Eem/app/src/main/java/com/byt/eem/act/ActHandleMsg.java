package com.byt.eem.act;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.DialogFactory;
import com.souja.lib.utils.MDateUtils;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//处理告警
public class ActHandleMsg extends BaseAct {
    @BindView(R.id.rg_status)
    RadioGroup rgStatus;
    @BindView(R.id.ed_cause)
    EditText edCause;
    @BindView(R.id.ed_exeProcess)
    EditText edExeProcess;
    @BindView(R.id.btn_save)
    AppCompatButton btnSave;
    @BindView(R.id.btn_cancel)
    AppCompatButton btnCancel;
    @BindView(R.id.tv_datetime)
    TextView tvDatetime;
    @BindView(R.id.ll_chooseDateTime)
    LinearLayout llChooseDateTime;

    private int alarmId;
    private String dateStr, timeStr = "00:00";

    @Override
    protected int setupViewRes() {
        return R.layout.act_handle_msg;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        alarmId = getIntent().getIntExtra("id", 0);
        rgStatus.check(R.id.rb_right);
        btnSave.setOnClickListener(view -> doProcess());
        btnCancel.setOnClickListener(view -> finish());

        TimePickerDialog dialogTime = new TimePickerDialog(_this, (view, hourOfDay, minute) -> {
            timeStr = hourOfDay + ":" + minute;
            LogUtil.e(timeStr);
        }, 0, 0, true);
        DatePickerDialog dialogDate = DialogFactory.getDatePickerDialog(_this, (view, year, monthOfYear, dayOfMonth) -> {
            String month, day;
            monthOfYear++;
            if (monthOfYear < 10) {
                month = "0" + monthOfYear;
            } else {
                month = String.valueOf(monthOfYear);
            }
            if (dayOfMonth < 10) {
                day = "0" + dayOfMonth;
            } else {
                day = String.valueOf(dayOfMonth);
            }
            dateStr = String.valueOf(year) + "-" + month + "-" + day;
            LogUtil.e(dateStr);
            dialogTime.show();
        });
        llChooseDateTime.setOnClickListener(view -> dialogDate.show());
    }

    private void doProcess() {
        int isReal = rgStatus.getCheckedRadioButtonId() == R.id.rb_right ? 1 : 0;
        if (TextUtils.isEmpty(dateStr)) {
            showToast("请选择处理时间");
            return;
        }
        String cause = edCause.getText().toString().trim();
        if (cause.isEmpty()) {
            showToast("请输入原因分析");
            return;
        }
        String handleProcess = edExeProcess.getText().toString().trim();
        if (handleProcess.isEmpty()) {
            showToast("请输入处理过程");
            return;
        }
        Param param = new Param();
        param.TAlarmRecordId = alarmId;
        param.Reason = cause;
        param.Solve = handleProcess;
        param.IsFact = isReal;
        param.OperateTime = MDateUtils.getCurrentDateTime();
        param.ProcessDate = dateStr + " " + timeStr;

        Post(MConstants.URL.PROCESS_ALARM_MSG, HttpUtil.formatParams(param.toString()), new IHttpCallBack() {

            @Override
            public void OnSuccess(String msg, ODataPage page, ArrayList data) {
                showToast("处理成功");
                addSubscription(MConstants.RX_PROCESS_ALARM_MSG, "");
                tvDatetime.postDelayed(() -> finish(), 100);
            }

            @Override
            public void OnFailure(String msg) {
                showToast(msg);
            }
        });
    }


    class Param extends BaseModel {

        /**
         * IsFact : 1
         * Reason : 原因不明
         * TAlarmRecordId : 155
         * ProcessDate : 2019-01-03 11:01
         * Solve : 关掉负载
         * OperateTime : 2019-01-03 11:01
         * Id : 155
         */

        private int IsFact;
        private String Reason;
        private int TAlarmRecordId;
        private String ProcessDate;
        private String Solve;
        private String OperateTime;
        private int Id;

        public int getIsFact() {
            return IsFact;
        }

        public void setIsFact(int IsFact) {
            this.IsFact = IsFact;
        }

        public String getReason() {
            return Reason;
        }

        public void setReason(String Reason) {
            this.Reason = Reason;
        }

        public int getTAlarmRecordId() {
            return TAlarmRecordId;
        }

        public void setTAlarmRecordId(int TAlarmRecordId) {
            this.TAlarmRecordId = TAlarmRecordId;
        }

        public String getProcessDate() {
            return ProcessDate;
        }

        public void setProcessDate(String ProcessDate) {
            this.ProcessDate = ProcessDate;
        }

        public String getSolve() {
            return Solve;
        }

        public void setSolve(String Solve) {
            this.Solve = Solve;
        }

        public String getOperateTime() {
            return OperateTime;
        }

        public void setOperateTime(String OperateTime) {
            this.OperateTime = OperateTime;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }
    }
}
