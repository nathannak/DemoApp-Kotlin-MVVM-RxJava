package demoapp.imgur.ui.list

import android.support.v7.util.DiffUtil
import demoapp.imgur.data.model.Image

/*
    Diff Util required by pagination library.
    For calculating items added when new page was fetched, it will be used.
 */

class ImgurDiffUtil : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(p0: Image, p1: Image): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: Image, p1: Image): Boolean {
        return p0.id == p1.id
    }
}