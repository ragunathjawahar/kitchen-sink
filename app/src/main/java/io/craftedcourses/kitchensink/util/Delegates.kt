package io.craftedcourses.kitchensink.util

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.ViewGroup
import kotlin.reflect.KProperty

/**
 * This is a replacement for DataBindingUtil.setContentView(activity, layoutRes)
 *
 * use val binding: ActivityMainBinding by BindActivity(R.layout.activity_main) instead
 * Inspiration Lisa Wray
 */
class BindActivity<in R : Activity, out T : ViewDataBinding>(
        @LayoutRes private val layoutRes: Int) {

    private var value: T? = null

    operator fun getValue(thisRef: R, property: KProperty<*>): T {
        if (value == null) {
            value = DataBindingUtil.setContentView(thisRef, layoutRes)
        }
        return value!!
    }
}