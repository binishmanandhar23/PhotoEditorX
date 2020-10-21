package com.binish.photoeditorx.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.setPadding
import com.binish.photoeditorx.R
import com.binish.photoeditorx.utils.Utils


class CustomImageButton : AppCompatImageButton {
    constructor(context: Context) : super(context) {
        initialSetup()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialSetup()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initialSetup()
    }

    private fun initialSetup() {
        defaultBackground()
        setPadding(20)
    }

    fun changeIcon(resId: Int, backgroundResId: Int){
        setImageResource(resId)
        setBackgroundResource(backgroundResId)
    }

    fun defaultBackground(){
        setBackgroundResource(R.color.color_transparent)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(!isEnabled)
            return false
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                Utils.pushInAnimation(this, context)
                performClick()
            }
        }
        return true
    }
}