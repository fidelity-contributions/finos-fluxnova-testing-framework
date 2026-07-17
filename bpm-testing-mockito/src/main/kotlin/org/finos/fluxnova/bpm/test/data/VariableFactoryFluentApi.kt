package org.finos.fluxnova.bpm.test.data

import org.finos.fluxnova.bpm.test.data.factory.VariableFactory
import org.finos.fluxnova.bpm.test.data.reader.CaseServiceVariableReader
import org.finos.fluxnova.bpm.test.data.reader.RuntimeServiceVariableReader
import org.finos.fluxnova.bpm.test.data.reader.TaskServiceVariableReader
import org.finos.fluxnova.bpm.test.data.writer.CaseServiceVariableWriter
import org.finos.fluxnova.bpm.test.data.writer.RuntimeServiceVariableWriter
import org.finos.fluxnova.bpm.test.data.writer.TaskServiceVariableWriter
import org.finos.fluxnova.bpm.engine.CaseService
import org.finos.fluxnova.bpm.engine.RuntimeService
import org.finos.fluxnova.bpm.engine.TaskService
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.variable.VariableMap
import java.util.*

/**
 * Getter from local scope.
 * @param factory factory defining the variable.
 */
fun <T> VariableMap.getOptional(factory: VariableFactory<T>): Optional<T> = factory.from(this).getOptional()

/**
 * Fluent setter.
 * @param factory factory defining the variable.
 * @param value new value.
 * @param isTransient flag for transient access, <code>false</code> by default.
 */
fun <T> VariableMap.set(factory: VariableFactory<T>, value: T, isTransient: Boolean = false) = this.apply {
  factory.on(this).set(value, isTransient)
}

/**
 * Fluent remover.
 * @param factory factory defining the variable.
 */
fun <T> VariableMap.remove(factory: VariableFactory<T>) = this.apply {
  factory.on(this).remove()
}

/**
 * Fluent updater.
 * @param factory factory defining the variable.
 * @param valueProcessor update function.
 * @param isTransient flag for transient access, <code>false</code> by default.
 */
fun <T> VariableMap.update(factory: VariableFactory<T>, valueProcessor: (T) -> T, isTransient: Boolean = false) = this.apply {
  factory.on(this).update(valueProcessor, isTransient)
}

/**
 * Getter from local scope.
 * @param factory factory defining the variable.
 */
fun <T> VariableScope.getOptional(factory: VariableFactory<T>): Optional<T> = factory.from(this).getOptional()

/**
 * Getter from local scope.
 * @param factory factory defining the variable.
 */
fun <T> VariableScope.getLocal(factory: VariableFactory<T>): T = factory.from(this).getLocal()

/**
 * Getter from local scope.
 * @param factory factory defining the variable.
 */
fun <T> VariableScope.getLocalOptional(factory: VariableFactory<T>): Optional<T> = factory.from(this).getLocalOptional()

/**
 * Fluent setter.
 * @param factory factory defining the variable.
 * @param value new value.
 * @param isTransient flag for transient access, <code>false</code> by default.
 */
fun <T> VariableScope.set(factory: VariableFactory<T>, value: T, isTransient: Boolean = false) = this.apply {
  factory.on(this).set(value, isTransient)
}

/**
 * Fluent remover.
 * @param factory factory defining the variable.
 */
fun <T> VariableScope.remove(factory: VariableFactory<T>) = this.apply {
  factory.on(this).remove()
}

/**
 * Fluent updater.
 * @param factory factory defining the variable.
 * @param valueProcessor update function.
 * @param isTransient flag for transient access, <code>false</code> by default.
 */
fun <T> VariableScope.update(factory: VariableFactory<T>, valueProcessor: (T) -> T, isTransient: Boolean = false) = this.apply {
  factory.on(this).update(valueProcessor, isTransient)
}

/**
 * Fluent local setter.
 * @param factory factory defining the variable.
 * @param value new value.
 * @param isTransient flag for transient access, <code>false</code> by default.
 */
fun <T> VariableScope.setLocal(factory: VariableFactory<T>, value: T, isTransient: Boolean = false) = this.apply {
  factory.on(this).setLocal(value, isTransient)
}

/**
 * Fluent local remover.
 * @param factory factory defining the variable.
 */
fun <T> VariableScope.removeLocal(factory: VariableFactory<T>) = this.apply {
  factory.on(this).removeLocal()
}

/**
 * Fluent local updater.
 * @param factory factory defining the variable.
 * @param valueProcessor update function.
 * @param isTransient flag for transient access, <code>false</code> by default.
 */
fun <T> VariableScope.updateLocal(factory: VariableFactory<T>, valueProcessor: (T) -> T, isTransient: Boolean = false) = this.apply {
  factory.on(this).updateLocal(valueProcessor, isTransient)
}

/**
 * Helper to access case service writer.
 * @param caseExecutionId id of the execution.
 */
fun CaseService.writer(caseExecutionId: String) =
    CaseServiceVariableWriter(this, caseExecutionId)

/**
 * Helper to access runtime service writer.
 * @param executionId id of the execution.
 */
fun RuntimeService.writer(executionId: String) =
    RuntimeServiceVariableWriter(this, executionId)

/**
 * Helper to access task service writer.
 * @param taskId id of the task.
 */
fun TaskService.writer(taskId: String) =
    TaskServiceVariableWriter(this, taskId)

/**
 * Helper to access case service reader.
 * @param caseExecutionId id of the execution.
 */
fun CaseService.reader(caseExecutionId: String) =
    CaseServiceVariableReader(this, caseExecutionId)

/**
 * Helper to access runtime service reader.
 * @param executionId id of the execution.
 */
fun RuntimeService.reader(executionId: String) =
    RuntimeServiceVariableReader(this, executionId)

/**
 * Helper to access task service reader.
 * @param taskId id of the task.
 */
fun TaskService.reader(taskId: String) =
    TaskServiceVariableReader(this, taskId)
