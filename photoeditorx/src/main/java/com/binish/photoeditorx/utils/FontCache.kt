package com.binish.photoeditorx.utils

import android.content.Context
import android.graphics.Typeface

class FontCache {
    companion object {
        private val sTypefaceCache = HashMap<String, Typeface>()

        fun getOTF(context: Context, font: String): Typeface {
            if (!sTypefaceCache.containsKey(font)) {
                val tf = Typeface.createFromAsset(
                        context.applicationContext.assets, "fonts/%s.otf".format(font))
                sTypefaceCache[font] = tf
            }
            return sTypefaceCache[font]!!
        }

    }
}

