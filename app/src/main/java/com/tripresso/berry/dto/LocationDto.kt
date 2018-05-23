package com.tripresso.berry.dto

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.tripresso.berry.util.retrofit.jackson.JsonDto
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.Date

@JsonDto
@Parcelize
@Entity(tableName = "location")
data class LocationDto(
  @JsonProperty("longitude") val longitude: Double,
  @JsonProperty("latitude") val latitude: Double,
  @JsonProperty("createTime") val createTime: Date
) : Parcelable {
  @PrimaryKey(autoGenerate = true) @IgnoredOnParcel @JsonIgnore var id: Int? = null
}