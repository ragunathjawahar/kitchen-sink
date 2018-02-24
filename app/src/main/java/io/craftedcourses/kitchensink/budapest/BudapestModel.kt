package io.craftedcourses.kitchensink.budapest

import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.Observable

object BudapestModel {
  fun bind(
      intentions: BudapestIntentions,
      bindings: Observable<Binding>
  ): Observable<BudapestState> {
    return bindings
        .filter { it == Binding.NEW }
        .map    { BudapestState.STRANGER }
  }
}
