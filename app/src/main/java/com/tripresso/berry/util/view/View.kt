package com.tripresso.berry.util.view

import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun View.clickThrottleFirst(): Observable<Unit> {
  return this.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
}