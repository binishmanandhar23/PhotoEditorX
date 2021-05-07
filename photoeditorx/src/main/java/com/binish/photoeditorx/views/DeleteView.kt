package com.binish.photoeditorx.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.binish.photoeditorx.R
import com.binish.photoeditorx.photoeditor.EnumClass
import com.binish.photoeditorx.photoeditor.PhotoEditor
import com.binish.photoeditorx.utils.Utils

class DeleteView : MotionLayout {
    private lateinit var imageView: ImageView
    private var isVibrated = false
    private var canDeleteVibrate = true
    private var isScaled = false
    private var deleteDrawableClose = R.drawable.ic_camera_delete
    private var deleteDrawableOpen = R.drawable.ic_camera_delete_open
    private var bottomPadding: Int? = 80

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initialize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet? = null) {
        if(attrs != null) {
            val styles = context.obtainStyledAttributes(attrs, R.styleable.DeleteView)
            bottomPadding = Utils.dp2px(styles.getDimension(R.styleable.DeleteView_bottomPadding, 80f))
            styles.recycle()
        }
        loadLayoutDescription(R.xml.edit_image_delete_scene)
        setTransition(R.id.editImageDeleteScene)
        firstConstraintLayout()
    }

    fun changeDeleteButton(defaultResId: Int, openedResId: Int) {
        deleteDrawableClose = defaultResId
        deleteDrawableOpen = openedResId
    }

    private fun firstConstraintLayout() {
        val constraintLayout = ConstraintLayout(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        constraintLayout.id = R.id.editImageDeleteLayout
        constraintLayout.layoutParams = params
        constraintLayout.elevation = 10f
        constraintLayout.setPadding(80, 0, 80, bottomPadding?: 80)
        constraintLayout.setBackgroundResource(R.drawable.transparent_gradient)
        addView(constraintLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.connect(
            constraintLayout.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            constraintLayout.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.connect(
            constraintLayout.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(this)

        firstImageView(constraintLayout)
    }

    private fun firstImageView(constraintLayout: ConstraintLayout) {
        imageView = ImageView(context)
        imageView.id = R.id.imageViewEditImageDelete
        imageView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imageView.setPadding(20, 20, 20, 20)
        imageView.setImageResource(deleteDrawableClose)
        constraintLayout.addView(imageView)

        val imageViewSet = ConstraintSet()
        imageViewSet.clone(constraintLayout)
        imageViewSet.connect(
            imageView.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        imageViewSet.connect(
            imageView.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        imageViewSet.connect(
            imageView.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        imageViewSet.connect(
            imageView.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        imageViewSet.applyTo(constraintLayout)
    }

    fun onHideDeleteView(photoEditor: PhotoEditor, view: View, rawX: Int, rawY: Int) {
        transitionToStart()
        if (Utils.isViewInBounds(imageView, rawX, rawY)) {
            Utils.pushInAnimation(imageView, context)
            imageView.setImageResource(deleteDrawableClose)
            isScaled = false
            photoEditor.viewUndo(view, (view.tag) as EnumClass.ViewType)
        }
    }

    fun onShowDeleteView(view: View, isInProgress: Boolean, rawX: Int, rawY: Int) {
        if (currentState == startState && !isInProgress)
            transitionToEnd()
        else if (currentState == endState && isInProgress)
            transitionToStart()

        if (Utils.isViewInBounds(imageView, rawX, rawY) && !isVibrated) {
            Utils.pushInAnimation(imageView, context)
            isVibrated = true
            if (!isScaled) {
                scaleImageForDeletion(view, true)
                isScaled = true
            }
        } else if (Utils.isViewInBounds(imageView, rawX, rawY)) {
            if (canDeleteVibrate) {
                Utils.performHapticFeedback(imageView)
                canDeleteVibrate = !canDeleteVibrate
            }
            imageView.setImageResource(deleteDrawableOpen)
            if (!isScaled) {
                scaleImageForDeletion(view, true)
                isScaled = true
            }
        } else {
            canDeleteVibrate = true
            imageView.setImageResource(deleteDrawableClose)
            if (isScaled) {
                scaleImageForDeletion(view, false)
                isScaled = false
            }
        }
    }

    var originalScale: Float? = null
    private fun scaleImageForDeletion(view: View?, scaleDown: Boolean) {
        if (scaleDown)
            originalScale = view?.scaleX

        val scaleFrom = if (scaleDown) originalScale ?: 1f else 0.1f
        val scaleTo = if (scaleDown) 0.1f else originalScale ?: 1f

        val animators = ArrayList<Animator>()
        animators.add(ObjectAnimator.ofFloat(view, "scaleX", scaleFrom, scaleTo))
        animators.add(ObjectAnimator.ofFloat(view, "scaleY", scaleFrom, scaleTo))
        AnimatorSet().apply {
            playTogether(animators as Collection<Animator>)
            duration = 200
            start()
        }
    }
}