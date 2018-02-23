package io.craftedcourses.kitchensink.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import io.craftedcourses.kitchensink.R
import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_counter.*

class CounterActivity : AppCompatActivity(), CounterView {
  private var newBinding = true

  private lateinit var disposable: Disposable
  private val bindings: BehaviorSubject<Binding>    = BehaviorSubject.createDefault(Binding.NEW)
  private val states: BehaviorSubject<CounterState> = BehaviorSubject.create()

  private val intentions: CounterIntentions by lazy {
    CounterIntentions(incrementButton.clicks(), decrementButton.clicks())
  }

  private val viewDriver: CounterViewDriver by lazy {
    CounterViewDriver(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_counter)
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
      newBinding = false
    }
    super.onStop()
  }

  override fun displayCounter(value: Int) {
    counterTextView.text = value.toString()
  }
}
