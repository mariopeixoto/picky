package com.picky;

import com.picky.core.Picky;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PickyAbstractMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    protected MavenProject project;

    @Parameter(defaultValue = "${basedir}")
    protected File basedir;

    @Parameter(defaultValue = "${plugin}")
    private PluginDescriptor descriptor;

    protected Plugin lookupPlugin(final String pluginName) {
        List<Plugin> plugins = this.project.getBuildPlugins();

        for (Plugin plugin : plugins) {
            if (plugin.getKey().equals(pluginName)) {
                return plugin;
            }
        }

        return null;
    }

    protected Picky setupPicky() throws MojoExecutionException {
        try {
//            loadClasspath(this.project.getCompileClasspathElements());
            Set<URL> urls = loadClasspath(this.project.getTestClasspathElements());
            Reflections reflections = setupReflections(urls);
            return new Picky(reflections);
        } catch (Exception e) {
            throw new MojoExecutionException("Could not setup picky", e);
        }
    }

    private File getPickyDir() {
        return new File(this.basedir.getAbsolutePath() + File.separator + ".picky");
    }

    private Reflections setupReflections(Set<URL> urls) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setUrls(urls);
        configurationBuilder.setScanners(new MethodAnnotationsScanner(), new SubTypesScanner());
        return new Reflections(configurationBuilder);
    }

    private Set<URL> loadClasspath(List<String> classpathElements) throws MojoExecutionException {
        Set<URL> urls = new HashSet<URL>();

        try {
            ClassRealm realm = this.descriptor.getClassRealm();

            for (String element : classpathElements)
            {
                File elementFile = new File(element);
                URL el = elementFile.toURI().toURL();

                realm.addURL(el);
                urls.add(el);
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Could not load test classpath into picky", e);
        }
        return urls;
    }

    protected String getParamValue(Plugin plugin, String param) throws MojoExecutionException {
        Xpp3Dom config = (Xpp3Dom)plugin.getConfiguration();
        if(config != null) {
            Xpp3Dom var = config.getChild(param);
            if (var != null) {
                return var.getValue();
            }
        }
        return null;
    }

    protected File getExcludesFile() throws MojoExecutionException {
        Plugin surefirePlugin = this.lookupPlugin("org.apache.maven.plugins:maven-surefire-plugin");
        getLog().info("Surefire plugin: " + surefirePlugin);
        String excludesFilePath = this.getParamValue(surefirePlugin, "excludesFile");
        getLog().info("Excludes file path: " + excludesFilePath);
        return new File(excludesFilePath);
    }
}
