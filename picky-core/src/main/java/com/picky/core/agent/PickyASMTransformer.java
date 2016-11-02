package com.picky.core.agent;

import com.picky.core.asm.PickyClassVisitor;
import com.picky.util.IOUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class PickyASMTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassReader cv = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(cv, ClassWriter.COMPUTE_MAXS);

        PickyClassVisitor v = new PickyClassVisitor();

        cv.accept(v, 0);

        return cw.toByteArray();
    }

}
