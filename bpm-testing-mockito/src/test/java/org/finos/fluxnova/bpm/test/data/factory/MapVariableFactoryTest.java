package org.finos.fluxnova.bpm.test.data.factory;

import org.finos.fluxnova.bpm.test.data.FluxnovaBpmData;
import org.finos.fluxnova.bpm.test.data.adapter.map.*;
import org.finos.fluxnova.bpm.engine.CaseService;
import org.finos.fluxnova.bpm.engine.RuntimeService;
import org.finos.fluxnova.bpm.engine.TaskService;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.variable.VariableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MapVariableFactoryTest {

  private final MapVariableFactory<String, Integer> variableFactory = new MapVariableFactory<>("string", String.class, Integer.class);

  @Test
  public void shouldHaveNameAndVariableClass() {
    assertThat(variableFactory.getName()).isEqualTo("string");
    assertThat(variableFactory.getKeyClass()).isEqualTo(String.class);
    assertThat(variableFactory.getValueClass()).isEqualTo(Integer.class);
  }

  @Test
  public void shouldHaveCorrectHashCodeAndEquals() {
    VariableFactory<Map<String, Integer>> foo = FluxnovaBpmData.mapVariable("foo", String.class, Integer.class);

    assertThat(variableFactory).isEqualTo(variableFactory);
    assertThat(variableFactory.hashCode()).isEqualTo(variableFactory.hashCode());

    assertThat(variableFactory).isNotEqualTo(foo);
    assertThat(variableFactory.hashCode()).isNotEqualTo(foo.hashCode());
  }

  @Test
  public void shouldReturnAdapterForDelegateExecution() {
    DelegateExecution delegateExecution = mock(DelegateExecution.class);

    assertThat(variableFactory.on(delegateExecution)).isInstanceOf(MapReadWriteAdapterVariableScope.class);
    assertThat(variableFactory.from(delegateExecution)).isInstanceOf(MapReadWriteAdapterVariableScope.class);
  }

  @Test
  public void shouldReturnAdapterForVariableMap() {
    VariableMap variableMap = mock(VariableMap.class);

    assertThat(variableFactory.on(variableMap)).isInstanceOf(MapReadWriteAdapterVariableMap.class);
    assertThat(variableFactory.from(variableMap)).isInstanceOf(MapReadWriteAdapterVariableMap.class);
  }

  @Test
  public void shouldReturnAdapterForRuntimeService() {
    RuntimeService runtimeService = mock(RuntimeService.class);
    String executionId = UUID.randomUUID().toString();

    assertThat(variableFactory.on(runtimeService, executionId)).isInstanceOf(MapReadWriteAdapterRuntimeService.class);
    assertThat(variableFactory.from(runtimeService, executionId)).isInstanceOf(MapReadWriteAdapterRuntimeService.class);
  }

  @Test
  public void shouldReturnAdapterForTaskService() {
    TaskService taskService = mock(TaskService.class);
    String taskId = UUID.randomUUID().toString();

    assertThat(variableFactory.on(taskService, taskId)).isInstanceOf(MapReadWriteAdapterTaskService.class);
    assertThat(variableFactory.from(taskService, taskId)).isInstanceOf(MapReadWriteAdapterTaskService.class);
  }

  @Test
  public void shouldReturnAdapterForCaseService() {
    CaseService caseService = mock(CaseService.class);
    String caseExecutionId = UUID.randomUUID().toString();

    assertThat(variableFactory.on(caseService, caseExecutionId)).isInstanceOf(MapReadWriteAdapterCaseService.class);
    assertThat(variableFactory.from(caseService, caseExecutionId)).isInstanceOf(MapReadWriteAdapterCaseService.class);
  }

}
