package org.oswayne.sample

import android.content.Context
import android.view.View
import android.widget.ImageView

import org.oswayne.view.adapter.AutoGridAdapter

class GridImageAdapter(images: List<Int>) : AutoGridAdapter<Int>(images) {

    override fun createView(context: Context, index: Int): View {
        return ImageView(context)
    }

    override fun initView(view: View, index: Int) {
        (view as ImageView).setImageResource(data[index])
    }
}
