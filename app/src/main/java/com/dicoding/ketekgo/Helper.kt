package com.dicoding.ketekgo

import android.view.View
import android.widget.ProgressBar

fun isLoading(state: Boolean, loading: ProgressBar) {
    if (state) loading.visibility = View.VISIBLE else loading.visibility = View.INVISIBLE
}