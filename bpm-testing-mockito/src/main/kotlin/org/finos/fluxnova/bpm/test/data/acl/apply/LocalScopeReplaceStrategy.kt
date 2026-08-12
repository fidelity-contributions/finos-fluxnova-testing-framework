package org.finos.fluxnova.bpm.test.data.acl.apply

import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.variable.VariableMap

/**
 * Replaces variables of local scope with given variable map.
 */
object LocalScopeReplaceStrategy : ValueApplicationStrategy {

  override fun apply(variableMap: VariableMap, variableScope: VariableScope): VariableScope =
    variableScope.apply {
      this.variablesLocal = variableMap
    }

  override fun toString(): String {
    return javaClass.canonicalName
  }
}
