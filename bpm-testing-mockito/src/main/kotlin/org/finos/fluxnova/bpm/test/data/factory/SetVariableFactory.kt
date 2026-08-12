package org.finos.fluxnova.bpm.test.data.factory

import org.finos.fluxnova.bpm.test.data.adapter.ReadAdapter
import org.finos.fluxnova.bpm.test.data.adapter.WriteAdapter
import org.finos.fluxnova.bpm.test.data.adapter.set.*
import org.finos.fluxnova.bpm.engine.CaseService
import org.finos.fluxnova.bpm.engine.HistoryService
import org.finos.fluxnova.bpm.engine.RuntimeService
import org.finos.fluxnova.bpm.engine.TaskService
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.externaltask.LockedExternalTask
import org.finos.fluxnova.bpm.engine.variable.VariableMap
import java.util.*

/**
 * Variable factory of a base parametrized set type.
 *
 * @param [T] member type of the factory.
 */
class SetVariableFactory<T>(override val name: String, val memberClass: Class<T>) : VariableFactory<Set<T>> {

  companion object {
    inline fun <reified T> forType(name: String) = SetVariableFactory(name, T::class.java)
  }

  override fun on(variableScope: VariableScope): WriteAdapter<Set<T>> {
    return SetReadWriteAdapterVariableScope(variableScope, name, memberClass)
  }

  override fun from(variableScope: VariableScope): ReadAdapter<Set<T>> {
    return SetReadWriteAdapterVariableScope(variableScope, name, memberClass)
  }

  override fun on(variableMap: VariableMap): WriteAdapter<Set<T>> {
    return SetReadWriteAdapterVariableMap(variableMap, name, memberClass)
  }

  override fun from(variableMap: VariableMap): ReadAdapter<Set<T>> {
    return SetReadWriteAdapterVariableMap(variableMap, name, memberClass)
  }

  override fun on(runtimeService: RuntimeService, executionId: String): WriteAdapter<Set<T>> {
    return SetReadWriteAdapterRuntimeService(
      runtimeService,
      executionId,
      name,
      memberClass
    )
  }

  override fun from(runtimeService: RuntimeService, executionId: String): ReadAdapter<Set<T>> {
    return SetReadWriteAdapterRuntimeService(
      runtimeService,
      executionId,
      name,
      memberClass
    )
  }

  override fun from(
    historyService: HistoryService,
    executionId: String
  ): ReadAdapter<Set<T>> {
    return SetReadAdapterHistoryService(
      historyService,
      executionId,
      name,
      memberClass
    )
  }

  override fun on(taskService: TaskService, taskId: String): WriteAdapter<Set<T>> {
    return SetReadWriteAdapterTaskService(taskService, taskId, name, memberClass)
  }

  override fun from(taskService: TaskService, taskId: String): ReadAdapter<Set<T>> {
    return SetReadWriteAdapterTaskService(taskService, taskId, name, memberClass)
  }

  override fun on(caseService: CaseService, caseExecutionId: String): WriteAdapter<Set<T>> {
    return SetReadWriteAdapterCaseService(
      caseService,
      caseExecutionId,
      name,
      memberClass
    )
  }

  override fun from(caseService: CaseService, caseExecutionId: String): ReadAdapter<Set<T>> {
    return SetReadWriteAdapterCaseService(
      caseService,
      caseExecutionId,
      name,
      memberClass
    )
  }

  override fun from(lockedExternalTask: LockedExternalTask): ReadAdapter<Set<T>> {
    return SetReadAdapterLockedExternalTask(
      lockedExternalTask,
      name,
      memberClass
    )
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val that = other as SetVariableFactory<*>
    return name == that.name && memberClass == that.memberClass
  }

  override fun hashCode(): Int {
    return Objects.hash(name, memberClass)
  }

  override fun toString(): String {
    return "SetVariableFactory{" +
            "name='" + name + '\'' +
            ", memberClazz=" + memberClass +
            '}'
  }
}
