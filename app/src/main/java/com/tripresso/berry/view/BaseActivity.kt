package com.tripresso.berry.view

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import com.tripresso.berry.di.Injectable
import com.tripresso.berry.util.BerryErrorHandler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

  @Inject
  lateinit var errorHandler: BerryErrorHandler

  private val compositeDisposableDelegate = lazy { CompositeDisposable() }
  protected val compositeDisposable by compositeDisposableDelegate

  override fun onDestroy() {
    super.onDestroy()
    if (compositeDisposableDelegate.isInitialized()) {
      compositeDisposable.dispose()
    }
  }

}