package io.craftedcourses.kitchensink.bmi

import com.jakewharton.rxbinding2.widget.RxSeekBar
import io.craftedcourses.kitchensink.R
import io.craftedcourses.kitchensink.mvi.MviActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_bmi.*
import kotlin.LazyThreadSafetyMode.NONE

class BmiActivity : MviActivity<BmiState>(), BmiView {

  private val intentions: BmiIntentions by lazy(NONE) {
    BmiIntentions(RxSeekBar.changes(weightSeekBar), RxSeekBar.changes(heightSeekBar))
  }

  private val viewDriver: BmiViewDriver by lazy(NONE) {
    BmiViewDriver()
  }

  override fun layoutResId(): Int =
      R.layout.activity_bmi

  override fun bindingFunction(): () -> Observable<BmiState> {
    return { BmiModel.bind(intentions, bindings, states) }
  }

  override fun renderFunction(): (BmiState) -> Unit {
    return { state -> viewDriver.render(this, state) }
  }

  override fun updateWeightText(weight: Int) {
    weightTextView.text = getString(R.string.weight_format, weight)
  }

  override fun updateHeightText(height: Int) {
    heightTextView.text = getString(R.string.height_format, height)
  }

  override fun updateBmiText(bmi: Int) {
    bmiTextView.text = getString(R.string.bmi_format, bmi)
  }
}