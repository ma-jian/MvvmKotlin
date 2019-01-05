package com.m.library.http


import com.m.library.utils.Log
import io.reactivex.Flowable
import io.reactivex.functions.Function
import java.util.*

class HttpResultListFunc<T> : Function<Throwable, Flowable<ArrayList<T>>> {

    override fun apply(throwable: Throwable): Flowable<ArrayList<T>> {
        Log.d("HttpResultListFunc: $throwable")
        return Flowable.error(ExceptionHandle.handleException(throwable))
    }
}