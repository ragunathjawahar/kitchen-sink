package io.craftedcourses.kitchensink.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import io.craftedcourses.kitchensink.R
import io.craftedcourses.kitchensink.mvi.MviDelegate
import kotlinx.android.synthetic.main.counter_activity.*

class CounterActivity : AppCompatActivity(), CounterView {
  private val mviDelegate: MviDelegate<CounterState> by lazy {
    MviDelegate<CounterState>()
  }

  private val intentions: CounterIntentions by lazy {
    CounterIntentions(incrementButton.clicks(), decrementButton.clicks())
  }

  private val viewDriver: CounterViewDriver by lazy {
    CounterViewDriver(this)
  }

  override fun displayCounter(value: Int) {
    counterTextView.text = value.toString()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.counter_activity)
    mviDelegate.onCreate(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    val bindingFunction = {
      CounterModel.bind(intentions, mviDelegate.bindings(), mviDelegate.states())
    }
    val renderFunction: (CounterState) -> Unit = { viewDriver.render(it) }

    mviDelegate.onStart(bindingFunction, renderFunction)
  }

  override fun onStop() {
    mviDelegate.onStop()
    super.onStop()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    mviDelegate.onSaveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    super.onRestoreInstanceState(savedInstanceState)
    mviDelegate.onRestoreInstanceState(savedInstanceState)
  }
}
