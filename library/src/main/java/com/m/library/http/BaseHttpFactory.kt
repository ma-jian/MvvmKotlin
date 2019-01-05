package com.m.library.http

import java.lang.reflect.ParameterizedType

/**
 * Created by majian
 * Date : 2018/12/19
 * Describe : 生成retrofit工厂 [ApiFactoryProcessor] 辅助类
 */


open class BaseHttpFactory<T>(retrofitInterface: RetrofitInterface) {
    @JvmField
    var mObtainService: T

    init {
        val genericSuperclass = javaClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val clazz: Class<T> = genericSuperclass.actualTypeArguments[0] as Class<T>
            mObtainService = retrofitInterface.obtainService(clazz)
        } else {
            throw IllegalArgumentException("must be give the retrofit service,that is interfaces, 指定需要的Service")
        }
    }
}
