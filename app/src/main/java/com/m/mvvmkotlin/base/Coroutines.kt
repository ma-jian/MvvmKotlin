package com.m.mvvmkotlin.base

import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CompletableDeferred
import org.reactivestreams.Subscription

/**
 * @Created by majian
 * @Date : 2019-04-24
 * @Describe :
 */
/**
 * 请求数据协成
 */
inline fun <reified R> runLaunch(block: CompletableDeferred<R>.() -> Unit): CompletableDeferred<R> {
    val deferred = CompletableDeferred<R>()
    block.invoke(deferred)
    return deferred
}

/**
 * 数据处理
 */
fun <T> Flowable<T>.subscribe(deferred: CompletableDeferred<T>) = this.subscribe(object : FlowableSubscriber<T> {
    private lateinit var sub: Subscription
    override fun onComplete() {}
    override fun onSubscribe(s: Subscription) {
        sub = s
        s.request(1)
        if (!deferred.isActive) {
            s.cancel()
        }
    }

    override fun onNext(t: T) {
        deferred.complete(t)
        sub.request(1)
    }

    override fun onError(t: Throwable?) {
        t?.let {
            deferred.completeExceptionally(t)
        }
    }
})
