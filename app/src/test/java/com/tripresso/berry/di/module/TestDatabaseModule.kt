package com.tripresso.berry.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.tripresso.berry.db.BerryDatabase
import com.tripresso.berry.db.dao.LocationDao
import com.tripresso.berry.di.ForApplication
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class TestDatabaseModule {

  @Singleton
  @Provides
  @Inject
  fun provideBerryDatabase(@ForApplication context: Context): BerryDatabase {
    return Room.inMemoryDatabaseBuilder(context, BerryDatabase::class.java)
      .allowMainThreadQueries()
      .build()
  }

  @Singleton
  @Provides
  @Inject
  fun provideDramaDao(berryDatabase: BerryDatabase): LocationDao {
    return berryDatabase.locationDao()
  }

}