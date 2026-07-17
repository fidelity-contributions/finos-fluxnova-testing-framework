package org.finos.fluxnova.bpm.test.data.adapter.basic

import org.finos.fluxnova.bpm.test.data.adapter.AbstractReadWriteAdapter
import org.finos.fluxnova.bpm.test.data.adapter.ValueWrapperUtil.getTypedValue
import org.finos.fluxnova.bpm.test.data.adapter.ValueWrapperUtil.isAssignableFrom
import org.finos.fluxnova.bpm.test.data.adapter.WrongVariableTypeException
import org.finos.fluxnova.bpm.engine.variable.value.TypedValue
import java.util.*

/**
 * Base class for all basic read-write-adapter.
 *
 * @param [T] variable type.
 * @param variableName name of the variable.
 * @param clazz        variable type.
 */
abstract class AbstractBasicReadWriteAdapter<T>(
  variableName: String,
  protected val clazz: Class<T>
) : AbstractReadWriteAdapter<T>(variableName) {
  /**
   * Retrieves the value or null.
   *
   * @param value raw value.
   * @return value or null.
   */
  protected fun getOrNull(value: Any?): T? {
    if (value == null) {
      return null
    }
    if (clazz == UUID::class.java && String::class.java.isAssignableFrom(value.javaClass)) {
      @Suppress("UNCHECKED_CAST")
      return UUID.fromString(value as String?) as T
    }
    if (clazz.isAssignableFrom(value.javaClass) || isAssignableFrom(clazz, value.javaClass)) {
      @Suppress("UNCHECKED_CAST")
      return value as T
    }
    throw WrongVariableTypeException("Error reading $variableName: Couldn't read value of $clazz from $value")
  }

  override fun getTypedValue(value: Any?, isTransient: Boolean): TypedValue {
    return getTypedValue(clazz, value, isTransient)
  }
}
