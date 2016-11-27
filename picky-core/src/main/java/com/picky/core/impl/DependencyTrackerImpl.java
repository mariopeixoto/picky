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
                    Collection<String> depsImmutable = Arrays.asList(props.getProperty(prop).split(","));
                    Collection<String> deps = new ArrayList<String>();
                    for (String dep : depsImmutable ){
                        if (StringUtils.isNotBlank(dep)) {
                            deps.add(dep);
                        }
                    }
                    dependencyMap.putDependency(prop, deps);
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
                    dependencyMap.putHash(prop, props.getProperty(prop));
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
        for (String clazz : dependencies.getCurrentClassWithHashes()) {
            Pair<String, String> hashes = dependencies.getHashPair(clazz);
            if (hashes.getParam2() == null) {
                try {
                    Class<?> classObj = Class.forName(clazz);
                    String newHash = this.hashCalculator.calculate(IOUtil.getClassFile(classObj));
                    hashes.setParam2(newHash);
                } catch (ClassNotFoundException e) {
                    //Class has been deleted, new hash must be null, which already is. So, ignore
                }
            }
        }
    }

    @Override
    public void saveHashes(DependencyMap dependencies) {
        Properties props = new Properties();

        computeHashes(dependencies, props);

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

    private void computeHashes(DependencyMap dependencies, Properties props) {
        Set<String> classes = dependencies.getAllReferencedClasses();
        List<String> tryLater = computeHashes(classes, props);
        if (tryLater.size() > 0) {
            int size = classes.size();
            while (size > tryLater.size()) {
                size = tryLater.size();
                Collections.reverse(tryLater);
                tryLater = computeHashes(tryLater, props);
            }
        }
    }

    private List<String> computeHashes(Collection<String> classes, Properties props) {
        List<String> tryLater = new ArrayList<String>();
        for (String clazz : classes) {
            try {
                Class<?> classObj = Class.forName(clazz);
                String newHash = this.hashCalculator.calculate(IOUtil.getClassFile(classObj));
                props.put(clazz, newHash);
            } catch (ClassNotFoundException e) {
                //Class has been deleted, class must not be included in the hash file. So, ignore
            } catch (ExceptionInInitializerError e) {
                //Save it for later
                tryLater.add(clazz);
            } catch (Throwable e) {
                //Ignore class
            }
        }

        return tryLater;
    }
}
