package com.m.library.http

import com.m.library.utils.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by majian
 * Date : 2018/1/14
 * Describe :
 */
@Singleton
class RequestInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
//        request = request.newBuilder().addHeader("client-type", "2")
//                .addHeader("auth-token", SPUtils.getInstance().getString(SaveConstant.TOKEN))
//                .build()
        val hasRequestBody = request.body() != null
        //打印请求信息
        val headers = request.headers()
        val str = StringBuilder()
        if (headers.size() > 0) {
            for (i: Int in 0..(headers.size() - 1)) {
                str.append("${headers.name(i)}:${headers.value(i)} | ")
            }
        }
//        Logger.d("Header: ${str}")

        var charset: Charset? = Charset.forName("UTF-8")
        var body: String? = ""
        if (hasRequestBody) {
            val requestBody = request.body()
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"))
            }
            charset?.let {
                body = buffer.readString(charset)
            }
            Log.d("\n\n|--------------------RequestBody--------------------------------------------------------" +
                    "\n Request -> ${request.method()} url -> ${request.url()} " +
                    "\n Header: ${str} " +
                    "\n params: $body" +
                    "\n ----------------------------------------------------------------------------|"
            )
        }

        val response = chain.proceed(request)
        val responseBody = response.body()
        val source = responseBody!!.source()
        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer()
        val UTF8 = Charset.forName("UTF-8")
        Log.d("\n\n|--------------------Response--------------------------------------------------------" +
                "\n Http -> : ${request.method()} url -> ${request.url()} " +
                "\n Header: ${str} " +
                "\n params: $body " +
                "\n data: ${buffer.clone().readString(UTF8)}" +
                "\n ----------------------------------------------------------------------------|"
        )
        return response

    }
}