package io.craftedcourses.kitchensink.budapest

import io.craftedcourses.kitchensink.infra.assertStates
import io.craftedcourses.kitchensink.infra.emits
import io.craftedcourses.kitchensink.mvi.Binding
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Test

@Suppress("IllegalIdentifier") // JVM tests can have space in function names
class BudapestModelTest {
  @Test fun `user sees stranger state when UI is setup`() {
    val nameTextChanges = PublishSubject.create<CharSequence>()
    val intentions = BudapestIntentions(nameTextChanges)
    val bindings = PublishSubject.create<Binding>()

    val observer = TestObserver<BudapestState>()
    BudapestModel.bind(intentions, bindings).subscribe(observer)

    observer.assertStates(
        { bindings.onNext(Binding.NEW) } emits BudapestState.STRANGER
    )
  }
}
