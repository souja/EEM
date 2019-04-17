package com.byt.eem.act

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.district.DistrictSearch
import com.baidu.mapapi.search.district.DistrictSearchOption
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener
import com.baidu.mapapi.search.geocode.*
import com.byt.eem.R
import com.byt.eem.base.BaseAct
import com.byt.eem.inflate
import com.byt.eem.setVisibility
import com.souja.lib.utils.ScreenUtil
import com.souja.lib.widget.TitleBar
import kotlinx.android.synthetic.main.activity_act_map.*
import kotlinx.android.synthetic.main.item_poi.view.*


class ActMap : BaseAct() {

    override fun setupViewRes() = R.layout.activity_act_map

    override fun initMain() {
        initView()
        initListeners()
    }

    /**
     * 初始化地图监听
     */
    private fun initListeners() {
        findViewById<TitleBar>(R.id.m_title)?.setRightClick {
            //保存位置信息
        }
        findViewById<TitleBar>(R.id.m_title)?.setSndMenuClick {
            ActQueryMapAddress.launch(this)
        }
        mBaiduMap!!.setOnMapStatusChangeListener(object : BaiduMap.OnMapStatusChangeListener {

            override fun onMapStatusChangeStart(p0: MapStatus?) {
            }

            override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {
            }

            override fun onMapStatusChange(p0: MapStatus?) {
            }

            override fun onMapStatusChangeFinish(status: MapStatus?) {
                //改变结束之后，获取地图可视范围的中心点坐标
                val latLng = status!!.target
                val op = ReverseGeoCodeOption()
                op.location(latLng)
                mGeoCodec!!.reverseGeoCode(op)
            }

        })

        // 初始化搜索模块，注册事件监听
        mGeoCodec = GeoCoder.newInstance()
        mGeoCodec!!.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {

            override fun onGetGeoCodeResult(p0: GeoCodeResult?) {

            }

            override fun onGetReverseGeoCodeResult(result: ReverseGeoCodeResult?) {
                //获取点击的坐标地址
                result?.poiList.let {
                    rv_address.adapter = PoiAdapter(it!!)
                }
            }

        })

        mDistrictSearch = DistrictSearch.newInstance()
        val listener = OnGetDistricSearchResultListener { districtResult ->
            if (null != districtResult && districtResult.error === SearchResult.ERRORNO.NO_ERROR) {
                //对检索所得行政区划边界数据进行处理
                mBaiduMap!!.clear()
                //获取边界坐标点，并展示
                val polyLines = districtResult.getPolylines()
                if (polyLines != null) {

                    val builder = LatLngBounds.Builder()
                    for (polyline in polyLines) {
                        val ooPolyline11 = PolylineOptions().width(10)
                                .points(polyline).dottedLine(true).color(Color.BLUE)
                        mBaiduMap!!.addOverlay(ooPolyline11)
                        val ooPolygon = PolygonOptions().points(polyline)
                                .stroke(Stroke(5, -0x55ff0078)).fillColor(Color.TRANSPARENT)
                        mBaiduMap!!.addOverlay(ooPolygon)
                        for (latLng in polyline) {
                            builder.include(latLng)
                        }
                    }
                    mBaiduMap!!.setMapStatus(MapStatusUpdateFactory
                            .newLatLngBounds(builder.build()))
                    mBaiduMap!!.setMapStatus(MapStatusUpdateFactory
                            .newMapStatus(MapStatus.Builder().zoom(mBaiduMap!!.maxZoomLevel - 2).build()))
                    addMarker()
                    val latLng = mMarkerF!!.position
                    val op = ReverseGeoCodeOption()
                    op.location(latLng)
                    mGeoCodec!!.reverseGeoCode(op)

                }
            }
            mDistrictSearch!!.destroy()
        }
        mDistrictSearch!!.setOnDistrictSearchListener(listener)
        mDistrictSearch!!.searchDistrict(DistrictSearchOption()
                .cityName("成都市"))

    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ActMap::class.java))
        }
    }

    private lateinit var mMapView: MapView
    private var mMarkerF: Marker? = null

    private var mDistrictSearch: DistrictSearch? = null

    private var mBaiduMap: BaiduMap? = null

    private var mGeoCodec: GeoCoder? = null // 搜索模块，也可去掉地图模块独立使用

    private var bdF = BitmapDescriptorFactory
            .fromResource(R.drawable.ic_location_marker)

    private var mScreenCenterPoint: Point? = null

    /**
     * 添加位置锚点
     */
    private fun addMarker() {
        if (null != mBaiduMap!!.mapStatus) {
            val llF = mBaiduMap!!.mapStatus.target
            mScreenCenterPoint = mBaiduMap!!.projection.toScreenLocation(llF)
            val ooF = MarkerOptions().position(llF).icon(bdF).perspective(true)
                    .fixedScreenPosition(mScreenCenterPoint)
            mMarkerF = mBaiduMap!!.addOverlay(ooF) as Marker

        }
    }


    // 初始化View
    private fun initView() {
        mMapView = MapView(this, BaiduMapOptions())
        mBaiduMap = mMapView.map
        fl_map.addView(mMapView)
        rv_address.layoutManager = LinearLayoutManager(this)
    }

    override fun onPause() {
        super.onPause()
        // activity 暂停时同时暂停地图控件
        mMapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        // activity 恢复时同时恢复地图控件
        mMapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        // activity 销毁时同时销毁地图控件
        mGeoCodec?.destroy()
        mMapView.onDestroy()
    }

    inner class PoiAdapter(private val mPoiList: List<PoiInfo>) : RecyclerView.Adapter<PoiAdapter.PoiHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PoiHolder(parent.inflate(R.layout.item_poi))

        override fun getItemCount() = mPoiList.size

        private var mPreCheckedPosition = 0

        private var mCurrentCheckedPosition = 0

        override fun onBindViewHolder(holder: PoiHolder, position: Int) {
            holder.bindData(mPoiList[position], position)
        }

        override fun onBindViewHolder(holder: PoiHolder, position: Int, payloads: MutableList<Any>) {
            if (payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads)
            } else {
                //局部刷新
                val visibility = payloads[0] as Boolean
                if (visibility)
                    mPreCheckedPosition = mCurrentCheckedPosition
                holder.itemView.iv_checked.setVisibility(visibility)
            }
        }

        inner class PoiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            init {
                ScreenUtil.initScale(itemView)
                itemView.setOnClickListener { _ ->
                    mPoiInfo?.let {
                        mBaiduMap!!.setMapStatus(MapStatusUpdateFactory
                                .newLatLng(it.location))
                        val position = itemView.tag as Int
                        mCurrentCheckedPosition = position
                        notifyItemChanged(mPreCheckedPosition, false)
                        notifyItemChanged(position, true)
                    }
                }
            }

            private var mPoiInfo: PoiInfo? = null

            fun bindData(poi: PoiInfo, position: Int) {
                mPoiInfo = poi
                itemView.tag = position
                itemView.tv_name.text = poi.name
                itemView.tv_address.text = poi.address
                itemView.iv_checked.setVisibility(position == mCurrentCheckedPosition)
            }

        }

    }

}