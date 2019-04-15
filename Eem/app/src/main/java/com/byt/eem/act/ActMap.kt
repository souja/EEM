package com.byt.eem.act

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.district.DistrictSearch
import com.baidu.mapapi.search.district.DistrictSearchOption
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener
import com.byt.eem.R


class ActMap : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ActMap::class.java))
        }
    }

    private var mLayout: FrameLayout? = null
    private lateinit var mMapView: MapView
    private var mMarkerF: Marker? = null

    private var mDistrictSearch: DistrictSearch? = null

    private var mBaiduMap: BaiduMap? = null

    private var bdF = BitmapDescriptorFactory
            .fromResource(R.drawable.ic_location_marker)

    private var mScreenCenterPoint: Point? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_act_map)
        initView()
        setContentView(mLayout)
        mDistrictSearch = DistrictSearch.newInstance()
        val listener = OnGetDistricSearchResultListener { districtResult ->
            if (null != districtResult && districtResult.error === SearchResult.ERRORNO.NO_ERROR) {
                //对检索所得行政区划边界数据进行处理
                mBaiduMap!!.clear()
                //获取边界坐标点，并展示
                val polyLines = districtResult.getPolylines()
                if (polyLines != null) {

                    val builder = LatLngBounds.Builder()
                    for (polyline in polyLines!!) {
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
                    addMarker()

                }
            }
            mDistrictSearch!!.destroy()
        }
        mDistrictSearch!!.setOnDistrictSearchListener(listener)
        mDistrictSearch!!.searchDistrict(DistrictSearchOption()
                .cityName("成都市"))


    }

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
        mLayout = FrameLayout(this)
        mLayout!!.addView(mMapView)
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
        mMapView.onDestroy()
    }

}
