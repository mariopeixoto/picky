package com.picky.core.asm;

import com.picky.core.api.DependencyBookkeeper;
import com.picky.util.ASMUtil;
import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class PickyMethodVisitor implements MethodVisitor {

    private static final Logger logger = LoggerFactory.getLogger(PickyMethodVisitor.class);

    private PickyVisitorHelper helper;

    private PickyAnnotationVisitor annotationVisitor;
    private PickySignatureVisitor signatureVisitor;

    public PickyMethodVisitor(PickyVisitorHelper helper, PickyAnnotationVisitor annotationVisitor, PickySignatureVisitor signatureVisitor) {
        this.helper = helper;
        this.annotationVisitor = annotationVisitor;
        this.signatureVisitor = signatureVisitor;
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        logger.debug("Visit Parameter Annotation: ({}, {}, {})", parameter, desc, visible);

        helper.addDependency(ASMUtil.getType(desc));

        return this.annotationVisitor;
    }

    public void visitTypeInsn(int opcode, String type) {
        logger.debug("Visit Type Insn: ({}, {})", opcode, type);

        helper.addDependency(ASMUtil.getObjectType(type));
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        logger.debug("Visit Field Insn: ({}, {}, {}, {})", opcode, owner, name, desc);

        helper.addDependency(owner);
        helper.addDependency(ASMUtil.getType(desc));
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        logger.debug("Visit Method Insn: ({}, {}, {}, {})", opcode, owner, name, desc);

        helper.addDependency(owner);
        helper.addDependency(ASMUtil.getReturnType(desc));
        helper.addDependencies(ASMUtil.getArgumentTypes(desc));
    }

    public void visitLdcInsn(Object cst) {
        logger.debug("Visit Ldc Insn: ({})", cst);
        if (cst instanceof Type) {
            helper.addDependency(ASMUtil.getType((Type) cst));
        }
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        logger.debug("Visit Multi A New Insn: ({}, {})", desc, dims);

        helper.addDependency(ASMUtil.getType(desc));
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        logger.debug("Visit Method Insn: ({}, {}, {}, {}, {}, {})", name, desc, signature, start, end, index);

        helper.addDependency(ASMUtil.getType(desc));
        helper.visitTypeSignature(signature, this.signatureVisitor);
    }

    public AnnotationVisitor visitAnnotationDefault() {
        logger.debug("Visit Annotation Default");
        return this.annotationVisitor;
    }

    public void visitCode() {
        logger.debug("Visit Code");
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        logger.debug("Visit Frame: ({}, {}, {}, {}, {})", type, nLocal, local, nStack, stack);
    }

    public void visitInsn(int opcode) {
        logger.debug("Visit Insn: ({})", opcode);
    }

    public void visitIntInsn(int opcode, int operand) {
        logger.debug("Visit Int Insn: ({}, {})", opcode, operand);
    }

    public void visitVarInsn(int opcode, int var) {
        logger.debug("Visit Var Insn: ({}, {})", opcode, var);
    }

    public void visitJumpInsn(int opcode, Label label) {
        logger.debug("Visit Jump Insn: ({}, {})", opcode, label);
    }

    public void visitLabel(Label label) {
        logger.debug("Visit Label: ({})", label);
    }

    public void visitIincInsn(int var, int increment) {
        logger.debug("Visit Iinc Insn: ({}, {})", var, increment);
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        logger.debug("Visit Table Switch Insn: ({}, {}, {}, {})", min, max, dflt, labels);
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        logger.debug("Visit Lookup Switch Insn: ({}, {}, {})", dflt, keys, labels);
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        logger.debug("Visit Try Catch Block: ({}, {}, {}, {})", start, end, handler, type);

        helper.addDependency(type);
    }

    public void visitLineNumber(int line, Label start) {
        logger.debug("Visit Line Number: ({}, {})", line, start);
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        logger.debug("Visit Maxs: ({}, {})", maxStack, maxLocals);
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
