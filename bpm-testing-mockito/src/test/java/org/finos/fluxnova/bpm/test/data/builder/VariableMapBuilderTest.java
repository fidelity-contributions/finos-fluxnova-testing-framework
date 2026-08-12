package org.finos.fluxnova.bpm.test.data.builder;

import org.finos.fluxnova.bpm.test.data.factory.VariableFactory;
import org.finos.fluxnova.bpm.engine.variable.VariableMap;
import org.junit.jupiter.api.Test;

import static org.finos.fluxnova.bpm.test.data.FluxnovaBpmData.stringVariable;
import static org.finos.fluxnova.bpm.test.data.Writers.C7.builder;
import static org.assertj.core.api.Assertions.assertThat;

public class VariableMapBuilderTest {

  private static final VariableFactory<String> FOO = stringVariable("foo");

  @Test
  public void builderCanCreateEmptyVariableMap() {
    assertThat(builder().build()).isEmpty();
  }

  @Test
  public void builderCanWriteVariable() {
    VariableMap variables = builder().set(FOO, "bar").build();

    assertThat(FOO.from(variables).get()).isEqualTo("bar");
  }

  @Test
  public void buildCreatesANewInstanceEveryTime() {

    VariableMapBuilder builder = builder()
      .set(FOO, "bar");

    // build copy with "bar"
    VariableMap bar = builder.build();
    assertThat(bar).containsEntry("foo", "bar");

    // modify builder, build copy with "baz"
    VariableMap baz = builder.set(FOO, "baz").build();
    assertThat(baz).containsEntry("foo", "baz");

    // "bar" is not changed
    assertThat(bar).containsEntry("foo", "bar");

    // modify "baz"
    FOO.on(baz).set("xxx");

    // build again
    assertThat(builder.build()).containsEntry("foo", "baz");
  }
}
