package io.craftedcourses.kitchensink.mvi

interface ViewDriver<in V, in S> {
  fun render(view: V, state: S)
}
