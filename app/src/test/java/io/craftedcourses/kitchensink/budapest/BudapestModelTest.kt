package io.craftedcourses.kitchensink.budapest

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

@Suppress("IllegalIdentifier") // JVM tests can have space in function names
class BudapestModelTest {
  private lateinit var nameTextChanges: PublishSubject<CharSequence>
  private lateinit var intentions: BudapestIntentions
  private lateinit var bindings: PublishSubject<Binding>
  private lateinit var states: BehaviorSubject<BudapestState>
  private lateinit var statesDisposable: Disposable
  private lateinit var budapestModelObservable: Observable<BudapestState>
  private lateinit var observer: TestObserver<BudapestState>

  @Before fun setup() {
    nameTextChanges = PublishSubject.create<CharSequence>()
    intentions      = BudapestIntentions(nameTextChanges)
    bindings        = PublishSubject.create()
    states          = BehaviorSubject.create()
    observer        = TestObserver()

    budapestModelObservable = BudapestModel.bind(intentions, bindings, states).share()
    statesDisposable = budapestModelObservable.subscribe { states.onNext(it) }
    budapestModelObservable.subscribe(observer)
  }

  @Test fun `user sees stranger state when UI is setup`() {
    observer.assertStates(
        { newBinding() } emits BudapestState.STRANGER
    )
  }

  @Test fun `user sees a name that they entered`() {
    observer.assertStates(
        { newBinding()      } emits BudapestState.STRANGER,
        { enterName("Alan") } emits BudapestState("Alan")
    )
  }

  @Test fun `user sees previous state when subscription is restored`() {
    // Setup
    observer.assertStates(
        { newBinding()      } emits BudapestState.STRANGER,
        { enterName("Alan") } emits BudapestState("Alan")
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

  private fun enterName(name: String) {
    nameTextChanges.onNext(name)
  }

  private fun disposeAndResubscribe(): TestObserver<BudapestState> {
    // Dispose
    statesDisposable.dispose()
    observer.dispose()

    // Subscribe again...
    val observer = TestObserver<BudapestState>()
    statesDisposable = budapestModelObservable.subscribe { states.onNext(it) }
    budapestModelObservable.subscribe(observer)
    return observer
  }
}
