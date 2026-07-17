package org.finos.fluxnova.bpm.test.data.acl

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData.customVariable
import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData.stringVariable
import org.finos.fluxnova.bpm.test.data.acl.apply.GlobalScopeReplaceStrategy
import org.finos.fluxnova.bpm.test.data.acl.apply.LocalScopeReplaceStrategy
import org.finos.fluxnova.bpm.test.data.acl.transform.IdentityVariableMapTransformer
import org.finos.fluxnova.bpm.test.data.guard.FluxnovaBpmDataGuards.exists
import org.finos.fluxnova.bpm.test.data.guard.VariablesGuard
import org.assertj.core.api.Assertions.assertThat
import org.finos.fluxnova.bpm.engine.variable.VariableMap
import org.junit.jupiter.api.Test

class FluxnovaBpmDataACLTest {

  val TRANSIENT_VAR = customVariable("__transient__", VariableMap::class.java)
  val FOO = stringVariable("foo")

  @Test
  fun `should create guardTransformingLocalReplace`() {
    val acl = FluxnovaBpmDataACL.guardTransformingLocalReplace(
      TRANSIENT_VAR.name,
      VariablesGuard(exists(FOO)),
      IdentityVariableMapTransformer
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard(exists(FOO)))
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(LocalScopeReplaceStrategy)
  }

  @Test
  fun `should create guardTransformingGlobalReplace`() {
    val acl = FluxnovaBpmDataACL.guardTransformingGlobalReplace(
      TRANSIENT_VAR.name,
      VariablesGuard(exists(FOO)),
      IdentityVariableMapTransformer
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard(exists(FOO)))
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(GlobalScopeReplaceStrategy)
  }

  @Test
  fun `should create guardTransformingReplace global`() {
    val acl = FluxnovaBpmDataACL.guardTransformingReplace(
      TRANSIENT_VAR.name,
      false,
      VariablesGuard(exists(FOO)),
      IdentityVariableMapTransformer
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard(exists(FOO)))
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(GlobalScopeReplaceStrategy)
  }

  @Test
  fun `should create guardTransformingReplace local`() {
    val acl = FluxnovaBpmDataACL.guardTransformingReplace(
      TRANSIENT_VAR.name,
      true,
      VariablesGuard(exists(FOO)),
      IdentityVariableMapTransformer
    )
    assertThat(acl.precondition).isEqualTo(VariablesGuard(exists(FOO)))
    assertThat(acl.variableMapTransformer).isEqualTo(IdentityVariableMapTransformer)
    assertThat(acl.factory).isEqualTo(TRANSIENT_VAR)
    assertThat(acl.valueApplicationStrategy).isEqualTo(LocalScopeReplaceStrategy)
  }

}
