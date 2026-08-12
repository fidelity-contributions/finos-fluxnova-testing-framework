package org.finos.fluxnova.bpm.test.data.reader;

import org.finos.fluxnova.bpm.test.data.Writers;
import org.finos.fluxnova.bpm.test.data.factory.VariableFactory;
import org.finos.fluxnova.bpm.engine.variable.VariableMap;
import org.junit.jupiter.api.Test;

import static org.finos.fluxnova.bpm.test.data.FluxnovaBpmData.stringVariable;
import static org.finos.fluxnova.bpm.test.data.Readers.C7.reader;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class VariableMapReaderTest {

  private static final VariableFactory<String> STRING = stringVariable("myString");

  private final String value = "value";

  private final VariableMap variableMap = Writers.C7.builder().set(STRING, value).build();

  @Test
  public void shouldDelegateGet() {
    assertThat(reader(variableMap).get(STRING)).isEqualTo(value);
  }

  @Test
  public void shouldDelegateGetOptional() {
    assertThat(reader(variableMap).getOptional(STRING)).hasValue(value);
    assertThat(reader(variableMap).getOptional(stringVariable("xxx"))).isEmpty();
  }

  @Test
  public void shouldDelegateGetLocalOptional() {
    assertThatThrownBy(() -> reader(variableMap).getLocalOptional(STRING)).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  public void shouldDelegateGetLocal() {
    assertThatThrownBy(() -> reader(variableMap).getLocalOptional(STRING)).isInstanceOf(UnsupportedOperationException.class);
  }
}
