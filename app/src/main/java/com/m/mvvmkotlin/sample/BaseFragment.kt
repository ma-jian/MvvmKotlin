package com.m.mvvmkotlin.sample

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.m.library.di.component.ApplicationComponent
import com.m.library.mvvm.IFragment

/**
 * @Created by majian
 * @Date : 2019/1/4
 * @Describe :
 */

abstract class BaseFragment : Fragment(), IFragment {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        initData()
    }


    override fun obtatinAppComponent(appComponent: ApplicationComponent) {
    }

    override fun showToast(msg: String) {
    }
    override fun showLoading() {

    }

    override fun hideLoading() {

    }
}