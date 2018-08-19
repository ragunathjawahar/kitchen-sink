package io.craftedcourses.kitchensink.bmi

import io.craftedcourses.kitchensink.bmi.BmiState.Companion.heightRangeCm
import io.craftedcourses.kitchensink.bmi.BmiState.Companion.weightRangeKg
import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxkotlin.withLatestFrom

object BmiModel {

  fun bind(intentions: BmiIntentions,
           bindings: Observable<Binding>,
           states: Observable<BmiState>
  ): Observable<BmiState> {
    val weightChanges = getAbsoluteChanges(intentions.changeWeight(), weightRangeKg)
    val heightChanges = getAbsoluteChanges(intentions.changeHeight(), heightRangeCm)
    return Observable.merge(
        newBindingUseCase(bindings),
        restoredBindingUseCase(bindings, states),
        changeWeightUseCase(weightChanges, states),
        changeHeightUseCase(heightChanges, states)
    )
  }

  private fun getAbsoluteChanges(progressChanges: Observable<Int>,
                                 range: Pair<Int, Int>
  ): Observable<Int> {
    return progressChanges.map { progressToValue(it, range) }
  }

  private fun progressToValue(progress: Int,
                              range: Pair<Int, Int>
  ): Int {
    val min = range.first
    val max = range.second
    return (progress * (max - min) / 100) + min
  }

  private fun newBindingUseCase(
      bindings: Observable<Binding>
  ): Observable<BmiState> {
    return bindings
        .filter { it == Binding.NEW }
        .map { BmiState.DEFAULT }
  }

  private fun restoredBindingUseCase(
      bindings: Observable<Binding>,
      states: Observable<BmiState>
  ): ObservableSource<BmiState> {
    return bindings
        .filter { it == Binding.RESTORED }
        .withLatestFrom(states) { _, previousState -> previousState }
  }

  private fun changeWeightUseCase(
      weightChanges: Observable<Int>,
      states: Observable<BmiState>
  ): Observable<BmiState> {
    return weightChanges.withLatestFrom(states) { weight, previousState ->
      previousState.updateWeight(weight)
    }
  }

  private fun changeHeightUseCase(
      heightChanges: Observable<Int>,
      states: Observable<BmiState>
  ): Observable<BmiState> {
    return heightChanges.withLatestFrom(states) { height, previousState ->
      previousState.updateHeight(height)
    }
  }
}