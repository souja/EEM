package com.byt.eem.baidu

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/12 11:56 PM
 * Description :
 */
class BaiduMapHelper(private val listener: BDAbstractLocationListener) {

    private var mLocationClient: LocationClient? = null

    fun start(context: Context) {
        if (mLocationClient == null) {
            mLocationClient = LocationClient(context.applicationContext)
            //声明LocationClient类
            mLocationClient?.registerLocationListener(listener)
            //注册监听函数

            val option = LocationClientOption()
            option.setIsNeedLocationPoiList(true)
            option.setIsNeedAddress(true)
            //可选，是否需要地址信息，默认为不需要，即参数为false
            //如果开发者需要获得当前点的地址信息，此处必须为true

            mLocationClient?.locOption = option
            //mLocationClient为第二步初始化过的LocationClient对象
            //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        }
        mLocationClient?.start()
    }

    fun destroy() {
        mLocationClient?.unRegisterLocationListener(listener)
        mLocationClient?.stop()
        mLocationClient = null
    }

//    class MyLocationListener : BDAbstractLocationListener() {
//        override fun onReceiveLocation(location: BDLocation) {
//            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
//            //以下只列举部分获取地址相关的结果信息
//            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
//
//            LogUtil.e(GsonUtil.objToJsonString(location))
//            val addr = location.addrStr    //获取详细地址信息
//            val country = location.country    //获取国家
//            val province = location.province    //获取省份
//            val city = location.city    //获取城市
//            val district = location.district    //获取区县
//            val street = location.street    //获取街道信息
//        }
//    }

}