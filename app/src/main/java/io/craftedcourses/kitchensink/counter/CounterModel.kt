package io.craftedcourses.kitchensink.counter

import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

object CounterModel {
  fun bind(
      intentions: CounterIntentions,
      bindings: Observable<Binding>,
      states: Observable<CounterState>
  ): Observable<CounterState> {
    val numbers = Observable.merge(
        intentions.increment(),
        intentions.decrement()
    )

    return Observable.merge(
        newBindingUseCase(bindings),
        incrementDecrementUseCase(numbers, states)
    )
  }

  private fun newBindingUseCase(
      bindings: Observable<Binding>
  ): Observable<CounterState> {
    return bindings
        .filter { it == Binding.NEW }
        .map    { CounterState.INITIAL }
  }

  private fun incrementDecrementUseCase(
      numbers: Observable<Int>,
      states: Observable<CounterState>
  ): Observable<CounterState> {
    val combiner = BiFunction<Int, CounterState, CounterState> { number, previousState ->
      previousState.add(number)
    }
    return numbers.withLatestFrom(states, combiner)
  }
}
