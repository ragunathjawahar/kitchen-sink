package io.craftedcourses.kitchensink.counter

import io.craftedcourses.kitchensink.mvi.ViewDriver

class CounterViewDriver : ViewDriver<CounterView, CounterState> {
  override fun render(view: CounterView, state: CounterState) {
    view.displayCounter(state.counter)
  }
}
