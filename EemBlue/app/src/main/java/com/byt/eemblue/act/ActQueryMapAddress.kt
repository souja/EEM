package com.byt.eemblue.act

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.poi.*
import com.byt.eemblue.R
import com.byt.eemblue.base.BaseAct
import com.byt.eemblue.base.BaseActEd
import com.byt.eemblue.inflate
import com.souja.lib.utils.ScreenUtil
import kotlinx.android.synthetic.main.activity_query_map_address.*
import kotlinx.android.synthetic.main.item_poi.view.*


class ActQueryMapAddress : BaseActEd() {

    companion object {
        fun launch(context: BaseAct, cityName: String, reqCode: Int) {
            context.startActivityForResult(Intent(context, ActQueryMapAddress::class.java).apply {
                putExtra("city", cityName)
            }, reqCode)
        }
    }

    override fun setupViewRes() = R.layout.activity_query_map_address

    private var mPoiSearch = PoiSearch.newInstance()

    override fun initMain() {
        iv_back.setOnClickListener {
            onBackPressed()
        }
        mPoiSearch.setOnGetPoiSearchResultListener(listener)
        et_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mPoiSearch.searchInCity(PoiCitySearchOption()
                        .city(intent.getStringExtra("city")) //必填
                        .keyword(s.toString()) //必填
                        .pageNum(0)
                        .pageCapacity(20))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        rv_result.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPoiSearch.destroy()
    }

    private var listener: OnGetPoiSearchResultListener = object : OnGetPoiSearchResultListener {
        override fun onGetPoiResult(poiResult: PoiResult) {
            poiResult.allPoi?.let {
                rv_result.adapter = PoiAdapter(it)
            }

        }

        override fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult) {

        }

        override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult) {

        }

        //废弃
        override fun onGetPoiDetailResult(poiDetailResult: PoiDetailResult) {

        }
    }

    inner class PoiAdapter(private val mPoiList: List<PoiInfo>) : RecyclerView.Adapter<PoiAdapter.PoiHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PoiHolder(parent.inflate(R.layout.item_poi))

        override fun getItemCount() = mPoiList.size

        override fun onBindViewHolder(holder: PoiHolder, position: Int) {
            holder.bindData(mPoiList[position])
        }

        inner class PoiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            init {
                ScreenUtil.initScale(itemView)
                itemView.setOnClickListener { _ ->
                    mPoiInfo?.let {
                        setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra("poi", mPoiInfo)
                        })
                        finish()
                    }
                }
            }

            private var mPoiInfo: PoiInfo? = null

            fun bindData(poi: PoiInfo) {
                mPoiInfo = poi
                itemView.tv_name.text = poi.name
                itemView.tv_address.text = poi.address
            }

        }

    }

}
