package com.tripresso.berry.di.module

import com.tripresso.berry.view.empty.EmptyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilderModule {

  @ContributesAndroidInjector
  fun contributeEmptyFragment(): EmptyFragment

}