package com.m.library.utils

import android.util.Log
import com.m.library.BuildConfig

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */

class Log {
    companion object {
        val TAG = BuildConfig.APPLICATION_ID
        val DEBUG = BuildConfig.ISDEBUG

        fun v(msg: String? = "") {
            if (DEBUG) Log.v(TAG, msg)
        }


        fun i(msg: String? = "") {
            if (DEBUG) Log.i(TAG, msg)
        }

        fun d(msg: String? = "") {
            if (DEBUG) Log.d(TAG, msg)
        }


        fun w(msg: String? = "") {
            if (DEBUG) Log.w(TAG, msg)
        }


        fun e(msg: String? = "") {
            if (DEBUG) Log.e(TAG, msg)
        }


        fun d(tag: String, msg: String) {
            if (DEBUG) Log.d(tag, msg)
        }
    }
}