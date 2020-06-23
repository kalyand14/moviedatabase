package com.android.omdb.core.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.android.omdb.core.AppConstants.THREE
import com.android.omdb.core.AppConstants.TWO


/**
 * The TwoThreeImageView class is responsible for making ImageView 2:3 aspect ratio.
 * The TwoThreeImageView is used for movie poster in the movie_list_item.xml.
 */
class TwoThreeImageView : AppCompatImageView {
    /**
     * Creates a TwoThreeImageView
     *
     * @param context Used to talk to the UI and app resources
     */
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
    }

    /**
     * This method measures the view and its content to determine the measured width and the measured
     * height, which will make 2:3 aspect ratio.
     *
     * @param widthMeasureSpec horizontal space requirements as imposed by the parent
     * @param heightMeasureSpec vertical space requirements as imposed by th parent
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val twoThreeHeight: Int = MeasureSpec.getSize(widthMeasureSpec) * THREE / TWO
        val twoThreeHeightSpec =
            MeasureSpec.makeMeasureSpec(twoThreeHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, twoThreeHeightSpec)
    }
}