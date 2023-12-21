package com.dicoding.ketekgo.data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.ketekgo.R

@SuppressLint("ClickableViewAccessibility")
class PasswordEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(
    context, attrs
) {

    private var eyeIcon: Drawable? = null
    private var isPasswordVisible = false

    init {
        eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye)
        eyeIcon?.setBounds(0, 0, eyeIcon!!.intrinsicWidth, eyeIcon!!.intrinsicHeight)

        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, eyeIcon, null)

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableBounds = eyeIcon?.bounds
                if (event.rawX >= (right - drawableBounds?.width()!!)) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        inputType = if (isPasswordVisible) {
            android.text.InputType.TYPE_CLASS_TEXT
        } else {
            android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        updateEyeIcon()
    }

    private fun updateEyeIcon() {
        val drawableRes = if (isPasswordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye
        eyeIcon = ContextCompat.getDrawable(context, drawableRes)
        eyeIcon?.setBounds(0, 0, eyeIcon!!.intrinsicWidth, eyeIcon!!.intrinsicHeight)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, eyeIcon, null)
    }
}