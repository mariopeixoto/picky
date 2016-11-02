package com.picky.util;

import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ASMUtil {

    private static final Logger logger = LoggerFactory.getLogger(ASMUtil.class);

    public static String getType(String clazz) {
        return getType(Type.getType(clazz));
    }

    public static String getReturnType(String method) {
        return getType(Type.getReturnType(method));
    }

    public static Collection<String> getArgumentTypes(String method) {
        return Arrays.asList(Type.getArgumentTypes(method)).stream()
            .map(t -> getType(t))
            .collect(Collectors.toList());
    }

    public static String getObjectType(String clazz) {
        return getType(Type.getObjectType(clazz));
    }

    public static Collection<String> getObjectTypes(Collection<String> classes) {
        return classes.stream()
            .map(c -> getObjectType(c))
            .collect(Collectors.toList());
    }

    public static String getType(Type t) {
        switch(t.getSort()) {
            case Type.ARRAY:
                return getType(t.getElementType());
            case Type.OBJECT:
                return t.getInternalName();
            default:
                return null;
        }
    }
}
