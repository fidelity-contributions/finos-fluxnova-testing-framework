package org.finos.fluxnova.bpm.test.data.acl.apply

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData.stringVariable
import org.finos.fluxnova.bpm.test.data.Writers.C7.builder
import org.assertj.core.api.Assertions
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class LocalScopeReplaceStrategyTest {

  private val FOO = stringVariable("foo")

  @Test
  fun `should apply local`() {
    val variables = builder().set(FOO, "bar").build()
    val executionMock = mock(DelegateExecution::class.java)

    LocalScopeReplaceStrategy.apply(variables, executionMock)

    verify(executionMock, never()).variables = any()
    verify(executionMock).variablesLocal = variables
    verifyNoMoreInteractions(executionMock)

    Assertions.assertThat(LocalScopeReplaceStrategy.toString()).isEqualTo(LocalScopeReplaceStrategy::class.java.canonicalName)
  }
}
