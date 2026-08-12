package org.finos.fluxnova.bpm.test.plugin.domain;

import org.finos.fluxnova.bpm.test.domain.coverage.ProcessTestMetadata;
import org.finos.fluxnova.bpm.test.plugin.domain.script.ScriptMetadata;
import lombok.Data;

@Data
public class ModelTestMetadata {
    private String fileName;
    private String processDefinitionKey;
    private ProcessTestMetadata processTestMetadata;
    private ScriptMetadata scriptMetadata;
    private boolean isExternalScript;
    private String error;
}
