package com.m.library

import android.app.Application
import android.content.Context
import com.m.library.di.component.ApplicationComponent

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */

interface ApplicationInterface {

    fun attachBaseContext(base: Context?)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)

    fun provideApplicationComponent(): ApplicationComponent
}