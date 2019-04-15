package com.byt.eem.act

import android.content.Context
import android.content.Intent
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.byt.eem.R
import com.byt.eem.baidu.BaiduMapHelper
import com.byt.eem.base.BaseActEd
import com.byt.eem.model.CityBean
import com.byt.eem.model.CountyBean
import com.byt.eem.model.ProvinceBean
import com.byt.eem.util.MConstants
import com.byt.eem.widget.CityDialog
import com.byt.eem.widget.CountyDialog
import com.byt.eem.widget.ProvinceCityCountyPickerDialog
import com.byt.eem.widget.ProvinceDialog
import com.souja.lib.inter.IHttpCallBack
import com.souja.lib.models.BaseModel
import com.souja.lib.models.ODataPage
import com.souja.lib.utils.GsonUtil
import com.souja.lib.widget.LoadingDialog
import kotlinx.android.synthetic.main.activity_act_new_project.*
import org.xutils.common.util.LogUtil
import java.lang.ref.WeakReference

class ActNewProject : BaseActEd() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ActNewProject::class.java))
        }
    }

    private var mProvinceList = ArrayList<ProvinceBean>()
    //缓存的城市列表
    private val mCityMap = SparseArray<ArrayList<CityBean>>()
    //缓存的县级列表
    private val mCountyMap = HashMap<String, ArrayList<CountyBean>>()

    private var mProvinceId = -1

    private var mCityId = -1

    private var mCountyId = -1

    override fun setupViewRes() = R.layout.activity_act_new_project

    private var mBaiduMapHelper: BaiduMapHelper? = null

    override fun initMain() {
        mBaiduMapHelper = BaiduMapHelper(MyLocationListener(this))
        mBaiduMapHelper?.start(this)
        tv_province.setOnClickListener {
            if (mProvinceList.isEmpty()) {
                Post(LoadingDialog(this, "正在加载..."), MConstants.URL.GET_PROVINCES, ProvinceBean::class.java, object : IHttpCallBack<ProvinceBean> {
                    override fun OnSuccess(msg: String?, page: ODataPage?, data: java.util.ArrayList<ProvinceBean>?) {
                        if (data == null || data.isEmpty()) {
                            showToast("暂无数据")
                        } else {
                            mProvinceList.clear()
                            mProvinceList.addAll(data!!)
                            showProvincePicker(it)
                        }
                    }

                    override fun OnFailure(msg: String?) {
                    }

                })
            } else {
                showProvincePicker(it)
            }
        }
        tv_city.setOnClickListener {
            if (mProvinceId > 0) {
                val list = mCityMap.get(mProvinceId)
                if (list == null || list.isEmpty()) {
                    Post(LoadingDialog(this, "正在加载..."), MConstants.URL.GET_CITIES_BY_ID + mProvinceId, CityBean::class.java, object : IHttpCallBack<CityBean> {
                        override fun OnFailure(msg: String?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun OnSuccess(msg: String?, page: ODataPage?, data: java.util.ArrayList<CityBean>?) {
                            if (data == null || data.isEmpty()) {
                                showToast("暂无数据")
                            } else {
                                mCityMap.put(mProvinceId, data!!)
                                showCityPicker(data, it)
                            }
                        }
                    })
                } else {
                    showCityPicker(list, it)
                }
            }
        }

        tv_county.setOnClickListener {
            if (mProvinceId> 0 && mCityId > 0) {
                val list = mCountyMap["$mProvinceId$mCityId"]
                if (list == null || list.isEmpty()) {
                    Post(LoadingDialog(this, "正在加载..."), MConstants.URL.GET_COUNTIES_BY_ID + mCityId, CountyBean::class.java, object : IHttpCallBack<CountyBean> {
                        override fun OnFailure(msg: String?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun OnSuccess(msg: String?, page: ODataPage?, data: java.util.ArrayList<CountyBean>?) {
                            if (data == null || data.isEmpty()) {
                                showToast("暂无数据")
                            } else {
                                mCountyMap["$mProvinceId$mCityId"] = data
                                showCountyPicker(data, it)
                            }
                        }
                    })
                } else {
                    showCountyPicker(list, it)
                }
            }
        }
        iv_location.setOnClickListener {
            ActMap.launch(this)
        }

    }

    private fun showProvincePicker(it: View?) {
        ProvinceDialog.newInstance(mProvinceList)
                .apply {
                    setItemClickListener(object : ProvinceCityCountyPickerDialog.OnItemClickListener {
                        override fun onItemClick(id: Int, code: String, name: String) {
                            if (mProvinceId != id) {
                                this@ActNewProject.tv_city.text = "市"
                                mCityId = -1
                                this@ActNewProject.tv_county.text = "县(区)"
                                mCountyId = -1
                            }
                            mProvinceId = id
                            (it as TextView).text = name
                        }

                    })
                }.show(supportFragmentManager, "province")
    }

    private fun showCountyPicker(list: ArrayList<CountyBean>, it: View?) {
        CountyDialog.newInstance(list).apply {
            setItemClickListener(object : ProvinceCityCountyPickerDialog.OnItemClickListener {
                override fun onItemClick(id: Int, code: String, name: String) {
                    mCountyId = id
                    (it as TextView).text = name
                }

            })
        }.show(supportFragmentManager, "county")
    }

    private fun showCityPicker(list: ArrayList<CityBean>, it: View?) {
        CityDialog.newInstance(list).apply {
            setItemClickListener(object : ProvinceCityCountyPickerDialog.OnItemClickListener {
                override fun onItemClick(id: Int, code: String, name: String) {
                    if (mCityId != id) {
                        this@ActNewProject.tv_county.text = "县(区)"
                        mCountyId = -1
                    }
                    mCityId = id
                    (it as TextView).text = name
                }

            })
        }.show(supportFragmentManager, "city")
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
