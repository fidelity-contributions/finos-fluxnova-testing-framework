package org.finos.fluxnova.bpm.test.data.acl

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData
import org.finos.fluxnova.bpm.test.data.acl.apply.GlobalScopeReplaceStrategy
import org.finos.fluxnova.bpm.test.data.acl.apply.LocalScopeReplaceStrategy
import org.finos.fluxnova.bpm.test.data.acl.transform.IdentityVariableMapTransformer
import org.finos.fluxnova.bpm.test.data.acl.transform.VariableMapTransformer
import org.finos.fluxnova.bpm.test.data.guard.VariablesGuard
import org.finos.fluxnova.bpm.engine.variable.VariableMap

/**
 * Helper methods to create unconditional transient variable mappers.
 */
object FluxnovaBpmDataMapper {
  /**
   * Constructs a mapper, maps variables using transformer and replaces them in a local scope.
   * @param variableName name of the transient variable to use.
   * @param variableMapTransformer transformer to map from external to internal representation.
   */
  @JvmStatic
  fun transformingLocalReplace(variableName: String, variableMapTransformer: VariableMapTransformer) = AntiCorruptionLayer(
    precondition = VariablesGuard.EMPTY,
    variableMapTransformer = variableMapTransformer,
    factory = FluxnovaBpmData.customVariable(variableName, VariableMap::class.java),
    valueApplicationStrategy = LocalScopeReplaceStrategy
  )

  /**
   * Constructs a mapper, maps variables using transformer and replaces them in a global scope.
   * @param variableName name of the transient variable to use.
   * @param variableMapTransformer transformer to map from external to internal representation.
   */
  @JvmStatic
  fun transformingGlobalReplace(variableName: String, variableMapTransformer: VariableMapTransformer) = AntiCorruptionLayer(
    precondition = VariablesGuard.EMPTY,
    variableMapTransformer = variableMapTransformer,
    factory = FluxnovaBpmData.customVariable(variableName, VariableMap::class.java),
    valueApplicationStrategy = GlobalScopeReplaceStrategy
  )

  /**
   * Constructs a mapper, maps variables using transformer and replaces them in a scope depending on flag.
   * @param variableName name of the transient variable to use.
   * @param variableMapTransformer transformer to map from external to internal representation.
   * @param local flag to control local or global scope
   */
  @JvmStatic
  fun transformingReplace(variableName: String, local: Boolean, variableMapTransformer: VariableMapTransformer) = if (local) {
    transformingLocalReplace(variableName, variableMapTransformer)
  } else {
    transformingGlobalReplace(variableName, variableMapTransformer)
  }

  /**
   * Constructs a mapper, maps variables 1:1 and replaces them in a local scope.
   * @param variableName name of the transient variable to use.
   */
  @JvmStatic
  fun identityLocalReplace(variableName: String) = AntiCorruptionLayer(
    precondition = VariablesGuard.EMPTY,
    variableMapTransformer = IdentityVariableMapTransformer,
    factory = FluxnovaBpmData.customVariable(variableName, VariableMap::class.java),
    valueApplicationStrategy = LocalScopeReplaceStrategy
  )

  /**
   * Constructs a mapper, maps variables 1:1 and replaces them in a global scope.
   * @param variableName name of the transient variable to use.
   */
  @JvmStatic
  fun identityGlobalReplace(variableName: String) = AntiCorruptionLayer(
    precondition = VariablesGuard.EMPTY,
    variableMapTransformer = IdentityVariableMapTransformer,
    factory = FluxnovaBpmData.customVariable(variableName, VariableMap::class.java),
    valueApplicationStrategy = GlobalScopeReplaceStrategy
  )

  /**
   * Constructs a mapper, maps variables 1:1 and replaces them in scope depending on flag.
   * @param variableName name of the transient variable to use.
   * @param local flag to control local or global scope
   */
  @JvmStatic
  fun identityReplace(variableName: String, local: Boolean) = if (local) {
    identityLocalReplace(variableName)
  } else {
    identityGlobalReplace(variableName)
  }

}
