package org.finos.fluxnova.bpm.test.data.acl

import org.finos.fluxnova.bpm.test.data.acl.apply.ValueApplicationStrategy
import org.finos.fluxnova.bpm.test.data.acl.transform.VariableMapTransformer
import org.finos.fluxnova.bpm.test.data.factory.VariableFactory
import org.finos.fluxnova.bpm.test.data.guard.VariablesGuard
import org.finos.fluxnova.bpm.test.data.guard.integration.GuardViolationException
import org.finos.fluxnova.bpm.engine.delegate.ExecutionListener
import org.finos.fluxnova.bpm.engine.delegate.TaskListener
import org.finos.fluxnova.bpm.engine.variable.VariableMap
import org.finos.fluxnova.bpm.engine.variable.Variables

/**
 * Defines the ACL (Anti-Corruption-Layer).
 * <p>
 *     An ACL consists of a variables guard and a mapping function, which is applied, if the condition is matched.
 *     A typical application of an ACL is the protection of external access to the process (signal, message correlation).
 *     To do so, signal / correlate with transient variables only and those got pumped into the execution if the guard is satisfied.
 * </p>
 * @constructor Creates a new ACL.
 * @property precondition Precondition to be fulfilled to pass the ACL.
 * @property variableMapTransformer Mapping to be applied.
 * @property factory Factory to use.
 * @property valueApplicationStrategy Strategy to apply values from transformer to given variable scope.
 */
class AntiCorruptionLayer(
  val precondition: VariablesGuard,
  val variableMapTransformer: VariableMapTransformer,
  internal val factory: VariableFactory<VariableMap>,
  internal val valueApplicationStrategy: ValueApplicationStrategy
) {

  companion object {
    const val DEFAULT = "_transient"

    /**
     * Helper to create a Map containing transient variables hidden in the given map under the given key.
     *
     * @param variableName the variable name to use for the additional variables
     * @param variables    the variables to store
     *
     * @return a newly created map containing the given variables as transient objectTypedValue.
     */
    fun wrapAsTypedTransientVariable(variableName: String, variables: VariableMap): VariableMap {
      return Variables.createVariables()
        .putValueTyped(variableName, Variables.objectValue(variables, true)
          .create()
        )
    }
  }

  /**
   * Retrieves the ACL in form of an execution listener.
   * @return Fluxnova Execution Listener responsible for variable extraction, guard check and modification.
   */
  fun getExecutionListener() = ExecutionListener { execution ->
    val variablesExternal = factory.from(execution).get()
    if (precondition.evaluate(variablesExternal).isEmpty()) {
      val variableInternal = variableMapTransformer.transform(variablesExternal)
      valueApplicationStrategy.apply(variableInternal, execution)
    }
  }

  /**
   * Retrieves the ACL in form of a task listener.
   * @return Fluxnova Task Listener responsible for variable extraction, guard check and modification.
   */
  fun getTaskListener() = TaskListener { task ->
    val variablesExternal = factory.from(task).get()
    if (precondition.evaluate(variablesExternal).isEmpty()) {
      val variableInternal = variableMapTransformer.transform(variablesExternal)
      valueApplicationStrategy.apply(variableInternal, task)
    }
  }

  /**
   * Checks if the preconditions are satisfied and constructs a variable map wrapping the variables.
   * @param variableMap variable map containing the variables.
   * @return new variable map
   */
  fun checkAndWrap(variableMap: VariableMap): VariableMap {
    val violations = precondition.evaluate(variableMap)
    if (violations.isNotEmpty()) {
      throw GuardViolationException(violations = violations, reason = "ACL Guard Error:")
    }
    return wrap(variableMap)
  }

  /**
   * Checks if the preconditions are satisfied and constructs a variable map transforming and wrapping the variables.
   * @param variableMap variable map containing the variables.
   * @return new variable map
   */
  fun checkAndTransformAndWrap(variableMap: VariableMap): VariableMap {
    val violations = precondition.evaluate(variableMap)
    if (violations.isNotEmpty()) {
      throw GuardViolationException(violations = violations, reason = "ACL Guard Error:")
    }
    return wrap(variableMapTransformer.transform(variableMap))
  }

  /**
   * Constructs a variable map wrapping the variables.
   * @param variableMap variable map containing the variables.
   * @return new variable map
   */
  fun wrap(variableMap: VariableMap): VariableMap {
    return wrapAsTypedTransientVariable(variableName = factory.name, variables = variableMap)
  }
}
