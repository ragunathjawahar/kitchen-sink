package io.craftedcourses.kitchensink.counter

import io.craftedcourses.kitchensink.mvi.BindingState
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Test

@Suppress("IllegalIdentifier") // JVM tests can have spaces in function names
class CounterModelTest {
  @Test fun `user sees zero when UI is setup`() {
    // Setup
    val incrementClicks = PublishSubject.create<Unit>()
    val decrementClicks = PublishSubject.create<Unit>()
    val bindingStates   = PublishSubject.create<BindingState>()
    val intentions      = CounterIntentions(incrementClicks, decrementClicks)
    val observer        = TestObserver<CounterState>()

    CounterModel.bind(intentions, bindingStates).subscribe(observer)

    // Act
    bindingStates.onNext(BindingState.NEW)

    // Assert
    observer.assertNoErrors()
    observer.assertValue(CounterState.INITIAL)
    observer.assertNotTerminated()
  }
}
