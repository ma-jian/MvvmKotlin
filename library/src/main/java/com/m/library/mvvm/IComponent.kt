package com.m.library.mvvm

import com.m.library.di.component.ApplicationComponent

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */

interface IComponent {
    /**
     * 提供ApplicationComponent实例
     */
    fun obtatinAppComponent(appComponent: ApplicationComponent)
}