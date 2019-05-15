package demoapp.imgur.ui.list

import android.widget.ImageView
import demoapp.imgur.data.model.Image

/*
    Interface declaring action when any image is selected. it is used for shared transition
 */
interface ImageSelectedListener {
    fun onImageSelected(imageView: ImageView, image: Image)
}
