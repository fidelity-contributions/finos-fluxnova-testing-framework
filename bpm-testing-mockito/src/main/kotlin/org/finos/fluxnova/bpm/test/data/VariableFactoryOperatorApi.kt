package org.finos.fluxnova.bpm.test.data

import org.finos.fluxnova.bpm.test.data.factory.VariableFactory
import org.finos.fluxnova.bpm.engine.delegate.VariableScope
import org.finos.fluxnova.bpm.engine.variable.VariableMap

/**
 * Operator getter from global scope.
 * @param factory factory defining the variable.
 */
operator fun <T> VariableMap.get(factory: VariableFactory<T>): T = factory.from(this).get()

/**
 * Operator getter from global scope.
 * @param factory factory defining the variable.
 */
operator fun <T> VariableScope.get(factory: VariableFactory<T>): T = factory.from(this).get()

