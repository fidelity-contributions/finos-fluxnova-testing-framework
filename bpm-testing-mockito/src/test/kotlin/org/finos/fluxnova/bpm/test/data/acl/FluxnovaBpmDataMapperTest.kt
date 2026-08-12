package org.finos.fluxnova.bpm.test.data.acl

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData.customVariable
import org.finos.fluxnova.bpm.test.data.acl.apply.GlobalScopeReplaceStrategy
import org.finos.fluxnova.bpm.test.data.acl.apply.LocalScopeReplaceStrategy
import org.finos.fluxnova.bpm.test.data.acl.transform.IdentityVariableMapTransformer
import org.finos.fluxnova.bpm.test.data.guard.VariablesGuard
import org.assertj.core.api.Assertions.assertThat
import org.finos.fluxnova.bpm.engine.variable.VariableMap
import org.junit.jupiter.api.Test

class FluxnovaBpmDataMapperTest {

  val TRANSIENT_VAR = customVariable("__transient__", VariableMap::class.java)

  @Test
  fun `should create transformingLocalReplace`() {
    val acl = FluxnovaBpmDataMapper.transformingLocalReplace(
      TRANSIENT_VAR.name,
      IdentityVariableMapTransformer
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard.EMPTY)
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(LocalScopeReplaceStrategy)
  }

  @Test
  fun `should create transformingGlobalReplace`() {
    val acl = FluxnovaBpmDataMapper.transformingGlobalReplace(
      TRANSIENT_VAR.name,
      IdentityVariableMapTransformer
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard.EMPTY)
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(GlobalScopeReplaceStrategy)
  }

  @Test
  fun `should create transformingReplace local`() {
    val acl = FluxnovaBpmDataMapper.transformingReplace(
      TRANSIENT_VAR.name,
      true,
      IdentityVariableMapTransformer
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard.EMPTY)
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(LocalScopeReplaceStrategy)
  }

  @Test
  fun `should create transformingReplace global`() {
    val acl = FluxnovaBpmDataMapper.transformingReplace(
      TRANSIENT_VAR.name,
      false,
      IdentityVariableMapTransformer
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard.EMPTY)
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(GlobalScopeReplaceStrategy)
  }


  @Test
  fun `should create identityLocalReplace`() {
    val acl = FluxnovaBpmDataMapper.identityLocalReplace(
      TRANSIENT_VAR.name
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard.EMPTY)
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(LocalScopeReplaceStrategy)
  }

  @Test
  fun `should create identityGlobalReplace`() {
    val acl = FluxnovaBpmDataMapper.identityGlobalReplace(
      TRANSIENT_VAR.name
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard.EMPTY)
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(GlobalScopeReplaceStrategy)
  }

  @Test
  fun `should create identityGlobalReplace local`() {
    val acl = FluxnovaBpmDataMapper.identityReplace(
      TRANSIENT_VAR.name,
      true
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard.EMPTY)
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(LocalScopeReplaceStrategy)
  }

  @Test
  fun `should create identityGlobalReplace global`() {
    val acl = FluxnovaBpmDataMapper.identityReplace(
      TRANSIENT_VAR.name,
      false
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard.EMPTY)
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(GlobalScopeReplaceStrategy)
  }

}
