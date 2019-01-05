package com.m.library.lifecycle

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.m.library.ApplicationInterface
import com.m.library.BuildConfig
import com.m.library.di.component.ApplicationComponent
import com.m.library.di.component.DaggerApplicationComponent
import com.m.library.di.module.ApplicationModule
import com.m.library.di.module.ClientModule
import com.squareup.leakcanary.LeakCanary
import javax.inject.Inject

/**
 * Created by majian
 * Date : 2018/12/19
 * Describe : application 代理
 */

class ApplicationDelegate : ApplicationInterface {

    private lateinit var applicationComponent: ApplicationComponent


    @JvmField
    @Inject
    var mActivityLifecycle: ActivityLifecycle? = null

    override fun attachBaseContext(base: Context?) {}

    override fun onCreate(application: Application) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(application))
                .clientModule(ClientModule())
                .build()
        applicationComponent.inject(this)

        application.registerActivityLifecycleCallbacks(mActivityLifecycle)
        if (BuildConfig.ISDEBUG) {
            if (LeakCanary.isInAnalyzerProcess(application)) {
                return
            }
            LeakCanary.install(application)
        }
        MultiDex.install(application)
    }



    override fun onTerminate(application: Application) {
        application.unregisterActivityLifecycleCallbacks(mActivityLifecycle)
    }

    override fun provideApplicationComponent() = applicationComponent
}