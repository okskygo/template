package com.tripresso.berry.util.retrofit

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type
import javax.inject.Inject

class BerryRxJava2CallAdapterFactory @Inject constructor(scheduler: Scheduler) :
  CallAdapter.Factory() {

  private class RxCallAdapterWrapper<R : Any> internal constructor(
    private val wrapped: CallAdapter<R, *>)
    : CallAdapter<R, Any> {

    override fun responseType(): Type {
      return this.wrapped.responseType()
    }

    override fun adapt(call: Call<R>): Any {
      val result = wrapped.adapt(call)
      if (result is Completable) {
        return result
          .onErrorResumeNext({ throwable: Throwable ->
            Completable.error(RetrofitWrappedException(throwable))
          })
          .observeOn(AndroidSchedulers.mainThread())
      } else if (result is Flowable<*>) {
        return result
          .onErrorResumeNext({ throwable: Throwable ->
            Flowable.error(RetrofitWrappedException(throwable))
          })
          .observeOn(AndroidSchedulers.mainThread())
      } else if (result is Observable<*>) {
        return result
          .onErrorResumeNext({ throwable: Throwable ->
            Observable.error(RetrofitWrappedException(throwable))
          })
          .observeOn(AndroidSchedulers.mainThread())
      } else if (result is Maybe<*>) {
        return result
          .onErrorResumeNext({ throwable: Throwable ->
            Maybe.error(RetrofitWrappedException(throwable))
          })
          .observeOn(AndroidSchedulers.mainThread())
      } else if (result is Single<*>) {
        return result
          .onErrorResumeNext({ throwable: Throwable ->
            Single.error(RetrofitWrappedException(throwable))
          })
          .observeOn(AndroidSchedulers.mainThread())
      }
      return result
    }

  }

  private val delegate: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(
    scheduler)

  override fun get(returnType: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit): CallAdapter<*, *> {
    return RxCallAdapterWrapper(this.delegate.get(returnType, annotations, retrofit)!!)
  }
}

