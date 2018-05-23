package com.tripresso.berry.widget.snackbar

import android.app.Activity
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_LONG
import android.view.View

fun Activity.showSnackbar(@StringRes messageRes: Int, duration: Int = LENGTH_LONG) {
  showSnackbar(getString(messageRes), duration)
}

fun Activity.showSnackbar(message: String, duration: Int = LENGTH_LONG) {
  findViewById<View>(android.R.id.content)?.let {
    Snackbar.make(it, message, duration).show()
  }
}