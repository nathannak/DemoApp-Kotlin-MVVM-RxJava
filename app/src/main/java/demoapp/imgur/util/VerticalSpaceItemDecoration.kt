package demoapp.imgur.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/*
    Class responsible for showing spaces between recyclerview items.
 */
class VerticalSpaceItemDecoration(
        private val cols: Int,
        private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight

        if (parent.getChildAdapterPosition(view) < cols) {
            outRect.top = verticalSpaceHeight
        } else {
            outRect.top = 0
        }
    }
}