package io.craftedcourses.kitchensink.bmi

import io.craftedcourses.kitchensink.mvi.ViewDriver

class BmiViewDriver : ViewDriver<BmiView, BmiState> {

  override fun render(view: BmiView, state: BmiState) {
    view.updateWeightText(state.weight)
    view.updateHeightText(state.height)
    view.updateBmiText(state.bmi)
  }
}