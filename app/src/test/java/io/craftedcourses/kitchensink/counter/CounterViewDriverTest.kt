package io.craftedcourses.kitchensink.counter

import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@Suppress("IllegalIdentifier") // JVM tests can have spaces in function names
class CounterViewDriverTest {
  @Test fun `display counter value`() {
    // Given
    val view = mock(CounterView::class.java)
    val viewDriver = CounterViewDriver(view)

    // When
    val counter = 5
    viewDriver.render(CounterState(5))

    // Then
    verify(view).displayCounter(counter)
  }
}
