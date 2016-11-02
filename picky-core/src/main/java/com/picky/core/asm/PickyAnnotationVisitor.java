package com.picky.core.asm;

import com.picky.core.api.DependencyBookkeeper;
import com.picky.util.ASMUtil;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickyAnnotationVisitor implements AnnotationVisitor {

    private static final Logger logger = LoggerFactory.getLogger(PickyAnnotationVisitor.class);

    private PickyVisitorHelper helper;

    public PickyAnnotationVisitor(PickyVisitorHelper helper) {
        this.helper = helper;
    }

    public void visit(String name, Object value) {
        logger.debug("Visit: ({}, {})", name, value);
        if (value instanceof Type) {
            helper.addDependency(ASMUtil.getType((Type) value));
        }
    }

    public void visitEnum(String name, String desc, String value) {
        logger.debug("Visit Enum: ({}, {}, {})", name, desc, value);

        helper.addDependency(ASMUtil.getType(desc));
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        logger.debug("Visit Annotation: ({}, {})", name, desc);

        helper.addDependency(ASMUtil.getType(desc));

        return this;
    }

    public AnnotationVisitor visitArray(String name) {
        logger.debug("Visit Array: ({})", name);
        return this;
    }

    public void visitEnd() {
        logger.debug("Visit End");
    }
}
