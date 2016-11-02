package com.picky.core.api;

import java.util.Collection;

public interface DependencyBookkeeper {

    void addDependency(String clazz, String dependency);
    void addDependencies(String clazz, Collection<String> dependencies);

    Collection<String> getDependencies(String clazz);

}
