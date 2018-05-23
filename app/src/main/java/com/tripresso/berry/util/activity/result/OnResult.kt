package com.tripresso.berry.util.activity.result

import android.content.Intent
import java.io.Serializable

interface OnResult : Serializable {
  fun response(requestCode: Int, resultCode: Int, data: Intent?)

  fun error(throwable: Throwable)
}
