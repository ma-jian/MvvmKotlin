package com.m.library.http

import android.net.ParseException
import com.google.gson.JsonParseException
import com.m.library.BuildConfig
import com.m.library.http.ExceptionHandle.ERROR.UNKNOWN
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 * Created by m
 */
class ExceptionHandle {

    object ERROR {
        /**
         * 未知错误
         */
        internal val UNKNOWN = 1000
        /**
         * 解析错误
         */
        internal val PARSE_ERROR = 1001
        /**
         * 网络错误
         */
        internal val NETWORD_ERROR = 1002
        /**
         * 协议出错
         */
        internal val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        internal val SSL_ERROR = 1005

        /**
         * 连接超时
         */
        internal val TIMEOUT_ERROR = 1006
    }

    class ResponeThrowable : Exception {
        var code: String = "0000"
        var errorMsg: String = ""

        constructor(throwable: Throwable, code: String) : super(throwable) {
            this.code = code
        }

        constructor(throwable: Throwable, code: String, errorMsg: String) : super(throwable) {
            this.code = code
            this.errorMsg = errorMsg

        }
    }

    companion object {
        private val UNAUTHORIZED = 401
        private val FORBIDDEN = 403
        private val NOT_FOUND = 404
        private val REQUEST_TIMEOUT = 408
        private val INTERNAL_SERVER_ERROR = 500
        private val BAD_GATEWAY = 502
        private val SERVICE_UNAVAILABLE = 503
        private val GATEWAY_TIMEOUT = 504

        fun handleException(e: Throwable): ResponeThrowable {
            val ex: ResponeThrowable
            if (e is HttpException) {
                ex = ResponeThrowable(e, ERROR.HTTP_ERROR.toString())
                when (e.code()) {
                    UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> ex.errorMsg = "网络不佳，请确定您的网络"
                    else -> ex.errorMsg = "网络不佳，请确定您的网络"
                }
                return ex
            } else if (e is ServerException) {
                ex = ResponeThrowable(e, e.code ?: "$UNKNOWN", e.msg ?: "")
                return ex
            } else if (e is JsonParseException
                    || e is JSONException
                    || e is ParseException) {
                ex = ResponeThrowable(e, ERROR.PARSE_ERROR.toString())
                ex.errorMsg = "解析错误"
                return ex
            } else if (e is ConnectException) {
                ex = ResponeThrowable(e, ERROR.NETWORD_ERROR.toString())
                ex.errorMsg = "连接失败"
                return ex
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                ex = ResponeThrowable(e, ERROR.SSL_ERROR.toString())
                ex.errorMsg = "证书验证失败"
                return ex
            } else if (e is ConnectTimeoutException) {
                ex = ResponeThrowable(e, ERROR.TIMEOUT_ERROR.toString())
                ex.errorMsg = "网络不佳，请确定您的网络"
                return ex
            } else if (e is java.net.SocketTimeoutException) {
                ex = ResponeThrowable(e, ERROR.TIMEOUT_ERROR.toString())
                ex.errorMsg = "网络不佳，请确定您的网络"
                return ex
            } else {
                ex = ResponeThrowable(e, ERROR.UNKNOWN.toString())
                ex.errorMsg = if (BuildConfig.ISDEBUG) e.message ?: "系统忙，请稍后重试" else "系统忙，请稍后重试"
                return ex
            }
        }
    }
}

