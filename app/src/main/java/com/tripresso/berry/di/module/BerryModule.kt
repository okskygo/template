package com.tripresso.berry.di.module

import android.app.Application
import android.content.Context
import com.tripresso.berry.di.ForApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class BerryModule {

  @Provides
  @Singleton
  @ForApplication
  fun provideContext(application: Application): Context = application

}
