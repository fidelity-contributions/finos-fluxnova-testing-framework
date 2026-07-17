package org.finos.fluxnova.bpm.test.domain.process_variables;

import java.util.HashMap;
import java.util.List;

public record ProcessVariables(
        HashMap<String, List<ProcessVariable>> processVariables) {

        public ProcessVariables() {
            this(new HashMap<>());
        }
}