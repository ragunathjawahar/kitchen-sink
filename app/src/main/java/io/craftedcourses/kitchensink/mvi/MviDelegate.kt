package io.craftedcourses.kitchensink.mvi

import android.os.Bundle
import android.os.Parcelable
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

private const val KEY_STATE = "state"

class MviDelegate<T : Parcelable> {
  private var newBinding = true
  private lateinit var disposable: Disposable
  private val bindings: PublishSubject<Binding> = PublishSubject.create()
  private val states: BehaviorSubject<T> = BehaviorSubject.create()

  fun onCreate(savedInstanceState: Bundle?) {
    savedInstanceState?.let {
      states.onNext(savedInstanceState.getParcelable(KEY_STATE))
      newBinding = false
    }
  }

  fun onStart(bindingFunction: () -> Observable<T>, renderFunction: (state: T) -> Unit) {
    val modelStates = bindingFunction().share()
    modelStates.subscribe(states)
    disposable = modelStates.subscribe { renderFunction(it) }

    bindings.onNext(if (newBinding) Binding.NEW else Binding.RESTORED)
  }

  fun onStop() {
    if (!disposable.isDisposed) {
      disposable.dispose()
      newBinding = false // TODO(rj) 12/Feb/18 - Determine if new based on saved state
    }
  }

  fun onSaveInstanceState(outState: Bundle?) {
    outState?.putParcelable(KEY_STATE, states.value)
  }

  fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    savedInstanceState?.let {
      states.onNext(savedInstanceState.getParcelable(KEY_STATE))
    }
  }

  fun bindings(): Observable<Binding> =
      bindings.toFlowable(BackpressureStrategy.LATEST).toObservable()

  fun states(): Observable<T> =
      states.toFlowable(BackpressureStrategy.LATEST).toObservable()
}
