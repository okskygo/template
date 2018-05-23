package com.tripresso.berry.view.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tripresso.berry.R
import com.tripresso.berry.util.view.clickThrottleFirst
import com.tripresso.berry.view.BaseActivity
import com.tripresso.berry.view.TEST_RESULT
import kotlinx.android.synthetic.main.activity_test_result.finishButton

class TestResultActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_test_result)
    finishButton.clickThrottleFirst()
      .subscribe {
        setResult(Activity.RESULT_OK, Intent().apply {
          putExtra(TEST_RESULT, "test_result")
        })
        finish()
      }
  }

}