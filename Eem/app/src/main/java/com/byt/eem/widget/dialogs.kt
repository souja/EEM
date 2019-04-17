package com.byt.eem.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.FrameLayout
import com.byt.eem.R
import com.byt.eem.inflate
import com.byt.eem.model.CityBean
import com.byt.eem.model.CountyBean
import com.byt.eem.model.ProvinceBean
import com.souja.lib.utils.ScreenUtil
import kotlinx.android.synthetic.main.item_district.view.*


open class BaseFullBottomSheetFragment : BottomSheetDialogFragment() {
    /**
     * 顶部向下偏移量
     */
    var topOffset = 0
    private var behavior: BottomSheetBehavior<FrameLayout>? = null
    /**
     * 获取屏幕高度
     *
     * @return height
     */
    private// 使用Point已经减去了状态栏高度
    val height: Int
        get() {
            var height = 1920
            if (context != null) {
                val wm = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val point = Point()
                wm.defaultDisplay.getSize(point)
                height = point.y - topOffset
            }
            return height
        }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (context == null) {
            super.onCreateDialog(savedInstanceState)
        } else BottomSheetDialog(context!!, R.style.TransparentBottomSheetStyle)
    }

    @Nullable
    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        // 设置软键盘不自动弹出
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val dialog = dialog as BottomSheetDialog
        val bottomSheet = dialog.delegate.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val layoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            behavior = BottomSheetBehavior.from(bottomSheet)
            // 初始为展开状态
            behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/13 2:01 PM
 * Description :
 */
open class ProvinceCityCountyPickerDialog<T> : DialogFragment() {

    private var mDatas: ArrayList<T>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatas = arguments!!.getSerializable("data") as ArrayList<T>?
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int, code: String, name: String)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            val view = layoutInflater.inflate(R.layout.dialog_province_list, null, false) as RecyclerView
            view.layoutManager = LinearLayoutManager(context)
            view.adapter = createAdapter(mDatas!!, mOnItemClickListener)
            view.hasFixedSize()
            setContentView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                height = (context.resources.displayMetrics.heightPixels / 2f).toInt()
            })
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    open fun createAdapter(datas: ArrayList<T>, mOnItemClickListener: OnItemClickListener?): RecyclerView.Adapter<BaseHolder<T>>? = null

    abstract inner class BaseAdapter<T>(private val datas: ArrayList<T>) : RecyclerView.Adapter<BaseHolder<T>>() {

        var mOnItemClickListener: OnItemClickListener? = null

        override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
            holder.bindData(datas[position])
        }

        override fun getItemCount(): Int = datas.size

    }

    inner class ProvinceAdapter(datas: ArrayList<ProvinceBean>) : BaseAdapter<ProvinceBean>(datas) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceHolder =
                ProvinceHolder(parent.inflate(R.layout.item_district), mOnItemClickListener)
    }

    inner class ProvinceHolder(itemView: View?, private var mOnItemClickListener: OnItemClickListener?) : BaseHolder<ProvinceBean>(itemView) {

        override fun onItemClick(data: ProvinceBean) {
            this.mOnItemClickListener?.onItemClick(data.id, data.provinceCode, data.provinceName)
        }

        override fun setContent() {
            itemView.tv_content.text = mData!!.provinceName
        }

    }

    inner class CityAdapter(datas: ArrayList<CityBean>) : BaseAdapter<CityBean>(datas) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder =
                CityHolder(parent.inflate(R.layout.item_district), mOnItemClickListener)

    }

    inner class CityHolder(itemView: View?, var mOnItemClickListener: OnItemClickListener?) : BaseHolder<CityBean>(itemView) {

        override fun onItemClick(data: CityBean) {
            this.mOnItemClickListener?.onItemClick(data.id, data.cityCode, data.cityName)
        }

        override fun setContent() {
            itemView.tv_content.text = mData!!.cityName
        }

    }

    inner class CountyAdapter(datas: ArrayList<CountyBean>) : BaseAdapter<CountyBean>(datas) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountyHolder =
                CountyHolder(parent.inflate(R.layout.item_district), mOnItemClickListener)

    }

    inner class CountyHolder(itemView: View?, private var mOnItemClickListener: OnItemClickListener?) : BaseHolder<CountyBean>(itemView) {
        override fun onItemClick(data: CountyBean) {
            this.mOnItemClickListener?.onItemClick(data.id, data.countyCode, data.countyName)
        }

        override fun setContent() {
            itemView.tv_content.text = mData!!.countyName
        }

    }

    abstract inner class BaseHolder<T>(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        abstract fun onItemClick(data: T)

        init {
            ScreenUtil.initScale(itemView)
            itemView!!.setOnClickListener { _ ->
                run {
                    mData?.let {
                        onItemClick(mData!!)
                        dismiss()
                    }
                }
            }
        }

        var mData: T? = null

        fun bindData(data: T) {
            mData = data
            setContent()
        }

        abstract fun setContent()

    }

}

class ProvinceDialog : ProvinceCityCountyPickerDialog<ProvinceBean>() {

    companion object {
        fun newInstance(datas: ArrayList<ProvinceBean>) = ProvinceDialog().apply {
            arguments = Bundle().apply {
                putSerializable("data", datas)
            }
        }
    }

    override fun createAdapter(datas: ArrayList<ProvinceBean>, onItemClickListener: OnItemClickListener?): RecyclerView.Adapter<BaseHolder<ProvinceBean>>? {
        return ProvinceAdapter(datas).apply {
            this.mOnItemClickListener = onItemClickListener
        }
    }

}

class CityDialog : ProvinceCityCountyPickerDialog<CityBean>() {

    companion object {
        fun newInstance(datas: ArrayList<CityBean>) = CityDialog().apply {
            arguments = Bundle().apply {
                putSerializable("data", datas)
            }
        }
    }

    override fun createAdapter(datas: ArrayList<CityBean>, mOnItemClickListener: OnItemClickListener?): RecyclerView.Adapter<BaseHolder<CityBean>>? {
        return CityAdapter(datas).apply {
            this.mOnItemClickListener = mOnItemClickListener
        }
    }

}

class CountyDialog : ProvinceCityCountyPickerDialog<CountyBean>() {

    companion object {
        fun newInstance(datas: ArrayList<CountyBean>) = CountyDialog().apply {
            arguments = Bundle().apply {
                putSerializable("data", datas)
            }
        }
    }

    override fun createAdapter(datas: ArrayList<CountyBean>, mOnItemClickListener: OnItemClickListener?): RecyclerView.Adapter<BaseHolder<CountyBean>>? {
        return CountyAdapter(datas).apply {
            this.mOnItemClickListener = mOnItemClickListener
        }
    }

}
