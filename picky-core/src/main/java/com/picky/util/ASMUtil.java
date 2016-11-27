package com.picky.util;

import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Collection;

public class ASMUtil {

    public static String getType(String clazz) {
        return getType(Type.getType(clazz));
    }

    public static String getReturnType(String method) {
        return getType(Type.getReturnType(method));
    }

    public static Collection<String> getArgumentTypes(String method) {
        Collection<String> argumentTypes = new ArrayList<String>();
        for (Type t : Type.getArgumentTypes(method)) {
            argumentTypes.add(getType(t));
        }
        return argumentTypes;
    }

    public static String getObjectType(String clazz) {
        return getType(Type.getObjectType(clazz));
    }

    public static Collection<String> getObjectTypes(Collection<String> classes) {
        Collection<String> objectTypes = new ArrayList<String>();
        for(String c : classes) {
            objectTypes.add(getObjectType(c));
        }
        return objectTypes;
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
