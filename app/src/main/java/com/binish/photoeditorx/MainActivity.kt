package com.binish.photoeditorx

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.binish.photoeditorx.utils.Utils
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_add_edit_text.*

class MainActivity : AppCompatActivity() {
    private var imageRotation = 0f
    private val imageBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpImage()
        setUpRotation()
        setUpTextAddition()
    }

    private fun setUpImage() {
        photoEditorView.source?.setImageResource(R.drawable.default_image)
        photoEditorView.source?.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    private fun setUpTextAddition() {
        photoEditorView.setOnClickListener {
            Blurry.with(this).animate(10).radius(20).async().color(R.color.color_black_transparent)
                .from(Utils.takeScreenshot(photoEditorView.source!!)).into(viewForEdit)
            editLayoutContainer.transitionToEnd()
            editViewForEdit.focusAndShowKeyboard()
        }

        textViewDoneForEdit.setOnClickListener {
            editViewForEdit.hideSoftKeyboard()
            editViewForEdit.postDelayed({
                editLayoutContainer.transitionToStart()
            },50)
        }
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