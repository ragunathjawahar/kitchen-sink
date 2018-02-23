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
    val newBindingStates = newBindingUseCase(bindings)
    val incrementStates  = incrementUseCase(intentions, states)
    val decrementStates  = decrementUseCase(intentions, states)

    return Observable.merge(
        newBindingStates, incrementStates, decrementStates
    )
  }

  private fun newBindingUseCase(bindings: Observable<Binding>): Observable<CounterState> {
    return bindings
        .filter { it == Binding.NEW }
        .map    { CounterState.INITIAL }
  }

  private fun incrementUseCase(
      intentions: CounterIntentions,
      states: Observable<CounterState>
  ): Observable<CounterState> {
    val combiner = BiFunction<Int, CounterState, CounterState> { plusOne, previousState ->
      previousState.add(plusOne)
    }

    return intentions
        .increment()
        .withLatestFrom(states, combiner)
  }

  private fun decrementUseCase(
      intentions: CounterIntentions,
      states: Observable<CounterState>
  ): Observable<CounterState> {
    val combiner = BiFunction<Int, CounterState, CounterState> { minusOne, previousState ->
      previousState.add(minusOne)
    }

    return intentions
        .decrement()
        .withLatestFrom(states, combiner)
  }
}
