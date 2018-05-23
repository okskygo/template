package com.tripresso.berry.util.retrofit.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class JacksonRootWrapConverterFactory private constructor(private val mapper: ObjectMapper) :
  Converter.Factory() {

  private class RootWrapperType(private val type: Type) : ParameterizedType {

    override fun getActualTypeArguments(): Array<Type> {
      return arrayOf(type)
    }

    override fun getOwnerType(): Type? {
      return null
    }

    override fun getRawType(): Type {
      return RootWrapperDto::class.java
    }
  }

  override fun responseBodyConverter(type: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit?): Converter<ResponseBody, *> {
    val javaType = mapper.typeFactory.constructType(type)
    val reader = mapper.readerFor(javaType)
    return JacksonResponseBodyConverter<Any>(reader)
//    val wrappedType = RootWrapperType(type)
//    val javaType = mapper.typeFactory.constructType(wrappedType)
//    val reader = mapper.readerFor(javaType)
//    return JacksonResponseBodyConverter<Any>(reader)
  }

  override fun requestBodyConverter(type: Type,
    parameterAnnotations: Array<Annotation>,
    methodAnnotations: Array<Annotation>,
    retrofit: Retrofit): Converter<*, RequestBody> {
    val javaType = mapper.typeFactory.constructType(type)
    val writer = mapper.writerFor(javaType)
    return JacksonRequestBodyConverter<Any>(writer)
  }

  companion object {

    fun create(mapper: ObjectMapper = ObjectMapper()): JacksonRootWrapConverterFactory {
      return JacksonRootWrapConverterFactory(mapper)
    }
  }
}