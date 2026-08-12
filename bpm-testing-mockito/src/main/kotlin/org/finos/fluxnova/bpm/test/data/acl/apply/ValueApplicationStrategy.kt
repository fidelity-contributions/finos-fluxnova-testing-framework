package org.finos.fluxnova.bpm.test.data.acl.apply

import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.variable.VariableMap


/**
 * Interface describing the strategy to assign values.
 */
@FunctionalInterface
interface ValueApplicationStrategy {
  /**
   * Strategy to assign variables stored in a map to the given variable scope.
   */
  fun apply(variableMap: VariableMap, variableScope: VariableScope): VariableScope
}
