package com.byt.eem.act

import android.content.Context
import android.content.Intent
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.byt.eem.R
import com.byt.eem.baidu.BaiduMapHelper
import com.byt.eem.base.BaseActEd
import com.souja.lib.utils.GsonUtil
import kotlinx.android.synthetic.main.activity_act_new_project.*
import org.xutils.common.util.LogUtil
import java.lang.ref.WeakReference

class ActNewProject : BaseActEd() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ActNewProject::class.java))
        }
    }

    override fun setupViewRes() = R.layout.activity_act_new_project

    private var mBaiduMapHelper: BaiduMapHelper? = null

    override fun initMain() {
        mBaiduMapHelper = BaiduMapHelper(MyLocationListener(this))
        mBaiduMapHelper?.start(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBaiduMapHelper?.destroy()
    }

    private fun updateAddress(address: String) {
        tv_address.setText(address)
    }

    class MyLocationListener(activity: ActNewProject) : BDAbstractLocationListener() {

        private val mActivityRef = WeakReference<ActNewProject>(activity)

        override fun onReceiveLocation(location: BDLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            LogUtil.e(GsonUtil.objToJsonString(location))
            val addr = location.addrStr    //获取详细地址信息
            val country = location.country    //获取国家
            val province = location.province    //获取省份
            val city = location.city    //获取城市
            val district = location.district    //获取区县
            val street = location.street    //获取街道信息
            location.latitude
            location.longitude
            mActivityRef.get()?.updateAddress(street)
        }
    }

}
