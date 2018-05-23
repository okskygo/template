package com.tripresso.berry.widget.imageview

import android.content.Context
import android.util.AttributeSet

class CircleNetworkImageView @JvmOverloads constructor(context: Context,
                                                       attrs: AttributeSet? = null,
                                                       defStyle: Int = 0)
  : NetworkImageView(context, attrs, defStyle) {

  override fun circle(): Boolean {
    return true
  }

}
