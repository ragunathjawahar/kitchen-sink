package io.craftedcourses.kitchensink.counter

import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxkotlin.withLatestFrom

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
        restoredBindingUseCase(bindings, states),
        incrementDecrementUseCase(numbers, states)
    )
  }

  private fun newBindingUseCase(
      bindings: Observable<Binding>
  ): Observable<CounterState> {
    return bindings
        .filter { it == Binding.NEW }
        .map    { CounterState.ZERO }
  }

  private fun restoredBindingUseCase(
      bindings: Observable<Binding>,
      states: Observable<CounterState>
  ): ObservableSource<CounterState> {
    return bindings
        .filter { it == Binding.RESTORED }
        .withLatestFrom(states) { _, previousState -> previousState }
  }

  private fun incrementDecrementUseCase(
      numbers: Observable<Int>,
      states: Observable<CounterState>
  ): Observable<CounterState> {
    return numbers.withLatestFrom(states) { number, previousState ->
      previousState.add(number)
    }
  }
}
