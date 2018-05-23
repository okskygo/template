package com.tripresso.berry.db.migration

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.Room
import android.arch.persistence.room.testing.MigrationTestHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tripresso.berry.db.BerryDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class Migration1To2Test {

  @get:Rule
  val testHelper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                                       BerryDatabase::class.java.canonicalName,
                                       FrameworkSQLiteOpenHelperFactory())

  @Test
  fun migrate() {
    testHelper.createDatabase("berry-db", 1).use {
      insertRepo(it)
    }
    testHelper.runMigrationsAndValidate("berry-db", 2, true, BerryDatabase.MIGRATION_1_2)
    val migratedRoomDatabase = getMigratedRoomDatabase()
    migratedRoomDatabase.locationDao()
      .queryLocations()
      .map { it[0] }
      .test()
      .assertValue { it.longitude == 11.1111 }
      .assertValue { it.latitude == 22.2222 }
      .assertValue { it.createTime == Date().apply { time = 0 } }
    testHelper.closeWhenFinished(migratedRoomDatabase)
  }

  private fun insertRepo(database: SupportSQLiteDatabase) {
    database.insert("location",
                    SQLiteDatabase.CONFLICT_REPLACE,
                    ContentValues().apply {
                      put("longitude", 11.1111)
                      put("latitude", 22.2222)
                    })
  }

  private fun getMigratedRoomDatabase(): BerryDatabase =
    Room.databaseBuilder(InstrumentationRegistry.getTargetContext(),
                         BerryDatabase::class.java,
                         "berry-db")
      .addMigrations(BerryDatabase.MIGRATION_1_2)
      .allowMainThreadQueries()
      .build()

}