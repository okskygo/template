package com.tripresso.berry.service.raw

import com.tripresso.berry.dto.TestPostDto
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface RawTestService {

  @GET("posts/{id}")
  fun fetchPost(@Path("id") id: Int): Flowable<TestPostDto>

}