package com.binish.photoeditorx.photoeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import java.util.*


/**
 *
 *
 * This is custom drawing view used to do painting on user touch events it it will paint on canvas
 * as per attributes provided to the paint
 *
 *
 * @author [Burhanuddin Rashid](https://github.com/burhanrashid52)
 * @version 0.1.1
 * @since 12/1/18
 */
class BrushDrawingView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    View(context, attrs, defStyle) {
    private var mBrushSize = DEFAULT_BRUSH_SIZE
    var eraserSize = DEFAULT_ERASER_SIZE
        private set
    private var mOpacity = DEFAULT_OPACITY
    private val mDrawnPaths: Stack<LinePath> = Stack<LinePath>()
    private val mRedoPaths: Stack<LinePath> = Stack<LinePath>()

    @get:VisibleForTesting
    val drawingPaint = Paint()
    private var mDrawCanvas: Canvas? = null
    private var mBrushDrawMode = false
    private var mPath: Path? = null
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var mBrushViewChangeListener: BrushViewChangeListener? = null
    private fun setupBrushDrawing() {
        //Caution: This line is to disable hardware acceleration to make eraser feature work properly
        setLayerType(LAYER_TYPE_HARDWARE, null)
        drawingPaint.color = Color.BLACK
        setupPathAndPaint()
        visibility = GONE
    }

    private fun setupPathAndPaint() {
        mPath = Path()
        drawingPaint.isAntiAlias = true
        drawingPaint.isDither = true
        drawingPaint.style = Paint.Style.STROKE
        drawingPaint.strokeJoin = Paint.Join.ROUND
        drawingPaint.strokeCap = Paint.Cap.ROUND
        drawingPaint.strokeWidth = mBrushSize
        drawingPaint.alpha = mOpacity
        drawingPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
    }

    private fun refreshBrushDrawing() {
        mBrushDrawMode = true
        setupPathAndPaint()
    }

    fun brushEraser() {
        mBrushDrawMode = true
        drawingPaint.strokeWidth = eraserSize
        drawingPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    var opacity: Int
        get() = mOpacity
        set(opacity) {
            mOpacity = opacity
            brushDrawingMode = true
        }
    var brushDrawingMode: Boolean
        get() = mBrushDrawMode
        set(brushDrawMode) {
            mBrushDrawMode = brushDrawMode
            if (brushDrawMode) {
                this.visibility = VISIBLE
                refreshBrushDrawing()
            }
        }

    fun setBrushEraserSize(brushEraserSize: Float) {
        eraserSize = brushEraserSize
        brushDrawingMode = true
    }

    fun setBrushEraserColor(@ColorInt color: Int) {
        drawingPaint.color = color
        brushDrawingMode = true
    }

    var brushSize: Float
        get() = mBrushSize
        set(size) {
            mBrushSize = size
            brushDrawingMode = true
        }
    var brushColor: Int
        get() = drawingPaint.color
        set(color) {
            drawingPaint.color = color
            brushDrawingMode = true
        }

    fun clearAll() {
        mDrawnPaths.clear()
        mRedoPaths.clear()
        if (mDrawCanvas != null) {
            mDrawCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
        }
        invalidate()
    }

    fun setBrushViewChangeListener(brushViewChangeListener: BrushViewChangeListener?) {
        mBrushViewChangeListener = brushViewChangeListener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mDrawCanvas = Canvas(canvasBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        for (linePath in mDrawnPaths) {
            canvas.drawPath(linePath.drawPath, linePath.drawPaint)
        }
        canvas.drawPath(mPath!!, drawingPaint)
    }

    /**
     * Handle touch event to draw paint on canvas i.e brush drawing
     *
     * @param event points having touch info
     * @return true if handling touch events
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mBrushDrawMode) {
            val touchX = event.x
            val touchY = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> touchStart(touchX, touchY)
                MotionEvent.ACTION_MOVE -> touchMove(touchX, touchY)
                MotionEvent.ACTION_UP -> touchUp()
            }
            invalidate()
            true
        } else {
            false
        }
    }

    fun undo(): Boolean {
        if (!mDrawnPaths.empty()) {
            mRedoPaths.push(mDrawnPaths.pop())
            invalidate()
        }

        mBrushViewChangeListener?.onViewRemoved(this)
        return !mDrawnPaths.empty()
    }

    fun redo(): Boolean {
        if (!mRedoPaths.empty()) {
            mDrawnPaths.push(mRedoPaths.pop())
            invalidate()
        }

        mBrushViewChangeListener?.onViewAdd(this)

        return !mRedoPaths.empty()
    }

    private fun touchStart(x: Float, y: Float) {
        mRedoPaths.clear()
        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mTouchX = x
        mTouchY = y
        mBrushViewChangeListener?.onStartDrawing()

    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mTouchX)
        val dy = Math.abs(y - mTouchY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath!!.quadTo(mTouchX, mTouchY, (x + mTouchX) / 2, (y + mTouchY) / 2)
            mTouchX = x
            mTouchY = y
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mTouchX, mTouchY)
        // Commit the path to our offscreen
        mDrawCanvas!!.drawPath(mPath!!, drawingPaint)
        // kill this so we don't double draw
        mDrawnPaths.push(LinePath(mPath, drawingPaint))
        mPath = Path()
        mBrushViewChangeListener?.onStopDrawing()
        mBrushViewChangeListener?.onViewAdd(this)

    }

    @get:VisibleForTesting
    val drawingPath
        get() = Pair(mDrawnPaths, mRedoPaths)

    companion object {
        const val DEFAULT_BRUSH_SIZE = 25.0f
        const val DEFAULT_ERASER_SIZE = 50.0f
        const val DEFAULT_OPACITY = 255
        private const val TOUCH_TOLERANCE = 4f
    }

    init {
        setupBrushDrawing()
    }
}