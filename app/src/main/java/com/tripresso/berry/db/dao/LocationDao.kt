package com.tripresso.berry.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.tripresso.berry.dto.LocationDto
import io.reactivex.Single

@Dao
interface LocationDao {

  @Query("SELECT * FROM location")
  fun queryLocations(): Single<List<LocationDto>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(locationDto: LocationDto)

}