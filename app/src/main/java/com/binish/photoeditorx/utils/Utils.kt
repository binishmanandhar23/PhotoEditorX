package com.binish.photoeditorx.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import android.graphics.drawable.PictureDrawable
import android.util.DisplayMetrics
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.animation.AnimationUtils
import com.binish.photoeditorx.R

object Utils {
    fun dp2px(dp: Float): Int {
        return (Resources.getSystem().displayMetrics.density * dp).toInt()
    }

    fun px2dp(px: Float): Float {
        return (px - 0.5f) / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun performHapticFeedback(view: View) {
        view.isHapticFeedbackEnabled = true
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }

    fun pushInAnimation(view: View, context: Context) {
        val pushInAnimation = AnimationUtils.loadAnimation(context, R.anim.scale)
        view.startAnimation(pushInAnimation)
        performHapticFeedback(view)
    }

    fun pictureDrawable2Bitmap(picture: Picture): Bitmap? {
        val pd = PictureDrawable(picture)
        val bitmap = Bitmap.createBitmap(pd.intrinsicWidth, pd.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawPicture(pd.picture)
        return bitmap
    }
}