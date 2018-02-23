package io.craftedcourses.kitchensink.counter

data class CounterState(val counter: Int) {
  companion object {
    val INITIAL = CounterState(0)
  }

  fun add(number: Int): CounterState =
      copy(counter = counter + number)
}
