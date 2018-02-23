package io.craftedcourses.kitchensink.counter

import io.reactivex.Observable

class CounterIntentions(
    private val incrementClicks: Observable<Unit>,
    private val decrementClicks: Observable<Unit>
) {
  fun increment(): Observable<Int> =
      incrementClicks.map { +1 }

  fun decrement(): Observable<Int> =
      decrementClicks.map { -1 }
}
