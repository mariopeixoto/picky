package com.picky.core.asm;

import com.picky.core.api.DependencyBookkeeper;
import com.picky.core.impl.PickyDependencyBookkeeper;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.Collection;

public class PickyVisitorHelper {

    private static final PickyVisitorHelper instance = new PickyVisitorHelper();

    private DependencyBookkeeper dependencyBookkeeper;
    private String currentClass;

    private PickyVisitorHelper() {
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
        Collection<String> fullyQualified = new ArrayList<String>();
        for (String c : classes) {
            if (c != null) {
                fullyQualified.add(getFullyQualifiedName(c));
            }
        }
        return fullyQualified;
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

    public static PickyVisitorHelper getInstance() {
        return instance;
    }
}
