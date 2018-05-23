package com.tripresso.berry.widget.imageview

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.tripresso.berry.R
import com.tripresso.berry.util.glide.GlideApp
import java.io.File

open class NetworkImageView @kotlin.jvm.JvmOverloads constructor(context: Context,
                                                                 attrs: AttributeSet? = null,
                                                                 defStyleAttr: Int = 0)
  : AdjustableAppCompatImageView(context, attrs, defStyleAttr) {

  private var originalScaleType: ScaleType = scaleType
  private var defaultImageId: Int = 0
  private var defaultImageScaleType: ScaleType
  private var viewTarget: Target<Drawable>? = null
  private var fileTarget: FutureTarget<File>? = null

  init {
    defaultImageScaleType = scaleType
    if (attrs != null) {
      val a = context.theme
        .obtainStyledAttributes(attrs, R.styleable.NetworkImageView, 0, 0)
      val defaultImageResId: Int
      try {
        defaultImageResId = a.getResourceId(R.styleable.NetworkImageView_defaultImage, 0)
      } finally {
        a.recycle()
      }
      if (defaultImageResId != 0) {
        setDefaultImageResId(defaultImageResId, ImageView.ScaleType.CENTER_CROP)
      }
    }
  }

  /**
   * https://github.com/wasabeef/glide-transformations
   *
   * use this library must call [RequestOptions.disallowHardwareConfig]
   *
   */
  fun setImageUrl(url: String?) {
    clearImageView()
    url ?: return
    if (isActivityDestroyed()) return
    setDefaultImageScaleType()
    loadBitmapInToImageView(url)
  }

  fun setImageUrl(url: String?,
                  rawImageDownloadListener: RawImageDownloadListener) {
    clearImageView()
    url ?: return
    if (isActivityDestroyed()) return

    fileTarget = GlideApp.with(this)
      .download(url)
      .listener(object : RequestListener<File> {
        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<File>?,
                                  isFirstResource: Boolean): Boolean {
          return false
        }

        override fun onResourceReady(resource: File?,
                                     model: Any?,
                                     target: Target<File>?,
                                     dataSource: DataSource?,
                                     isFirstResource: Boolean): Boolean {
          rawImageDownloadListener.onLoaded(resource)
          loadBitmapInToImageView(resource)
          return false
        }
      })
      .submit()
  }

  fun setDefaultImageResId(@DrawableRes defaultImage: Int, scaleType: ScaleType) {
    defaultImageId = defaultImage
    defaultImageScaleType = scaleType

    setImageResource(defaultImage)
    this.scaleType = defaultImageScaleType
  }

  private fun loadBitmapInToImageView(source: Any?) {
    if (isActivityDestroyed()) return
    val options = RequestOptions()
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .placeholder(defaultImageId)
      .dontAnimate()
      .dontTransform()
      .circle(circle())

    viewTarget = GlideApp.with(this)
      .load(source)
      .apply(options)
      .listener(object : RequestListener<Drawable> {
        override fun onResourceReady(resource: Drawable?,
                                     model: Any?,
                                     target: Target<Drawable>?,
                                     dataSource: DataSource?,
                                     isFirstResource: Boolean): Boolean {

          return false
        }

        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<Drawable>?,
                                  isFirstResource: Boolean): Boolean {
          setOriginalImageScaleType()
          return false
        }

      })
      .into(this)
  }

  private fun isActivityDestroyed(): Boolean {

    context ?: return true

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      val destroyed = (context as? Activity)?.isDestroyed
      if (destroyed != null && destroyed) {
        return true
      }
    } else {
      val finishing = (context as? Activity)?.isFinishing
      if (finishing != null && finishing) {
        return true
      }
    }
    return false
  }

  fun clearImageView() {
    if (defaultImageId != 0) {
      setDefaultImageScaleType()
      setImageResource(defaultImageId)
    } else {
      setImageDrawable(null)
    }
    cancelLoading()
  }

  private fun setDefaultImageScaleType() {
    this.scaleType = defaultImageScaleType
  }

  private fun setOriginalImageScaleType() {
    this.scaleType = originalScaleType
  }

  fun cancelLoading() {
    if (isActivityDestroyed()) return
    viewTarget?.let {
      Glide.with(this).clear(it)
    }
    fileTarget?.let {
      Glide.with(this).clear(it)
    }
  }

  open fun circle(): Boolean {
    return false
  }

}

interface RawImageDownloadListener {
  fun onLoaded(file: File?)
}

fun RequestOptions.scaleType(scaleType: ImageView.ScaleType): RequestOptions {
  if (scaleType == ImageView.ScaleType.CENTER_CROP) {
    return this.centerCrop()
  } else if (scaleType == ImageView.ScaleType.FIT_CENTER) {
    return this.fitCenter()
  }
  return this
}

fun RequestOptions.circle(boolean: Boolean): RequestOptions {
  if (boolean) {
    return this.circleCrop()
  }
  return this
}