package org.oswayne.view.adapter

import android.content.Context
import android.view.View

abstract class AutoGridAdapter<O>(protected val data: List<O>) {

    val count: Int
        get() = data.size

    abstract fun createView(context: Context, index: Int): View

    abstract fun initView(view: View, index: Int)
}