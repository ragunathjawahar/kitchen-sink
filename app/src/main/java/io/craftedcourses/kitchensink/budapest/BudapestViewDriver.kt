package io.craftedcourses.kitchensink.budapest

class BudapestViewDriver(private val view: BudapestView) {
  fun render(state: BudapestState) {
    if (state.name.isBlank()) {
      view.greetStranger()
    } else {
      view.greetPerson(state.name)
    }
  }
}
