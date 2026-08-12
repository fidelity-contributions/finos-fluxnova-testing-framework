package org.finos.fluxnova.bpm.test.data.acl.transform

import org.finos.fluxnova.bpm.test.data.Writers.C7.builder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IdentityVariableMapTransformerTest {

  @Test
  fun `should pass the input to output`() {

    val vars = builder().build()

    assertThat(IdentityVariableMapTransformer.transform(vars))
      .isEqualTo(vars) // equals comparison
      .isSameAs(vars) // == comparison
  }
}
