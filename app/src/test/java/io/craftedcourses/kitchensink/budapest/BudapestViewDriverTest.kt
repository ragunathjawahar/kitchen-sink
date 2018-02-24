package io.craftedcourses.kitchensink.budapest

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@Suppress("IllegalIdentifier") // JVM tests can have spaces in function names
class BudapestViewDriverTest {
  private lateinit var view: BudapestView
  private lateinit var viewDriver: BudapestViewDriver

  @Before fun setup() {
    // Given
    view = mock(BudapestView::class.java)
    viewDriver = BudapestViewDriver(view)
  }

  @Test fun `greet stranger`() {
    // When
    val strangerState = BudapestState.STRANGER
    viewDriver.render(strangerState)

    // Then
    verify(view).greetStranger()
    verifyNoMoreInteractions(view)
  }

  @Test fun `greet person`() {
    // When
    val personState = BudapestState("Alan")
    viewDriver.render(personState)

    // Then
    verify(view).greetPerson(personState.name)
    verifyNoMoreInteractions(view)
  }
}
