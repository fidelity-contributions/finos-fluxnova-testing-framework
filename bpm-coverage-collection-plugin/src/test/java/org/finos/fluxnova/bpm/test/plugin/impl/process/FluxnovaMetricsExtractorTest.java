package org.finos.fluxnova.bpm.test.plugin.impl.process;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.test.plugin.domain.model.Model;
import org.finos.fluxnova.bpm.test.plugin.domain.report.FluxnovaReport;
import org.finos.fluxnova.bpm.test.plugin.domain.report.Metrics;
import org.finos.fluxnova.bpm.test.plugin.domain.suite.Event;
import org.finos.fluxnova.bpm.test.plugin.domain.suite.Run;
import org.finos.fluxnova.bpm.test.plugin.domain.suite.Suite;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxnovaMetricsExtractorTest {

    @Test
    void extract_returnMetricsFromFluxnovaReport() {
        //Given
        FluxnovaReport fluxnovaReport = getFluxnovaReport();
        FluxnovaMetricsExtractor fluxnovaMetricsExtractor = new FluxnovaMetricsExtractor();
        //When
        Metrics metrics = fluxnovaMetricsExtractor.extract("OrderDemo", "com.a.b.OrderDemo", fluxnovaReport);
        //Then
        assertEquals(5, metrics.totalElements());
        List<Event> events = metrics.events();
        assertEquals(3, events.size());
        assertTrue(events.stream().anyMatch(event -> event.modelKey().equals("OrderDemo")));
        assertFalse(events.stream().anyMatch(event -> event.modelKey().equals("AnotherDemo")));
    }

    @Test
    void extract_suiteNotExistsThrowsException() {
        //Given
        FluxnovaReport fluxnovaReport = new FluxnovaReport(new ArrayList<>(), new ArrayList<>());
        FluxnovaMetricsExtractor fluxnovaMetricsExtractor = new FluxnovaMetricsExtractor();
        //When
        Exception exception = assertThrows(TestException.class, () -> {
            fluxnovaMetricsExtractor.extract("OrderDemo", "com.a.b.OrderDemo", fluxnovaReport);
        });
        //Then
        assertEquals("Suite com.a.b.OrderDemo not found for process OrderDemo", exception.getMessage());
    }

    @Test
    void extract_missingModelInfoThrowsException() {
        //Given
        FluxnovaReport fluxnovaReport = getFluxnovaReport();
        fluxnovaReport.models().clear();
        fluxnovaReport.models().add(new Model("AnotherDemo", 7));
        FluxnovaMetricsExtractor fluxnovaMetricsExtractor = new FluxnovaMetricsExtractor();
        //When
        Exception exception = assertThrows(TestException.class, () -> {
            fluxnovaMetricsExtractor.extract("OrderDemo", "com.a.b.OrderDemo", fluxnovaReport);
        });
        //Then
        assertEquals("Fluxnova report for OrderDemo missing total elements count", exception.getMessage());
    }

    private static FluxnovaReport getFluxnovaReport() {
        Event firstEventForFirstTest = new Event("END", "OrderDemo", "Event_1");
        Event secondEventForFirstTest = new Event("START", "OrderDemo", "Event_1");
        Event thirdEventForFirstTest = new Event("TAKE", "OrderDemo", "FLOW_1");
        List<Event> firstEvents = new ArrayList<>();
        firstEvents.add(firstEventForFirstTest);
        firstEvents.add(secondEventForFirstTest);
        firstEvents.add(thirdEventForFirstTest);
        Run firstRun = new Run("firstTest", firstEvents);
        Event firstEventForSecondTest = new Event("END", "AnotherDemo", "Event_1");
        Event secondEventForSecondTest = new Event("START", "AnotherDemo", "Event_1");
        Event thirdEventForSecondTest = new Event("TAKE", "AnotherDemo", "FLOW_1");
        List<Event> secondEvents = new ArrayList<>();
        secondEvents.add(firstEventForSecondTest);
        secondEvents.add(secondEventForSecondTest);
        secondEvents.add(thirdEventForSecondTest);
        Run secondTest = new Run("secondTest", secondEvents);
        List<Run> runs = new ArrayList<>();
        runs.add(firstRun);
        runs.add(secondTest);
        Suite firstSuite = new Suite("com.a.b.OrderDemo", runs);
        Suite secondSuite = new Suite("com.a.b.AnotherDemo", runs);
        List<Suite> suites = new ArrayList<>();
        suites.add(firstSuite);
        suites.add(secondSuite);
        Model firstModel = new Model("OrderDemo", 5);
        Model secondModel = new Model("AnotherDemo", 7);
        List<Model> models = new ArrayList<>();
        models.add(firstModel);
        models.add(secondModel);
        return new FluxnovaReport(suites, models);
    }
}
