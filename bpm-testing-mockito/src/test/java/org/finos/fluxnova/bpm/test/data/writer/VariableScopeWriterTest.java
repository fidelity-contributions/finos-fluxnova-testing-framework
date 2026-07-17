package org.finos.fluxnova.bpm.test.data.writer;

import org.finos.fluxnova.bpm.test.data.factory.VariableFactory;
import org.finos.fluxnova.bpm.test.mockito.delegate.DelegateExecutionFake;
import org.junit.jupiter.api.Test;

import static org.finos.fluxnova.bpm.test.data.FluxnovaBpmData.stringVariable;
import static org.finos.fluxnova.bpm.test.data.Writers.C7.writer;
import static org.assertj.core.api.Assertions.assertThat;

public class VariableScopeWriterTest {

  private static final VariableFactory<String> STRING = stringVariable("myString");

  @Test
  public void testSet() {
    DelegateExecutionFake execution = DelegateExecutionFake.of().withId("4711");

    writer(execution)
      .set(STRING, "value")
      .variables();
    assertThat(execution.getVariable(STRING.getName())).isEqualTo("value");
  }

  @Test
  public void testSetLocal() {
    DelegateExecutionFake execution = DelegateExecutionFake.of().withId("4711");
    writer(execution)
      .setLocal(STRING, "value");
    assertThat(execution.getVariableLocal(STRING.getName())).isEqualTo("value");
  }

  @Test
  public void testRemove() {
    DelegateExecutionFake execution = DelegateExecutionFake.of().withId("4711");
    STRING.on(execution).set("value");
    writer(execution)
      .remove(STRING);
    assertThat(execution.getVariableNames()).isEmpty();
  }

  @Test
  public void testRemoveLocal() {
    DelegateExecutionFake execution = DelegateExecutionFake.of().withId("4711");
    STRING.on(execution).setLocal("value");
    writer(execution)
      .removeLocal(STRING);
    assertThat(execution.getVariableNames()).isEmpty();
  }

}
