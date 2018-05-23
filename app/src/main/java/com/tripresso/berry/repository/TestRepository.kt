package com.tripresso.berry.repository

import com.tripresso.berry.dto.TestPostDto
import com.tripresso.berry.service.TestService
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepository @Inject constructor(private val testService: TestService) {

  fun fetchPost(id: Int): Flowable<TestPostDto> = testService.fetchPost(id)

}