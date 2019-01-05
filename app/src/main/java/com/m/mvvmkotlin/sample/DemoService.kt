package com.m.mvvmkotlin.sample

import io.reactivex.Flowable
import retrofit2.http.GET
import com.m.annotation.ApiFactory
import com.m.library.http.HttpResHeaderList
import retrofit2.http.Path

/**
 * @Created by majian
 * @Date : 2018/12/20
 * @Describe :  [ApiFactory]
 */

@ApiFactory
interface DemoService {
    @GET("users/{user}")
    fun getUser(@Path("user") user: String): Flowable<Any>

    @GET("api/discover/topic/list")
    fun topicList(): Flowable<HttpResHeaderList<DemoBean>>
}