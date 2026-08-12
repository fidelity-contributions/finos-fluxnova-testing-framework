package org.finos.fluxnova.bpm.test.data.guard.integration

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData.stringVariable
import org.finos.fluxnova.bpm.test.data.guard.VariablesGuard
import org.finos.fluxnova.bpm.test.data.guard.condition.exists
import org.finos.fluxnova.bpm.test.data.guard.condition.hasValue
import org.finos.fluxnova.bpm.test.mockito.delegate.DelegateExecutionFake
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

val ORDER_REFERENCE = stringVariable("orderReference")

class GuardExecutionListenerTest {

  @Test
  fun `should do nothing`() {
    val execution = DelegateExecutionFake.of().withId("4711").withCurrentActivityName("some")
    ORDER_REFERENCE.on(execution).set("1")

    val listener = createListener(true)
    listener.notify(execution)

    // nothing to do here
    assertThat(true).isTrue
  }

  @Test
  fun `should not throw exception if disabled `() {
    val execution = DelegateExecutionFake.of().withId("4711").withCurrentActivityName("some")
    ORDER_REFERENCE.on(execution).set("2")

    val listener = createListener(false)
    listener.notify(execution)

    // nothing to do here
    assertThat(true).isTrue
  }

  @Test
  fun `should throw exception if enabled `() {
    val execution = DelegateExecutionFake.of().withId("4711").withCurrentActivityName("some")
    ORDER_REFERENCE.on(execution).set("2")

    val listener = createListener(true)
    assertThrows<GuardViolationException>(
      "Guard violated by execution '${execution.id}' in activity '${execution.currentActivityName}'",
    ) {
      listener.notify(execution)
    }
  }

  @Test
  fun `should print name of named guard`() {
    val execution = DelegateExecutionFake()

    val listener = DefaultGuardExecutionListener(VariablesGuard("NamedGuard", listOf(ORDER_REFERENCE.exists())))
    val exception = assertThrows<GuardViolationException> {
      listener.notify(execution)
    }
    assertThat(exception.message).startsWith("NamedGuard")
  }

  private fun createListener(throwE: Boolean = true) =
    DefaultGuardExecutionListener(VariablesGuard(listOf(ORDER_REFERENCE.hasValue("1"))), throwE)
}
