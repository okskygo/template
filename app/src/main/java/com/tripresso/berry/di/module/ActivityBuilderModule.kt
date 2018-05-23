package com.tripresso.berry.di.module

import com.tripresso.berry.view.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilderModule {

  @ContributesAndroidInjector
  fun contributeMainActivity(): MainActivity

}