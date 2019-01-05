package com.m.library.http

import com.m.library.di.module.BASE
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */
open class RetrofitInterfaceImpl @Inject constructor(@Named(BASE) var retrofit: Retrofit) : RetrofitInterface {
    override fun <T> obtainService(clazz: Class<T>) = retrofit.create(clazz)
}