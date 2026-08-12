package org.finos.fluxnova.bpm.test.data.factory

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData
import org.assertj.core.api.Assertions
import org.finos.fluxnova.bpm.engine.variable.Variables
import org.junit.jupiter.api.Test
import java.util.*

class UUIDVariableFactoryTest {

  private val variables = Variables.createVariables()

  @Test
  fun readWriteUuid() {
    val anUuid = UUID.randomUUID()
    val uuidVariable = FluxnovaBpmData.uuidVariable("myUuid")

    uuidVariable.on(variables).set(anUuid)
    val result: UUID = uuidVariable.from(variables).get()
    Assertions.assertThat(result).isEqualTo(anUuid)
  }
}
