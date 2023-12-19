package com.dicoding.ketekgo

import android.view.View
import android.widget.EditText
import android.widget.ProgressBar

fun isLoading(state: Boolean, loading: ProgressBar) {
    if (state) loading.visibility = View.VISIBLE else loading.visibility = View.INVISIBLE
}

fun checkField(textField: EditText): Boolean {
    val valid: Boolean
    if (textField.text.toString().isEmpty()){
        textField.error = "Error"
        valid = false
    } else {
        valid = true
    }
    return valid
}