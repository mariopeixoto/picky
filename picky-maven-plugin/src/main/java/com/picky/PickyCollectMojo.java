package com.picky;

import com.picky.core.Config;
import com.picky.core.Picky;
import com.picky.core.api.DependencyTracker;
import com.picky.core.asm.PickyVisitorHelper;
import com.picky.core.impl.DependencyTrackerImpl;
import com.picky.core.model.DependencyMap;
import com.picky.util.IOUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.util.*;

@Mojo(
    name = "collect",
    defaultPhase = LifecyclePhase.TEST
)
public class PickyCollectMojo extends PickyAbstractMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        Config.loadConfig(super.basedir);

        Picky picky = super.setupPicky();
        picky.saveNewHashes();

        this.cleanUpExcludesFile(super.getExcludesFile());
    }

    private void cleanUpExcludesFile(File excludesFile) throws MojoExecutionException {
        Collection<String> lines = IOUtil.readLines(excludesFile);
        Collection<String> newLines = new ArrayList<String>();

        for (String line : lines) {
            if ("# Picky exclusions".equals(line)) {
                break;
            }
            newLines.add(line);
        }

        IOUtil.writeLines(excludesFile, newLines, false);
    }

}
