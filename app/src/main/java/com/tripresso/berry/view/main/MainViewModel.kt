package com.tripresso.berry.view.main

import android.arch.lifecycle.ViewModel
import com.tripresso.berry.dto.TestPostDto
import com.tripresso.berry.repository.TestRepository
import com.tripresso.berry.util.result.Result
import com.tripresso.berry.util.result.toResult
import io.reactivex.Flowable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val testRepository: TestRepository) : ViewModel() {

  fun fetchPost(id: Int): Flowable<Result<TestPostDto>> = testRepository.fetchPost(id).toResult()
}
