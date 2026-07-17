package org.finos.fluxnova.bpm.test.plugin.impl.process;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.test.plugin.domain.model.Model;
import org.finos.fluxnova.bpm.test.plugin.domain.report.FluxnovaReport;
import org.finos.fluxnova.bpm.test.plugin.domain.suite.Event;
import org.finos.fluxnova.bpm.test.plugin.domain.suite.Run;
import org.finos.fluxnova.bpm.test.plugin.domain.suite.Suite;
import org.finos.fluxnova.bpm.test.plugin.domain.report.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FluxnovaMetricsExtractor {

    public Metrics extract(String processDefinitionKey, String testClass, FluxnovaReport fluxnovaReport) {
        Optional<Suite> suiteOptional = fluxnovaReport.suites().stream().filter(s -> s.id().equals(testClass)).findFirst();
        if (suiteOptional.isPresent()) {
            Suite suite = suiteOptional.get();
            return getMetrics(processDefinitionKey, fluxnovaReport, suite);
        } else {
            throw new TestException(String.format("Suite %s not found for process %s", testClass, processDefinitionKey));
        }
    }

    private static Metrics getMetrics(String processDefinitionKey, FluxnovaReport fluxnovaReport, Suite suite) {
        List<Event> processEvents = getEvents(processDefinitionKey, suite);
        double totalElementsCountForProcess = getTotalElementsCount(processDefinitionKey, fluxnovaReport.models());
        return new Metrics(processEvents, totalElementsCountForProcess);
    }

    private static List<Event> getEvents(String processDefinitionKey, Suite suite) {
        List<Event> processEvents = new ArrayList<>();
        for (Run run : suite.runs()) {
            for (Event event : run.events()) {
                if (event.modelKey().equals(processDefinitionKey)) {
                    processEvents.add(event);
                }
            }
        }
        return processEvents;
    }

    private static double getTotalElementsCount(String processDefinitionKey, List<Model> models) {
        for(Model model : models) {
            if (model.key().equals(processDefinitionKey)) {
                return model.totalElementCount();
            }
        }
        throw new TestException(String.format("Fluxnova report for %s missing total elements count", processDefinitionKey));
    }
}
