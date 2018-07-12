package io.craftedcourses.kitchensink.bmi

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

class BmiModelTest {

  private lateinit var weightChanges: PublishSubject<Int>
  private lateinit var heightChanges: PublishSubject<Int>
  private lateinit var intentions: BmiIntentions
  private lateinit var bindings: PublishSubject<Binding>
  private lateinit var states: BehaviorSubject<BmiState>
  private lateinit var bmiModelObservable: Observable<BmiState>
  private lateinit var observer: TestObserver<BmiState>
  private lateinit var statesDisposable: Disposable

  @Before
  fun setUp() {
    weightChanges = PublishSubject.create()
    heightChanges = PublishSubject.create()
    intentions = BmiIntentions(weightChanges, heightChanges)
    bindings = PublishSubject.create()
    states = BehaviorSubject.create()
    observer = TestObserver()

    bmiModelObservable = BmiModel.bind(intentions, bindings, states)
    statesDisposable = bmiModelObservable.subscribe { states.onNext(it) }
    bmiModelObservable.subscribe(observer)
  }

  @Test
  fun `user sees default weight height`() {
    observer.assertStates(
        { newBinding() } emits BmiState.DEFAULT
    )
  }

  private fun newBinding() {
    bindings.onNext(Binding.NEW)
  }

  @Test
  fun `user can slide weight seekbar`() {
    val bmiState = BmiState.DEFAULT
    observer.assertStates(
        { newBinding() } emits bmiState,
        { changeWeight(10) } emits bmiState.copy(weight = 51)
    )
  }

  private fun changeWeight(weightProgress: Int) {
    weightChanges.onNext(weightProgress)
  }

  @Test
  fun `user can slide height seekbar`() {
    val bmiState = BmiState.DEFAULT
    observer.assertStates(
        { newBinding() } emits bmiState,
        { changeHeight(10) } emits bmiState.copy(height = 157)
    )
  }

  private fun changeHeight(heightProgress: Int) {
    heightChanges.onNext(heightProgress)
  }

  @Test
  fun `user can slide weight and height seekbar`() {
    val bmiState = BmiState.DEFAULT
    observer.assertStates(
        { newBinding() } emits bmiState,
        { changeWeight(10) } emits bmiState.copy(weight = 51),
        { changeHeight(10) } emits bmiState.copy(weight = 51, height = 157)
    )
  }


  @Test
  fun `user sees previous state when subscription is restored`() {
    // Setup
    val bmiState = BmiState.DEFAULT
    observer.assertStates(
        { newBinding() } emits bmiState,
        { changeWeight(10) } emits bmiState.copy(weight = 51),
        { changeHeight(10) } emits bmiState.copy(weight = 51, height = 157)
    )
    val lastKnownState = states.value

    // Act
    val newObserver = disposeAndResubscribe()

    // Assert
    newObserver.assertStates(
        { restoredBinding() } emits lastKnownState
    )
  }

  private fun restoredBinding() {
    bindings.onNext(Binding.RESTORED)
  }

  private fun disposeAndResubscribe(): TestObserver<BmiState> {
    // Dispose
    statesDisposable.dispose()
    observer.dispose()

    // Subscribe again...
    statesDisposable = bmiModelObservable.subscribe { states.onNext(it) }
    return bmiModelObservable.test()
  }
}