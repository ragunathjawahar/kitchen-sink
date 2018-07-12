package io.craftedcourses.kitchensink.bmi

import com.jakewharton.rxbinding2.widget.RxSeekBar
import io.craftedcourses.kitchensink.R
import io.craftedcourses.kitchensink.databinding.ActivityBmiBinding
import io.craftedcourses.kitchensink.mvi.MviActivity
import io.craftedcourses.kitchensink.util.BindActivity
import io.reactivex.Observable
import kotlin.LazyThreadSafetyMode.NONE

class BmiActivity : MviActivity<BmiState>() {

  private val viewDataBinding: ActivityBmiBinding by BindActivity(layoutResId())

  private val intentions: BmiIntentions by lazy(NONE) {
    BmiIntentions(
        RxSeekBar.changes(viewDataBinding.weightSeekBar),
        RxSeekBar.changes(viewDataBinding.heightSeekBar)
    )
  }

  private val viewDriver: BmiViewDataBindingDriver by lazy(NONE) {
    BmiViewDataBindingDriver()
  }

  override fun layoutResId(): Int =
      R.layout.activity_bmi

  override fun bindingFunction(): () -> Observable<BmiState> {
    return { BmiModel.bind(intentions, bindings, states) }
  }

  override fun renderFunction(): (BmiState) -> Unit {
    return { state -> viewDriver.render(viewDataBinding, state) }
  }
}