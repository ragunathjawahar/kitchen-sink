package io.craftedcourses.kitchensink.counter

import io.craftedcourses.kitchensink.mvi.BindingState
import io.reactivex.Observable

object CounterModel {
  fun bind(
      intentions: CounterIntentions,
      bindingStates: Observable<BindingState>
  ): Observable<CounterState> {
    return Observable.merge(
        Observable.never(),
        Observable.just(CounterState.INITIAL)
    )
  }
}
