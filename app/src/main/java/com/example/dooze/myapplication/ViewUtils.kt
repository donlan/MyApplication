package com.example.dooze.myapplication

import android.content.res.Resources


object ViewUtils {

    fun dp2px(dp: Float, res: Resources): Int {
        return if (dp == 0f) {
            0
        } else {
            (dp * res.displayMetrics.density + 0.5f).toInt()
        }
    }

    fun px2dp(px: Float, res: Resources): Float {
        return (px - 0.5f) / res.displayMetrics.density
    }

}