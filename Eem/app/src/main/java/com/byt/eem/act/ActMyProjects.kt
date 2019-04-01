package com.byt.eem.act

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.byt.eem.R
import com.byt.eem.base.BaseAct
import com.byt.eem.inflate
import com.byt.eem.toggle
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  ProjectHolder(parent.inflate(R.layout.item_project))

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {



    }

    class ProjectHolder(view:View):RecyclerView.ViewHolder(view){

        init {
            itemView.iv_toggle.setOnClickListener {
                itemView.ll_content_drawer.toggle(true)
            }
        }

        fun bindView(){

        }

    }

}
