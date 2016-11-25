package com.picky.core.asm;

import org.objectweb.asm.signature.SignatureVisitor;

public class PickySignatureVisitor implements SignatureVisitor {

//    private static final Logger logger = LoggerFactory.getLogger(PickySignatureVisitor.class);

    private PickyVisitorHelper helper;
    private String signatureClassName;

    public PickySignatureVisitor(PickyVisitorHelper helper) {
        this.helper = helper;
    }

    @Override
    public void visitFormalTypeParameter(String name) {
//        logger.debug("Visit Formal Type Parameter: ({})", name);
    }

    @Override
    public SignatureVisitor visitClassBound() {
//        logger.debug("Visit Class Bound");
        return this;
    }

    @Override
    public SignatureVisitor visitInterfaceBound() {
//        logger.debug("Visit Interface Bound");
        return this;
    }

    @Override
    public SignatureVisitor visitSuperclass() {
//        logger.debug("Visit Superclass");
        return this;
    }

    @Override
    public SignatureVisitor visitInterface() {
//        logger.debug("Visit Interface");
        return this;
    }

    @Override
    public SignatureVisitor visitParameterType() {
//        logger.debug("Visit Parameter Type");
        return this;
    }

    @Override
    public SignatureVisitor visitReturnType() {
//        logger.debug("Visit Return Type");
        return this;
    }

    @Override
    public SignatureVisitor visitExceptionType() {
//        logger.debug("Visit Exception Type");
        return this;
    }

    @Override
    public void visitBaseType(char descriptor) {
//        logger.debug("Visit Base Type: ({})", descriptor);

    }

    @Override
    public void visitTypeVariable(String name) {
//        logger.debug("Visit Type Variable: ({})", name);
    }

    @Override
    public SignatureVisitor visitArrayType() {
//        logger.debug("Visit Array Type");
        return this;
    }

    @Override
    public void visitClassType(String name) {
//        logger.debug("Visit Class Type: ({})", name);
        signatureClassName = name;
        helper.addDependency(name);
    }

    @Override
    public void visitInnerClassType(String name) {
//        logger.debug("Visit Inner Class Type: ({})", name);
//        signatureClassName = signatureClassName + "$" + name;
//        System.out.println(signatureClassName);
//        helper.addDependency(signatureClassName);
    }

    @Override
    public void visitTypeArgument() {
//        logger.debug("Visit Type Argument");
    }

    @Override
    public SignatureVisitor visitTypeArgument(char wildcard) {
//        logger.debug("Visit Type Argument", wildcard);
        return this;
    }

    @Override
    public void visitEnd() {
//        logger.debug("Visit End");
    }
}
