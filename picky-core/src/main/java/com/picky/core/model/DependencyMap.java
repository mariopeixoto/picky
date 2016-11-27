package com.picky.core.model;

import java.util.*;

public class DependencyMap {

    private Map<String, Collection<String>> dependencies;
    private Map<String, Pair<String, String>> hashes;

    private Map<String, Boolean> computedClasses;

    public DependencyMap() {
        this.dependencies = new HashMap<String, Collection<String>>();
        this.hashes = new HashMap<String, Pair<String, String>>();

        this.computedClasses = new HashMap<String, Boolean>();
    }

    public boolean isEmpty() {
        return this.dependencies.isEmpty();
    }

    public void putDependency(String clazz, Collection<String> deps) {
        this.dependencies.put(clazz, deps);
    }

    public void putHash(String clazz, String hash) {
        this.hashes.put(clazz, new Pair<String, String>(hash));
    }

    public Collection<String> getDependencies(String clazz) {
        return this.dependencies.get(clazz);
    }

    public Pair<String, String> getHashPair(String clazz) {
        return this.hashes.get(clazz);
    }

    public Collection<String> getCurrentClassWithHashes() {
        return this.hashes.keySet();
    }

    public Set<String> getAllReferencedClasses() {
        Set<String> classes = new HashSet<String>();
        for (Map.Entry<String, Collection<String>> entry : this.dependencies.entrySet()) {
            classes.add(entry.getKey());
            classes.addAll(entry.getValue());
        }

        for (Map.Entry<String, Pair<String, String>> entry : this.hashes.entrySet()) {
            classes.add(entry.getKey());
        }

        return classes;
    }

    public boolean isAffected(String clazz) {
        Boolean affected = this.computedClasses.get(clazz);
        if (affected != null) {
            return affected;
        } else {
            Pair<String, String> hashes = this.getHashPair(clazz);
            if (hashes != null) {//Ignore new classes
                affected = !hashes.getParam1().equals(hashes.getParam2());
                this.computedClasses.put(clazz, affected);

                if (!affected) {
                    Collection<String> deps = this.getDependencies(clazz);
                    if (deps != null) {
                        for (String dep : deps) {
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
