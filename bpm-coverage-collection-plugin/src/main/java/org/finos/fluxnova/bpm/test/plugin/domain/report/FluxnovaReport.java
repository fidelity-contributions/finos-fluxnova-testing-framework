package org.finos.fluxnova.bpm.test.plugin.domain.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.finos.fluxnova.bpm.test.plugin.domain.model.Model;
import org.finos.fluxnova.bpm.test.plugin.domain.suite.Suite;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FluxnovaReport(
        List<Suite> suites,
        List<Model> models
) { }
