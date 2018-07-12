package io.craftedcourses.kitchensink.bmi

object BmiCalculator {

  fun calculateBmi(weight: Int, height: Int): Int {
    val heightMeters = height * 0.01
    return (weight / (heightMeters * heightMeters)).toInt()
  }
}