package com.m.library.mvvm

import android.os.Bundle
import android.view.View

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :Fragment 接口
 */

interface IFragment : IComponent, IView {

    /**
     * 布局接口
     */
//    fun layoutId(): Int

    /**
     * 布局初始化
     */
    fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 数据接口
     */
    fun initData()
}