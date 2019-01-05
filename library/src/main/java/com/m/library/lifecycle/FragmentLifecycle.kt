package com.m.library.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.m.library.IApplication
import com.m.library.mvvm.IFragment
import com.m.library.utils.Log
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by majian
 * Date : 2018/1/14
 * Describe : Fragment 生命周期 [ActivityLifecycle]
 */
@Singleton
class FragmentLifecycle @Inject constructor() : FragmentManager.FragmentLifecycleCallbacks() {
    var TAG = "FragmentLifecycle = "
    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Log.v(TAG + " -- onFragmentCreated = " + f.javaClass)
        if (f is IFragment) {
            f.obtatinAppComponent((f.activity!!.application as IApplication).obtainApplicationComponent())
        }
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Log.v(TAG + " -- onFragmentViewCreated = " + f.javaClass)
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Log.v(TAG + " -- onFragmentActivityCreated = " + f.javaClass)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Log.v(TAG + " -- onFragmentStarted = " + f.javaClass)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Log.v(TAG + " -- onFragmentResumed = " + f.javaClass)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Log.v(TAG + " -- onFragmentPaused = " + f.javaClass)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Log.v(TAG + "onFragmentStopped = " + f.javaClass)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Log.v(TAG + " -- onFragmentSaveInstanceState = " + f.javaClass)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Log.v(TAG + " -- onFragmentViewDestroyed = " + f.javaClass)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Log.v(TAG + " -- onFragmentDestroyed = " + f.javaClass)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        Log.v(TAG + " -- onFragmentDetached = " + f.javaClass)
    }
}