package io.craftedcourses.kitchensink.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import io.craftedcourses.kitchensink.R
import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.counter_activity.*

private const val KEY_STATE = "state"

class CounterActivity : AppCompatActivity(), CounterView {
  private var newBinding = true

  private lateinit var disposable: Disposable
  private val bindings: PublishSubject<Binding>     = PublishSubject.create()
  private val states: BehaviorSubject<CounterState> = BehaviorSubject.create()

  private val intentions: CounterIntentions by lazy {
    CounterIntentions(incrementButton.clicks(), decrementButton.clicks())
  }

  private val viewDriver: CounterViewDriver by lazy {
    CounterViewDriver(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.counter_activity)
    savedInstanceState?.let {
      states.onNext(savedInstanceState.getParcelable(KEY_STATE))
      newBinding = false
    }
  }

  override fun onStart() {
    super.onStart()
    val counterStates = CounterModel.bind(intentions, bindings, states).share()
    counterStates.subscribe(states)
    disposable = counterStates.subscribe { viewDriver.render(it) }

    bindings.onNext(if (newBinding) Binding.NEW else Binding.RESTORED)
  }

  override fun onStop() {
    if (!disposable.isDisposed) {
      disposable.dispose()
      newBinding = false // TODO(rj) 12/Feb/18 - Determine if new based on saved state
    }
    super.onStop()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    outState?.putParcelable(KEY_STATE, states.value)
    super.onSaveInstanceState(outState)
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    savedInstanceState?.let {
      states.onNext(savedInstanceState.getParcelable(KEY_STATE))
    }
    super.onRestoreInstanceState(savedInstanceState)
  }

  override fun displayCounter(value: Int) {
    counterTextView.text = value.toString()
  }
}
