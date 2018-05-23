package com.tripresso.berry.util.activity.result

/**
 * Created by victor on 16/03/16.
 */
interface Locale {
  companion object {
    const val RX_ACTIVITY_RESULT_NOT_REGISTER =
      "You must call RxActivityResult.register(application) before attempting to use startIntent"
  }
}
