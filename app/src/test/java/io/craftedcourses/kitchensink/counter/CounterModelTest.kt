package io.craftedcourses.kitchensink.counter

import io.craftedcourses.kitchensink.infra.assertStates
import io.craftedcourses.kitchensink.infra.emits
import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier") // JVM tests can have spaces in function names
class CounterModelTest {
  private lateinit var incrementClicks: PublishSubject<Unit>
  private lateinit var decrementClicks: PublishSubject<Unit>
  private lateinit var intentions: CounterIntentions
  private lateinit var bindings: PublishSubject<Binding>
  private lateinit var states: BehaviorSubject<CounterState>
  private lateinit var observer: TestObserver<CounterState>

  @Before fun setup() {
    incrementClicks = PublishSubject.create()
    decrementClicks = PublishSubject.create()
    intentions      = CounterIntentions(incrementClicks, decrementClicks)
    bindings        = PublishSubject.create()
    states          = BehaviorSubject.create()
    observer        = TestObserver()

    val counterStates = CounterModel.bind(intentions, bindings, states).share()
    counterStates.subscribe(states)
    counterStates.subscribe(observer)
  }

  @Test fun `user sees zero when UI is setup`() {
    observer.assertStates(
        { newBinding() } emits CounterState.INITIAL
    )
  }

  @Test fun `user can increment counter`() {
    observer.assertStates(
        { newBinding() } emits CounterState.INITIAL,
        { increment()  } emits CounterState(1),
        { increment()  } emits CounterState(2)
    )
  }

  @Test fun `user can decrement counter`() {
    observer.assertStates(
        { newBinding() } emits CounterState.INITIAL,
        { decrement()  } emits CounterState(-1),
        { decrement()  } emits CounterState(-2)
    )
  }

  @Test fun `user can increment and decrement counter`() {
    observer.assertStates(
        { newBinding() } emits CounterState.INITIAL,
        { increment()  } emits CounterState(1),
        { increment()  } emits CounterState(2),
        { increment()  } emits CounterState(3),
        { decrement()  } emits CounterState(2)
    )
  }

  private fun newBinding() {
    bindings.onNext(Binding.NEW)
  }

  private fun increment() {
    incrementClicks.onNext(Unit)
  }

  private fun decrement() {
    decrementClicks.onNext(Unit)
  }
}
