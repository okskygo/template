package com.tripresso.berry.db.migration

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

class Migration1To2 : Migration(1, 2) {

  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("ALTER TABLE location ADD COLUMN createTime INTEGER NOT NULL DEFAULT 0")
  }

}