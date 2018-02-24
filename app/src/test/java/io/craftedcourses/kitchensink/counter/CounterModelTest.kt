package io.craftedcourses.kitchensink.counter

import io.craftedcourses.kitchensink.infra.assertStates
import io.craftedcourses.kitchensink.infra.emits
import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
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
  private lateinit var counterModelObservable: Observable<CounterState>
  private lateinit var observer: TestObserver<CounterState>
  private lateinit var statesDisposable: Disposable

  @Before fun setup() {
    incrementClicks = PublishSubject.create()
    decrementClicks = PublishSubject.create()
    intentions      = CounterIntentions(incrementClicks, decrementClicks)
    bindings        = PublishSubject.create()
    states          = BehaviorSubject.create()
    observer        = TestObserver()

    counterModelObservable = CounterModel.bind(intentions, bindings, states).share()
    statesDisposable       = counterModelObservable.subscribe { states.onNext(it) }
    counterModelObservable.subscribe(observer)
  }

  @Test fun `user sees zero when UI is setup`() {
    observer.assertStates(
        { newBinding() } emits CounterState.ZERO
    )
  }

  @Test fun `user can increment counter`() {
    observer.assertStates(
        { newBinding() } emits CounterState.ZERO,
        { increment()  } emits CounterState(1),
        { increment()  } emits CounterState(2)
    )
  }

  @Test fun `user can decrement counter`() {
    observer.assertStates(
        { newBinding() } emits CounterState.ZERO,
        { decrement()  } emits CounterState(-1),
        { decrement()  } emits CounterState(-2)
    )
  }

  @Test fun `user can increment and decrement counter`() {
    observer.assertStates(
        { newBinding() } emits CounterState.ZERO,
        { increment()  } emits CounterState(1),
        { increment()  } emits CounterState(2),
        { increment()  } emits CounterState(3),
        { decrement()  } emits CounterState(2)
    )
  }

  @Test fun `user sees previous state when subscription is restored`() {
    // Setup
    observer.assertStates(
        { newBinding() } emits CounterState.ZERO,
        { increment()  } emits CounterState(1),
        { increment()  } emits CounterState(2)
    )
    val lastKnownState = states.value

    // Act
    val newObserver = disposeAndResubscribe()

    // Assert
    newObserver.assertStates(
        { restoredBinding() } emits lastKnownState
    )
  }

  private fun newBinding() {
    bindings.onNext(Binding.NEW)
  }

  private fun restoredBinding() {
    bindings.onNext(Binding.RESTORED)
  }

  private fun increment() {
    incrementClicks.onNext(Unit)
  }

  private fun decrement() {
    decrementClicks.onNext(Unit)
  }

  private fun disposeAndResubscribe(): TestObserver<CounterState> {
    // Dispose
    statesDisposable.dispose()
    observer.dispose()

    // Subscribe again...
    val observer = TestObserver<CounterState>()
    counterModelObservable.subscribe(states)
    counterModelObservable.subscribe(observer)
    return observer
  }
}
