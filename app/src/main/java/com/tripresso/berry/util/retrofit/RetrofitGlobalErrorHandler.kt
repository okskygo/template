package com.tripresso.berry.util.retrofit

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class RetrofitGlobalErrorHandler(private val context: Context) {

  private val errorMessageSubject: PublishSubject<String> = PublishSubject.create()

  init {
    subscribeErrorToShowToast(context)
  }

  @SuppressLint("ShowToast")
  private fun subscribeErrorToShowToast(context: Context) {
    errorMessageSubject.subscribeOn(AndroidSchedulers.mainThread())
      .observeOn(AndroidSchedulers.mainThread())
      .map(object : Function<String, String> {
        private var previous: String? = null
        private var counter: Int = 0

        @Throws(Exception::class)
        override fun apply(msg: String): String {
          val old = previous
          previous = msg
          if (old != null && old == msg) {
            counter++
            return String.format(Locale.ENGLISH, "%s (%d)", msg, counter)
          } else {
            counter = 0
            return msg
          }
        }
      })
      .map { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT) }
      .subscribe(object : Consumer<Toast> {

        private var previous: Toast? = null

        @Throws(Exception::class)
        override fun accept(toast: Toast) {
          previous?.cancel()
          toast.show()
          previous = toast
        }
      })
  }

  fun tryCatch(throwable: Throwable): Boolean {
    val converted = tryConvertRetrofitException(context, throwable)
    converted?.let { errorMessageSubject.onNext(it) }
    return converted != null
  }

  companion object {

    fun tryConvertRetrofitException(context: Context, throwable: Throwable): String? {

      if (throwable !is RetrofitWrappedException) {
        return null
      }

      val cause = throwable.cause ?: return null

      if (cause is IOException) {
        return cause.message
      } else if (cause is HttpException) {
        val code = cause.response().code()
        return cause.message
      }

      return null
    }
  }

}