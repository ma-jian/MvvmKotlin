package com.m.library.di.module

import android.app.Application
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */
@Module(includes = [RetrofitModule::class])
class ApplicationModule(var application: Application) {
    @Singleton
    @Provides
    fun provideApplication() = application

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder().create()
}