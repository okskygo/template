package com.tripresso.berry.view.empty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tripresso.berry.R
import com.tripresso.berry.view.BaseFragment

class EmptyFragment : BaseFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_empty, container, false)
  }

}
