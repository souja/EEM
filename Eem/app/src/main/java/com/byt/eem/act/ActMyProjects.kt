package com.byt.eem.act

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import com.byt.eem.*
import com.byt.eem.base.BaseAct
import kotlinx.android.synthetic.main.activity_act_my_projects.*
import kotlinx.android.synthetic.main.item_project.view.*

class ActMyProjects : BaseAct() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ActMyProjects::class.java))
        }
    }

    override fun setupViewRes() = R.layout.activity_act_my_projects

    override fun initMain() {
        recycler_project.layoutManager = LinearLayoutManager(_this)
        recycler_project.adapter = ProjectAdapter()
    }

}


class ProjectAdapter:RecyclerView.Adapter<ProjectAdapter.ProjectHolder>(){

    private val arr = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  ProjectHolder(parent.inflate(R.layout.item_project))

    override fun getItemCount(): Int {
        return 40
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        holder.bindView(arr.get(position, false))
        holder.itemView.iv_toggle.setOnClickListener {
            if (holder.itemView.ll_content_drawer.isVisible()) {
                arr.delete(position)
            } else {
                arr.put(position, true)
            }
            notifyItemChanged(position)
        }

    }

    class ProjectHolder(view:View):RecyclerView.ViewHolder(view){

        init {
        }

        fun bindView(open:Boolean){
            if (open) {
                itemView.ll_content_drawer.setVisibility(true)
            } else {
                itemView.ll_content_drawer.setVisibility(false, true)
            }
        }

    }



}
