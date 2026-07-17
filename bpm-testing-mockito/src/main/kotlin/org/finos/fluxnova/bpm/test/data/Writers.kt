package org.finos.fluxnova.bpm.test.data

import org.finos.fluxnova.bpm.test.data.builder.VariableMapBuilder
import org.finos.fluxnova.bpm.test.data.writer.CaseServiceVariableWriter
import org.finos.fluxnova.bpm.test.data.writer.GlobalVariableWriter
import org.finos.fluxnova.bpm.test.data.writer.RuntimeServiceVariableWriter
import org.finos.fluxnova.bpm.test.data.writer.TaskServiceVariableWriter
import org.finos.fluxnova.bpm.test.data.writer.VariableMapWriter
import org.finos.fluxnova.bpm.test.data.writer.VariableScopeWriter
import org.finos.fluxnova.bpm.test.data.writer.VariableWriter
import org.finos.fluxnova.bpm.engine.CaseService
import org.finos.fluxnova.bpm.engine.RuntimeService
import org.finos.fluxnova.bpm.engine.TaskService
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.variable.VariableMap

/**
 * Access creators for writers.
 */
object Writers {

  /**
   * Writers for Fluxnova.
   */
  object C7 {

    /**
     * Creates a new variable map builder.
     *
     * @return new writer with empty variable map.
     */
    @JvmStatic
    fun builder(): VariableMapBuilder {
      return VariableMapBuilder()
    }


    /**
     * Creates a new variable map builder.
     *
     * @param variables pre-created, potentially non-empty variables.
     * @return new writer
     */
    @JvmStatic
    fun writer(variables: VariableMap): GlobalVariableWriter<*> {
      return VariableMapWriter(variables)
    }

    /**
     * Creates a new variable scope writer.
     *
     * @param variableScope scope to work on (delegate execution or delegate task).
     * @return new writer working on provided variable scope.
     */
    @JvmStatic
    fun writer(variableScope: VariableScope): VariableWriter<*> {
      return VariableScopeWriter(variableScope)
    }

    /**
     * Creates a new execution variable writer.
     *
     * @param runtimeService runtime service to use.
     * @param executionId    id of the execution.
     * @return new writer working on provided process execution.
     */
    @JvmStatic
    fun writer(runtimeService: RuntimeService, executionId: String): VariableWriter<*> {
      return RuntimeServiceVariableWriter(runtimeService, executionId)
    }

    /**
     * Creates a new task variable writer.
     *
     * @param taskService task service to use.
     * @param taskId      task id.
     * @return new writer working on provided user task.
     */
    @JvmStatic
    fun writer(taskService: TaskService, taskId: String): VariableWriter<*> {
      return TaskServiceVariableWriter(taskService, taskId)
    }

    /**
     * Creates a new caseExecution variable writer.
     *
     * @param caseService     task service to use.
     * @param caseExecutionId caseExecution id.
     * @return new writer working on provided user task.
     */
    @JvmStatic
    fun writer(caseService: CaseService, caseExecutionId: String): VariableWriter<*> {
      return CaseServiceVariableWriter(caseService, caseExecutionId)
    }
  }

}
