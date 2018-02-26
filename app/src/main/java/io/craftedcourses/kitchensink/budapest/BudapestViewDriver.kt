package io.craftedcourses.kitchensink.budapest

import io.craftedcourses.kitchensink.mvi.ViewDriver

class BudapestViewDriver : ViewDriver<BudapestView, BudapestState> {
  override fun render(view: BudapestView, state: BudapestState) {
    if (state.name.isBlank()) {
      view.greetStranger()
    } else {
      view.greetPerson(state.name)
    }
  }
}
