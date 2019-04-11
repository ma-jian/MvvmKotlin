package com.m.library.http

/**
 * Created by majian
 * Date : 2018/12/19
 * Describe : 生成retrofit工厂 [ApiFactoryProcessor] 辅助类
 */


open class BaseHttpFactory<T>(retrofitInterface: RetrofitInterface, clazz: Class<T>) {
    @JvmField
    var mObtainService: T

    init {
        if (clazz.isInterface) {
            mObtainService = retrofitInterface.obtainService(clazz)
        } else {
            throw IllegalArgumentException("must be give the retrofit service,that is interfaces, 指定需要的Service")
        }
    }
}
