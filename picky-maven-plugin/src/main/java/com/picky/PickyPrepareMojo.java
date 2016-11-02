package com.picky;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(
    name = "wrapup",
    defaultPhase = LifecyclePhase.TEST
)
public class PickyPrepareMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {

    }

}
