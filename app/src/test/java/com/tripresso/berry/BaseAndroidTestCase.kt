package com.tripresso.berry

import android.content.Context
import com.tripresso.berry.di.TestBerryComponent
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = TestBerryApplication::class)
abstract class BaseAndroidTestCase {

  protected val context: Context = RuntimeEnvironment.systemContext

  protected val application: TestBerryApplication =
    RuntimeEnvironment.application as TestBerryApplication

  protected val component: TestBerryComponent = application.component
}