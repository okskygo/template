package com.tripresso.berry.view

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment : Fragment() {

  private val compositeDisposableDelegate = lazy { CompositeDisposable() }
  protected val compositeDisposable by compositeDisposableDelegate

  override fun onDestroyView() {
    super.onDestroyView()
    if (compositeDisposableDelegate.isInitialized()) {
      compositeDisposable.dispose()
    }
  }
}