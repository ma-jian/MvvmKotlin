package com.m.library.http

/**
 * Created by majian
 * Date : 2018/12/19
 * Describe : Retrofit实现类  [RetrofitInterfaceImpl] 接口
 */

interface RetrofitInterface {

    fun <T> obtainService(clazz: Class<T>): T

}