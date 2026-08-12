package org.finos.fluxnova.bpm.test.data.reader

import org.finos.fluxnova.bpm.test.data.factory.VariableFactory
import org.finos.fluxnova.bpm.engine.externaltask.LockedExternalTask
import java.util.*

/**
 * Allows reading multiple variable values from [LockedExternalTask]).
 * @param lockedExternalTask scope to operate on.
 */
class LockedExternalTaskReader(private val lockedExternalTask: LockedExternalTask) : VariableReader {
    override fun <T> getOptional(variableFactory: VariableFactory<T>): Optional<T> {
        return variableFactory.from(lockedExternalTask).getOptional()
    }

    override operator fun <T> get(variableFactory: VariableFactory<T>): T {
        return variableFactory.from(lockedExternalTask).get()
    }

    override fun <T> getLocal(variableFactory: VariableFactory<T>): T {
        return variableFactory.from(lockedExternalTask).getLocal()
    }

    override fun <T> getLocalOptional(variableFactory: VariableFactory<T>): Optional<T> {
        return variableFactory.from(lockedExternalTask).getLocalOptional()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as LockedExternalTaskReader
        return lockedExternalTask == that.lockedExternalTask
    }

    override fun hashCode(): Int {
        return lockedExternalTask.hashCode()
    }
}
