package com.byt.eemblue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Author : hikobe8@github.com
 * Time : 2019/4/1 11:25 PM
 * Description :
 */
fun ViewGroup.inflate(resId: Int, attachToRoot: Boolean = false) = LayoutInflater.from(context).inflate(resId, this, attachToRoot)!!

fun View.setVisibility(visible: Boolean, isGone: Boolean = false) {
    visibility = if (visible) View.VISIBLE else if (isGone) View.GONE else View.INVISIBLE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.toggle(isGone: Boolean = false) {
    if (isVisible()) {
        setVisibility(false, isGone)
    } else {
        setVisibility(true)
    }
}
