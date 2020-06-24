package com.android.omdb.core.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.android.omdb.core.AppConstants.ONE


/**
 * Set column spacing to make each column have the same spacing.
 *
 * Reference: @see "https://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing"
 */
class GridSpacingItemDecoration
/**
 * Constructor
 *
 * @param spanCount The number of columns
 * @param spacing The spacing between each grid item
 * @param includeEdge Whether to include left and right margins
 */(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // item position
        val position = parent.getChildAdapterPosition(view)
        // item column
        val column = position % spanCount
        if (includeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = spacing - column * spacing / spanCount
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (column + ONE) * spacing / spanCount

            // top edge
            if (position < spanCount) {
                outRect.top = spacing
            }
            // item bottom
            outRect.bottom = spacing
        } else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = column * spacing / spanCount
            // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            outRect.right = spacing - (column + ONE) * spacing / spanCount
            if (position >= spanCount) {
                // item top
                outRect.top = spacing
            }
        }
    }

}