package io.craftedcourses.kitchensink.mvi

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import kotlin.LazyThreadSafetyMode.NONE

abstract class MviActivity<T : Parcelable> : AppCompatActivity() { // TODO(rj) 24/Feb/18 - Remove constraint <T : Parcelable>
  private val mviDelegate: MviDelegate<T> by lazy(NONE) { MviDelegate<T>() }

  protected val bindings: Observable<Binding>
    get() = mviDelegate.bindings()

  protected val states: Observable<T>
    get() = mviDelegate.states()

  final override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layoutResId())
    mviDelegate.onCreate(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    mviDelegate.onStart(bindingFunction(), renderFunction())
  }

  override fun onStop() {
    mviDelegate.onStop()
    super.onStop()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    mviDelegate.onSaveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    super.onRestoreInstanceState(savedInstanceState)
    mviDelegate.onRestoreInstanceState(savedInstanceState)
  }

  @LayoutRes protected abstract fun layoutResId(): Int

  protected abstract fun bindingFunction(): () -> Observable<T>

  protected abstract fun renderFunction(): (T) -> Unit
}
