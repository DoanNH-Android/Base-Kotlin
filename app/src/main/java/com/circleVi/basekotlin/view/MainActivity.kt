package com.circleVi.basekotlin.view

import com.circleVi.basekotlin.R
import com.circleVi.basekotlin.view.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onAttachView() {
        //do nothing
    }

    override fun onDetachView() {
        //do nothing
    }
}
