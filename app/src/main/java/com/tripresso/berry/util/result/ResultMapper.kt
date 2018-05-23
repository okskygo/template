package com.tripresso.berry.util.result

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

fun <T> Flowable<T>.toResult(): Flowable<Result<T>> {
  return compose { item: Flowable<T> ->
    item
      .map { Result.success(it) }
      .onErrorReturn { e ->
        Result.failure(e.message ?: "unknown", e)
      }
      .observeOn(AndroidSchedulers.mainThread())
      .startWith(Result.inProgress())
  }
}

fun <T> Observable<T>.toResult(): Observable<Result<T>> {
  return compose { item ->
    item
      .map { Result.success(it) }
      .onErrorReturn { e ->
        Result.failure(e.message ?: "unknown", e)
      }
      .observeOn(AndroidSchedulers.mainThread())
      .startWith(Result.inProgress())
  }
}

fun <T> Single<T>.toResult(): Observable<Result<T>> {
  return toObservable().toResult()
}

fun <T> Completable.toResult(): Observable<Result<T>> {
  return toObservable<T>().toResult()
}