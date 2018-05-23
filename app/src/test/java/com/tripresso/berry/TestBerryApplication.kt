package com.tripresso.berry

import android.app.Activity
import android.app.Application
import com.tripresso.berry.di.DaggerTestBerryComponent
import com.tripresso.berry.di.TestBerryComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class TestBerryApplication : Application(), HasActivityInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  lateinit var component: TestBerryComponent

  override fun onCreate() {
    super.onCreate()
    component = DaggerTestBerryComponent.builder()
      .application(this)
      .build()
    component.inject(this)
  }

  override fun activityInjector(): AndroidInjector<Activity> {
    return dispatchingAndroidInjector
  }

}