package com.byt.eem.act;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseActEd;
import com.byt.eem.base.BaseHolder;
import com.byt.eem.model.PageModel;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.DialogFactory;
import com.souja.lib.utils.MTool;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

//消息中心
public class ActMsgCenter extends BaseActEd {
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.rv_devices)
    RecyclerView rvDevices;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;


    private List<Alarm> mList;
    private AdapterAlarms mAdapter;
    private int pageIndex = 1, pageAmount = 1;

    @Override
    protected int setupViewRes() {
        return R.layout.act_msg_center;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        Consumer handleOnProcessMsg = s -> mAdapter.handleItem();
        addAction(MConstants.RX_PROCESS_ALARM_MSG, handleOnProcessMsg);
        smartRefresh.setEnableLoadMore(false);
        smartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                getList();
            }
        });
        mList = new ArrayList<>();
        mAdapter = new AdapterAlarms(_this, mList);
        rvDevices.setAdapter(mAdapter);
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
                if (editable.length() == 0) mAdapter.setData(mList);
                List<Alarm> tempList = new ArrayList<>();
                String key = editable.toString();
                for (Alarm model : mList) {
                    if ((model.getDeviceName()!=null&&model.getDeviceName().contains(key))
                            || model.getDeployAdress().contains(key)
                            || model.getAlarmName().contains(key)) {
                        tempList.add(model);
                    }
                }
                mAdapter.setData(tempList);
            }
        });

        getList();
        getPageInfo();
    }

    PageModel mPageModel;

    private void getList() {
        if (mPageModel == null) mPageModel = new PageModel(pageIndex);
        else mPageModel.pageIndex = pageIndex;

        Post(MConstants.URL.GET_ALARM_MSG_LIST, HttpUtil.formatParams(mPageModel.toString()),
                Alarm.class, new IHttpCallBack<Alarm>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<Alarm> data) {
                        if (pageIndex == 1) mList.clear();
                        if (data.size() > 0) {
                            mList.addAll(data);
                        }
                        mAdapter.setData(mList);
                        smartRefresh.finishRefresh();
                        smartRefresh.finishLoadMore();
                        smartRefresh.setEnableLoadMore(pageIndex < pageAmount);
                    }

                    @Override
                    public void OnFailure(String msg) {
                        smartRefresh.finishRefresh();
                        smartRefresh.finishLoadMore();
                        showToast(msg);
                        if (pageIndex > 1) pageIndex--;
                    }
                });

    }

    class Alarm extends BaseModel {

        /**
         * TAlarmRecordId : 161
         * DeviceCode : testwd2018
         * DeployAdress : 神仙树
         * AlarmName : 1路漏电报警
         * HappenTime : 2018-12-07T11:16:05
         * IsProcessed : 未处理
         */

        private int TAlarmRecordId;
        private String DeviceCode;
        private String DeployAdress;
        private String AlarmName;
        private String HappenTime;
        private String IsProcessed;
        private String contactPhone = "18628084571";
        private String deviceName = "测试设备名称";

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public int getTAlarmRecordId() {
            return TAlarmRecordId;
        }

        public void setTAlarmRecordId(int TAlarmRecordId) {
            this.TAlarmRecordId = TAlarmRecordId;
        }

        public String getDeviceCode() {
            return DeviceCode;
        }

        public void setDeviceCode(String DeviceCode) {
            this.DeviceCode = DeviceCode;
        }

        public String getDeployAdress() {
            return DeployAdress;
        }

        public void setDeployAdress(String DeployAdress) {
            this.DeployAdress = DeployAdress;
        }

        public String getAlarmName() {
            return AlarmName;
        }

        public void setAlarmName(String AlarmName) {
            this.AlarmName = AlarmName;
        }

        public String getHappenTime() {
            return HappenTime;
        }

        public void setHappenTime(String HappenTime) {
            this.HappenTime = HappenTime;
        }

        public String getIsProcessed() {
            return IsProcessed;
        }

        public void setIsProcessed(String IsProcessed) {
            this.IsProcessed = IsProcessed;
        }
    }

    class AdapterAlarms extends MBaseAdapter<Alarm> {
        private int handleIndex = 0;

        public void setData(List<Alarm> list) {
            mList = list;
            notifyDataSetChanged();
        }


        public AdapterAlarms(Context context, List<Alarm> list) {
            super(context, list);
        }

        @Override
        public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
            return new HolderAlarm(mInflater.inflate(R.layout.item_msg_center, parent, false));
        }

        @Override
        public void onBindView(RecyclerView.ViewHolder holder, int position) {
            HolderAlarm mHolder = (HolderAlarm) holder;
            Alarm model = getItem(position);
            //安装地址
            mHolder.tvInstallAddress.setText(model.getDeployAdress());
            //故障名称
            mHolder.tvErrName.setText(model.getAlarmName());
            //处理状态
            mHolder.tvHandleStatus.setText(model.getIsProcessed());
            //发生时间
            mHolder.tvOccurTime.setText(model.getHappenTime().replace("T", "/"));
            //设备名称
            mHolder.tvDeviceType.setText(model.getDeviceName() + position);//TODO 缺少设备名称
            //联系电话
            mHolder.tvContactPhone.setText(model.getContactPhone());//todo 缺少联系电话
            //点击item提示是否拨打电话
            mHolder.tvContactPhone.setOnClickListener(view ->
                    DialogFactory.NewDialog(mContext, null, "是否拨打电话：" + model.getContactPhone(),
                            "确定", (d, i) -> {
                                d.dismiss();
                                MTool.CallPhone(mContext, model.getContactPhone());
                            }, "取消", (d, i) -> d.dismiss()).show());
            //点击item处理消息
            mHolder.itemView.setOnClickListener(view -> {
                handleIndex = position;
                NEXT(new Intent(_this, ActHandleMsg.class)
                        .putExtra("id", model.getTAlarmRecordId()));
            });
        }

        public void handleItem() {
            mList.remove(handleIndex);
            notifyDataSetChanged();
        }
    }

    static class HolderAlarm extends BaseHolder {

        @BindView(R.id.tv_deviceType)
        TextView tvDeviceType;
        @BindView(R.id.tv_errName)
        TextView tvErrName;
        @BindView(R.id.tv_occurTime)
        TextView tvOccurTime;
        @BindView(R.id.tv_handleStatus)
        TextView tvHandleStatus;
        @BindView(R.id.tv_installAddress)
        TextView tvInstallAddress;
        @BindView(R.id.tv_contactPhone)
        TextView tvContactPhone;

        public HolderAlarm(View itemView) {
            super(itemView);
        }
    }


    class PageInfo extends BaseModel {
        public int PageCt;//总页数
    }

    class ParamB extends BaseModel {
        int pageSize = 10;
    }

    private void getPageInfo() {
        Post(MConstants.URL.GET_ALARM_MSG_LIST_PAGEINFO, HttpUtil.formatParams(new ParamB().toString()),
                PageInfo.class, new IHttpCallBack<PageInfo>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<PageInfo> data) {
                        if (data.size() > 0) {
                            PageInfo pageInfo = data.get(0);
                            pageAmount = pageInfo.PageCt;
                            smartRefresh.setEnableLoadMore(pageIndex < pageAmount);
                        }
                    }

                    @Override
                    public void OnFailure(String msg) {
                        LogUtil.e(msg + "\n获取分页信息失败");
                    }
                });

    }
}
