package com.byt.eem.act

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import com.baidu.mapapi.search.core.PoiInfo
import com.byt.eem.CommonAlertDialog
import com.byt.eem.R
import com.byt.eem.baidu.BaiduMapHelper
import com.byt.eem.base.BaseActEd
import com.byt.eem.model.*
import com.byt.eem.util.HttpUtil
import com.byt.eem.util.MConstants
import com.byt.eem.widget.*
import com.souja.lib.base.ActBase
import com.souja.lib.inter.IHttpCallBack
import com.souja.lib.models.ODataPage
import com.souja.lib.widget.TitleBar
import kotlinx.android.synthetic.main.activity_new_project.*
import org.xutils.http.RequestParams
import java.text.SimpleDateFormat
import java.util.*

class ActNewProject : BaseActEd() {

    companion object {
        fun launch(context: ActBase, requestCode: Int, myProject: MyProjectBean? = null) {
            context.startActivityForResult(Intent(context, ActNewProject::class.java).apply {
                myProject?.let {
                    putExtra("data", it)
                }
            }, requestCode)
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

    override fun setupViewRes() = R.layout.activity_new_project

    private var mBaiduMapHelper: BaiduMapHelper? = null

    private var mPostProjectInfo: MyProjectBean = MyProjectBean() //创建项目所需信息

    private var mUpdate = false

    override fun initMain() {
        mUpdate = intent.let {
            if (intent.hasExtra("data")) {
                mPostProjectInfo = intent.getSerializableExtra("data") as MyProjectBean
                mProvinceId = mPostProjectInfo.tProvinceId
                mCityName = mPostProjectInfo.cityName
                mCityId = mPostProjectInfo.tCityId
                mCountyId = mPostProjectInfo.tCountyId
                et_project_name.setText(mPostProjectInfo.tProjectName)
                tv_user.text = mPostProjectInfo.userName
                et_phone.setText(mPostProjectInfo.phone)
                et_contact.setText(mPostProjectInfo.contactName)
                tv_province.text = mPostProjectInfo.provinceName
                tv_city.text = mPostProjectInfo.cityName
                tv_county.text = mPostProjectInfo.countyName
                tv_address.text = mPostProjectInfo.address
            }
            intent.hasExtra("data")
        }
        findViewById<TitleBar>(R.id.m_title)?.apply {
            if (mUpdate) {
                title = "编辑项目"
            }
            setLeftClick {
                onBackPressed()
            }
        }
        tv_province.setOnClickListener {
            if (mProvinceList.isEmpty()) {
                Post(dialog, MConstants.URL.GET_PROVINCES, ProvinceBean::class.java, object : IHttpCallBack<ProvinceBean> {
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
                    Post(getDialog(), MConstants.URL.GET_CITIES_BY_ID + mProvinceId, CityBean::class.java, object : IHttpCallBack<CityBean> {
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
                    Post(getDialog(), MConstants.URL.GET_COUNTIES_BY_ID + mCityId, CountyBean::class.java, object : IHttpCallBack<CountyBean> {
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
                ActMap.launch(this, mCityName, 1, mPostProjectInfo?.latitude?.toDouble()
                        ?: 0.0, mPostProjectInfo?.longitude?.toDouble() ?: 0.0)
            }
        }

        rl_user.setOnClickListener {
            Post(getDialog(), MConstants.URL.GET_ALL_USER, UserControllerBean::class.java, object : IHttpCallBack<UserControllerBean> {
                override fun OnFailure(msg: String?) {
                }

                override fun OnSuccess(msg: String?, page: ODataPage?, data: java.util.ArrayList<UserControllerBean>?) {
                    if (data == null || data.isEmpty()) {
                        showToast("暂无数据")
                    } else {
                        UserDialog.newInstance(data).apply {
                            setItemClickListener(object : CommonSelectDialog.OnItemClickListener {
                                override fun onItemClick(id: Int, code: String, name: String) {
                                    mPostProjectInfo.userName = name
                                    mPostProjectInfo.tUserId = id
                                    this@ActNewProject.tv_user.text = name
                                }
                            })
                        }.show(supportFragmentManager, "user")
                    }
                }
            })
        }

        btn_save.setOnClickListener { _ ->
            if (checkInput()) {
                Post(getDialog("数据保存中..."),
                        if (mUpdate) MConstants.URL.UPDATE_PROJECT else MConstants.URL.SAVE_PROJECT
                        , getPostParams(), object : IHttpCallBack<Any> {
                    override fun OnSuccess(msg: String?, page: ODataPage?, data: java.util.ArrayList<Any>?) {
                        showToast("保存成功")
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun OnFailure(msg: String?) {
                        showToast("保存失败")
                    }

                })
            }
        }
    }

    private fun getPostParams(): RequestParams? {
        val newProjectBean = NewProjectBean()
        mPostProjectInfo.apply {
            newProjectBean.tProjectName = tProjectName
            newProjectBean.address = address
            newProjectBean.latitude = latitude
            newProjectBean.longitude = longitude
            newProjectBean.tUserId = tUserId
            newProjectBean.contactName = contactName
            newProjectBean.phone = phone
            newProjectBean.tCityId = tCityId
            newProjectBean.tProvinceId = tProvinceId
            newProjectBean.tCountyId = tCountyId
            newProjectBean.pTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        }

        return HttpUtil.formatParams(newProjectBean.toString())
    }

    private fun checkInput(): Boolean {
        if (et_project_name.text.isEmpty()) {
            showToast("请输入项目名称")
            return false
        }
        mPostProjectInfo.tProjectName = et_project_name.text.toString()
        if (tv_user.text.isEmpty()) {
            showToast("请选择业主")
            return false
        }
        if (et_phone.text.isEmpty()) {
            showToast("请输入联系电话")
            return false
        }
        mPostProjectInfo.phone = et_phone.text.toString()
        if (et_contact.text.isEmpty()) {
            showToast("请输入联系人")
            return false
        }
        mPostProjectInfo.contactName = et_contact.text.toString()
        if (mProvinceId < 1 || mCityId < 1 || mCountyId < 1 || mPostProjectInfo.address.isEmpty()) {
            showToast("请输入项目地址")
            return false
        }
        return true
    }

    private fun showAlertDialog() {
        CommonAlertDialog
                .newInstance("确定退出?", null, null)
                .apply {
                    setPositiveClickListener(DialogInterface.OnClickListener { _, _ ->
                        finish()
                    })
                }
                .show(supportFragmentManager, "exit")
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
                            mPostProjectInfo.tProvinceId = mProvinceId
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
                mPostProjectInfo.latitude = it.location.latitude.toString().substring(0, 9)
                mPostProjectInfo.longitude = it.location.longitude.toString().substring(0, 9)
            }
        }
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.fragments.isEmpty())
            super.onBackPressed()
        else {
            showAlertDialog()
        }
    }

}
