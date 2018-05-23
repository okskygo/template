package com.tripresso.berry.di.module

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.support.v4.app.Fragment
import dagger.Module
import dagger.android.AndroidInjector
import dagger.internal.Beta
import dagger.multibindings.Multibinds

@Beta
@Module
abstract class AndroidInjectionModule private constructor() {

  @Multibinds
  internal abstract fun activityInjectorFactories()
      : Map<Class<out Activity>, AndroidInjector.Factory<out Activity>>

  @Multibinds
  internal abstract fun fragmentInjectorFactories()
      : Map<Class<out Fragment>, AndroidInjector.Factory<out Fragment>>

  @Multibinds
  internal abstract fun serviceInjectorFactories()
      : Map<Class<out Service>, AndroidInjector.Factory<out Service>>

  @Multibinds
  internal abstract fun broadcastReceiverInjectorFactories()
      : Map<Class<out BroadcastReceiver>, AndroidInjector.Factory<out BroadcastReceiver>>

  @Multibinds
  internal abstract fun contentProviderInjectorFactories()
      : Map<Class<out ContentProvider>, AndroidInjector.Factory<out ContentProvider>>

}