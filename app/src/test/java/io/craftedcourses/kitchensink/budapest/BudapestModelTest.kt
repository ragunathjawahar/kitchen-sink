package io.craftedcourses.kitchensink.budapest

import io.craftedcourses.kitchensink.infra.assertStates
import io.craftedcourses.kitchensink.infra.emits
import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

@Suppress("IllegalIdentifier") // JVM tests can have space in function names
class BudapestModelTest {
  private lateinit var nameTextChanges: PublishSubject<CharSequence>
  private lateinit var intentions: BudapestIntentions
  private lateinit var bindings: PublishSubject<Binding>
  private lateinit var observer: TestObserver<BudapestState>

  @Before fun setup() {
    nameTextChanges = PublishSubject.create<CharSequence>()
    intentions = BudapestIntentions(nameTextChanges)
    bindings = PublishSubject.create<Binding>()
    observer = TestObserver()

    BudapestModel.bind(intentions, bindings).subscribe(observer)
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

  private fun enterName(name: String) {
    nameTextChanges.onNext(name)
  }

  private fun newBinding() {
    bindings.onNext(Binding.NEW)
  }
}
