package com.binish.photoeditorx.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.binish.photoeditorx.R
import java.text.SimpleDateFormat
import java.util.*

class TimeView: ConstraintLayout {
    var typeface: Typeface? = null
    var timerViewType = TimerViewType.TYPE_1

    constructor(context: Context) : super(context) { initialize()}
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { initialize() }

    private fun initialize(){
        setBackgroundResource(R.color.color_transparent)
        simpleTextView(System.currentTimeMillis())
        setPadding(20,20,20,20)
    }

    fun changeFont(typeface: Typeface?){
        removeAllViews()
        this.typeface = typeface
        initialize()
    }

    fun changeView(timerViewType: TimerViewType){
        removeAllViews()
        this.timerViewType = timerViewType
        initialize()
    }

    private fun simpleTextView(currentTime: Long){
        val date = Date(currentTime)
        val hourSecondFormat = if(timerViewType == TimerViewType.TYPE_1) SimpleDateFormat("hh:mm", Locale.getDefault()) else SimpleDateFormat("HH:mm", Locale.getDefault())
        val textView = TextView(context)
        textView.id = R.id.timeViewHourSecond
        textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView.setTextColor(ContextCompat.getColor(context,R.color.color_white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        textView.text = hourSecondFormat.format(date)
        if(typeface != null) textView.typeface = typeface
        addView(textView)

        if(timerViewType == TimerViewType.TYPE_1) {
            val amPmFormat = SimpleDateFormat("aa", Locale.getDefault())
            val amPmTextView = TextView(context)
            amPmTextView.id = R.id.timeViewAmPm
            amPmTextView.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            amPmTextView.setTextColor(ContextCompat.getColor(context, R.color.color_white))
            amPmTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            amPmTextView.text = amPmFormat.format(date)
            if (typeface != null) amPmTextView.typeface = typeface
            addView(amPmTextView)

            val amPmConstraintSet = ConstraintSet()
            amPmConstraintSet.clone(this)
            amPmConstraintSet.connect(
                amPmTextView.id,
                ConstraintSet.START,
                textView.id,
                ConstraintSet.END,
                10
            )
            amPmConstraintSet.connect(
                amPmTextView.id,
                ConstraintSet.BOTTOM,
                textView.id,
                ConstraintSet.BOTTOM,
                10
            )
            amPmConstraintSet.applyTo(this)
        }
    }

    enum class TimerViewType{
        TYPE_1,
        TYPE_2,
        TYPE_3,
    }
}