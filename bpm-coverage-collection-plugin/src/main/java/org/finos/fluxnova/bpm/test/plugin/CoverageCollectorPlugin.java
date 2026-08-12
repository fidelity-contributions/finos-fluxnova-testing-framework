package org.finos.fluxnova.bpm.test.plugin;

import org.finos.fluxnova.bpm.test.plugin.impl.CoverageCollector;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;

import static org.finos.fluxnova.bpm.test.coverage.CoverageProperties.getSkipCoverage;

@Mojo(name = "collect-coverage", defaultPhase = LifecyclePhase.VERIFY )
public class CoverageCollectorPlugin extends AbstractMojo {

    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true, readonly = true )
    protected File buildDirectory;

    @Parameter(defaultValue = "${project.basedir}", property = "srcDir", required = true )
    protected File srcDirectory;

    @Parameter( defaultValue = "${session}", readonly = true, required = true)
    protected MavenSession mavenSession;

    @Inject
    private CoverageCollector coverageCollector;

    private static final Logger logger = LoggerFactory.getLogger(CoverageCollectorPlugin.class);

    public void execute() {
        boolean skipCoverage = getSkipCoverage();
        if (skipCoverage) {
            logger.info("Skipping coverage collection");
        } else {
            logger.info("Coverage collection started...");
            coverageCollector.collect(buildDirectory.toPath(), srcDirectory.toPath(), mavenSession);
            logger.info("Coverage collection finished");
        }
    }
}
