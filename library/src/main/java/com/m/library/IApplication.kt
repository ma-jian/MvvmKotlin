package com.m.library

import com.m.library.di.component.ApplicationComponent

/* 
 * Created by majian
 * Date : 2018/12/19
 * Describe :
 */

interface IApplication {
    fun obtainApplicationComponent(): ApplicationComponent
}