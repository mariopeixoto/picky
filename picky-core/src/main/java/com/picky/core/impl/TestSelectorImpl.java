package com.picky.core.impl;

import com.picky.core.api.DependencyTracker;
import com.picky.core.api.TestFinder;
import com.picky.core.api.TestSelector;
import com.picky.core.model.DependencyMap;
import org.reflections.Reflections;

import java.util.*;

public class TestSelectorImpl implements TestSelector {

//    private static final Logger logger = LoggerFactory.getLogger(TestSelectorImpl.class);

    private TestFinder testFinder;
    private DependencyTracker dependencyTracker;

    public TestSelectorImpl(Reflections reflections) {
        this.testFinder = new TestFinderImpl(reflections);
        this.dependencyTracker = new DependencyTrackerImpl();
    }

    @Override
    public List<String> findNonAffectedTests() {
        //Load previous hashes
        DependencyMap dependencies = this.dependencyTracker.loadPreviousDependencies();
        //Find all tests
        Set<Class<?>> testClasses = this.testFinder.findTestClasses();

        if (!dependencies.isEmpty()) {
            Set<Class<?>> nonAffectedTests = new HashSet<Class<?>>(testClasses);

//            logger.debug("First Non Affected Tests: {}", nonAffectedTests);

            Set<Class<?>> newTests = this.findNewTests(testClasses, dependencies);
            nonAffectedTests.removeAll(newTests);

//            logger.debug("New tests removed: {}", nonAffectedTests);

            Set<Class<?>> affectedTests = this.findAffectedTests(testClasses, dependencies);
            nonAffectedTests.removeAll(affectedTests);

//            logger.debug("Final Non Affected: {}", nonAffectedTests);

            List<String> nonAffectedResult = new ArrayList<String>();
            for (Class<?> testClass : nonAffectedTests) {
                nonAffectedResult.add(testClass.getName());
            }
            return nonAffectedResult;
        }

        return Collections.emptyList();
    }

    private Set<Class<?>> findNewTests(Set<Class<?>> testClasses, DependencyMap dependencies) {
        //Find new tests (the ones that don't have previous hashes) - select those
        Set<Class<?>> newTests = new HashSet<Class<?>>();

        for (Class<?> testClass : testClasses) {
            if (testClass != null && dependencies.getDependencies(testClass.getName()) == null) {
                newTests.add(testClass);
            }
        }

        return newTests;
    }

    private Set<Class<?>> findAffectedTests(Set<Class<?>> testClasses, DependencyMap dependencies) {
        Set<Class<?>> affectedTests = new HashSet<Class<?>>();

        dependencyTracker.computeNewHashes(dependencies);

        for (Class<?> testClass : testClasses) {
            if (dependencies.isAffected(testClass.getName())) {
                affectedTests.add(testClass);
            }
        }

        return affectedTests;
    }

}
