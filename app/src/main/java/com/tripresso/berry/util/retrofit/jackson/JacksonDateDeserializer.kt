package com.tripresso.berry.util.retrofit.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JacksonDateDeserializer : JsonDeserializer<Date>() {

  @Throws(IOException::class, JsonProcessingException::class)
  override fun deserialize(jsonparser: JsonParser,
    deserializationcontext: DeserializationContext): Date {

    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = jsonparser.text
    try {
      return format.parse(date)
    } catch (e: ParseException) {
      throw RuntimeException(e)
    }
  }

}