package com.tripresso.berry.view

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.tripresso.berry.di.Injectable
import javax.inject.Inject

@SuppressLint("Registered")
open class InjectableBaseActivity : BaseActivity(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  fun <T : ViewModel> provideViewModel(modelClass: Class<T>): T =
    ViewModelProviders.of(this, viewModelFactory).get(modelClass)
}