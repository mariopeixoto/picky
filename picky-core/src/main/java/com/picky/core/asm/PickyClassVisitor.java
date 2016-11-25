package com.picky.core.asm;

import com.picky.util.ASMUtil;
import org.objectweb.asm.*;

import java.util.Arrays;
import java.util.Collection;

public class PickyClassVisitor implements ClassVisitor {

//    private static final Logger logger = LoggerFactory.getLogger(PickyClassVisitor.class);
    
    private PickyVisitorHelper helper;

    private PickyAnnotationVisitor annotationVisitor;
    private PickyFieldVisitor fieldVisitor;
    private PickyMethodVisitor methodVisitor;
    private PickySignatureVisitor signatureVisitor;

    public PickyClassVisitor(PickyVisitorHelper helper) {
        this.helper = helper;
        this.annotationVisitor = new PickyAnnotationVisitor(this.helper);
        this.signatureVisitor = new PickySignatureVisitor(this.helper);
        this.fieldVisitor = new PickyFieldVisitor(this.helper, this.annotationVisitor);
        this.methodVisitor = new PickyMethodVisitor(this.helper, this.annotationVisitor, this.signatureVisitor);
    }

    public PickyClassVisitor() {
        this(PickyVisitorHelper.getInstance());
    }

    public void visit(int version, int access, String name, String signature, String  superName, String[] interfaces) {
//        logger.debug("Visit: ({}, {}, {}, {}, {}, {})", version, access, name, signature, superName, interfaces);

        this.helper.setCurrentClass(this.helper.getFullyQualifiedName(name));

        if (signature == null) {
            this.helper.addDependency(superName);
            this.helper.addDependencies(Arrays.asList(interfaces));
        } else {
            this.helper.visitSignature(signature, this.signatureVisitor);
        }
    }

    public void visitSource(String source, String debug) {
//        logger.debug("Visit Source: ({}, {})", source, debug);
    }

    public void visitOuterClass(String owner, String name, String desc) {
//        logger.debug("Visit Outer Class: ({}, {}, {})", owner, name, desc);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//        logger.debug("Visit Annotation: ({}, {})", desc, visible);

        this.helper.addDependency(ASMUtil.getType(desc));

        return this.annotationVisitor;
    }

    public void visitAttribute(Attribute attr) {
//        logger.debug("Visit Attribute: ({})", attr);
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
//        logger.debug("Visit Inner Class: ({}, {}, {}, {})", name, outerName, innerName, access);
//        System.out.println("Visit Inner Class: (" + name + ", " + outerName + ", " + innerName + ", " + access + ")");
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
//        logger.debug("Visit Field: ({}, {}, {}, {}, {})", access, name, desc, signature, value);

//        System.out.println("Visit Field: (" + access + ", " + name + ", " + desc + ", " + signature + ", " + value  + ")");

        if (signature == null) this.helper.addDependency(ASMUtil.getType(desc));
        else this.helper.visitTypeSignature(signature, this.signatureVisitor);

        return this.fieldVisitor;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//        logger.debug("Visit Method: ({}, {}, {}, {}, {})", access, name, desc, signature, exceptions);

        if (signature == null) {
            this.helper.addDependency(ASMUtil.getReturnType(desc));
            this.helper.addDependencies(ASMUtil.getArgumentTypes(desc));
        } else {
            this.helper.visitSignature(signature, this.signatureVisitor);
        }

        if (exceptions != null) {
            this.helper.addDependencies(ASMUtil.getObjectTypes(Arrays.asList(exceptions)));
        }

        return this.methodVisitor;
    }

    public void visitEnd() {
//        logger.debug("Visit End");
    }

    public Collection<String> getDependencies(String clazz) {
        return helper.getDependencyBookkeeper().getDependencies(clazz);
    }

}
