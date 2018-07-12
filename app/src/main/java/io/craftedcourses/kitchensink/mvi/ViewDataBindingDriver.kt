package io.craftedcourses.kitchensink.mvi

import android.databinding.ViewDataBinding

interface ViewDataBindingDriver<in V: ViewDataBinding, in S> {
  fun render(viewDataBinding: V, state: S)
}
