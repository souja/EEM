package com.byt.eem.act

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import com.byt.eem.*
import com.byt.eem.base.BaseAct
import com.byt.eem.model.MyProjectBean
import com.byt.eem.util.MConstants
import com.souja.lib.inter.IHttpCallBack
import com.souja.lib.models.ODataPage
import com.souja.lib.utils.ScreenUtil
import com.souja.lib.widget.TitleBar
import kotlinx.android.synthetic.main.activity_my_projects.*
import kotlinx.android.synthetic.main.item_project.view.*


class ActMyProjects : BaseAct(), ProjectAdapter.OnItemClickListener {

    override fun onDeleteClick(projectBean: MyProjectBean, position: Int) {
        CommonAlertDialog
                .newInstance("确认删除吗?", null, null)
                .apply {
                    setPositiveClickListener(DialogInterface.OnClickListener { _, _ ->
                        deleteProject(projectBean.id, position)
                    })
                }
                .show(supportFragmentManager, "delete")
    }

    private fun deleteProject(id: Int, position: Int) {
        Post(getDialog("删除中..."),
                MConstants.URL.DELETE_PROJECT + id,
                Any::class.java,
                object : IHttpCallBack<Any> {
                    override fun OnSuccess(msg: String?, page: ODataPage?, data: ArrayList<Any>?) {
                        showToast("删除成功")
                        mAdapter!!.delete(position)
                    }

                    override fun OnFailure(msg: String?) {
                        showToast("删除失败")
                    }

                })
    }

    override fun onEditClick(projectBean: MyProjectBean, position: Int) {
        ActNewProject.launch(this, 1, projectBean)
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ActMyProjects::class.java))
        }
    }

    private var mAdapter: ProjectAdapter? = null

    override fun setupViewRes() = R.layout.activity_my_projects

    override fun initMain() {
        recycler_project.layoutManager = LinearLayoutManager(_this)
        mAdapter = ProjectAdapter()
        mAdapter!!.setHasStableIds(true)
        mAdapter!!.setOnItemClickListener(this)
        recycler_project.adapter = mAdapter
        findViewById<TitleBar>(R.id.m_title)?.setRightClick {
            ActNewProject.launch(_this, 1)
        }
        Post(getDialog(), MConstants.URL.GET_MY_PROJECTS, MyProjectBean::class.java, object : IHttpCallBack<MyProjectBean> {
            override fun OnSuccess(msg: String?, page: ODataPage?, data: ArrayList<MyProjectBean>?) {
                mAdapter?.setData(data!!)
            }

            override fun OnFailure(msg: String?) {
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Post(getDialog(), MConstants.URL.GET_MY_PROJECTS, MyProjectBean::class.java, object : IHttpCallBack<MyProjectBean> {
                override fun OnSuccess(msg: String?, page: ODataPage?, data: ArrayList<MyProjectBean>?) {
                    mAdapter?.setData(data!!)
                }

                override fun OnFailure(msg: String?) {
                }

            })
        }
    }

}


class ProjectAdapter : RecyclerView.Adapter<ProjectAdapter.ProjectHolder>() {

    private val arr = SparseBooleanArray()
    private val mDataList = ArrayList<MyProjectBean>()
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onDeleteClick(projectBean: MyProjectBean, position: Int)
        fun onEditClick(projectBean: MyProjectBean, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProjectHolder(parent.inflate(R.layout.item_project))

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        holder.bindView(mDataList[position], position)
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.itemView.apply {
                val open = payloads[0] == true
                if (open) {
                    ObjectAnimator.ofFloat(iv_toggle, "rotation", 0f, 90f)
                            .apply {
                                duration = 400
                            }.start()
                    ll_content_drawer.setVisibility(true)
                } else {
                    ObjectAnimator.ofFloat(iv_toggle, "rotation", 90f, 0f)
                            .apply {
                                duration = 400
                            }.start()
                    ll_content_drawer.setVisibility(false, true)
                }
            }
        }
    }

    override fun getItemId(position: Int) = position.toLong()

    fun setData(data: ArrayList<MyProjectBean>) {
        mDataList.clear()
        mDataList.addAll(data)
        notifyDataSetChanged()
    }

    fun delete(position: Int) {
        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ProjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            ScreenUtil.initScale(itemView)
            itemView.iv_edit.setOnClickListener {
                val pos = itemView.iv_toggle.tag as Int
                mOnItemClickListener?.onEditClick(mDataList[pos], pos)
            }
            itemView.iv_delete.setOnClickListener {
                val pos = itemView.iv_toggle.tag as Int
                mOnItemClickListener?.onDeleteClick(mDataList[pos], pos)
            }
            itemView.iv_toggle.setOnClickListener {
                val pos = itemView.iv_toggle.tag as Int
                var open = false
                if (itemView.ll_content_drawer.isVisible()) {
                    arr.delete(pos)
                } else {
                    open = true
                    arr.put(pos, true)
                }
                notifyItemChanged(pos, open)
            }
        }

        fun bindView(data: MyProjectBean, position: Int) {
            itemView.apply {
                iv_toggle.tag = position
                ll_content_drawer.setVisibility(false, true)
                tv_project_name.text = data.tProjectName
                tv_user_name.text = data.userName
                tv_count.text = data.counts.toString()
                tv_contact.text = data.contactName
                tv_phone.text = data.phone
                tv_addres.text = data.address
            }
        }

    }


}
