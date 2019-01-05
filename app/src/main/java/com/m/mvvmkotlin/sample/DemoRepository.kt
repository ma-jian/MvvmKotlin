package com.m.mvvmkotlin.sample

import android.annotation.SuppressLint
import com.m.library.utils.Log
import com.m.library.utils.RxThread
import javax.inject.Inject

/**
 * @Created by majian
 * @Date : 2018/12/20
 * @Describe :  数据处理类 继承 自动生成的 [BaseRepository] 继承接口 [RepositoryInterface]
 * @param T
 */
class DemoRepository @Inject constructor() : BaseRepository() {

    @SuppressLint("CheckResult")
    fun getUser(user: String, task: DemoTaskInterface.RespositoryTask) {
        Log.d("DemoRepository:$mDemoServiceRepository")

        mSubscription.add(mDemoServiceRepository.getUser(user)
                .subscribe({ t ->
                    task.result(t.toString())
                }, {
                    task.result("error ${it.message}")
                    Log.e(it.message)
                }))
    }
}