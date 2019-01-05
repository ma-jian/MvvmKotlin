package com.m.mvvmkotlin.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.m.library.di.component.ApplicationComponent
import com.m.library.mvvm.IActivity
import com.m.mvvmkotlin.bind.ViewModelFactory

/**
 * @Created by majian
 * @Date : 2019/1/4
 * @Describe :
 */

abstract class BaseActivity : AppCompatActivity(), IActivity {

    /**
     * [ApplicationComponent]
     */
    override fun obtatinAppComponent(appComponent: ApplicationComponent) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * [ViewModelFactory] viewmodel 工厂 。di 实现 model注入
         */
        ViewModelFactory.getInstance().bind(this)
        initView(savedInstanceState)
        initData()
    }


    override fun showLoading() {
    }

    override fun showToast(msg: String) {
    }

    override fun hideLoading() {
    }
}