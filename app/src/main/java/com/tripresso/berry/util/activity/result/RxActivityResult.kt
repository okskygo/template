/*
 * Copyright 2016 Víctor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tripresso.berry.util.activity.result

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

@SuppressLint("StaticFieldLeak")
lateinit var activitiesLifecycle: ActivitiesLifecycleCallbacks

@JvmOverloads fun <T : Context> T.startIntentSender(intentSender: IntentSender,
  fillInIntent: Intent?,
  flagsMask: Int,
  flagsValues: Int,
  extraFlags: Int,
  options: Bundle? = null): Observable<Result<T>> {

  val requestIntentSender = RequestIntentSender(intentSender,
    fillInIntent, flagsMask, flagsValues, extraFlags, options)
  return RxActivityResult(this).startHolderActivity(requestIntentSender, null)

}

@JvmOverloads fun <T : Context> T.startIntent(intent: Intent,
  onPreResult: OnPreResult<*>? = null): Observable<Result<T>> {
  return RxActivityResult(this).startHolderActivity(Request(intent), onPreResult)
}

@JvmOverloads fun <T : Fragment> T.startIntentSender(intentSender: IntentSender,
  fillInIntent: Intent?,
  flagsMask: Int,
  flagsValues: Int,
  extraFlags: Int,
  options: Bundle? = null): Observable<Result<T>> {

  val requestIntentSender = RequestIntentSender(intentSender,
    fillInIntent, flagsMask, flagsValues, extraFlags, options)
  return RxActivityResult(this).startHolderActivity(requestIntentSender, null)

}

@JvmOverloads fun <T : Fragment> T.startIntent(intent: Intent,
  onPreResult: OnPreResult<*>? = null): Observable<Result<T>> {
  return RxActivityResult(this).startHolderActivity(Request(intent), onPreResult)
}

class RxActivityResult<T>(val t: T) {
  private val subject = PublishSubject.create<Result<T>>()

  fun startHolderActivity(request: Request,
    onPreResult: OnPreResult<*>?): Observable<Result<T>> {

    val onResult = if (t is Activity) onResultActivity() else onResultFragment()
    request.onResult = onResult
    request.onPreResult = onPreResult

    HolderActivity.request = request

    activitiesLifecycle.getOLiveActivity().subscribe { activity ->
      activity.startActivity(Intent(activity, HolderActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
    }

    return subject
  }

  @Suppress("UNCHECKED_CAST")
  private fun onResultActivity(): OnResult {
    return object : OnResult {
      override fun response(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activitiesLifecycle.liveActivity == null) return

        //If true it means some other activity has been stacked as a secondary process.
        //Wait until the current activity be the target activity
        if (activitiesLifecycle.liveActivity?.javaClass != (t as? Any)?.javaClass) {
          return
        }

        val activity = activitiesLifecycle.liveActivity as? T ?: return
        subject.onNext(Result(activity, requestCode, resultCode, data))
        subject.onComplete()
      }

      override fun error(throwable: Throwable) {
        subject.onError(throwable)
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun onResultFragment(): OnResult {
    return object : OnResult {
      override fun response(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activitiesLifecycle.liveActivity == null) return

        val fragmentActivity =
          activitiesLifecycle.liveActivity as? FragmentActivity ?: return

        val targetFragment =
          getTargetFragment(fragmentActivity.supportFragmentManager.fragments)

        val fragment = targetFragment as? T ?: return

        subject.onNext(Result(fragment, requestCode, resultCode, data))
        subject.onComplete()

        //If code reaches this point it means some other activity has been stacked as a secondary process.
        //Do nothing until the current activity be the target activity to get the associated fragment
      }

      override fun error(throwable: Throwable) {
        subject.onError(throwable)
      }
    }
  }

  fun getTargetFragment(fragments: List<Fragment>?): Fragment? {
    if (fragments == null) return null

    for (fragment in fragments) {
      if (fragment.isVisible && fragment.javaClass == (t as? Any)?.javaClass) {
        return fragment
      } else if (fragment.isAdded) {
        val childFragments = fragment.childFragmentManager.fragments
        val candidate = getTargetFragment(childFragments)
        if (candidate != null) return candidate
      }
    }

    return null
  }

}