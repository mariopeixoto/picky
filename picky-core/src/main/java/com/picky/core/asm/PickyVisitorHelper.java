package com.picky.core.asm;

import com.picky.core.api.DependencyBookkeeper;
import com.picky.core.impl.PickyDependencyBookkeeper;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class PickyVisitorHelper {

    private static final Logger logger = LoggerFactory.getLogger(PickyMethodVisitor.class);

    private DependencyBookkeeper dependencyBookkeeper;
    private String currentClass;

    public PickyVisitorHelper() {
        this.dependencyBookkeeper = new PickyDependencyBookkeeper();
    }

    public DependencyBookkeeper getDependencyBookkeeper() {
        return this.dependencyBookkeeper;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public void addDependency(String dependency) {
        if (dependency != null)
            this.dependencyBookkeeper.addDependency(this.currentClass, getFullyQualifiedName(dependency));
    }

    public void addDependencies(Collection<String> dependencies) {
        if (dependencies != null)
            this.dependencyBookkeeper.addDependencies(this.currentClass, getFullyQualifiedNames(dependencies));
    }


    public String getFullyQualifiedName(String clazz) {
        return clazz.replace("/", ".");
    }

    public Collection<String> getFullyQualifiedNames(Collection<String> classes) {
        return classes.stream()
            .filter(Objects::nonNull)
            .map(elm -> getFullyQualifiedName(elm))
            .collect(Collectors.toList());
    }

    public void visitSignature(String signature, SignatureVisitor visitor) {
        if (signature != null) {
            new SignatureReader(signature).accept(visitor);
        }
    }

    public void visitTypeSignature(String signature, SignatureVisitor visitor) {
        if (signature != null) {
            new SignatureReader(signature).acceptType(visitor);
        }
    }
}
