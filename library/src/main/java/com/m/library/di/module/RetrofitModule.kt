package com.m.library.di.module

import com.m.library.http.RetrofitInterface
import com.m.library.http.RetrofitInterfaceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * @Created by majian
 * @Date : 2018/12/21
 * @Describe :
 */
@Module(includes = [ClientModule::class])
abstract class RetrofitModule {

    @Singleton
    @Binds
    abstract fun bindRetrofitInterface(retrofitInterfaceImpl: RetrofitInterfaceImpl): RetrofitInterface
}