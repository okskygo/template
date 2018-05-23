package com.tripresso.berry.util.permission

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import com.tripresso.berry.R
import com.tripresso.berry.util.activity.startApplicationDetailsSettings
import com.tripresso.berry.widget.snackbar.showSnackbar
import io.reactivex.Observable
import java.io.Serializable

fun Context.requestPermissions(vararg permissions: String): Observable<Boolean> {

  if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
    return Observable.just(true)
  }

  val temporaryDeny = permissions.filter {
    PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(this, it)
  }
  if (temporaryDeny.isEmpty()) {
    return Observable.just(true)
  }

  return PermissionHolderActivity.start(this, temporaryDeny.toTypedArray())
    .map {
      if (it.hasPermanentDeny()) {
        showRedirectSettingDialog(this)
      } else if (it.hasTemporaryDeny()) {
        (this as? Activity)?.showSnackbar(R.string.request_permission_message, Snackbar.LENGTH_LONG)
      }
      it.allGranted()
    }
}

private fun showRedirectSettingDialog(context: Context) {
  AlertDialog.Builder(context)
    .setTitle(R.string.request_permission_title)
    .setMessage(R.string.request_permission_message)
    .setPositiveButton(R.string.go) { _, _ ->
      context.startApplicationDetailsSettings()
    }.setCancelable(false)
    .show()
}

class PermissionResult : Serializable {
  private val allGranted: Boolean
  private val permanentDeny = ArrayList<String>()
  private val temporaryDeny = ArrayList<String>()

  constructor(allGranted: Boolean) {
    this.allGranted = allGranted
  }

  constructor(allGranted: Boolean,
    temporaryDeny: List<String>,
    permanentDeny: List<String>) {
    this.allGranted = allGranted
    this.temporaryDeny.addAll(temporaryDeny)
    this.permanentDeny.addAll(permanentDeny)
  }

  fun hasPermanentDeny(): Boolean {
    return permanentDeny.isNotEmpty()
  }

  fun hasTemporaryDeny(): Boolean {
    return temporaryDeny.isNotEmpty()
  }

  fun allGranted(): Boolean {
    return allGranted
  }

}

