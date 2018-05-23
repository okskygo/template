package com.tripresso.berry.util.retrofit

import retrofit2.HttpException

class RetrofitWrappedException internal constructor(cause: Throwable) : RuntimeException(cause) {

  val is404NotFound: Boolean
    get() = if (cause is HttpException) {
      cause.code() == 404
    } else false

}
