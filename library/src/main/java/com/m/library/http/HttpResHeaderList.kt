package com.m.library.http

import java.io.Serializable

/**
 * Created by majian
 * Date : 2018/1/29
 * Describe :
 *
iPage (integer): 当前页数 ,
pageCount (integer): 总页数 ,
pageSize (integer): 每页显示条数 ,
resCode (string): 返回代码 ,
resDesc (string, optional): 返回代码描述 ,
resultCount (integer): 查询总记录数 ,
resultList (Array[关联附件], optional): 返回列表
 */
data class HttpResHeaderList<T>(var iPage: Int = 0,
                                var pageCount: Int = 0,
                                var pageSize: Int = 0,
                                var resCode: String? = null,
                                var resDesc: String? = null,
                                var resultCount: Int? = 0,
                                var resultList: ArrayList<T>? = null) : Serializable {
    fun isSuccess(): Boolean {
        return "0000" == resCode
    }
}