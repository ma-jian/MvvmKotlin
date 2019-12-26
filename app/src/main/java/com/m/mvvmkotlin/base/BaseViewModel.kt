package com.m.mvvmkotlin.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.m.library.http.ExceptionHandle
import com.m.library.http.ServerException
import com.m.library.utils.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

/**
 * @Created by majian
 * @Date : 2019/2/13
 * @Describe :
 */

open class BaseViewModel : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + exceptionHandler
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.let {
            handleException(it)
            Log.e("exceptionHandler ${it.message}")
        }
    }

    private val errorMessage = MutableLiveData<ExceptionHandle.ResponeThrowable>()
    private val successMessage = MutableLiveData<String>()

    fun success(msg: String?) {
        successMessage.value = msg
    }

    private fun handleException(throwable: Throwable?) {
        throwable?.also {
            errorMessage.value = ExceptionHandle.handleException(throwable)
        }
    }


    fun error(code: String, msg: String?) {
        handleException(ServerException(code, msg))
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }


    fun getSuccess() = successMessage

    fun getError() = errorMessage
}