package com.byt.eem.act

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import com.baidu.mapapi.search.core.PoiInfo
import com.byt.eem.R
import com.byt.eem.baidu.BaiduMapHelper
import com.byt.eem.base.BaseActEd
import com.byt.eem.model.*
import com.byt.eem.util.MConstants
import com.byt.eem.widget.*
import com.souja.lib.inter.IHttpCallBack
import com.souja.lib.models.ODataPage
import com.souja.lib.widget.LoadingDialog
import kotlinx.android.synthetic.main.activity_act_new_project.*

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

    private var mCityName = ""

    private var mCountyId = -1

    override fun setupViewRes() = R.layout.activity_act_new_project

    private var mBaiduMapHelper: BaiduMapHelper? = null

    private var mPostProjectInfo: MyProjectBean = MyProjectBean() //创建项目所需信息

    override fun initMain() {
        tv_province.setOnClickListener {
            if (mProvinceList.isEmpty()) {
                Post(LoadingDialog(this, "正在加载..."), MConstants.URL.GET_PROVINCES, ProvinceBean::class.java, object : IHttpCallBack<ProvinceBean> {
                    override fun OnSuccess(msg: String?, page: ODataPage?, data: java.util.ArrayList<ProvinceBean>?) {
                        if (data == null || data.isEmpty()) {
                            showToast("暂无数据")
                        } else {
                            mProvinceList.clear()
                            mProvinceList.addAll(data)
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
            if (mProvinceId > 0 && mCityId > 0) {
                val list = mCountyMap["$mProvinceId$mCityId"]
                if (list == null || list.isEmpty()) {
                    Post(LoadingDialog(this, "正在加载..."), MConstants.URL.GET_COUNTIES_BY_ID + mCityId, CountyBean::class.java, object : IHttpCallBack<CountyBean> {
                        override fun OnFailure(msg: String?) {
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
            if (TextUtils.isEmpty(mCityName) || mCountyId == -1) {
                showToast("请先选择城市!")
            } else {
                ActMap.launch(this, mCityName, 1)
            }
        }

        rl_user.setOnClickListener {
            Post(LoadingDialog(this, "正在加载..."), MConstants.URL.GET_ALL_USER, UserControllerBean::class.java, object : IHttpCallBack<UserControllerBean> {
                override fun OnFailure(msg: String?) {
                }

                override fun OnSuccess(msg: String?, page: ODataPage?, data: java.util.ArrayList<UserControllerBean>?) {
                    if (data == null || data.isEmpty()) {
                        showToast("暂无数据")
                    } else {
                        UserDialog.newInstance(data).apply {
                            setItemClickListener(object : CommonSelectDialog.OnItemClickListener{
                                override fun onItemClick(id: Int, code: String, name: String) {
                                    mPostProjectInfo.userName = name
                                    mPostProjectInfo.tUserId = id
                                    tv_user.text = name
                                }
                            })
                        }.show(supportFragmentManager, "user")
                    }
                }
            })
        }

        btn_save.setOnClickListener {
            showToast(mPostProjectInfo.toString())
        }
    }

    private fun showProvincePicker(it: View?) {
        ProvinceDialog.newInstance(mProvinceList)
                .apply {
                    setItemClickListener(object : CommonSelectDialog.OnItemClickListener {
                        override fun onItemClick(id: Int, code: String, name: String) {
                            if (mProvinceId != id) {
                                this@ActNewProject.tv_city.text = "市"
                                mCityId = -1
                                this@ActNewProject.tv_county.text = "县(区)"
                                mCountyId = -1
                            }
                            mProvinceId = id
                            (it as TextView).text = name
                            mPostProjectInfo.tProvinceId = mCountyId
                            mPostProjectInfo.provinceName = name
                        }

                    })
                }.show(supportFragmentManager, "province")
    }

    private fun showCountyPicker(list: ArrayList<CountyBean>, it: View?) {
        CountyDialog.newInstance(list).apply {
            setItemClickListener(object : CommonSelectDialog.OnItemClickListener {
                override fun onItemClick(id: Int, code: String, name: String) {
                    mCountyId = id
                    (it as TextView).text = name
                    mPostProjectInfo.tCountyId = mCountyId
                    mPostProjectInfo.countyName = name
                }

            })
        }.show(supportFragmentManager, "county")
    }

    private fun showCityPicker(list: ArrayList<CityBean>, it: View?) {
        CityDialog.newInstance(list).apply {
            setItemClickListener(object : CommonSelectDialog.OnItemClickListener {
                override fun onItemClick(id: Int, code: String, name: String) {
                    if (mCityId != id) {
                        this@ActNewProject.tv_county.text = "县(区)"
                        mCountyId = -1
                    }
                    mCityId = id
                    mCityName = name
                    (it as TextView).text = name
                    mPostProjectInfo.tCityId = mCityId
                    mPostProjectInfo.cityName = name
                }

            })
        }.show(supportFragmentManager, "city")
    }

    override fun onDestroy() {
        super.onDestroy()
        mBaiduMapHelper?.destroy()
    }

    private fun updateAddress(address: String) {
        tv_address.text = address
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<PoiInfo>("poi")?.let {
                updateAddress(it.address)
                mPostProjectInfo.address = it.address
                mPostProjectInfo.latitude = it.location.latitude.toString()
                mPostProjectInfo.longitude = it.location.longitude.toString()
            }
        }
    }

}
