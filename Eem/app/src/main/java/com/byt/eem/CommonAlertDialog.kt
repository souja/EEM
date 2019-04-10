package com.byt.eem

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/10 11:28 PM
 * Description :
 */
class CommonAlertDialog : DialogFragment() {

    companion object {
        fun newInstance(title: String,
                        negativeText: String?,
                        positiveText: String?) = CommonAlertDialog().apply {
            arguments = Bundle().apply {
                putString("title", title)
                putString("negativeText", negativeText)
                putString("positiveText", positiveText)
            }
        }
    }

    private var mTitle: String? = null
    private var mNegativeText: String? = null
    private var mPositiveText: String? = null
    private var mNegativeClickListener: DialogInterface.OnClickListener? = null
    private var mPositiveClickListener: DialogInterface.OnClickListener? = null

    fun setNegativeClickListener(negativeClickListener: DialogInterface.OnClickListener) {
        mNegativeClickListener = negativeClickListener
    }

    fun setPositiveClickListener(positiveClickListener: DialogInterface.OnClickListener) {
        mPositiveClickListener = positiveClickListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTitle = arguments!!.getString("title")
        mNegativeText = arguments!!.getString("negativeText", context!!.getString(android.R.string.cancel))
        mPositiveText = arguments!!.getString("positiveText", context!!.getString(android.R.string.ok))
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog
                .Builder(context!!)
                .setTitle(mTitle)
                .setNegativeButton(mNegativeText, mNegativeClickListener)
                .setPositiveButton(mPositiveText, mPositiveClickListener)
                .create()
    }

}