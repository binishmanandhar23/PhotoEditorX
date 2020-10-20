package com.binish.photoeditorx.views

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.binish.photoeditorx.R


class CustomEditText : AppCompatEditText {
    private var showKeyboardDelayed = false
    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initialize()
    }

    private fun initialize() {
        setBackgroundResource(R.color.color_transparent)
    }


    fun focusAndShowKeyboard() {
        requestFocus()
        showKeyboardDelayed = true
        maybeShowKeyboard()
    }

    @Override
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus);
        maybeShowKeyboard()
    }

    private fun maybeShowKeyboard() {
        if (hasWindowFocus() && showKeyboardDelayed) {
            if (isFocused) {
                post {
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
            showKeyboardDelayed = false
        }
    }

    fun hideSoftKeyboard() {
        clearFocus()
        post {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}