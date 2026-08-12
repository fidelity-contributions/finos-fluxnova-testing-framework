package org.finos.fluxnova.bpm.test.data.factory

import org.finos.fluxnova.bpm.test.data.adapter.ReadAdapter
import org.finos.fluxnova.bpm.test.data.adapter.WriteAdapter
import org.finos.fluxnova.bpm.engine.CaseService
import org.finos.fluxnova.bpm.engine.HistoryService
import org.finos.fluxnova.bpm.engine.RuntimeService
import org.finos.fluxnova.bpm.engine.TaskService
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.externaltask.LockedExternalTask
import org.finos.fluxnova.bpm.engine.variable.VariableMap

/**
 * Typed variable factory.
 *
 * @param [T] type of the factory.
 */
interface VariableFactory<T> {

  @Suppress("UNCHECKED_CAST")
  val nonNull: VariableFactory<T & Any> get() = this as VariableFactory<T & Any>

  /**
   * Creates a write adapter for variable scope.
   *
   * @param variableScope underlying scope to work on.
   * @return write adapter.
   */
  fun on(variableScope: VariableScope): WriteAdapter<T>

  /**
   * Creates a read adapter on variable scope.
   *
   * @param variableScope underlying scope to work on.
   * @return read adapter.
   */
  fun from(variableScope: VariableScope): ReadAdapter<T>

  /**
   * Creates a write adapter for variable map.
   *
   * @param variableMap underlying scope to work on.
   * @return write adapter.
   */
  fun on(variableMap: VariableMap): WriteAdapter<T>

  /**
   * Creates a read adapter on variable scope.
   *
   * @param variableMap underlying map to work on.
   * @return read adapter.
   */
  fun from(variableMap: VariableMap): ReadAdapter<T>

  /**
   * Creates a write adapter on execution.
   *
   * @param runtimeService underlying runtime service to work on.
   * @param executionId    id identifying execution.
   * @return write adapter
   */
  fun on(runtimeService: RuntimeService, executionId: String): WriteAdapter<T>

  /**
   * Creates a read adapter on execution.
   *
   * @param runtimeService underlying runtime service to work on.
   * @param executionId    id identifying execution.
   * @return read adapter.
   */
  fun from(runtimeService: RuntimeService, executionId: String): ReadAdapter<T>

  /**
   * Creates a read adapter on historic execution
   *
   * @param historyService underlying history service to work on.
   * @param executionId    id identifying execution.
   * @return read adapter.
   */
  fun from(historyService: HistoryService, executionId: String): ReadAdapter<T>

  /**
   * Creates a write adapter on task.
   *
   * @param taskService underlying task service to work on.
   * @param taskId      id identifying task.
   * @return write adapter
   */
  fun on(taskService: TaskService, taskId: String): WriteAdapter<T>

  /**
   * Creates a read adapter on task.
   *
   * @param taskService underlying task service to work on.
   * @param taskId      id identifying task.
   * @return read adapter.
   */
  fun from(taskService: TaskService, taskId: String): ReadAdapter<T>

  /**
   * Creates a write adapter on task.
   *
   * @param caseService     underlying case service to work on.
   * @param caseExecutionId id identifying case execution.
   * @return write adapter
   */
  fun on(caseService: CaseService, caseExecutionId: String): WriteAdapter<T>

  /**
   * Creates a read adapter on task.
   *
   * @param caseService     underlying case service to work on.
   * @param caseExecutionId id identifying case execution.
   * @return read adapter.
   */
  fun from(caseService: CaseService, caseExecutionId: String): ReadAdapter<T>

  /**
   * Creates a read adapter on external task.
   *
   * @param lockedExternalTask underlying external task to work on.
   * @return read adapter.
   */
  fun from(lockedExternalTask: LockedExternalTask): ReadAdapter<T>

  /**
   * Retrieves the variable name.
   *
   * @return name of the variable.
   */
  val name: String
}
