package io.craftedcourses.kitchensink.counter

import com.jakewharton.rxbinding2.view.clicks
import io.craftedcourses.kitchensink.R
import io.craftedcourses.kitchensink.mvi.MviActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.counter_activity.*
import kotlin.LazyThreadSafetyMode.NONE

class CounterActivity : MviActivity<CounterState>(), CounterView {
  private val intentions: CounterIntentions by lazy(NONE) {
    CounterIntentions(incrementButton.clicks(), decrementButton.clicks())
  }

  private val viewDriver: CounterViewDriver by lazy(NONE) {
    CounterViewDriver()
  }

  override fun layoutResId(): Int =
      R.layout.counter_activity

  override fun bindingFunction(): () -> Observable<CounterState> =
      { CounterModel.bind(intentions, bindings, states) }

  override fun renderFunction(): (CounterState) -> Unit =
      { state -> viewDriver.render(this, state) }

  override fun displayCounter(value: Int) {
    counterTextView.text = value.toString()
  }
}
