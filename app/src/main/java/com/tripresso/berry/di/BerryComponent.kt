package com.tripresso.berry.di

import android.app.Application
import com.tripresso.berry.BerryApplication
import com.tripresso.berry.di.module.ActivityBuilderModule
import com.tripresso.berry.di.module.AndroidInjectionModule
import com.tripresso.berry.di.module.BerryModule
import com.tripresso.berry.di.module.DatabaseModule
import com.tripresso.berry.di.module.FragmentBuilderModule
import com.tripresso.berry.di.module.RetrofitModule
import com.tripresso.berry.di.module.ServiceBuilderModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
  (AndroidInjectionModule::class),
  (BerryModule::class),
  (ActivityBuilderModule::class),
  (ServiceBuilderModule::class),
  (FragmentBuilderModule::class),
  (RetrofitModule::class),
  (DatabaseModule::class)
])
interface BerryComponent {

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(application: Application): Builder

    fun build(): BerryComponent

  }

  fun inject(application: BerryApplication)
}
