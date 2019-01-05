package com.m.mvvmkotlin.sample

import com.m.library.mvvm.IView

/**
 * @Created by majian
 * @Date : 2018/12/20
 * @Describe :
 */

interface DemoTaskInterface : IView {

    interface ViewModelTask {
        fun topicList()
    }

    interface RespositoryTask {
        fun result(result: String)
    }


}