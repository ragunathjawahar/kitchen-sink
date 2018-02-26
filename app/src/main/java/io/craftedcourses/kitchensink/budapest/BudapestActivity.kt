package io.craftedcourses.kitchensink.budapest

import com.jakewharton.rxbinding2.widget.textChanges
import io.craftedcourses.kitchensink.R
import io.craftedcourses.kitchensink.mvi.MviActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.budapest_activity.*
import kotlin.LazyThreadSafetyMode.NONE

class BudapestActivity : MviActivity<BudapestState>(), BudapestView {
  private val intentions: BudapestIntentions by lazy(NONE) {
    BudapestIntentions(nameEditText.textChanges().skipInitialValue())
  }

  private val viewDriver: BudapestViewDriver by lazy(NONE) {
    BudapestViewDriver(this)
  }

  override fun layoutResId(): Int =
      R.layout.budapest_activity

  override fun bindingFunction(): () -> Observable<BudapestState> =
      { BudapestModel.bind(intentions, bindings, states) }

  override fun renderFunction(): (BudapestState) -> Unit =
      { state -> viewDriver.render(state) }

  override fun greetStranger() {
    greetingTextView.setText(R.string.hello_stranger)
  }

  override fun greetPerson(name: String) {
    greetingTextView.text = getString(R.string.hello_person, name)
  }
}
