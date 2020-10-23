package com.binish.photoeditorx.fragments

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.binish.photoeditorx.R
import com.binish.photoeditorx.utils.Utils
import com.binish.photoeditorx.views.DateView
import com.binish.photoeditorx.views.TimeView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_bottom_sheet_sticker_dialog.*

class BottomSheetStickerDialog(val listener: BottomMapDetailFragmentInteraction) :
    BottomSheetDialogFragment() {
    private var root: View? = null
    private var bitmap: Bitmap? = null
    private var typeFace: Typeface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            bitmap = getParcelable(BITMAP)
        }

        typeFace = Typeface.createFromAsset(requireActivity().assets, "fonts/editFonts/Metropolis-Bold.otf") //Font for TimerView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.layout_bottom_sheet_sticker_dialog, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sticker1.setOnClickListener {
            Utils.pushInAnimation(it, requireContext())
            listener.onStickerClicker((sticker1.drawable as BitmapDrawable).bitmap)
            dismiss()
        }

        sticker2.setOnClickListener {
            Utils.pushInAnimation(it, requireContext())
            listener.onStickerClicker((sticker2.drawable as BitmapDrawable).bitmap)
            dismiss()
        }

        placeTimerView(TimeView(requireContext()))
        placeDateView(DateView(requireContext()))
    }

    private fun placeTimerView(timeView: TimeView){
        timeView.changeFont(typeFace)
        timeView.id = R.id.stickerTime
        timeView.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 100)
        containerStickerDialog.addView(timeView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(containerStickerDialog)
        constraintSet.connect(timeView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(timeView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(timeView.id, ConstraintSet.TOP, dummyDialogView.id, ConstraintSet.BOTTOM)
        constraintSet.connect(timeView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.setHorizontalBias(timeView.id,0.1f)
        constraintSet.setVerticalBias(timeView.id,0.05f)
        constraintSet.applyTo(containerStickerDialog)

        timeView.setOnClickListener {
            Utils.pushInAnimation(it, requireContext())
            val newTimerView = TimeView(requireContext())
            newTimerView.changeFont(typeFace)
            listener.onStickerTime(newTimerView) // You shouldn't pass a view with a parent attached, it complicates the multi-touch listener logic which is why we are creating a new TimerView Object
            dismiss()
        }
    }
    
    private fun placeDateView(dateView: DateView){
        dateView.changeFont(typeFace)
        dateView.id = R.id.stickerDate
        dateView.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 100)
        containerStickerDialog.addView(dateView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(containerStickerDialog)
        constraintSet.connect(dateView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(dateView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(dateView.id, ConstraintSet.TOP, dummyDialogView.id, ConstraintSet.BOTTOM)
        constraintSet.connect(dateView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.setHorizontalBias(dateView.id,0.8f)
        constraintSet.setVerticalBias(dateView.id,0.3f)
        constraintSet.applyTo(containerStickerDialog)
        
        dateView.setOnClickListener {
            Utils.pushInAnimation(it, requireContext())
            val newDateView = DateView(requireContext())
            newDateView.changeFont(typeFace)
            listener.onStickerTime(newDateView) // You shouldn't pass a view with a parent attached, it complicates the multi-touch listener logic which is why we are creating a new DateView Object
            dismiss()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)

        val resources = resources
        if (resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            val parent = view?.parent as View
            val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(
                resources.getDimensionPixelSize(R.dimen.bottomDialogMarginStart), // LEFT
                resources.getDimensionPixelSize(R.dimen.bottomDialogMarginTop), // Top
                resources.getDimensionPixelSize(R.dimen.bottomDialogMarginEnd), // RIGHT
                resources.getDimensionPixelSize(R.dimen.bottomDialogMarginBottom) // BOTTOM
            )
            parent.layoutParams = layoutParams
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_HALF_EXPANDED
            onStart()
        }
        return bottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.also {
            val bottomSheet = dialog?.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet?.setBackgroundResource(R.color.color_black_transparent)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            val layout = root?.findViewById(R.id.containerStickerDialog) as ConstraintLayout
            layout.viewTreeObserver?.addOnGlobalLayoutListener {
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        try {
                            layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            behavior.peekHeight = layout.height
                            view?.requestLayout()
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val BITMAP = "BITMAP"
        fun newInstance(listener: BottomMapDetailFragmentInteraction) =
            BottomSheetStickerDialog(listener)

        fun newInstance(bitmap: Bitmap, listener: BottomMapDetailFragmentInteraction) =
            BottomSheetStickerDialog(listener).apply {
                arguments = Bundle().apply {
                    putParcelable(BITMAP, bitmap)
                }
            }
    }

    interface BottomMapDetailFragmentInteraction {
        fun onStickerClicker(bitmap: Bitmap)
        fun onStickerTime(view: View)
    }
}