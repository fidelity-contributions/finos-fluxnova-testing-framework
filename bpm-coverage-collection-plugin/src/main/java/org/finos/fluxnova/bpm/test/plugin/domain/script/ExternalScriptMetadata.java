package org.finos.fluxnova.bpm.test.plugin.domain.script;

import java.util.List;

public record ExternalScriptMetadata(String fileName, String filePath, List<String> totalLines) {
}
