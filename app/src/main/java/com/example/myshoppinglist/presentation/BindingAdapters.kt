package com.example.myshoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.example.myshoppinglist.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputName")
fun errorInputName(textInputLayout: TextInputLayout, isErrorInputName: Boolean) {
    val message = if (isErrorInputName) {
        textInputLayout.context.getString(R.string.invalid_name)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputCount")
fun errorInputCount(textInputLayout: TextInputLayout, errorInputCount: Boolean) {
    val message = if (errorInputCount) {
        textInputLayout.context.getString(R.string.invalid_count)
    } else {
        null
    }
    textInputLayout.error = message
}