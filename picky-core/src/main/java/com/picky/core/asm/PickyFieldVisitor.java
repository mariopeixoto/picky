package com.picky.core.asm;

import com.picky.core.api.DependencyBookkeeper;
import com.picky.util.ASMUtil;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickyFieldVisitor implements FieldVisitor {

    private static final Logger logger = LoggerFactory.getLogger(PickyFieldVisitor.class);

    private PickyVisitorHelper helper;

    private PickyAnnotationVisitor annotationVisitor;

    public PickyFieldVisitor(PickyVisitorHelper helper, PickyAnnotationVisitor annotationVisitor) {
        this.helper = helper;
        this.annotationVisitor = annotationVisitor;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        logger.debug("Visit Annotation: ({}, {})", desc, visible);

        helper.addDependency(ASMUtil.getType(desc));

        return this.annotationVisitor;
    }

    public void visitAttribute(Attribute attr) {
        logger.debug("Visit Attribute: ({})", attr);
    }

    public void visitEnd() {
        logger.debug("Visit End");
    }
}
