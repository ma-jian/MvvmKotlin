package com.m.library.http


import com.m.library.utils.Log
import io.reactivex.Flowable
import io.reactivex.functions.Function

/**
 *
 */
class HttpResultFunc<T> : Function<Throwable, Flowable<T>> {
    override fun apply(throwable: Throwable): Flowable<T> {
        Log.d("HttpResultFunc: $throwable")
        return Flowable.error(ExceptionHandle.handleException(throwable))
    }
}
