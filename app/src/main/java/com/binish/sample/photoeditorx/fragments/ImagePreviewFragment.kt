package com.binish.sample.photoeditorx.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.binish.sample.photoeditorx.R
import kotlinx.android.synthetic.main.fragment_display_saved_image.*

class ImagePreviewFragment : Fragment() {
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            bitmap = getParcelable(BITMAP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_saved_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageViewEditPreviewBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        imageViewSavedImage.setImageBitmap(bitmap)
    }


    companion object {
        private const val BITMAP = "BITMAP"
        fun newInstance(bitmap: Bitmap?) = ImagePreviewFragment().apply {
            arguments = Bundle().apply {
                putParcelable(BITMAP, bitmap)
            }
        }
    }
}