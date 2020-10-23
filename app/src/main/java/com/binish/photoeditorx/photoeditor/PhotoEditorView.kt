package com.binish.photoeditorx.photoeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.binish.photoeditorx.R
import com.binish.photoeditorx.photoeditor.FilterImageView.OnImageChangedListener
import com.binish.photoeditorx.photoeditor.EnumClass.PhotoFilter.*
import com.binish.photoeditorx.photoeditor.EnumClass.PhotoFilter
import com.binish.photoeditorx.utils.Utils


/**
 *
 *
 * This ViewGroup will have the [BrushDrawingView] to draw paint on it with [ImageView]
 * which our source image
 *
 *
 * @author [Burhanuddin Rashid](https://github.com/burhanrashid52)
 * @version 0.1.1
 * @since 1/18/2018
 */
class PhotoEditorView : RelativeLayout {
    private var mImgSource: FilterImageView? = null
    var brushDrawingView: BrushDrawingView? = null
        private set
    private var mImageFilterView: ImageFilterView? = null

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @SuppressLint("Recycle")
    private fun init(attrs: AttributeSet?) {
        //Setup image attributes
        mImgSource = FilterImageView(context)
        mImgSource!!.id = imgSrcId
        mImgSource!!.adjustViewBounds = true
        val imgSrcParam = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        imgSrcParam.addRule(CENTER_IN_PARENT, TRUE)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.PhotoEditorView)
            val imgSrcDrawable = a.getDrawable(R.styleable.PhotoEditorView_photo_src)
            if (imgSrcDrawable != null) {
                mImgSource!!.setImageDrawable(imgSrcDrawable)
            }
        }

        //Setup brush view
        brushDrawingView = BrushDrawingView(context)
        brushDrawingView!!.visibility = GONE
        brushDrawingView!!.id = brushSrcId
        //Align brush to the size of image view
        val brushParam = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        brushParam.addRule(CENTER_IN_PARENT, TRUE)
        brushParam.addRule(ALIGN_TOP, imgSrcId)
        brushParam.addRule(ALIGN_BOTTOM, imgSrcId)

        //Setup GLSurface attributes
        mImageFilterView = ImageFilterView(context)
        mImageFilterView!!.id = glFilterId
        mImageFilterView!!.visibility = GONE

        //Align brush to the size of image view
        val imgFilterParam = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        imgFilterParam.addRule(CENTER_IN_PARENT, TRUE)
        imgFilterParam.addRule(ALIGN_TOP, imgSrcId)
        imgFilterParam.addRule(ALIGN_BOTTOM, imgSrcId)
        mImgSource!!.setOnImageChangedListener(object : OnImageChangedListener {
            override fun onBitmapLoaded(sourceBitmap: Bitmap?) {
                mImageFilterView?.setFilterEffect(NONE)
                mImageFilterView!!.setSourceBitmap(sourceBitmap)
                Log.d(
                    TAG,
                    "onBitmapLoaded() called with: sourceBitmap = [$sourceBitmap]"
                )
            }
        })


        //Add image source
        addView(mImgSource, imgSrcParam)

        //Add Gl FilterView
        addView(mImageFilterView, imgFilterParam)

        //Add brush view
        addView(brushDrawingView, brushParam)
    }

    /**
     * Source image which you want to edit
     *
     * @return source ImageView
     */
    val source: ImageView?
        get() = mImgSource

    fun saveFilter(onSaveBitmap: OnSaveBitmap) {
        if (mImageFilterView!!.visibility == VISIBLE) {
            mImageFilterView!!.saveBitmap(object : OnSaveBitmap {
                override fun onBitmapReady(saveBitmap: Bitmap?) {
                    Log.e(TAG, "saveFilter: $saveBitmap")
                    mImgSource!!.setImageBitmap(saveBitmap!!)
                    mImageFilterView!!.visibility = GONE
                    onSaveBitmap.onBitmapReady(saveBitmap)
                }

                override fun onFailure(e: Exception?) {
                    onSaveBitmap.onFailure(e)
                }
            })
        } else {
            onSaveBitmap.onBitmapReady(mImgSource?.bitmap)
        }
    }

    fun setFilterEffect(filterType: PhotoFilter?) {
        mImageFilterView!!.visibility = VISIBLE
        mImageFilterView!!.setSourceBitmap(mImgSource?.bitmap)
        mImageFilterView?.setFilterEffect(filterType)
    }

    fun setFilterEffect(customEffect: CustomEffect?) {
        mImageFilterView!!.visibility = VISIBLE
        mImageFilterView!!.setSourceBitmap(mImgSource?.bitmap)
        mImageFilterView?.setFilterEffect(customEffect)
    }

    fun rotate(rotate: Rotation) {
        if (mImgSource?.bitmap != null)
            source?.setImageBitmap(
                Utils.rotateBitmap(
                    mImgSource?.bitmap!!, when (rotate) {
                        Rotation.ROTATE_0 -> 0f
                        Rotation.ROTATE_90 -> 90f
                        Rotation.ROTATE_180 -> 180f
                        Rotation.ROTATE_270 -> 270f
                    }
                )
            )
    }

    companion object {
        private const val TAG = "PhotoEditorView"
        private const val imgSrcId = 1
        private const val brushSrcId = 2
        private const val glFilterId = 3
    }

    enum class Rotation {
        ROTATE_0,
        ROTATE_90,
        ROTATE_180,
        ROTATE_270,
    }
}