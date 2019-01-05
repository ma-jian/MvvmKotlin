package com.m.library.utils

import android.app.Activity
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by majian
 * Date : 2018/1/19
 * Describe : App管理 [ApplicationDelegate]
 */
@Singleton
class ActivityManager @Inject constructor() {
    private var activityList: LinkedList<Activity>? = null
    private var currentActivity: Activity? = null


    fun addActivity(activity: Activity) {
        synchronized(ActivityManager::class.java) {
            if (!getActivitys().contains(activity)) {
                getActivitys().add(activity)
            }
        }
    }

    fun getActivitys(): LinkedList<Activity> {
        if (activityList == null) {
            activityList = LinkedList()
        }
        return activityList!!
    }

    fun setCurrentActivity(activity: Activity?) {
        this.currentActivity = activity
    }

    fun getCurrentActivity(): Activity? {
        return if (currentActivity != null) currentActivity else null
    }

    fun removeActivity(activity: Activity): Boolean {
        synchronized(ActivityManager::class.java) {
            if (getActivitys().contains(activity)) {
                return getActivitys().remove(activity)
            } else {
                return false
            }
        }
    }

    fun killActivitys() {
        synchronized(ActivityManager::class.java) {
            for (activity in getActivitys()) {
                activity.finish()
            }
        }
    }

    fun systemExit() {
        try {
            killActivitys()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}