package com.tripresso.berry.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.tripresso.berry.db.dao.LocationDao
import com.tripresso.berry.db.migration.Migration1To2
import com.tripresso.berry.dto.LocationDto

@Database(entities = [(LocationDto::class)], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class BerryDatabase : RoomDatabase() {

  abstract fun locationDao(): LocationDao

  companion object {
    @JvmField
    val MIGRATION_1_2 = Migration1To2()
  }
}