package com.tripresso.berry.di.module

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.tripresso.berry.BuildConfig
import com.tripresso.berry.di.ForApplication
import com.tripresso.berry.service.raw.RawTestService
import com.tripresso.berry.util.retrofit.BerryRxJava2CallAdapterFactory
import com.tripresso.berry.util.retrofit.RetrofitGlobalErrorHandler
import com.tripresso.berry.util.retrofit.jackson.JacksonRootWrapConverterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.RxThreadFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Module
class RetrofitModule {

  @Provides
  @Singleton
  @Inject
  fun provideRetrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper,
    factory: BerryRxJava2CallAdapterFactory): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .addCallAdapterFactory(factory)
      .addConverterFactory(JacksonRootWrapConverterFactory.create(objectMapper))
      .baseUrl(BuildConfig.TEST_API)
      .build()
  }

  @Provides
  fun provideRetrofitScheduler(): Scheduler {
    val threadPoolExecutor = ThreadPoolExecutor(10,
      10,
      60L,
      TimeUnit.SECONDS,
      LinkedBlockingQueue<Runnable>(),
      RxThreadFactory("BerryRetrofitThreadPool"))
    threadPoolExecutor.allowCoreThreadTimeOut(true)
    return Schedulers.from(threadPoolExecutor)
  }

  @Provides
  @Singleton
  fun provideObjectMapper(): ObjectMapper {
    return ObjectMapper()
  }

  @Provides
  fun provideOkHttpClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    httpClient.addInterceptor {
      it.proceed(it.request()
        .newBuilder()
        .apply { header("Accept", "application/json") }
        .build())
    }

    val logLevelForDebug = HttpLoggingInterceptor.Level.BODY

    httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG)
      logLevelForDebug
    else
      HttpLoggingInterceptor.Level.NONE))
    return httpClient.build()
  }

  @Provides
  @Singleton
  @Inject
  fun provideRawTestService(retrofit: Retrofit): RawTestService {
    return retrofit.create(RawTestService::class.java)
  }

  @Provides
  @Singleton
  @Inject
  fun provideRetrofitGlobalErrorHandler(@ForApplication context: Context): RetrofitGlobalErrorHandler {
    return RetrofitGlobalErrorHandler(context)
  }

}
