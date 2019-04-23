package com.byt.eem.frag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.act.ActMsgCenter;
import com.byt.eem.base.BaseHolder;
import com.byt.eem.base.MBaseLazyFragmentB;
import com.byt.eem.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragMsg extends MBaseLazyFragmentB {

    @BindView(R.id.smartRefresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_msgList)
    RecyclerView rvMsgList;

    Unbinder unbinder;

    private List<Msg> mList;
    private AdapterMsg mAdapter;

    @Override
    public void onFirstUserVisible() {
        _contentView = LayoutInflater.from(mBaseActivity).inflate(R.layout.frag_msg, null, false);
        setContentView(_contentView);
        unbinder = ButterKnife.bind(this, _contentView);
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> getMsgList());
        mList = new ArrayList<>();
        mAdapter = new AdapterMsg(mBaseActivity, mList);
        rvMsgList.setAdapter(mAdapter);
        getMsgList();
    }

    private void getMsgList() {
        Post(MConstants.URL.GET_ALARM_STATISTICS, Msg.class, new IHttpCallBack<Msg>() {

            @Override
            public void OnSuccess(String msg, ODataPage page, ArrayList<Msg> data) {
                mRefreshLayout.finishRefresh();
                mList.clear();
                if (data.size() > 0) {
                    mList.addAll(data);
                }
                mAdapter.notifyDataSetChanged();
                if (mList.size() == 0) {
                    ShowEmptyView();
                } else HideEmptyView();
            }

            @Override
            public void OnFailure(String msg) {
                mRefreshLayout.finishRefresh();
                showToast(msg);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    class Msg extends BaseModel {

        /**
         * MessageType : 告警消息
         * Counts : 1658
         * AlarmTime : 2019-03-26T14:18:44
         */

        private String MessageType;
        private String Counts;
        private String AlarmTime;

        public String getMessageType() {
            return MessageType;
        }

        public void setMessageType(String MessageType) {
            this.MessageType = MessageType;
        }

        public String getCounts() {
            return Counts;
        }

        public void setCounts(String Counts) {
            this.Counts = Counts;
        }

        public String getAlarmTime() {
            return AlarmTime;
        }

        public void setAlarmTime(String AlarmTime) {
            this.AlarmTime = AlarmTime;
        }
    }

    class AdapterMsg extends MBaseAdapter<Msg> {


        public AdapterMsg(Context context, List<Msg> list) {
            super(context, list, null);
        }

        @Override
        public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
            return new HolderMsg(mInflater.inflate(R.layout.item_msg, parent, false));
        }

        @Override
        public void onBindView(RecyclerView.ViewHolder holder, int position) {
            HolderMsg mHolder = (HolderMsg) holder;
            Msg model = getItem(position);
            mHolder.tvMsgType.setText(model.getMessageType());
            mHolder.tvMsgTime.setText(String.valueOf("报警时间：" + model.getAlarmTime()
                    .replace("T", "-")));
            mHolder.tvMsgCount.setText(model.getCounts());
            mHolder.itemView.setOnClickListener(view -> GO(ActMsgCenter.class));
        }
    }

    static class HolderMsg extends BaseHolder {

        @BindView(R.id.tv_msgType)
        TextView tvMsgType;
        @BindView(R.id.tv_msgTime)
        TextView tvMsgTime;
        @BindView(R.id.tv_msgCount)
        TextView tvMsgCount;

        public HolderMsg(View itemView) {
            super(itemView);
        }
    }
}
