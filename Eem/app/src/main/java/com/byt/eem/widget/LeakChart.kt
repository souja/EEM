package com.byt.eem.widget

import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import com.byt.eem.R
import com.byt.eem.act.ActDeviceInfoHistory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/25 10:34 PM
 * Description :
 */
class LeakChart(private val chart: LineChart):IChart{

    override fun setData(data: ArrayList<ActDeviceInfoHistory.History>, type: Int) {
        val values = ArrayList<Entry>()
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            mAxisMinimum = 0f
            mAxisMaximum = data.size.toFloat()
            setLabelCount(data.size, true)
            data.forEachIndexed { index, history ->
                values.add(Entry(index.toFloat(), history.firstLeakage.toFloat()))
            }
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return data[value.toInt()].operateTime.substring(data[value.toInt()].operateTime.lastIndexOf("T") + 1, data[value.toInt()].operateTime.lastIndexOf("T") + 6)
                }
            }
        }
        val set1: LineDataSet = chart.data.getDataSetByIndex(0) as LineDataSet
                set1.values = values
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.notifyDataSetChanged()
        chart.data.notifyDataChanged()
        chart.notifyDataSetChanged()
    }

    init {
        chart.description.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val yAxis = chart.axisLeft
        yAxis.labelCount = 10
        // disable dual axis (only use LEFT axis)
        chart.axisRight.isEnabled = false
        chart.setDrawBorders(false)
        chart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            form = Legend.LegendForm.CIRCLE
        }
        chart.legend.apply {

        }
        // horizontal grid lines
//        yAxis.enableGridDashedLine(10f, 10f, 0f)

        // axis range
        yAxis.axisMaximum = 47.5f
        yAxis.axisMinimum = -38.5f
        chart.axisLeft.setDrawAxisLine(true)


        val values = ArrayList<Entry>()
        var set1: LineDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) run {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "漏电流")
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.setDrawIcons(false)

            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.rgb(104, 241, 175)
            set1.setCircleColor(Color.rgb(104, 241, 175))

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
//            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
//            set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // set color of filled area
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(chart.context, R.drawable.chart_leak)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.rgb(104, 241, 175)
            }

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }

    }

}

interface IChart{
    fun setData(mva: ArrayList<ActDeviceInfoHistory.History>, type:Int = 0)
}
