package com.picky;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(
    name = "select",
    defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES
)
public class PickySelectMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Initializing selection phase");

        getLog().info("Finishing selection phase");
    }

}
