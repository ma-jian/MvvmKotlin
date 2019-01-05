package com.m.library.di.component


import android.app.Application
import com.google.gson.Gson
import com.m.library.di.module.ApplicationModule
import com.m.library.lifecycle.ApplicationDelegate
import com.m.library.utils.ActivityManager
import dagger.Component
import javax.inject.Singleton

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun activityManager(): ActivityManager

    fun application(): Application

    fun gson(): Gson

    fun inject(application: ApplicationDelegate)
}