package org.finos.fluxnova.bpm.test.data.guard.integration

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData
import org.finos.fluxnova.bpm.test.data.guard.FluxnovaBpmDataGuards
import org.finos.fluxnova.bpm.test.data.guard.GuardViolation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class GuardViolationExceptionTest {

  val FOO = FluxnovaBpmData.stringVariable("foo")
  val c1 = FluxnovaBpmDataGuards.exists(FOO)
  val c2 = FluxnovaBpmDataGuards.hasValue(FOO, "bar")

  @Test
  fun buildMessage() {

    val expected = "reason\n\tnot exists,\n\twrong VAL"
    val ex = GuardViolationException(reason = "reason", violations = listOf(
      GuardViolation(c1, Optional.empty(), "not exists"),
      GuardViolation(c2, Optional.of("VAL"), "wrong VAL")
    ))

    assertThat(ex.message).isEqualTo(expected)
  }
}
