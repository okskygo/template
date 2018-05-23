package com.tripresso.berry.util.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.tripresso.berry.util.activity.result.startIntent
import io.reactivex.Observable

private const val REQUEST_PERMISSION = "request_permission"
private const val RESULT_PERMISSION = "result_permission"
private const val REQUEST_CODE = 1132

class PermissionHolderActivity : Activity() {

  companion object {
    fun start(context: Context,
      permissions: Array<String>): Observable<PermissionResult> {
      val intent = Intent(context, PermissionHolderActivity::class.java)
        .apply {
          putExtra(REQUEST_PERMISSION, permissions)
        }

      return context.startIntent(intent)
        .map {
          if (it.resultCode == Activity.RESULT_OK && it.data?.extras != null) {
            it.data.extras.getSerializable(RESULT_PERMISSION) as PermissionResult
          } else {
            PermissionResult(false, permissions.toList(), emptyList())
          }
        }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val permissions = intent.getStringArrayExtra(REQUEST_PERMISSION)
    if (permissions.isEmpty()) {
      finishForResult(PermissionResult(true))
      return
    }
    if (hasPermissions(*permissions)) {
      finishForResult(PermissionResult(true))
      return
    } else {
      ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
    }
  }

  private fun finishForResult(permissionResult: PermissionResult) {
    val intent = Intent()
    intent.putExtra(RESULT_PERMISSION, permissionResult)
    setResult(RESULT_OK, intent)
    finish()
  }

  override fun onRequestPermissionsResult(requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (REQUEST_CODE == requestCode) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        finishForResult(PermissionResult(true))
      } else {
        val permanentDeny = mutableListOf<String>()
        val temporaryDeny = mutableListOf<String>()

        for (i in grantResults.indices) {
          val perm = permissions[i]
          if (grantResults[i] == PackageManager.PERMISSION_DENIED
            && ActivityCompat.shouldShowRequestPermissionRationale(this, perm)
          ) {
            temporaryDeny.add(perm)
          } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
            permanentDeny.add(perm)
          }
        }
        finishForResult(PermissionResult(false, temporaryDeny, permanentDeny))
      }
    }
  }

  private fun hasPermissions(vararg permissions: String): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      permissions
        .filter {
          ActivityCompat.checkSelfPermission(this,
            it) != PackageManager.PERMISSION_GRANTED
        }
        .forEach { return false }
    }
    return true
  }
}
