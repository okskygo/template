package com.tripresso.berry.util.retrofit.jackson

import com.fasterxml.jackson.databind.ObjectReader
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

internal class JacksonResponseBodyConverter<T>(private val adapter: ObjectReader) :
  Converter<ResponseBody, T> {

  @Throws(IOException::class)
  override fun convert(value: ResponseBody): T {
    value.use {
      return adapter.readValue<T>(value.charStream())
//      return adapter.readValue<RootWrapperDto<T>>(it.charStream()).data
    }
  }
}