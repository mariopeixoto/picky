package com.picky;

import com.picky.core.Config;
import com.picky.core.Picky;
import com.picky.core.agent.PickyAgent;
import com.picky.util.IOUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Mojo(
    name = "select",
    defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES
)
public class PickySelectMojo extends PickyAbstractMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Initializing selection phase");
        Config.loadConfig(super.basedir);

        Picky picky = super.setupPicky();
        List<String> nonAffectedTestClasses = picky.findNonAffectedTests();

        getLog().info("Found " + nonAffectedTestClasses.size() + " non affected test classes");

        this.appendExclusions(nonAffectedTestClasses);

        this.loadAgent();

        getLog().info("Finishing selection phase");
    }

    private void appendExclusions(List<String> exclusions) throws MojoExecutionException {
        if (!exclusions.isEmpty()) {
            File excludesFile = super.getExcludesFile();
            IOUtil.writeLines(excludesFile, prependPickyMarker(exclusions), true);
        }
    }

    private List<String> prependPickyMarker(List<String> exclusions) {
        List<String> finalExclusions = new ArrayList<String>();
        finalExclusions.add("# Picky exclusions");
        finalExclusions.addAll(exclusions);
        return finalExclusions;
    }

    private void loadAgent() {
        Properties projProps = super.project.getProperties();
        String agentJarPath = PickyAgent.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " ");

        String argLine = projProps.getProperty("argLine");
        argLine = "-javaagent:'" + agentJarPath + "'='" + super.basedir.getAbsolutePath() + "' " + (argLine == null?"":argLine);
        projProps.setProperty("argLine", argLine);
    }

}
