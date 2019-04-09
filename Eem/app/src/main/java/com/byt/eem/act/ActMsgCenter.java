package com.byt.eem.act;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseActEd;
import com.byt.eem.base.BaseHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.models.BaseModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected int setupViewRes() {
        return R.layout.act_msg_center;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);


        getList();
    }

    private void getList() {

    }

    class Alarm extends BaseModel {

    }

    class AdapterAlarms extends MBaseAdapter<Alarm> {


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

}
