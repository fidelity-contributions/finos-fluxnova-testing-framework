package org.finos.fluxnova.bpm.test.data.reader

import org.finos.fluxnova.bpm.test.data.factory.VariableFactory
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import java.util.*

/**
 * Allows reading multiple variable values from [VariableScope] (such as [org.finos.fluxnova.bpm.engine.delegate.DelegateExecution] and [org.finos.fluxnova.bpm.engine.delegate.DelegateTask]).
 * @param variableScope scope to operate on.
 */
class VariableScopeReader(private val variableScope: VariableScope) : VariableReader {
  override fun <T> getOptional(variableFactory: VariableFactory<T>): Optional<T> {
    return variableFactory.from(variableScope).getOptional()
  }

  override operator fun <T> get(variableFactory: VariableFactory<T>): T {
    return variableFactory.from(variableScope).get()
  }

  override fun <T> getLocal(variableFactory: VariableFactory<T>): T {
    return variableFactory.from(variableScope).getLocal()
  }

  override fun <T> getLocalOptional(variableFactory: VariableFactory<T>): Optional<T> {
    return variableFactory.from(variableScope).getLocalOptional()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val that = other as VariableScopeReader
    return variableScope == that.variableScope
  }

  override fun hashCode(): Int {
    return variableScope.hashCode()
  }
}
