package com.tripresso.berry.view.test

import android.Manifest
import android.os.Bundle
import com.tripresso.berry.R
import com.tripresso.berry.util.permission.requestPermissions
import com.tripresso.berry.util.view.clickThrottleFirst
import com.tripresso.berry.view.BaseActivity
import kotlinx.android.synthetic.main.activity_test_request_permission.requestButton
import timber.log.Timber

class TestRequestPermissionActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_test_request_permission)
    requestButton.clickThrottleFirst()
      .flatMap {
        requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE)
      }
      .subscribe {
        Timber.d(">>>>>>> permission isSuccess : $it")
      }
  }

}