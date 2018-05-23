package com.tripresso.berry.view.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.tripresso.berry.R
import com.tripresso.berry.util.activity.result.startIntent
import com.tripresso.berry.util.view.clickThrottleFirst
import com.tripresso.berry.view.InjectableBaseActivity
import com.tripresso.berry.view.TEST_RESULT
import com.tripresso.berry.view.test.TestRequestPermissionActivity
import com.tripresso.berry.view.test.TestResultActivity
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.crashButton
import kotlinx.android.synthetic.main.activity_main.imageView
import kotlinx.android.synthetic.main.activity_main.permissionButton
import kotlinx.android.synthetic.main.activity_main.resultButton
import timber.log.Timber

class MainActivity : InjectableBaseActivity() {

  private val mainViewModel: MainViewModel by lazy {
    provideViewModel(MainViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    imageView.setImageUrl("https://pic.pimg.tw/orangejohn/1468471509-2846652721_n.jpg?v=1468471512")
    resultButton.clickThrottleFirst()
      .subscribe {
        startIntent(Intent(this, TestResultActivity::class.java))
          .subscribe { result ->
            if (result.resultCode == Activity.RESULT_OK) {
              Timber.d(">>>>>>> TestResult -> ${result.data?.getStringExtra(TEST_RESULT)}")
            }
          }
          .addTo(compositeDisposable)
      }
    permissionButton.clickThrottleFirst()
      .subscribe {
        startActivity(Intent(this, TestRequestPermissionActivity::class.java))
      }
    crashButton.clickThrottleFirst()
      .subscribe {
        Crashlytics.getInstance().crash()
      }
    mainViewModel.fetchPost(1)
      .subscribe { postDto -> Timber.d(">>>>>>> $postDto") }
      .addTo(compositeDisposable)
  }
}
