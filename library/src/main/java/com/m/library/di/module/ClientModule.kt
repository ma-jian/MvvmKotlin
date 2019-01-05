package com.m.library.di.module

import com.m.library.BuildConfig
import com.m.library.http.RequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */
@Module
class ClientModule {
    private var TIME_OUT: Long = 30

    @Singleton
    @Provides
    @Named(BASE)
    fun provideRetrofit(okHttpClient: OkHttpClient, builder: Retrofit.Builder, @Named(BASE) baseUrl: HttpUrl): Retrofit {
        return builder.baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(@Named(LOG) logInterceptor: Interceptor, builder: OkHttpClient.Builder): OkHttpClient {

        return builder
                //                .addNetworkInterceptor(cacheInterceptor)
//                .addInterceptor(interceptor)
                .addInterceptor(logInterceptor)
                //                .cache(cache)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
    }

    @Singleton
    @Provides
    fun provideClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Singleton
    @Provides
    @Named(REQUEST)
    fun provideInterceptor(interceptor: RequestInterceptor): Interceptor {
        return interceptor
    }
//
    @Singleton
    @Provides
    @Named(LOG)
    fun provideLogInterceptor(): Interceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    @Named(BASE)
    fun provideHttpUrl(): HttpUrl {
        return HttpUrl.parse(BuildConfig.BASE_URL)!!
    }
}


const val BASE = "base"
const val UPDATE = "update"
const val REQUEST = "request"
const val LOG = "log"

