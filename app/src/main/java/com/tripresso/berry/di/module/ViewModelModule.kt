package com.tripresso.berry.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.silver.cat.nilo.config.dagger.ViewModelFactory
import com.silver.cat.nilo.config.dagger.ViewModelKey
import com.tripresso.berry.view.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(MainViewModel::class)
  fun bindSettingViewModel(mainViewModel: MainViewModel): ViewModel

  @Binds
  fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}