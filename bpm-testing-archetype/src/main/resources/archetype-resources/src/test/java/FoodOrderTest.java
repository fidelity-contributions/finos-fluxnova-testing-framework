package ${groupId};

import org.finos.fluxnova.bpm.test.helpers.DelegateHelpers;
import org.finos.fluxnova.bpm.test.helpers.VariableHelpers;
import org.finos.fluxnova.bpm.test.mockito.FluxnovaMockito;
import org.finos.fluxnova.bpm.test.process.ProcessTestExtension;
import org.finos.fluxnova.bpm.engine.delegate.BpmnError;
import org.finos.fluxnova.bpm.engine.repository.Deployment;
import org.finos.fluxnova.bpm.engine.runtime.ProcessInstance;
import org.finos.fluxnova.spin.impl.json.jackson.JacksonJsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.finos.fluxnova.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FoodOrderTest extends ProcessTestExtension {

    @Autowired
    DelegateMocks delegateMocks;

    @BeforeAll
    public static void before() {
        setup("bpmn/FoodOrder.bpmn", "bpmn/scripts/error-logger.groovy", "bpmn/scripts/order-details.js");
    }

    @AfterAll
    public static void after() {
        teardown();
    }

    @Test
    void happyPathTest_vegan() {
        DelegateHelpers
                .mockDelegate(delegateMocks.exampleOneDelegate())
                .setVariable("websiteUrl", "http://pizza.com")
                .mock();

        Deployment veganMenu = FluxnovaMockito.registerCallActivityMock("VeganMenuProcess")
                .onExecutionAddVariable("menu", "vegan_menu").deploy(repositoryService());
        fluxnova.manageDeployment(veganMenu);

        ProcessInstance pi = runtimeService()
                .createProcessInstanceByKey("PizzaOrderProcessKey")
                .setVariable("firstName", "John")
                .execute();

        JacksonJsonNode orderDetails = (JacksonJsonNode) VariableHelpers.getVariableValue("orderDetails", pi.getProcessInstanceId());
        Assertions.assertNotNull(orderDetails);
        assertEquals("John Doe", orderDetails.prop("name").stringValue());
        assertEquals("123 Main St, Springfield", orderDetails.prop("address").stringValue());
        assertEquals("Chopped", orderDetails.prop("restaurant").stringValue());
        assertEquals("http://pizza.com", orderDetails.prop("website").stringValue());
        assertEquals("vegan_menu", orderDetails.prop("menu").stringValue());

        assertThat(pi).variables().containsEntry("result", "success");
        assertThat(pi).isEnded();
    }

    @Test
    void happyPathTest_nonVegan() {
        DelegateHelpers
                .mockDelegate(delegateMocks.exampleOneDelegate())
                .setVariable("websiteUrl", "http://pizza.com")
                .mock();

        Deployment veganMenu = FluxnovaMockito.registerCallActivityMock("StandardMenuProcess")
                .onExecutionAddVariable("menu", "standard_menu").deploy(repositoryService());
        fluxnova.manageDeployment(veganMenu);

        ProcessInstance pi = runtimeService()
                .createProcessInstanceByKey("PizzaOrderProcessKey")
                .setVariable("firstName", "Sam")
                .setVariable("isWeekend", true)
                .execute();

        JacksonJsonNode orderDetails = (JacksonJsonNode) VariableHelpers.getVariableValue("orderDetails", pi.getProcessInstanceId());
        Assertions.assertNotNull(orderDetails);
        assertEquals("Sam Doe", orderDetails.prop("name").stringValue());
        assertEquals("456 Elm St, Shelbyville", orderDetails.prop("address").stringValue());
        assertEquals("Pizza Hut", orderDetails.prop("restaurant").stringValue());
        assertEquals("http://pizza.com", orderDetails.prop("website").stringValue());
        assertEquals("standard_menu", orderDetails.prop("menu").stringValue());

        assertThat(pi).variables().containsEntry("result", "success");
        assertThat(pi).isEnded();
    }

    @Test
    void initiateOrderFail() {
        DelegateHelpers
                .mockDelegate(delegateMocks.exampleOneDelegate())
                .setVariablesToNull("websiteUrl")
                .throwsException(new BpmnError("500"))
                .mock();

        ProcessInstance pi = runtimeService()
                .createProcessInstanceByKey("PizzaOrderProcessKey")
                .setVariable("firstName", "John")
                .execute();

        assertThat(pi).variables().containsEntry("result", "fail");
    }
}
