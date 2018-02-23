package io.craftedcourses.kitchensink.counter

class CounterViewDriver(private val view: CounterView) {
  fun render(state: CounterState) {
    view.displayCounter(state.counter)
  }
}
