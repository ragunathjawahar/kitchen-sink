package io.craftedcourses.kitchensink.bmi

import io.reactivex.Observable

class BmiIntentions(
    private val weightSliderChanges: Observable<Int>,
    private val heightSliderChanges: Observable<Int>
) {

  fun changeWeight(): Observable<Int> =
      weightSliderChanges

  fun changeHeight(): Observable<Int> =
      heightSliderChanges
}