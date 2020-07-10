package com.m.mvvmkotlin

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.IBinder
import android.util.ArrayMap
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import java.lang.Exception
import java.util.*


/**
 * dialog stack
 */

@Suppress("UNREACHABLE_CODE")
class DialogStackManager {
    companion object {
        val stack = Stack<StackEmpty>()
        val comparator: Comparator<StackEmpty> = Comparator { o1, o2 ->
            if (o1.launchMode == o2.launchMode) {
                o1.code.compareTo(o2.code)
            } else {
                o2.launchMode.compareTo(o1.launchMode)
            }
        }

        fun addToStack(
            code: Int,
            dialog: String?,
            tag: String? = null,
            targetActivity: (MutableList<Class<out Activity>>.() -> Unit)? = null,
            targetFragment: (MutableList<Class<out Fragment>>.() -> Unit)? = null,
            launchMode: LaunchMode = LaunchMode.STANDARD
        ) {
            val activity = arrayListOf<Class<out Activity>>()
            val fragment = arrayListOf<Class<out Fragment>>()
            targetActivity?.invoke(activity)
            targetFragment?.invoke(fragment)
            dialog?.run {
                stack.push(StackEmpty(code, dialog, activity, fragment, tag ?: "", launchMode))
                stack.sortWith(comparator)
            }
            stack.forEach {
                println("$it")
            }
            println("------------------------------------------------------")
            showView()
        }

        fun showView() {
            if (stack.isNotEmpty()) {
                findTopActivity()?.run {
                    lifecycle.addObserver(object : LifecycleObserver {

                        fun onDestory() {}

                    })
                    val fragments = supportFragmentManager?.fragments
                    fragments?.filter { it.isAdded && it.isResumed && it.isVisible }
                }
            }
        }


        fun findTopActivity(): FragmentActivity? {
            val systemService =
                MyApplication.application?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val componentName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val taskInfo = systemService.appTasks.first().taskInfo
                taskInfo.topActivity
            } else {
                val runningTasks = systemService.getRunningTasks(1)
                if (runningTasks.isNotEmpty()) {
                    runningTasks[0].topActivity
                } else {
                    null
                }
            }
            try {
                val activityThread = Class.forName("android.app.ActivityThread")
                val method = activityThread.getMethod("currentActivityThread")
                val currentActivityThread = method.invoke(null)
                val field = activityThread.getDeclaredField("mActivities")
                field.isAccessible = true
                val map = field.get(currentActivityThread) as ArrayMap<IBinder, Any>
                map.keys.forEach { i ->
                    map[i]?.let {
                        val f = it.javaClass.getDeclaredField("activity")
                        f.isAccessible = true
                        val ac = f.get(it) as FragmentActivity
                        Log.e("majian", "ac ${ac.javaClass.simpleName}")
                        if (ac.javaClass.simpleName == componentName?.className) {
                            return ac
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    data class StackEmpty(
        val code: Int,
        val dialog: String,
        val activity: MutableList<Class<out Activity>>,
        val fragment: MutableList<Class<out Fragment>>,
        val tag: String = "",
        val launchMode: LaunchMode = LaunchMode.STANDARD
    )

    enum class LaunchMode {
        STANDARD, SINGLETOP, SINGLEINSTANCE
    }

}