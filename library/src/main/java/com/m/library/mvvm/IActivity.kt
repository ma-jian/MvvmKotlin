package com.m.library.mvvm

import android.os.Bundle

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :activity 接口
 */

interface IActivity : IComponent, IView {

    /**
     * 布局接口
     */
//    fun layoutId(): Int

    /**
     * 布局初始化
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 数据接口
     */
    fun initData()
}