package com.m.mvvmkotlin.sample

import androidx.databinding.ObservableField
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.lifecycle.ViewModel
import com.m.mvvmkotlin.base.BaseViewModel

/*
 * Created by majian
 * Date : 2018/12/20
 * Describe :
 */

class DemoViewModel2 : BaseViewModel() {
    val data2 = ObservableField<String>()

    override fun onCleared() {
        super.onCleared()
    }

    val textChange = TextViewBindingAdapter.OnTextChanged { s, start, before, count -> data2.set(s.toString()) }
}