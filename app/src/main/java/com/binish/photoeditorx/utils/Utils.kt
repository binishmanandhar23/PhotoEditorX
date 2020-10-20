package com.binish.photoeditorx.utils

import android.R.attr
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.util.DisplayMetrics
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
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
        val bitmap = Bitmap.createBitmap(
            pd.intrinsicWidth,
            pd.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawPicture(pd.picture)
        return bitmap
    }

    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.width,
            source.height,
            matrix,
            true
        )
    }

    fun takeScreenshot(view: View): Bitmap {
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun getColors(): ArrayList<Int> {
        val colorsList = ArrayList<Int>()
        colorsList.add(R.color.color_white)
        colorsList.add(R.color.color_black)
        colorsList.add(R.color.colorLightPeach)
        colorsList.add(R.color.color_black)
        colorsList.add(R.color.color_orange)
        colorsList.add(R.color.colorTwilight)
        colorsList.add(R.color.color_purple_dark)
        colorsList.add(R.color.colorDarkGreyBlue)
        colorsList.add(R.color.colorSea)
        colorsList.add(R.color.colorDarkTeal)
        colorsList.add(R.color.colorBrownGrey)
        colorsList.add(R.color.color_blue)
        return colorsList
    }

    fun getTextStyle(assets: AssetManager): List<Typeface> {
        val typefaceList = ArrayList<Typeface>()
        val fontsName = assets.list("fonts/editFonts")
        if (fontsName != null) {
            for (i in fontsName.indices) {
                typefaceList.add(
                    Typeface.createFromAsset(
                        assets,
                        "fonts/editFonts/${fontsName[i]}"
                    )
                )
            }
        }
        return typefaceList
    }
}