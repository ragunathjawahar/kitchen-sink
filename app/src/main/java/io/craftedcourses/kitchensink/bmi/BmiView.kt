package io.craftedcourses.kitchensink.bmi

interface BmiView {
  fun updateWeightText(weight: Int)
  fun updateHeightText(height: Int)
  fun updateBmiText(bmi: Int)
}