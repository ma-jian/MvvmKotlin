package com.m.library.http


import com.m.library.utils.Log
import io.reactivex.functions.Function

class ServerDataFunc<T> : Function<HttpResHeader<T>, T> {
    override fun apply(tHttpResHeader: HttpResHeader<T>): T {
        if (!tHttpResHeader.isSuccess()) {
            Log.e("请求失败: resCode:${tHttpResHeader.resCode}; resDesc:${tHttpResHeader.resDesc} ")
            throw ServerException(tHttpResHeader.resCode, tHttpResHeader.resDesc)
        }
        return if (tHttpResHeader.data == null) tHttpResHeader.resDesc as T else tHttpResHeader.data!!
    }
}
