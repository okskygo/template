package com.tripresso.berry.service

import com.tripresso.berry.dto.TestPostDto
import com.tripresso.berry.service.raw.RawTestService
import io.reactivex.Flowable
import javax.inject.Inject

class TestService @Inject constructor(private val rawTestService: RawTestService) {

  fun fetchPost(id: Int): Flowable<TestPostDto> = rawTestService.fetchPost(id)

}