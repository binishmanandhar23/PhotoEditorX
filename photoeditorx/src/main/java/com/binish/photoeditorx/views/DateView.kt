package com.binish.photoeditorx.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.binish.photoeditorx.R
import java.text.SimpleDateFormat
import java.util.*

class DateView: ConstraintLayout {
    var dateType = DateType.TYPE_1
    var typeface: Typeface? = null

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
        textViewInitialization(System.currentTimeMillis())
        setPadding(20,20,20,20)
    }

    private fun textViewInitialization(currentMillis: Long){
        val date = Date(currentMillis)
        val dateFormat = if(dateType == DateType.TYPE_1) SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) else SimpleDateFormat("EEEE, MMM yyyy", Locale.getDefault())
        val textView = TextView(context)
        textView.id = R.id.dateViewDate
        textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView.setTextColor(ContextCompat.getColor(context,R.color.color_white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        textView.text = dateFormat.format(date)
        if(typeface != null) textView.typeface = typeface
        addView(textView)
    }

    fun changeView(dateType: DateType){
        removeAllViews()
        this.dateType= dateType
        initialize()
    }

    fun changeFont(typeface: Typeface?){
        removeAllViews()
        this.typeface = typeface
        initialize()
    }

    enum class DateType{
        TYPE_1,
        TYPE_2,
    }
}