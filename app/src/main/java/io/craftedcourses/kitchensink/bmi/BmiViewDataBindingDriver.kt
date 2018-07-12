package io.craftedcourses.kitchensink.bmi

import io.craftedcourses.kitchensink.databinding.ActivityBmiBinding
import io.craftedcourses.kitchensink.mvi.ViewDataBindingDriver

class BmiViewDataBindingDriver : ViewDataBindingDriver<ActivityBmiBinding, BmiState> {

  override fun render(viewDataBinding: ActivityBmiBinding, state: BmiState) {
    viewDataBinding.state = state
  }
}