package com.android.omdb.core.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.android.omdb.core.AppConstants.THREE
import com.android.omdb.core.AppConstants.TWO


/**
 * The ThreeTwoImageView class is responsible for making ImageView 3:2 aspect ratio.
 * The ThreeTwoImageView is used for movie backdrop image in the activity_detail.xml.
 */
class ThreeTwoImageView : AppCompatImageView {
    /**
     * Creates ThreeTwoImageView
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
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    /**
     * This method measures the view and its content to determine the measured width and the measured
     * height, which will make 3:2 aspect ratio.
     *
     * @param widthMeasureSpec horizontal space requirements as imposed by the parent
     * @param heightMeasureSpec vertical space requirements as imposed by th parent
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val threeTwoHeight: Int = MeasureSpec.getSize(widthMeasureSpec) * TWO / THREE
        val threeTwoHeightSpec =
            MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, threeTwoHeightSpec)
    }
}