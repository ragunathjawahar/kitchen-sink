package io.craftedcourses.kitchensink.counter

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
    // Act
    bindings.onNext(Binding.NEW)

    // Assert
    with(observer) {
      assertNoErrors()
      assertValues(CounterState.INITIAL)
      assertNotTerminated()
    }
  }

  @Test fun `user can increment counter`() {
    // Act
    bindings.onNext(Binding.NEW)

    incrementClicks.onNext(Unit)
    incrementClicks.onNext(Unit)

    // Assert
    with(observer) {
      assertNoErrors()
      assertValues(
          CounterState.INITIAL,
          CounterState(1),
          CounterState(2)
      )
      assertNotTerminated()
    } // TODO(rj) 23/Feb/18 - A nicer API for assertions
  }

  @Test fun `user can decrement counter`() {
    // Act
    bindings.onNext(Binding.NEW)

    decrementClicks.onNext(Unit)
    decrementClicks.onNext(Unit)

    // Assert
    with(observer) {
      assertNoErrors()
      assertValues(
          CounterState.INITIAL,
          CounterState(-1),
          CounterState(-2)
      )
      assertNotTerminated()
    }
  }

  @Test fun `user can increment and decrement counter`() {
    // Act
    bindings.onNext(Binding.NEW)

    decrementClicks.onNext(Unit)
    incrementClicks.onNext(Unit)
    incrementClicks.onNext(Unit)
    incrementClicks.onNext(Unit)
    incrementClicks.onNext(Unit)

    // Assert
    with(observer) {
      assertNoErrors()
      assertValues(
          CounterState.INITIAL,
          CounterState(-1),
          CounterState(0),
          CounterState(1),
          CounterState(2),
          CounterState(3)
      )
      assertNotTerminated()
    }
  }
}
