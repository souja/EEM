package com.byt.eem.act

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import com.byt.eem.R
import com.byt.eem.base.BaseAct
import com.byt.eem.inflate
import com.byt.eem.isVisible
import com.byt.eem.model.MyProject
import com.byt.eem.setVisibility
import com.byt.eem.util.MConstants
import com.souja.lib.inter.IHttpCallBack
import com.souja.lib.models.ODataPage
import com.souja.lib.utils.ScreenUtil
import com.souja.lib.widget.TitleBar
import kotlinx.android.synthetic.main.activity_act_my_projects.*
import kotlinx.android.synthetic.main.item_project.view.*

class ActMyProjects : BaseAct() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ActMyProjects::class.java))
        }
    }

    private var mAdapter: ProjectAdapter? = null

    override fun setupViewRes() = R.layout.activity_act_my_projects

    override fun initMain() {
        recycler_project.layoutManager = LinearLayoutManager(_this)
        mAdapter = ProjectAdapter()
        recycler_project.adapter = mAdapter
        findViewById<TitleBar>(R.id.m_title)?.setRightClick {
            ActNewProject.launch(_this)
        }
        Post(MConstants.URL.GET_MY_PROJECTS, MyProject::class.java, object : IHttpCallBack<MyProject> {
            override fun OnSuccess(msg: String?, page: ODataPage?, data: ArrayList<MyProject>?) {
                mAdapter?.setData(data!!)
            }

            override fun OnFailure(msg: String?) {
            }

        })
    }

}


class ProjectAdapter : RecyclerView.Adapter<ProjectAdapter.ProjectHolder>() {

    private val arr = SparseBooleanArray()
    private val mDataList = ArrayList<MyProject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProjectHolder(parent.inflate(R.layout.item_project))

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        holder.bindView(mDataList.get(position), arr.get(position, false))
        holder.itemView.iv_toggle.setOnClickListener {
            if (holder.itemView.ll_content_drawer.isVisible()) {
                arr.delete(position)
            } else {
                arr.put(position, true)
            }
            notifyItemChanged(position)
        }

    }

    fun setData(data: ArrayList<MyProject>) {
        mDataList.clear()
        mDataList.addAll(data)
        notifyDataSetChanged()
    }

    class ProjectHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            ScreenUtil.initScale(itemView)
        }

        fun bindView(data: MyProject, open: Boolean) {
            if (open) {
                itemView.ll_content_drawer.setVisibility(true)
            } else {
                itemView.ll_content_drawer.setVisibility(false, true)
            }
            itemView.tv_project_name.text = data.projectName
        }

    }


}
