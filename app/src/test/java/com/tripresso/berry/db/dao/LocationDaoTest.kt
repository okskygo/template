package com.tripresso.berry.db.dao

import com.tripresso.berry.BaseAndroidTestCase
import com.tripresso.berry.db.BerryDatabase
import com.tripresso.berry.dto.LocationDto
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date
import javax.inject.Inject

class LocationDaoTest : BaseAndroidTestCase() {

  @Inject
  lateinit var locationDao: LocationDao

  @Inject
  lateinit var database: BerryDatabase

  @Before
  fun inject() {
    component.inject(this)
  }

  @After
  fun closeDb() {
    database.close()
  }

  @Test
  fun writeLocationAndReadInList() {
    val testLocationDto = insertTestLocationDto()
    locationDao.queryLocations()
      .map { it[0] }
      .test()
      .assertValue { it.createTime == testLocationDto.createTime }
      .assertValue { it.latitude == testLocationDto.latitude }
      .assertValue { it.longitude == testLocationDto.longitude }
  }

  private fun insertTestLocationDto(): LocationDto {
    val locationDto = LocationDto(1.111,
                                  2.222,
                                  Date().apply { time = 12345 })
    locationDao.insert(locationDto)
    return locationDto
  }

}