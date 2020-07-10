package com.m.mvvmkotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import com.m.library.IApplication
import com.m.library.lifecycle.ApplicationDelegate

/**
 * Created by majian
 * Date : 2018/12/19
 * Describe : 代理类 [ApplicationDelegate]  [IApplication]
 */

class MyApplication : Application(), IApplication {
    private lateinit var applicationDelegate: ApplicationDelegate

    companion object {
        lateinit var currentActivity: Activity
        var application: MyApplication? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        applicationDelegate = ApplicationDelegate()
        applicationDelegate.attachBaseContext(base)
        application = this
    }

    override fun onCreate() {
        super.onCreate()
        applicationDelegate.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationDelegate.onTerminate(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }


    override fun obtainApplicationComponent() = applicationDelegate.provideApplicationComponent()
}