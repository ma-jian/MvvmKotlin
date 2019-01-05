package com.m.library.utils

import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * Created by majian
 * Date : 2018/1/18
 * Describe :Rx相关
 */
class RxThread {
    companion object {
        /**
         * Rx 线程转换
         */
        fun <T> applyAsync(): FlowableTransformer<T, T> {
            return FlowableTransformer {
                it.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        /**
         * 对key进行md5 加密
         */
        fun md5Password(password: String): String {
            try {
                // 得到一个信息摘要器
                val digest = MessageDigest.getInstance("md5")
                val result = digest.digest(password.toByteArray())
                val buffer = StringBuffer()
                // 把每一个byte 做一个与运算 0xff;
                for (b in result) {
                    // 与运算
                    val number = b.and(0xff.toByte()) // 加盐
                    val str = Integer.toHexString(number.toInt())
                    if (str.length == 1) {
                        buffer.append("0")
                    }
                    buffer.append(str)
                }

                // 标准的md5加密后的结果
                return buffer.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                return password
            }
        }
    }
}