package com.dicoding.ketekgo

import android.content.Context
import android.content.SharedPreferences
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

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SurveyPreferences", Context.MODE_PRIVATE)

    fun isSurveyShown(): Boolean {
        return sharedPreferences.getBoolean("isSurveyShown", false)
    }

    fun setSurveyShown() {
        sharedPreferences.edit().putBoolean("isSurveyShown", true).apply()
    }
}
