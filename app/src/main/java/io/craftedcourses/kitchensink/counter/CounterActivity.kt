package io.craftedcourses.kitchensink.counter

import com.jakewharton.rxbinding2.view.clicks
import io.craftedcourses.kitchensink.R
import io.craftedcourses.kitchensink.mvi.MviActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.counter_activity.*

class CounterActivity : MviActivity<CounterState>(), CounterView {
  private val intentions: CounterIntentions by lazy {
    CounterIntentions(incrementButton.clicks(), decrementButton.clicks())
  }

  private val viewDriver: CounterViewDriver by lazy {
    CounterViewDriver(this)
  }

  override fun layoutResId(): Int =
      R.layout.counter_activity

  override fun bindingFunction(): () -> Observable<CounterState> =
      { CounterModel.bind(intentions, bindings, states) }

  override fun renderFunction(): (CounterState) -> Unit =
      { state -> viewDriver.render(state) }

  override fun displayCounter(value: Int) {
    counterTextView.text = value.toString()
  }
}
