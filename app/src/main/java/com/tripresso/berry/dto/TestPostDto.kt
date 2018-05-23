package com.tripresso.berry.dto

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import com.tripresso.berry.util.retrofit.jackson.JsonDto
import kotlinx.android.parcel.Parcelize

@JsonDto
@Parcelize
data class TestPostDto(
  @JsonProperty("userId") val userId: Int,
  @JsonProperty("id") val id: Int,
  @JsonProperty("title") val title: String,
  @JsonProperty("body") val body: String
) : Parcelable