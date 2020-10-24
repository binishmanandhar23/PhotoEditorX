package com.binish.photoeditorx.photoeditor

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import com.binish.photoeditorx.models.StrokeProperties
import java.util.*


/**
 *
 *
 * This class is used to wrap the styles to apply on the TextView on [PhotoEditor.addText] and [PhotoEditor.editText]
 *
 *
 * @author [Christian Caballero](https://github.com/Sulfkain)
 * @since 14/05/2019
 */
class TextStyleBuilder {
    private val values: MutableMap<TextStyle, Any> = HashMap()
    private fun getValues(): Map<TextStyle, Any> {
        return values
    }

    /**
     * Set this textSize style
     *
     * @param size Size to apply on text
     */
    fun withTextSize(size: Float) {
        values[TextStyle.SIZE] = size
    }

    val textSize: Float
        get() = values[TextStyle.SIZE] as Float

    /**
     * Set this color style
     *
     * @param color Color to apply on text
     */
    fun withTextColor(color: Int) {
        values[TextStyle.COLOR] = color
    }

    val textColor: Int
        get() = values[TextStyle.COLOR] as Int

    /**
     * Set this [Typeface] style
     *
     * @param textTypeface TypeFace to apply on text
     */
    fun withTextFont(textTypeface: Typeface) {
        values[TextStyle.FONT_FAMILY] = textTypeface
    }

    val textFont: Typeface?
        get() = values[TextStyle.FONT_FAMILY] as Typeface?

    /**
     * Set this gravity style
     *
     * @param gravity Gravity style to apply on text
     */
    fun withGravity(gravity: Int) {
        values[TextStyle.GRAVITY] = gravity
    }

    /**
     * Set this background color
     *
     * @param background Background color to apply on text, this method overrides the preview set on [TextStyleBuilder.withBackgroundDrawable]
     */
    fun withBackgroundColor(background: Int) {
        values[TextStyle.BACKGROUND] = background
    }

    /**
     * Set this background [Drawable], this method overrides the preview set on [TextStyleBuilder.withBackgroundColor]
     *
     * @param bgDrawable Background drawable to apply on text
     */
    fun withBackgroundDrawable(bgDrawable: Drawable) {
        values[TextStyle.BACKGROUND] = bgDrawable
    }

    /**
     * Set this textAppearance style
     *
     * @param textAppearance Text style to apply on text
     */
    fun withTextAppearance(textAppearance: Int) {
        values[TextStyle.TEXT_APPEARANCE] = textAppearance
    }

    fun withStrokeWidthColor(strokeWidthColor: StrokeProperties) {
        values[TextStyle.STROKE_WIDTH_COLOR] = strokeWidthColor
    }

    val strokeWidthColor: StrokeProperties?
        get() = values[TextStyle.STROKE_WIDTH_COLOR] as StrokeProperties?

    fun withInnerShadow(strokeInnerShadow: StrokeProperties) {
        values[TextStyle.STROKE_INNER_SHADOW] = strokeInnerShadow
    }

    fun withOuterShadow(strokeOuterShadow: StrokeProperties) {
        values[TextStyle.STROKE_OUTER_SHADOW] = strokeOuterShadow
    }

    fun withTextAlign(textAlign: Int) {
        values[TextStyle.TEXT_ALIGN] = textAlign
    }

    val textAlign: Int
        get() = values[TextStyle.TEXT_ALIGN] as Int

    fun withTextJustify(textJustify: Int) {
        values[TextStyle.TEXT_JUSTIFY] = textJustify
    }

    val textJustify: Int
        get() = values[TextStyle.TEXT_JUSTIFY] as Int

    /**
     * Method to apply all the style setup on this Builder}
     *
     * @param textView TextView to apply the style
     */
    fun applyStyle(textView: MagicTextView) {
        for ((key, value) in values) {
            when (key) {
                TextStyle.SIZE -> {
                    val size = value as Float
                    applyTextSize(textView, size)
                }
                TextStyle.COLOR -> {
                    val color = value as Int
                    applyTextColor(textView, color)
                }
                TextStyle.FONT_FAMILY -> {
                    val typeface = value as Typeface
                    applyFontFamily(textView, typeface)
                }
                TextStyle.GRAVITY -> {
                    val gravity = value as Int
                    applyGravity(textView, gravity)
                }
                TextStyle.BACKGROUND -> {
                    if (value is Drawable) {
                        applyBackgroundDrawable(textView, value)
                    } else if (value is Int) {
                        applyBackgroundColor(textView, value)
                    }
                }
                TextStyle.TEXT_APPEARANCE -> {
                    if (value is Int) {
                        applyTextAppearance(textView, value)
                    }
                }
                TextStyle.STROKE_WIDTH_COLOR -> {
                    if (value is StrokeProperties) {
                        val strokeWidthColor: StrokeProperties = value
                        applyStokeWidth(textView, strokeWidthColor)
                    }
                }
                TextStyle.TEXT_ALIGN -> {
                    if (value is Int) {
                        applyTextAlign(textView, value)
                    }
                }
                TextStyle.TEXT_JUSTIFY -> {
                    if (value is Int) {
                        applyTextJustify(textView, value)
                    }
                }
                else->{}
            }
        }
    }

    private fun applyTextSize(textView: MagicTextView, size: Float) {
        textView.textSize = size
    }

    private fun applyTextColor(textView: MagicTextView, color: Int) {
        textView.setTextColor(color)
    }

    private fun applyFontFamily(textView: MagicTextView, typeface: Typeface?) {
        textView.typeface = typeface
    }

    private fun applyGravity(textView: MagicTextView, gravity: Int) {
        textView.gravity = gravity
    }

    private fun applyBackgroundColor(textView: MagicTextView, color: Int) {
        textView.setBackgroundColor(color)
    }

    private fun applyBackgroundDrawable(textView: MagicTextView, bg: Drawable?) {
        textView.background = bg
    }

    private fun applyTextAppearance(textView: MagicTextView, styleAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(styleAppearance)
        } else {
            textView.setTextAppearance(textView.context, styleAppearance)
        }
    }

    private fun applyStokeWidth(textView: MagicTextView, strokeWidthColor: StrokeProperties) {
        textView.setStroke(strokeWidthColor.width, strokeWidthColor.color)
    }

    private fun applyInnerShadow(textView: MagicTextView, strokeInnerShadow: StrokeProperties) {
        val shadow = strokeInnerShadow.innerShadow
        textView.addInnerShadow(shadow.r, shadow.dx, shadow.dy, shadow.color)
    }

    private fun applyOuterShadow(textView: MagicTextView, strokeOuterShadow: StrokeProperties) {
        val shadow = strokeOuterShadow.outerShadow
        textView.addOuterShadow(shadow.r, shadow.dx, shadow.dy, shadow.color)
    }

    private fun applyTextAlign(textView: MagicTextView, textAlign: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) textView.justificationMode =
            textView.justificationMode
        textView.textAlignment = textAlign
    }

    private fun applyTextJustify(textView: MagicTextView, justification: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) textView.justificationMode =
            justification
    }

    /**
     * Enum to maintain current supported style properties used on on [PhotoEditor.addText] and [PhotoEditor.editText]
     */
    private enum class TextStyle(val property: String) {
        SIZE("TextSize"), COLOR("TextColor"), GRAVITY("Gravity"), FONT_FAMILY("FontFamily"), BACKGROUND(
            "Background"
        ),
        TEXT_APPEARANCE("TextAppearance"), STROKE_WIDTH_COLOR("StrokeWidthColor"), STROKE_INNER_SHADOW(
            "StrokeInnerShadow"
        ),
        STROKE_OUTER_SHADOW("StrokeOuterShadow"), TEXT_ALIGN("TextAlign"), TEXT_JUSTIFY("TextJustify");

    }
}