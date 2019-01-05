package com.m.library.mvvm

/**
 * Created by majian
 * Date : 2018/1/14
 * Describe :
 */
interface IView {
    fun showToast(msg: String)

    fun showLoading()

    fun hideLoading()

//    注册极光
//    fun regiestJpush(id: String)
}