package com.dicoding.submission_intermediate_storyapp2.ui.custom_ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.submission_intermediate_storyapp2.R

class CustomButton: AppCompatButton {

    private lateinit var backgroundEnabled: Drawable
    private lateinit var backgroundDisabled: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int) : super(
        context,
        attrs,
        defStyleAttrs
    ) {
        init()
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, R.color.white)
        backgroundEnabled = ContextCompat.getDrawable(context, R.drawable.bg_button_enabled) as Drawable
        backgroundDisabled = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled) as Drawable
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) backgroundEnabled else backgroundDisabled

        textSize = 12f
        setTextColor(txtColor)
        gravity = Gravity.CENTER
        isAllCaps = false
        letterSpacing = 0.1f
    }

}