package com.byt.eem.widget

import android.graphics.Color
import com.byt.eem.act.ActDeviceInfoHistory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/27 9:22 PM
 * Description :
 */
class MultiBarChart(private val chart: BarChart) : IChart {

    override fun setData(data: ArrayList<ActDeviceInfoHistory.History>, type: Int) {
        chart.description.isEnabled = false
        chart.setPinchZoom(false)

        chart.setDrawBarShadow(false)

        chart.setDrawGridBackground(false)
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.yOffset = 0f
        l.xOffset = 10f
        l.yEntrySpace = 0f
        l.textSize = 8f

        val xAxis = chart.xAxis
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                var v = 0
                when {
                    value.toInt() > data.size -> return ""
                    value.toInt() == data.size -> v = data.size - 1
                    value.toInt() > 0 -> v = value.toInt() - 1
                }
                return data[v].operateTime.substring(data[v].operateTime.lastIndexOf("T") + 1, data[v].operateTime.lastIndexOf("T") + 6)
            }
        }
        val leftAxis = chart.axisLeft
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        leftAxis.axisMaximum = when (type) {
            2 -> 86f
            3 -> if (data[0].commonAxVoltage.toFloat() <= 0) 86f else data[0].commonAxVoltage.toFloat() + 80f
            else -> if (data[0].secondChannelTemperature.toFloat() <= 0) 86f else Math.abs(data[0].secondChannelTemperature.toFloat()) + 60f
        }
        leftAxis.labelCount = 10
        leftAxis.setDrawAxisLine(true)
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return (value - 38f).toString()
            }
        }
        val values1 = ArrayList<BarEntry>()
        val values2 = ArrayList<BarEntry>()
        val values3 = ArrayList<BarEntry>()
        val values4 = ArrayList<BarEntry>()
        for (i in 0 until data.size) {
            when (type) {
                2 -> {
                    values1.add(BarEntry(i.toFloat(), data[i].axelectricity.toFloat() + 38f))
                    values2.add(BarEntry(i.toFloat(), data[i].bxelectricity.toFloat() + 38f))
                    values3.add(BarEntry(i.toFloat(), data[i].cxelectricity.toFloat() + 38f))
                }
                3 -> {
                    values1.add(BarEntry(i.toFloat(), data[i].commonAxVoltage.toFloat() + 38f))
                    values2.add(BarEntry(i.toFloat(), data[i].commonBxVoltage.toFloat() + 38f))
                    values3.add(BarEntry(i.toFloat(), data[i].commonCxVoltage.toFloat() + 38f))
                }
                4 -> {
                    values1.add(BarEntry(i.toFloat(), data[i].firstChannelTemperature.toFloat() + 38f))
                    values2.add(BarEntry(i.toFloat(), data[i].secondChannelTemperature.toFloat() + 38f))
                    values3.add(BarEntry(i.toFloat(), data[i].thirdChannelTemperature.toFloat() + 38f))
                    values4.add(BarEntry(i.toFloat(), data[i].fourthChannelTemperature.toFloat() + 38f))
                }
            }
        }
        val set1: BarDataSet
        val set2: BarDataSet
        val set3: BarDataSet
        var set4: BarDataSet? = null
        if (chart.data != null && chart.data.dataSetCount > 0) {

            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
            set2 = chart.data.getDataSetByIndex(1) as BarDataSet
            set3 = chart.data.getDataSetByIndex(2) as BarDataSet
            set1.values = values1
            set2.values = values2
            set3.values = values3
            if (type == 4) {
                set4 = chart.data.getDataSetByIndex(3) as BarDataSet
                set4.values = values4
            }
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()

        } else {
            when (type) {
                2 -> {
                    set1 = BarDataSet(values1, "A相电流")
                    set2 = BarDataSet(values2, "B相电流")
                    set3 = BarDataSet(values3, "C相电流")
                }
                3 -> {
                    set1 = BarDataSet(values1, "A相电压")
                    set2 = BarDataSet(values2, "B相电压")
                    set3 = BarDataSet(values3, "C相电压")
                }
                else -> {
                    set1 = BarDataSet(values1, "1路温度")
                    set2 = BarDataSet(values2, "2路温度")
                    set3 = BarDataSet(values3, "3路温度")
                    set4 = BarDataSet(values4, "4路温度")
                }
            }
            // create 4 DataSets
            set1.color = Color.rgb(104, 241, 175)
            set2.color = Color.rgb(164, 228, 251)
            set3.color = Color.rgb(242, 247, 158)
            val chartData: BarData
            if (type == 4) {
                set4!!.color = Color.rgb(180, 237, 128)
                chartData = BarData(set1, set2, set3, set4)
            } else {
                chartData = BarData(set1, set2, set3)
            }

//            chartData.setValueFormatter(LargeValueFormatter())
            chartData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return String.format("%.1f", value - 38f)
                }
            })
            chart.data = chartData
        }
        // specify the width each bar should have
        chart.barData.barWidth = barWidth

        // restrict the x-axis range
        chart.xAxis.axisMinimum = 1f
        chart.axisRight.isEnabled = false
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.xAxis.axisMaximum = 1f + chart.barData.getGroupWidth(groupSpace, barSpace) * 10
        chart.groupBars(1f, groupSpace, barSpace)
        chart.invalidate()
    }

    val groupSpace = 0.16f
    val barSpace = 0.03f // x4 DataSet
    val barWidth = 0.2f // x4 DataSet
}