package io.craftedcourses.kitchensink.counter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

@Suppress("IllegalIdentifier") // JVM tests can have spaces in function names
class CounterStateTest {
  @Test fun `add positive digit`() {
    // Given
    val previousState = CounterState.INITIAL

    // When
    val nextState = previousState.add(1)

    // Then
    val expectedState = CounterState(1)
    assertThat(nextState).isEqualTo(expectedState)
    assertThat(nextState).isNotSameAs(previousState)
  }

  @Test fun `add negative digit`() {
    // Given
    val previousState = CounterState.INITIAL

    // When
    val nextState = previousState.add(-1)

    // Then
    val expectedState = CounterState(-1)
    assertThat(nextState).isEqualTo(expectedState)
    assertThat(nextState).isNotSameAs(previousState)
  }
}
