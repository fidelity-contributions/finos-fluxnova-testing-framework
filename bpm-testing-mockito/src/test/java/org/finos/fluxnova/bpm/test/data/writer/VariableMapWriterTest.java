package org.finos.fluxnova.bpm.test.data.writer;

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData;
import org.finos.fluxnova.bpm.test.data.factory.VariableFactory;
import org.finos.fluxnova.bpm.engine.variable.VariableMap;
import org.finos.fluxnova.bpm.engine.variable.Variables;
import org.junit.jupiter.api.Test;

import static org.finos.fluxnova.bpm.test.data.Writers.C7.writer;
import static org.assertj.core.api.Assertions.assertThat;

public class VariableMapWriterTest {

  private static final VariableFactory<String> STRING = FluxnovaBpmData.stringVariable("myString");

  private final VariableMap variables = Variables.createVariables();

  @Test
  public void testSet() {
    writer(variables)
      .set(STRING, "value");
    assertThat(variables.get(STRING.getName())).isEqualTo("value");
  }

  @Test
  public void testRemove() {
    STRING.on(variables).set("value");
    writer(variables)
      .remove(STRING);
    assertThat(variables).isEmpty();
  }

}
