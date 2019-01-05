package com.m.mvvmkotlin.sample

import androidx.databinding.ObservableField
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.lifecycle.ViewModel

/*
 * Created by majian
 * Date : 2018/12/20
 * Describe :
 */

class DemoViewModel2 : ViewModel(), DemoTaskInterface.RespositoryTask {

    override fun result(result: String) {}

    val data2 = ObservableField<String>()

    override fun onCleared() {
        super.onCleared()
    }


    val textChange = object : TextViewBindingAdapter.OnTextChanged {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            data2.set(s.toString())
        }
    }
}