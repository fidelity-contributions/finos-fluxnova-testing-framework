package org.finos.fluxnova.bpm.test.data

import org.finos.fluxnova.bpm.test.data.reader.*
import org.finos.fluxnova.bpm.engine.CaseService
import org.finos.fluxnova.bpm.engine.HistoryService
import org.finos.fluxnova.bpm.engine.RuntimeService
import org.finos.fluxnova.bpm.engine.TaskService
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.externaltask.LockedExternalTask
import org.finos.fluxnova.bpm.engine.runtime.ProcessInstanceWithVariables
import org.finos.fluxnova.bpm.engine.variable.VariableMap

/**
 * Access creator for readers.
 */
object Readers {

  /**
   * Readers for Fluxnova.
   */
  object C7 {
    /**
     * Creates a new task variable reader.
     *
     * @param taskService the Fluxnova task service
     * @param taskId      the id of the task to use
     * @return variable reader working on task
     */
    @JvmStatic
    fun reader(taskService: TaskService, taskId: String): VariableReader {
      return TaskServiceVariableReader(taskService, taskId)
    }

    /**
     * Creates a new runtime execution variable reader.
     *
     * @param runtimeService the Fluxnova runtime service
     * @param executionId    the executionId to use
     * @return variable reader working on execution
     */
    @JvmStatic
    fun reader(runtimeService: RuntimeService, executionId: String): VariableReader {
      return RuntimeServiceVariableReader(runtimeService, executionId)
    }

    /**
     * Creates a new history execution variable reader.
     *
     * @param historyService the Fluxnova history service
     * @param executionId    the executionId to use
     * @return variable reader working on execution
     */
    @JvmStatic
    fun reader(historyService: HistoryService, executionId: String): VariableReader {
      return HistoryServiceVariableReader(historyService, executionId)
    }

    /**
     * Creates a new case execution variable reader.
     *
     * @param caseService     the Fluxnova case service
     * @param caseExecutionId the caseExecutionId to use
     * @return variable reader working on execution
     */
    @JvmStatic
    fun reader(caseService: CaseService, caseExecutionId: String): VariableReader {
      return CaseServiceVariableReader(caseService, caseExecutionId)
    }

    /**
     * Creates a new variableScope variable reader.
     *
     * @param variableScope the variable scope to use (DelegateExecution, DelegateTask)
     * @return variable reader working on variableScope
     */
    @JvmStatic
    fun reader(variableScope: VariableScope): VariableReader {
      return VariableScopeReader(variableScope)
    }

    /**
     * Creates a new variableMap variable reader.
     *
     * @param variableMap the variableMap to use
     * @return variable reader working on variableMap
     */
    @JvmStatic
    fun reader(variableMap: VariableMap): VariableReader {
      return VariableMapReader(variableMap)
    }

    /**
     * Creates a new processInstance variable reader.
     *
     * @see .reader
     * @param processInstance the processInstance with variables to read from
     * @return variable reader working on the variableMap provided by instance
     */
    @JvmStatic
    fun reader(processInstance: ProcessInstanceWithVariables): VariableReader {
      return reader(processInstance.variables)
    }

    /**
     * Creates a new extern variable reader.
     *
     * @param lockedExternalTask the external tasks to use
     * @return variable reader working on external task
     */
    @JvmStatic
    fun reader(lockedExternalTask: LockedExternalTask): VariableReader {
      return LockedExternalTaskReader(lockedExternalTask)
    }

  }
}
