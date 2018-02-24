package io.craftedcourses.kitchensink.mvi

abstract class ViewDriver<V, in S>(private val view: V) {
  abstract fun render(state: S)
}
