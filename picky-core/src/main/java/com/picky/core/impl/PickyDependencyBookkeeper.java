package com.picky.core.impl;

import com.picky.core.Config;
import com.picky.core.api.DependencyBookkeeper;

import java.util.*;

public class PickyDependencyBookkeeper implements DependencyBookkeeper {

    private Map<String, Set<String>> dependencyBook;

    public PickyDependencyBookkeeper() {
        this.dependencyBook = new HashMap<String, Set<String>>();
    }

    @Override
    public void addDependency(String clazz, String dependency) {
        addDependencies(clazz, Arrays.asList(dependency));
    }

    @Override
    public void addDependencies(String clazz, Collection<String> dependencies) {
        this.getSet(clazz).addAll(onlyWhitelisted(clazz, dependencies));
    }

    @Override
    public Collection<String> getDependencies(String clazz) {
        return this.dependencyBook.get(clazz);
    }

    private Set<String> getSet(String clazz) {
        Set<String> dependencies = this.dependencyBook.get(clazz);
        if (dependencies == null) {
            dependencies = new HashSet<String>();
            this.dependencyBook.put(clazz, dependencies);
        }

        return dependencies;
    }

    private Collection<String> onlyWhitelisted(String clazz, Collection<String> dependencies) {
        Collection<String> whitelisted = new ArrayList<String>();
        for (String d : dependencies) {
            if (!d.matches(Config.BLACKLISTED_REGEX) && !d.equals(clazz)) {
                whitelisted.add(d);
            }
        }
        return whitelisted;
    }

}
