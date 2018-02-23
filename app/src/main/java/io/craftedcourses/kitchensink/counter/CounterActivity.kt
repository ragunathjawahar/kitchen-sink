package io.craftedcourses.kitchensink.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.craftedcourses.kitchensink.R

class CounterActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_counter)
  }
}
