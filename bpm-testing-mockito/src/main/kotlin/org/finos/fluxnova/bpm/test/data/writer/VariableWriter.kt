package org.finos.fluxnova.bpm.test.data.writer

/**
 * Inverting calls to [org.finos.fluxnova.bpm.test.data.adapter.WriteAdapter].
 *
 * @param <S> type of concrete Writer for fluent usage.
</S> */
interface VariableWriter<S : VariableWriter<S>> : LocalVariableWriter<S>, GlobalVariableWriter<S>
