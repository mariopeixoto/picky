package com.picky.core.asm;

import java.io.IOException;

import com.picky.core.api.DependencyBookkeeper;
import com.picky.core.impl.SHA1Calculator;
import com.picky.core.mock.ClassToAnalyze;
import com.picky.util.IOUtil;
import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickyDependencyTracker {

    private static final Logger logger = LoggerFactory.getLogger(PickyDependencyTracker.class);

    public static void main(final String[] args) throws IOException {
        PickyClassVisitor v = new PickyClassVisitor();
        ClassReader cv = new ClassReader(IOUtil.getClassFile(SHA1Calculator.class));

        cv.accept(v, 0);

        v.getDependencies(SHA1Calculator.class.getName()).forEach(logger::info);
    }

}
