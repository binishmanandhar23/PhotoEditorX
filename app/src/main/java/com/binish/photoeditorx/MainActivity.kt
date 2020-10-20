package com.binish.photoeditorx

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.binish.photoeditorx.adapter.TextEditsAdapter
import com.binish.photoeditorx.photoeditor.*
import com.binish.photoeditorx.utils.Utils
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_add_edit_text.*

class MainActivity : AppCompatActivity() {
    private var imageRotation = 0f
    private val imageBitmap = MutableLiveData<Bitmap>()
    private lateinit var ttCommonBold: Typeface
    private var textSize = 32f
    private val textStyle = TextStyleBuilder()
    private lateinit var photoEditor: PhotoEditor
    private var isTextStyleVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ttCommonBold = Typeface.createFromAsset(assets, "fonts/editFonts/TTCommonBold.otf")

        setUpImage()
        setUpRotation()
        setUpTextAddition()
        setUpPhotoEditor()
    }

    private fun setUpImage() {
        photoEditorView.source?.setImageResource(R.drawable.default_image)
        photoEditorView.source?.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    private fun setUpTextAddition() {
        textStyle.withTextFont(ttCommonBold)
        textStyle.withTextColor(ContextCompat.getColor(this, R.color.color_white))
        textStyle.withTextSize(textSize)
        textStyle.withTextAlign(TextView.TEXT_ALIGNMENT_TEXT_START)
        setUpEditView()

        photoEditorView.setOnClickListener {
            showOrHideEditView(true)
            setUpEdits(null)
        }

        imageButtonColorTextPicker.setOnClickListener {
            recyclerViewColorForEdit.visibility = if(isTextStyleVisible) View.VISIBLE else View.GONE
            recyclerViewStyleForEdit.visibility = if(isTextStyleVisible) View.GONE else View.VISIBLE
            if (isTextStyleVisible)
                imageButtonColorTextPicker.changeIcon(R.drawable.ic_text_style, R.drawable.camera_edit_color_button)
            else
                imageButtonColorTextPicker.changeIcon(R.drawable.ic_color, R.color.color_transparent)

            isTextStyleVisible = !isTextStyleVisible
        }
    }

    private fun setUpPhotoEditor() {
        photoEditor = PhotoEditor.Builder(this, photoEditorView as PhotoEditorView)
            .setPinchTextScalable(true).build()
        photoEditor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
                showOrHideEditView(true)
                editViewForEdit.setText(text)
                setUpEdits(rootView)
            }

            override fun onAddViewListener(
                view: View?,
                viewType: EnumClass.ViewType?,
                numberOfAddedViews: Int
            ) {

            }

            override fun onRemoveViewListener(
                viewType: EnumClass.ViewType?,
                numberOfAddedViews: Int
            ) {

            }

            override fun onStartViewChangeListener(viewType: EnumClass.ViewType?) {

            }

            override fun onStopViewChangeListener(
                view: View?,
                viewType: EnumClass.ViewType?,
                currX: Float,
                currY: Float,
                rawX: Int,
                rawY: Int
            ) {

            }

            override fun onMoveViewChangeListener(
                view: View?,
                isInProgress: Boolean,
                rawX: Int,
                rawY: Int
            ) {

            }

        })
    }

    private fun setUpEdits(rootView: View?) {
        recyclerViewColorForEdit.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewColorForEdit.adapter = TextEditsAdapter(
            this,
            Utils.getColors(),
            textStyle,
            object : TextEditsAdapter.OnTextEditsInteractionAdapter() {
                override fun onColorSelected(color: Int) {
                    val colorCode = ContextCompat.getColor(this@MainActivity, color)
                    textStyle.withTextColor(colorCode)
                    editViewForEdit.setTextColor(colorCode)
                    if (rootView != null)
                        photoEditor.editText(rootView, editViewForEdit.text.toString(), textStyle)
                }
            })

        recyclerViewStyleForEdit.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewStyleForEdit.adapter = TextEditsAdapter(
            this,
            Utils.getTextStyle(assets),
            textStyle,
            object : TextEditsAdapter.OnTextEditsInteractionAdapter(){
                override fun onTextStyleSelected(typeface: Typeface) {
                    textStyle.withTextFont(typeface)
                    editViewForEdit.typeface = typeface
                    if(rootView != null)
                        photoEditor.editText(rootView, editViewForEdit.text.toString(), textStyle)
                }
            }
        )

        textViewDoneForEdit.setOnClickListener {
            showOrHideEditView(false)
            if(rootView != null)
                photoEditor.editText(rootView, editViewForEdit.text.toString(), textStyle)
            else if (editViewForEdit.text.toString().isNotEmpty())
                photoEditor.addText(editViewForEdit.text.toString(), textStyle)
        }
    }

    private fun showOrHideEditView(show: Boolean) {
        if (show) {
            Blurry.with(this).animate(10).radius(20).async().color(R.color.color_black_transparent)
                .from(Utils.takeScreenshot(photoEditorView.source!!)).into(viewForEdit)
            editLayoutContainer.transitionToEnd()
            editViewForEdit.focusAndShowKeyboard()
        } else {
            editViewForEdit.hideSoftKeyboard()
            editViewForEdit.postDelayed({
                editLayoutContainer.transitionToStart()
            }, 50)
        }
    }

    private fun setUpEditView(){
        editViewForEdit.setTextColor(textStyle.textColor)
        editViewForEdit.textSize = textStyle.textSize
        editViewForEdit.typeface = textStyle.textFont
    }

    private fun setUpRotation() {
        imageButtonRotateLeft.setOnClickListener {
            imageRotation = when (imageRotation) {
                0f -> 270f
                90f -> 0f
                180f -> 90f
                270f -> 180f
                else -> 270f
            }
            rotateWork()
        }

        imageButtonRotateRight.setOnClickListener {
            imageRotation = when (imageRotation) {
                0f -> 90f
                90f -> 180f
                180f -> 270f
                270f -> 0f
                else -> 90f
            }
            rotateWork()
        }
        imageBitmap.observe(this, Observer {
            photoEditorView.source?.setImageBitmap(it)
        })
    }

    private fun rotateWork() {
        Thread {
            imageBitmap.postValue(
                Utils.rotateBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.default_image
                    ), imageRotation
                )
            )
        }.start()
    }
}