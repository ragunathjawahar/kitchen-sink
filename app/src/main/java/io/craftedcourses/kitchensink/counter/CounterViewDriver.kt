package io.craftedcourses.kitchensink.counter

import io.craftedcourses.kitchensink.mvi.ViewDriver

class CounterViewDriver(
    private val view: CounterView
) : ViewDriver<CounterView, CounterState>(view) {
  override fun render(state: CounterState) {
    view.displayCounter(state.counter)
  }
}
