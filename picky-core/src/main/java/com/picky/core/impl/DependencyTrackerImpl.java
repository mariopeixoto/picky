package com.picky.core.impl;

import com.picky.core.Config;
import com.picky.core.GeneralException;
import com.picky.core.api.DependencyTracker;
import com.picky.core.api.HashCalculator;
import com.picky.core.model.DependencyMap;
import com.picky.core.model.Pair;
import com.picky.util.IOUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class DependencyTrackerImpl implements DependencyTracker {

    private HashCalculator hashCalculator;

    public DependencyTrackerImpl() {
        this.hashCalculator = new SHA1Calculator();
    }

    @Override
    public DependencyMap loadPreviousDependencies() {
        File pickyDir = new File(Config.pickyDirPath);
        if (!pickyDir.exists()) {
            pickyDir.mkdir();
            return new DependencyMap();
        } else {
            return this.loadDependenciesAndHashes();
        }
    }

    private DependencyMap loadDependenciesAndHashes() {
        DependencyMap dependencyMap = new DependencyMap();

        this.loadDependencies(dependencyMap);
        this.loadHashes(dependencyMap);

        return dependencyMap;
    }

    private void loadDependencies(DependencyMap dependencyMap) {
        try {
            File dependencies = new File(Config.dependenciesFilePath);
            if (dependencies.exists()) {
                Properties props = new Properties();
                props.load(new FileInputStream(dependencies));

                for (String prop : props.stringPropertyNames()) {
                    Collection<String> deps = Arrays.asList(props.getProperty(prop).split(","));
                    Collection<Class<?>> depsClasses = new ArrayList<Class<?>>();
                    for (String dep : deps) {
                        if (StringUtils.isNotBlank(dep)) {
                            depsClasses.add(Class.forName(dep));
                        }
                    }
                    dependencyMap.putDependency(Class.forName(prop), depsClasses);
                }
            } else {
                dependencies.createNewFile();
            }
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    private void loadHashes(DependencyMap dependencyMap) {
        try {
            File hashes = new File(Config.hashesFilePath);
            if (hashes.exists()) {
                Properties props = new Properties();
                props.load(new FileInputStream(hashes));

                for (String prop : props.stringPropertyNames()) {
                    dependencyMap.putHash(Class.forName(prop), props.getProperty(prop));
                }
            } else {
                hashes.createNewFile();
            }
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public void computeNewHashes(DependencyMap dependencies) {
        for (Class<?> clazz : dependencies.getCurrentClassWithHashes()) {
            Pair<String, String> hashes = dependencies.getHashPair(clazz);
            if (hashes.getParam2() == null) {
                String newHash = this.hashCalculator.calculate(IOUtil.getClassFile(clazz));
                hashes.setParam2(newHash);
            }
        }
    }

    @Override
    public void saveHashes(DependencyMap dependencies) {
        Properties props = new Properties();

        for (Class<?> clazz : dependencies.getAllReferencedClasses()) {
            String newHash = this.hashCalculator.calculate(IOUtil.getClassFile(clazz));
            props.put(clazz.getName(), newHash);
        }

        try {
            File hashesFile = new File(Config.hashesFilePath);
            if (!hashesFile.exists()) {
                hashesFile.createNewFile();
            }
            IOUtil.writeProperties(hashesFile, props);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }
}
