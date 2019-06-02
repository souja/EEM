package com.byt.eem.frag

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.byt.eem.R
import com.byt.eem.act.ActDeviceInfoHistory
import com.byt.eem.base.MBaseFragmentHor
import com.byt.eem.inflate
import com.souja.lib.utils.ScreenUtil
import kotlinx.android.synthetic.main.frag_his_data_text.*
import kotlinx.android.synthetic.main.item_history_detail.view.*
import java.util.*

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/22 10:07 PM
 * Description :
 */
class FragHistoryDataText : MBaseFragmentHor() {

    override fun setupLayoutRes() = R.layout.frag_his_data_text

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_history?.adapter = HistoryAdapter()
    }

    fun setData(data: ArrayList<ActDeviceInfoHistory.History>) {
        (rv_history?.adapter as HistoryAdapter).setData(data)
    }

    override fun initMain() = Unit
}

class HistoryAdapter : RecyclerView.Adapter<HistoryHolder>() {

    private val mDataList = ArrayList<ActDeviceInfoHistory.History>()

    fun setData(data: ArrayList<ActDeviceInfoHistory.History>) {
        mDataList.clear()
        mDataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        return HistoryHolder(parent.inflate(R.layout.item_history_detail))
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bindData(mDataList[position])
    }

}

class HistoryHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    init {
        ScreenUtil.initScale(itemView)
    }

    fun bindData(history: ActDeviceInfoHistory.History) {
        itemView.tv_time.text = history.operateTime.subSequence(history.operateTime.lastIndexOf("T") + 1, history.operateTime.lastIndexOf("T") + 6)
        itemView.tv_av.text = history.commonAxVoltage + "V"
        itemView.tv_bv.text = history.commonBxVoltage + "V"
        itemView.tv_cv.text = history.commonCxVoltage + "V"
        itemView.tv_ae.text = history.axelectricity + "A"
        itemView.tv_be.text = history.bxelectricity + "A"
        itemView.tv_ce.text = history.cxelectricity + "A"
        itemView.tv_lou.text = history.firstLeakage + "mA"
        itemView.tv_tmp1.text = history.firstChannelTemperature + "\u2103"
        itemView.tv_tmp2.text = history.secondChannelTemperature + "\u2103"
        itemView.tv_tmp3.text = history.thirdChannelTemperature + "\u2103"
        itemView.tv_tmp4.text = history.fourthChannelTemperature + "\u2103"
    }


}