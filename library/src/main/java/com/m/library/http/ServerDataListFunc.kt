package com.m.library.http

import com.m.library.utils.Log
import io.reactivex.functions.Function
import java.util.*

class ServerDataListFunc<T> : Function<HttpResHeaderList<T>, ArrayList<T>> {
    override fun apply(tHttpResHeader: HttpResHeaderList<T>): ArrayList<T> {
        if (!tHttpResHeader.isSuccess()) {
            Log.e("请求失败: resCode:${tHttpResHeader.resCode}; resDesc:${tHttpResHeader.resDesc} ")
            throw ServerException(tHttpResHeader.resCode, tHttpResHeader.resDesc)
        }
        return if (tHttpResHeader.resultList == null) ArrayList<T>() else tHttpResHeader.resultList!!
    }
}
