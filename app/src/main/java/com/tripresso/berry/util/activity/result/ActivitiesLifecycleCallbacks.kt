package com.tripresso.berry.util.activity.result

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ActivitiesLifecycleCallbacks(val application: Application) {
  @Volatile var liveActivity: Activity? = null
  var activityLifecycleCallbacks: ActivityLifecycleCallbacks? = null

  /**
   * Emits just one time a valid reference to the current activity
   *
   * @return the current activity
   */
  @Volatile var emitted = false

  /**
   * Emits just one time a valid reference to the current activity
   *
   * @return the current activity
   */
  fun getOLiveActivity(): Observable<Activity> {
    emitted = false
    return Observable.interval(50, 50, TimeUnit.MILLISECONDS)
      .map{
        liveActivity ?: 0
      }
      .takeWhile { candidate ->
        var continueEmitting = true
        if (emitted) continueEmitting = false
        if (candidate is Activity) emitted = true
        continueEmitting
      }
      .filter { candidate -> candidate is Activity }
      .map { activity -> activity as Activity }
  }

  init {
    registerActivityLifeCycle()
  }

  private fun registerActivityLifeCycle() {
    if (activityLifecycleCallbacks != null) {
      application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        liveActivity = activity
      }

      override fun onActivityStarted(activity: Activity) {}

      override fun onActivityResumed(activity: Activity) {
        liveActivity = activity
      }

      override fun onActivityPaused(activity: Activity) {
        liveActivity = null
      }

      override fun onActivityStopped(activity: Activity) {}

      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

      override fun onActivityDestroyed(activity: Activity) {}
    }

    application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
  }
}
