package org.finos.fluxnova.bpm.test.data.acl.transform

import org.finos.fluxnova.bpm.engine.variable.VariableMap

/**
 * Performs no transformation (1:1 mapping).
 */
object IdentityVariableMapTransformer : VariableMapTransformer {
  override fun transform(variableMap: VariableMap): VariableMap = variableMap
}
