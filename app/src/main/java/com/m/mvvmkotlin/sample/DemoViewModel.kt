package com.m.mvvmkotlin.sample

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/*
 * Created by majian
 * Date : 2018/12/20
 * Describe :
 */

class DemoViewModel(private var demoRepository: DemoRepository) : ViewModel(), DemoTaskInterface.RespositoryTask {

    val data = ObservableField<String>()

    override fun result(result: String) {
        data.set(result)
    }

    fun get(user: String) {
        demoRepository.getUser(user, this)
    }

    override fun onCleared() {
        super.onCleared()
        demoRepository.onCleared()
    }
}