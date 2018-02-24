package io.craftedcourses.kitchensink.budapest

import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

object BudapestModel {
  fun bind(
      intentions: BudapestIntentions,
      bindings: Observable<Binding>,
      states: Observable<BudapestState>
  ): Observable<BudapestState> {
    return Observable.merge(
        newBindingUseCase(bindings),
        restoredBindingUseCase(bindings, states),
        enterNameUseCase(intentions.enterName())
    )
  }

  private fun newBindingUseCase(
      bindings: Observable<Binding>
  ): Observable<BudapestState> {
    return bindings
        .filter { it == Binding.NEW }
        .map    { BudapestState.STRANGER }
  }

  private fun restoredBindingUseCase(
      bindings: Observable<Binding>,
      states: Observable<BudapestState>
  ): Observable<BudapestState>? {
    val combiner = BiFunction<Binding, BudapestState, BudapestState> { _, previousState ->
      previousState
    }
    return bindings
        .filter { it == Binding.RESTORED }
        .withLatestFrom(states, combiner)
  }

  private fun enterNameUseCase(
      enterName: Observable<String>
  ): Observable<BudapestState> {
    return enterName
        .map { BudapestState(it) }
  }
}
