package org.finos.fluxnova.bpm.test.data.factory

import org.finos.fluxnova.bpm.test.data.adapter.ReadAdapter
import org.finos.fluxnova.bpm.test.data.adapter.WriteAdapter
import org.finos.fluxnova.bpm.test.data.adapter.listofmaps.*
import org.finos.fluxnova.bpm.engine.CaseService
import org.finos.fluxnova.bpm.engine.HistoryService
import org.finos.fluxnova.bpm.engine.RuntimeService
import org.finos.fluxnova.bpm.engine.TaskService
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.externaltask.LockedExternalTask
import org.finos.fluxnova.bpm.engine.variable.VariableMap
import java.util.*

/**
 * Variable factory of a base parametrized list of maps type.
 *
 * @param [K]> member key type of the factory.
 * @param [V] member value type of the factory.
 */
class ListOfMapsVariableFactory<K, V>(
  override val name: String,
  val keyClass: Class<K>,
  val valueClass: Class<V>
) : VariableFactory<List<Map<K, V>>> {

  companion object {
    /**
     * static factory method for creating an instance
     */
    inline fun <reified K, reified V> forType(name: String) =
      ListOfMapsVariableFactory(name, K::class.java, V::class.java)
  }

  override fun on(variableScope: VariableScope): WriteAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterVariableScope(variableScope, name, keyClass, valueClass)
  }

  override fun from(variableScope: VariableScope): ReadAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterVariableScope(variableScope, name, keyClass, valueClass)
  }

  override fun on(variableMap: VariableMap): WriteAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterVariableMap(variableMap, name, keyClass, valueClass)
  }

  override fun from(variableMap: VariableMap): ReadAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterVariableMap(variableMap, name, keyClass, valueClass)
  }

  override fun on(
    runtimeService: RuntimeService,
    executionId: String
  ): WriteAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterRuntimeService(
      runtimeService, executionId, name, keyClass, valueClass
    )
  }

  override fun from(
    runtimeService: RuntimeService,
    executionId: String
  ): ReadAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterRuntimeService(
      runtimeService, executionId, name, keyClass, valueClass
    )
  }

  override fun from(
    historyService: HistoryService,
    executionId: String
  ): ReadAdapter<List<Map<K, V>>> {
    return ListOfMapsReadAdapterHistoryService(
      historyService,
      executionId,
      name,
      keyClass,
      valueClass
    )
  }

  override fun on(taskService: TaskService, taskId: String): WriteAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterTaskService(taskService, taskId, name, keyClass, valueClass)
  }

  override fun from(taskService: TaskService, taskId: String): ReadAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterTaskService(taskService, taskId, name, keyClass, valueClass)
  }

  override fun on(
    caseService: CaseService,
    caseExecutionId: String
  ): WriteAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterCaseService(
      caseService, caseExecutionId, name, keyClass, valueClass
    )
  }

  override fun from(
    caseService: CaseService,
    caseExecutionId: String
  ): ReadAdapter<List<Map<K, V>>> {
    return ListOfMapsReadWriteAdapterCaseService(
      caseService, caseExecutionId, name, keyClass, valueClass
    )
  }

  override fun from(lockedExternalTask: LockedExternalTask): ReadAdapter<List<Map<K, V>>> {
    return ListOfMapsReadAdapterLockedExternalTask(lockedExternalTask, name, keyClass, valueClass)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false
    val that = other as ListOfMapsVariableFactory<*, *>
    return name == that.name && keyClass == that.keyClass && valueClass == that.valueClass
  }

  override fun hashCode(): Int {
    return Objects.hash(name, keyClass, valueClass)
  }

  override fun toString(): String {
    return "ListOfMapsVariableFactory{" +
            "name='" +
            name +
            '\'' +
            ", keyClazz=" +
            keyClass +
            ", valueClazz=" +
            valueClass +
            '}'
  }
}
