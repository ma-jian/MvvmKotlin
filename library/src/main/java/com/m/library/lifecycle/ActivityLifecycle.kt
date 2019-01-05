package com.m.library.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.m.library.IApplication
import com.m.library.mvvm.IComponent
import com.m.library.utils.ActivityManager
import com.m.library.utils.Log
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by majian
 * Date : 2018/1/14
 * Describe :Activity 生命周期 [ApplicationDelegate]
 */
@Singleton
class ActivityLifecycle @Inject constructor(private var appManager: ActivityManager) : Application.ActivityLifecycleCallbacks {
    var TAG = "ActivityLifecycle = "
    @Inject
    @JvmField
    var fragmentLifecycle: FragmentLifecycle? = null

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        appManager.addActivity(activity!!)
        if (activity is IComponent) {
            activity.obtatinAppComponent((activity.application as IApplication).obtainApplicationComponent())
            regiestFragmentLifecycle(activity)
            Log.v(TAG + "onActivityCreated = " + activity.javaClass.name)
        }
    }

    private fun regiestFragmentLifecycle(activity: Activity?) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycle!!, true)
            Log.v(TAG + "registerFragmentLifecycle = " + activity.javaClass.name)
        }
    }

    override fun onActivityPaused(activity: Activity?) {
        Log.v(TAG + "onActivityPaused = " + activity?.javaClass?.name)
    }

    override fun onActivityResumed(activity: Activity?) {
        appManager.setCurrentActivity(activity)
        Log.v(TAG + "onActivityResumed = " + activity!!.javaClass.name)
    }

    override fun onActivityStarted(activity: Activity?) {
        Log.v(TAG + "onActivityStarted = " + activity!!.javaClass.name)
    }

    override fun onActivityDestroyed(activity: Activity?) {
        appManager.removeActivity(activity!!)
        Log.v(TAG + "onActivityDestroyed = " + activity.javaClass.name)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Log.v(TAG + "onActivitySaveInstanceState = " + activity?.javaClass?.name)
    }

    override fun onActivityStopped(activity: Activity?) {
        Log.v(TAG + "onActivityStopped = " + activity?.javaClass?.name)
        if (appManager.getCurrentActivity() == activity) {
            appManager.setCurrentActivity(null)
        }
    }
}