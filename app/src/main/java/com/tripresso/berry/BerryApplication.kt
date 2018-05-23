package com.tripresso.berry

import android.app.Activity
import android.app.Application
import android.app.Service
import com.tripresso.berry.di.AppInjector
import com.tripresso.berry.util.BerryErrorHandler
import com.tripresso.berry.util.activity.result.ActivitiesLifecycleCallbacks
import com.tripresso.berry.util.activity.result.activitiesLifecycle
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

class BerryApplication : Application(), HasActivityInjector, HasServiceInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  @Inject
  lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

  @Inject
  lateinit var errorHandler: BerryErrorHandler

  override fun onCreate() {
    super.onCreate()
    AppInjector.init(this)
    initRxErrorHandler()
    initLogger()
    initRxActivityResult()
  }

  private fun initRxActivityResult() {
    activitiesLifecycle = ActivitiesLifecycleCallbacks(this)
  }

  private fun initRxErrorHandler() {
    RxJavaPlugins.setErrorHandler { throwable ->
      errorHandler.handle(throwable)
    }
  }

  private fun initLogger() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun activityInjector(): AndroidInjector<Activity> {
    return dispatchingAndroidInjector
  }

  override fun serviceInjector(): AndroidInjector<Service> {
    return dispatchingServiceInjector
  }
}