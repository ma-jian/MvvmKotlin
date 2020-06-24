package com.m.mvvmkotlin

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.*


/**
 * dialog stack
 */

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

        }

        fun showView() {
            val activity = MyApplication.currentActivity
            if (stack.isNotEmpty()) {
                if (MyApplication.currentActivity is FragmentActivity) {
                    val fragments =
                        (MyApplication.currentActivity as FragmentActivity).supportFragmentManager.fragments
                    fragments.filter { it.isAdded && it.isResumed && it.isVisible }
                }
            }
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