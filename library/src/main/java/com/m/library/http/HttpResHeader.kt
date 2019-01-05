package com.m.library.http
import java.io.Serializable

/**
 * Created by majian
 * Date : 2018/1/29
 * Describe :
 * data  返回数据 ,
resCode (string): 返回代码 ,
resDesc (string, optional): 返回代码描述
 */
data class HttpResHeader<T>(var resCode: String? = null,
                            var resDesc: String? = null,
                            var data: T? = null) : Serializable {

    fun isSuccess(): Boolean {
        return "0000" == resCode
    }
}