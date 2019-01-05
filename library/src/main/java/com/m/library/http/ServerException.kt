package com.m.library.http

/**
 *  处理异常
 */
data class ServerException(var code: String? = null, var msg: String? = null) : RuntimeException()