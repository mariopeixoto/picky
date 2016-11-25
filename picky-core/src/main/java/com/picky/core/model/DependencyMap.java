package com.picky.core.model;

import java.util.*;

public class DependencyMap {

    private Map<Class<?>, Collection<Class<?>>> dependencies;
    private Map<Class<?>, Pair<String, String>> hashes;

    private Map<Class<?>, Boolean> computedClasses;

    public DependencyMap() {
        this.dependencies = new HashMap<Class<?>, Collection<Class<?>>>();
        this.hashes = new HashMap<Class<?>, Pair<String, String>>();

        this.computedClasses = new HashMap<Class<?>, Boolean>();
    }

    public boolean isEmpty() {
        return this.dependencies.isEmpty();
    }

    public void putDependency(Class<?> clazz, Collection<Class<?>> deps) {
        this.dependencies.put(clazz, deps);
    }

    public void putHash(Class<?> clazz, String hash) {
        this.hashes.put(clazz, new Pair<String, String>(hash));
    }

    public Collection<Class<?>> getDependencies(Class<?> clazz) {
        return this.dependencies.get(clazz);
    }

    public Pair<String, String> getHashPair(Class<?> clazz) {
        return this.hashes.get(clazz);
    }

    public Collection<Class<?>> getCurrentClassWithHashes() {
        return this.hashes.keySet();
    }

    public Set<Class<?>> getAllReferencedClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (Map.Entry<Class<?>, Collection<Class<?>>> entry : this.dependencies.entrySet()) {
            classes.add(entry.getKey());
            classes.addAll(entry.getValue());
        }

        for (Map.Entry<Class<?>, Pair<String, String>> entry : this.hashes.entrySet()) {
            classes.add(entry.getKey());
        }

        return classes;
    }

    public boolean isAffected(Class<?> clazz) {
        Boolean affected = this.computedClasses.get(clazz);
        if (affected != null) {
            return affected;
        } else {
            Pair<String, String> hashes = this.hashes.get(clazz);
            if (hashes != null) {//Ignore new classes
                affected = !hashes.getParam1().equals(hashes.getParam2());
                this.computedClasses.put(clazz, affected);

                if (!affected) {
                    Collection<Class<?>> deps = this.getDependencies(clazz);
                    if (deps != null) {
                        for (Class<?> dep : deps) {
                            boolean depAffected = this.isAffected(dep);
                            if (depAffected) {
                                affected = true;
                                break;
                            }
                        }
                    }
                }
            } else {
                affected = true;
            }
            this.computedClasses.put(clazz, affected);
            return affected;
        }
    }

}
