package io.craftedcourses.kitchensink.budapest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.craftedcourses.kitchensink.R
import kotlinx.android.synthetic.main.budapest_activity.*

class BudapestActivity : AppCompatActivity(), BudapestView {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.budapest_activity)
  }

  override fun greetStranger() {
    greetingTextView.setText(R.string.hello_stranger)
  }

  override fun greetPerson(name: String) {
    greetingTextView.text = getString(R.string.hello_person, name)
  }
}
