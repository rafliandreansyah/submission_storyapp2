package com.dicoding.submission_intermediate_storyapp2.ui.custom_ui

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class CustomEmailEditText: AppCompatEditText, View.OnTouchListener {

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
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                 checkError(s)
            }

        })
    }

    private fun checkError(s: Editable) {
        val str = s.toString().trim();
        if (str.isNotEmpty()) {
            if (emailIsValid(str)) {
                error = null
            } else {
                error = "Email tidak valid"
            }
        } else {
            error = null
        }
    }

    private fun emailIsValid(text: String) : Boolean {
        return if (TextUtils.isEmpty(text)) {
            false;
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Masukkan email..."
        textSize = 12f
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }
}