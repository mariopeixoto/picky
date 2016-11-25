package com.picky.core.agent;

import com.picky.core.Config;
import com.picky.core.GeneralException;
import com.picky.core.api.HashCalculator;
import com.picky.core.asm.PickyClassVisitor;
import com.picky.core.asm.PickyVisitorHelper;
import com.picky.core.impl.SHA1Calculator;
import com.picky.util.IOUtil;
import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.Properties;

public class PickyASMTransformer implements ClassFileTransformer {

    private PickyVisitorHelper helper;
    private Properties dependencies;

    public PickyASMTransformer() {
        this.helper = PickyVisitorHelper.getInstance();
        this.dependencies = new Properties();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.matches(Config.BLACKLISTED_REGEX)) {
            ClassReader cv = new ClassReader(classfileBuffer);
            PickyClassVisitor v = new PickyClassVisitor();

            cv.accept(v, 0);

            this.saveDependencies(className, v);
        }

        return classfileBuffer;
    }

    private void saveDependencies(String className, PickyClassVisitor v) {
        try {
            String fullyQualifiedName = this.helper.getFullyQualifiedName(className);
            Collection<String> deps = v.getDependencies(fullyQualifiedName);

            this.dependencies.put(fullyQualifiedName, StringUtils.join(deps, ","));

            File depFile = new File(Config.dependenciesFilePath);
            if (!depFile.exists()) { depFile.createNewFile(); }
            IOUtil.writeProperties(depFile, this.dependencies);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }
}
