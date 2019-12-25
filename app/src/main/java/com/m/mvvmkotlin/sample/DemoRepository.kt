package com.m.mvvmkotlin.sample

import android.annotation.SuppressLint
import com.m.library.utils.Log
import com.m.mvvmkotlin.base.runLaunch
import com.m.mvvmkotlin.base.subscribe
import com.m.mvvmkotlin.bind.BaseRepository
import javax.inject.Inject

/**
 * @Created by majian
 * @Date : 2018/12/20
 * @Describe :  数据处理类 继承 自动生成的 [BaseRepository] 继承接口 [RepositoryInterface]
 * @param T
 */
class DemoRepository @Inject constructor() : BaseRepository() {

    @SuppressLint("CheckResult")
    fun getUser(user: String) = runLaunch<Any> {
        Log.d("DemoRepository : $mDemoServiceRepository")
        mDemoServiceRepository.getUser(user).subscribe(this)
    }
}