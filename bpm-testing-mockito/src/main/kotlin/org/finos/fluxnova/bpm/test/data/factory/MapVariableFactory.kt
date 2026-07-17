package org.finos.fluxnova.bpm.test.data.factory

import org.finos.fluxnova.bpm.test.data.adapter.ReadAdapter
import org.finos.fluxnova.bpm.test.data.adapter.WriteAdapter
import org.finos.fluxnova.bpm.test.data.adapter.map.*
import org.finos.fluxnova.bpm.engine.CaseService
import org.finos.fluxnova.bpm.engine.HistoryService
import org.finos.fluxnova.bpm.engine.RuntimeService
import org.finos.fluxnova.bpm.engine.TaskService
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.externaltask.LockedExternalTask
import org.finos.fluxnova.bpm.engine.variable.VariableMap
import java.util.*

/**
 * Variable factory of a base parametrized map type.
 *
 * @param [K]> member key type of the factory.
 * @param [V] member value type of the factory.
 */
class MapVariableFactory<K, V>(
  override val name: String,
  val keyClass: Class<K>,
  val valueClass: Class<V>
) : VariableFactory<Map<K, V>> {

  companion object {
    inline fun <reified K, reified V> forType(name: String) = MapVariableFactory(name, K::class.java, V::class.java)
  }

  override fun on(variableScope: VariableScope): WriteAdapter<Map<K, V>> {
    return MapReadWriteAdapterVariableScope(
      variableScope,
      name,
      keyClass,
      valueClass
    )
  }

  override fun from(variableScope: VariableScope): ReadAdapter<Map<K, V>> {
    return MapReadWriteAdapterVariableScope(
      variableScope,
      name,
      keyClass,
      valueClass
    )
  }

  override fun on(variableMap: VariableMap): WriteAdapter<Map<K, V>> {
    return MapReadWriteAdapterVariableMap(
      variableMap,
      name,
      keyClass,
      valueClass
    )
  }

  override fun from(variableMap: VariableMap): ReadAdapter<Map<K, V>> {
    return MapReadWriteAdapterVariableMap(
      variableMap,
      name,
      keyClass,
      valueClass
    )
  }

  override fun on(runtimeService: RuntimeService, executionId: String): WriteAdapter<Map<K, V>> {
    return MapReadWriteAdapterRuntimeService(
      runtimeService,
      executionId,
      name,
      keyClass,
      valueClass
    )
  }

  override fun from(runtimeService: RuntimeService, executionId: String): ReadAdapter<Map<K, V>> {
    return MapReadWriteAdapterRuntimeService(
      runtimeService,
      executionId,
      name,
      keyClass,
      valueClass
    )
  }

  override fun from(
    historyService: HistoryService,
    executionId: String
  ): ReadAdapter<Map<K, V>> {
    return MapReadAdapterHistoryService(
      historyService,
      executionId,
      name,
      keyClass,
      valueClass
    )
  }

  override fun on(taskService: TaskService, taskId: String): WriteAdapter<Map<K, V>> {
    return MapReadWriteAdapterTaskService(
      taskService,
      taskId,
      name,
      keyClass,
      valueClass
    )
  }

  override fun from(taskService: TaskService, taskId: String): ReadAdapter<Map<K, V>> {
    return MapReadWriteAdapterTaskService(
      taskService,
      taskId,
      name,
      keyClass,
      valueClass
    )
  }

  override fun on(caseService: CaseService, caseExecutionId: String): WriteAdapter<Map<K, V>> {
    return MapReadWriteAdapterCaseService(
      caseService,
      caseExecutionId,
      name,
      keyClass,
      valueClass
    )
  }

  override fun from(caseService: CaseService, caseExecutionId: String): ReadAdapter<Map<K, V>> {
    return MapReadWriteAdapterCaseService(
      caseService,
      caseExecutionId,
      name,
      keyClass,
      valueClass
    )
  }

  override fun from(lockedExternalTask: LockedExternalTask): ReadAdapter<Map<K, V>> {
    return MapReadAdapterLockedExternalTask(
      lockedExternalTask,
      name,
      keyClass,
      valueClass
    )
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val that = other as MapVariableFactory<*, *>
    return name == that.name && keyClass == that.keyClass && valueClass == that.valueClass
  }

  override fun hashCode(): Int {
    return Objects.hash(name, keyClass, valueClass)
  }

  override fun toString(): String {
    return "MapVariableFactory{" +
      "name='" + name + '\'' +
      ", keyClazz=" + keyClass +
      ", valueClazz=" + valueClass +
      '}'
  }
}
