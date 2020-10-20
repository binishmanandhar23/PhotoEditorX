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
        setBackgroundResource(R.color.color_transparent)
        setPadding(10)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                Utils.pushInAnimation(this, context)
                performClick()
            }
        }
        return true
    }
}