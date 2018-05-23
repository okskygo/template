package com.tripresso.berry.util.activity.result

import android.content.Intent
import io.reactivex.Observable

interface OnPreResult<T> {
  fun response(requestCode: Int, resultCode: Int, data: Intent?): Observable<T>
}
