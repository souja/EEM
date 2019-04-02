package com.byt.eem.act

import android.content.Context
import android.content.Intent
import com.byt.eem.R
import com.byt.eem.base.BaseAct

class ActNewProject : BaseAct() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ActNewProject::class.java))
        }
    }

    override fun setupViewRes() = R.layout.activity_act_new_project

    override fun initMain() {
    }

}
