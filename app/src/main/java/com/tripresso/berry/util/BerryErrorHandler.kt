package com.tripresso.berry.util

import com.tripresso.berry.util.retrofit.RetrofitGlobalErrorHandler
import io.reactivex.exceptions.Exceptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BerryErrorHandler @Inject constructor(
  private val retrofitGlobalErrorHandler: RetrofitGlobalErrorHandler) {

  fun handle(throwable: Throwable) {
    if (retrofitGlobalErrorHandler.tryCatch(throwable)) {
      return
    }
    if (throwable is Exception) {
      throw throwable
    } else if (throwable is Error) {
      throw throwable
    }
    throw Exceptions.propagate(throwable)
  }

}