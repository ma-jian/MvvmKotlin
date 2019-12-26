package com.m.mvvmkotlin.sample

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.m.mvvmkotlin.base.BaseViewModel
import kotlinx.coroutines.launch

/**
 * Created by majian
 * Date : 2018/12/20
 * Describe :
 */

class DemoViewModel(private var demoRepository: DemoRepository) : BaseViewModel() {

    val data = ObservableField<String>()

    fun get(user: String) = launch {
        val await = demoRepository.getUser(user).await()
        data.set(await.toString())
    }

    override fun onCleared() {
        super.onCleared()
        demoRepository.onCleared()
    }
}