package org.carder.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imagesId = ArrayList<Int>()
        imagesId.add(R.mipmap.ic_launcher)
        imagesId.add(R.mipmap.ic_launcher)
        imagesId.add(R.mipmap.ic_launcher)
        imagesId.add(R.mipmap.ic_launcher)

        val adapter = GridImageAdapter(imagesId)
        giv.setAdapter(adapter)
    }
}
