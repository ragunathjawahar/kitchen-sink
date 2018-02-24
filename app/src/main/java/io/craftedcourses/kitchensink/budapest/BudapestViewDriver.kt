package io.craftedcourses.kitchensink.budapest

import io.craftedcourses.kitchensink.mvi.ViewDriver

class BudapestViewDriver(
    private val view: BudapestView
) : ViewDriver<BudapestView, BudapestState>(view) {
  override fun render(state: BudapestState) {
    if (state.name.isBlank()) {
      view.greetStranger()
    } else {
      view.greetPerson(state.name)
    }
  }
}
