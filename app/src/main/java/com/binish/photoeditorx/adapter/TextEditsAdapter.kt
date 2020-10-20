package com.binish.photoeditorx.adapter

import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.binish.photoeditorx.R
import com.binish.photoeditorx.photoeditor.TextStyleBuilder
import com.binish.photoeditorx.utils.Utils

class TextEditsAdapter : RecyclerView.Adapter<TextEditsAdapter.ViewHolder> {
    private lateinit var activity: Activity
    private var colorsList: ArrayList<Int>? = null
    private var typeFaceList: List<Typeface>? = null
    private lateinit var listener: OnTextColorInteraction
    private lateinit var editType: EditType
    private var textStyle: TextStyleBuilder? = null
    private var selectedIndex = 0

    constructor(
        activity: Activity,
        colorsList: ArrayList<Int>,
        textStyle: TextStyleBuilder,
        listener: OnTextColorInteraction
    ) : super() {
        this.activity = activity
        this.colorsList = colorsList
        this.textStyle = textStyle
        this.listener = listener
        editType = EditType.Color

        for (i in colorsList.indices) {
            if (textStyle.textColor == ContextCompat.getColor(activity, colorsList[i]))
                selectedIndex = i
        }
    }

    constructor(
        activity: Activity,
        typeFaceList: List<Typeface>,
        textStyle: TextStyleBuilder,
        listener: OnTextColorInteraction
    ) : super() {
        this.activity = activity
        this.typeFaceList = typeFaceList
        this.textStyle = textStyle
        this.listener = listener
        editType = EditType.Style
        for (i in typeFaceList.indices) {
            if (textStyle.textFont == typeFaceList[i])
                selectedIndex = i
        }

    }

    constructor(activity: Activity, textStyle: TextStyleBuilder, listener: OnTextColorInteraction) {
        this.activity = activity
        this.textStyle = textStyle
        this.listener = listener
        editType = EditType.Align

        selectedIndex = when (textStyle.textAlign) {
            TextView.TEXT_ALIGNMENT_TEXT_START -> 0
            TextView.TEXT_ALIGNMENT_CENTER -> 1
            TextView.TEXT_ALIGNMENT_TEXT_END -> 2
            else -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.layout_text_edits, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (editType) {
            EditType.Color -> {
                val color = colorsList!![position]
                holder.constraintLayoutOuterColor.visibility = View.VISIBLE
                holder.constraintLayoutSelectedColor.visibility =
                    if (selectedIndex == position) View.VISIBLE else View.GONE
                holder.constraintLayoutInnerColor.backgroundTintList =
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                        ContextCompat.getColorStateList(activity, color)
                    else
                        AppCompatResources.getColorStateList(activity, color)

                holder.colorChooserMainContainer.setOnClickListener {
                    Utils.pushInAnimation(it, activity)
                    notifySelectedChanges(position)
                    listener.onColorSelected(color)
                }
            }
            EditType.Style -> {
                val typeface = typeFaceList!![position]
                holder.textViewStyleEdit.visibility = View.VISIBLE
                holder.textViewStyleEdit.backgroundTintList =
                    if (selectedIndex == position) ContextCompat.getColorStateList(
                        activity,
                        R.color.colorEditButtonBlack
                    ) else null
                holder.textViewStyleEdit.typeface = typeface
                holder.colorChooserMainContainer.setOnClickListener {
                    Utils.pushInAnimation(it, activity)
                    notifySelectedChanges(position)
                    listener.onTextStyleSelected(typeface)
                }
            }
            EditType.Align -> {
                /*holder.imageButtonAlignEdit.visibility = View.VISIBLE
                holder.imageButtonAlignEdit.backgroundTintList =
                    if (selectedIndex == position) ContextCompat.getColorStateList(
                        activity,
                        R.color.colorEditButtonBlack
                    ) else null
                when (position + 1) {
                    1 -> holder.imageButtonAlignEdit.setImageResource(R.drawable.ic_camera_text_align_start)
                    2 -> holder.imageButtonAlignEdit.setImageResource(R.drawable.ic_camera_text_align_center)
                    3 -> holder.imageButtonAlignEdit.setImageResource(R.drawable.ic_camera_text_align_end)
                }
                holder.imageButtonAlignEdit.setOnClickListener {
                    AppUtil.pushInAnimation(it, activity)
                    notifySelectedChanges(position)
                    listener.onTextAlignSelected(
                        when (position + 1) {
                            1 -> TextView.TEXT_ALIGNMENT_TEXT_START
                            2 -> TextView.TEXT_ALIGNMENT_CENTER
                            3 -> TextView.TEXT_ALIGNMENT_TEXT_END
                            else -> TextView.TEXT_ALIGNMENT_TEXT_START
                        }
                    )
                }*/
            }
        }
    }

    override fun getItemCount(): Int =
        when (editType) {
            EditType.Color -> colorsList?.size ?: 0
            EditType.Style -> typeFaceList?.size ?: 0
            EditType.Align -> 3
        }

    private fun notifySelectedChanges(position: Int) {
        notifyItemChanged(selectedIndex)
        selectedIndex = position
        notifyItemChanged(selectedIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val constraintLayoutInnerColor =
            itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutInnerColor)!!
        val constraintLayoutOuterColor =
            itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutOuterColor)!!
        val constraintLayoutSelectedColor =
            itemView.findViewById<ConstraintLayout>(R.id.constraintLayoutSelectedColor)!!
        val colorChooserMainContainer =
            itemView.findViewById<ConstraintLayout>(R.id.colorChooserMainContainer)!!

        val textViewStyleEdit = itemView.findViewById<TextView>(R.id.textViewStyleEdit)!!
//        val imageButtonAlignEdit = itemView.findViewById<ImageButton>(R.id.imageButtonAlignEdit)!!
    }

    interface OnTextColorInteraction {
        fun onColorSelected(color: Int)
        fun onTextStyleSelected(typeface: Typeface)
        fun onTextAlignSelected(textAlignment: Int)
    }

    abstract class OnTextEditsInteractionAdapter : OnTextColorInteraction {
        override fun onColorSelected(color: Int) {

        }

        override fun onTextStyleSelected(typeface: Typeface) {

        }

        override fun onTextAlignSelected(textAlignment: Int) {

        }
    }
}
enum class EditType {
    Color,
    Style,
    Align
}