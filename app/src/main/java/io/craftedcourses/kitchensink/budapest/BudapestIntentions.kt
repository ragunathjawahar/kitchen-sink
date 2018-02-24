package io.craftedcourses.kitchensink.budapest

import io.reactivex.Observable

class BudapestIntentions(
    private val nameTextChanges: Observable<CharSequence>
) {
  fun enterName(): Observable<String> =
      nameTextChanges.map { it.toString() }
}
