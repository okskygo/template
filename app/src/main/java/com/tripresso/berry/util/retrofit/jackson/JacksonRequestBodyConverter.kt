package com.tripresso.berry.util.retrofit.jackson

import com.fasterxml.jackson.databind.ObjectWriter
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.IOException

internal class JacksonRequestBodyConverter<T>(private val adapter: ObjectWriter) :
  Converter<T, RequestBody> {

  @Throws(IOException::class)
  override fun convert(value: T): RequestBody {
    return RequestBody.create(MEDIA_TYPE, adapter.writeValueAsBytes(value))
  }

  companion object {
    private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
  }
}