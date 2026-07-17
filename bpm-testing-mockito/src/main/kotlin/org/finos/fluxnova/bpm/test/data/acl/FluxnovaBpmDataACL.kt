package org.finos.fluxnova.bpm.test.data.acl

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData
import org.finos.fluxnova.bpm.test.data.acl.apply.GlobalScopeReplaceStrategy
import org.finos.fluxnova.bpm.test.data.acl.apply.LocalScopeReplaceStrategy
import org.finos.fluxnova.bpm.test.data.acl.transform.VariableMapTransformer
import org.finos.fluxnova.bpm.test.data.guard.VariablesGuard
import org.finos.fluxnova.bpm.engine.variable.VariableMap

/**
 * Helper methods to create anti corruption layers.
 */
object FluxnovaBpmDataACL {

  /**
   * Constructs an ACL with a guard, maps variables using transformer and replaces them in a local scope.
   * @param variableName name of the transient variable to use.
   * @param variableMapTransformer transformer to map from external to internal representation.
   * @param variablesGuard preconditions protecting the ACL.
   */
  @JvmStatic
  fun guardTransformingLocalReplace(variableName: String, variablesGuard: VariablesGuard, variableMapTransformer: VariableMapTransformer) = AntiCorruptionLayer(
    precondition = variablesGuard,
    variableMapTransformer = variableMapTransformer,
    factory = FluxnovaBpmData.customVariable(variableName, VariableMap::class.java),
    valueApplicationStrategy = LocalScopeReplaceStrategy
  )

  /**
   * Constructs an ACL with a guard, maps variables using transformer and replaces them in a global scope.
   * @param variableName name of the transient variable to use.
   * @param variableMapTransformer transformer to map from external to internal representation.
   * @param variablesGuard preconditions protecting the ACL.
   */
  @JvmStatic
  fun guardTransformingGlobalReplace(variableName: String, variablesGuard: VariablesGuard, variableMapTransformer: VariableMapTransformer) = AntiCorruptionLayer(
    precondition = variablesGuard,
    variableMapTransformer = variableMapTransformer,
    factory = FluxnovaBpmData.customVariable(variableName, VariableMap::class.java),
    valueApplicationStrategy = GlobalScopeReplaceStrategy
  )

  /**
   * Constructs an ACL with a guard, maps variables using transformer and replaces them in a scope controlled by the .
   * @param variableName name of the transient variable to use.
   * @param local flag to control the scope.
   * @param variableMapTransformer transformer to map from external to internal representation.
   * @param variablesGuard preconditions protecting the ACL.
   */
  @JvmStatic
  fun guardTransformingReplace(variableName: String, local: Boolean, variablesGuard: VariablesGuard, variableMapTransformer: VariableMapTransformer) = if (local) {
    guardTransformingLocalReplace(variableName, variablesGuard, variableMapTransformer)
  } else {
    guardTransformingGlobalReplace(variableName, variablesGuard, variableMapTransformer)
  }

}
