package com.picky.core.impl;

import com.picky.core.api.TestFinder;
import junit.framework.TestCase;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class TestFinderImpl implements TestFinder {

    private Reflections reflections;

    public TestFinderImpl(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public Set<Class<?>> findTestClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();

        Set<Method> testMethods = reflections.getMethodsAnnotatedWith(Test.class);
        for (Method m : testMethods) {
            classes.add(m.getDeclaringClass());
        }

        classes.addAll(reflections.getSubTypesOf(TestCase.class));

        return classes;
    }

}
