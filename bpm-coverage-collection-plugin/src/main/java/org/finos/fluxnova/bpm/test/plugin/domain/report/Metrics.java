package org.finos.fluxnova.bpm.test.plugin.domain.report;

import org.finos.fluxnova.bpm.test.plugin.domain.suite.Event;

import java.util.List;

public record Metrics(
        List<Event> events,
        double totalElements
) { }
