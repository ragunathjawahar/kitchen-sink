package io.craftedcourses.kitchensink.infra

import io.reactivex.observers.TestObserver

inline fun <reified T> TestObserver<T>.assertStates(vararg expectations: Pair<() -> Unit, T>) {
  val expectedStates = mutableListOf<T>()
  expectations.forEach { (action, state) ->
    action()
    expectedStates.add(state)
  }

  assertNoErrors()
  assertValues(*expectedStates.toTypedArray())
  assertNotTerminated()
}

infix fun <T> (() -> Unit).emits(state: T): Pair<() -> Unit, T> {
  return Pair(this, state)
}
