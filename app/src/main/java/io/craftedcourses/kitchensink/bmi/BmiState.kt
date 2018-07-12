package io.craftedcourses.kitchensink.bmi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BmiState(
    val weight: Int,
    val height: Int
) : Parcelable {

  companion object {
    private const val MIN_WEIGHT_KG = 40
    private const val MAX_WEIGHT_KG = 150
    private const val MIN_HEIGHT_CM = 150
    private const val MAX_HEIGHT_CM = 220
    val weightRangeKg = MIN_WEIGHT_KG to MAX_WEIGHT_KG
    val heightRangeCm = MIN_HEIGHT_CM to MAX_HEIGHT_CM
    val DEFAULT = BmiState(MIN_WEIGHT_KG, MIN_HEIGHT_CM)
  }

  val bmi: Int
    get() = BmiCalculator.calculateBmi(weight, height)

  fun updateWeight(weight: Int): BmiState =
      copy(weight = weight)

  fun updateHeight(height: Int): BmiState =
      copy(height = height)
}