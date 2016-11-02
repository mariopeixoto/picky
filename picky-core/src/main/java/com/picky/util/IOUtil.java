package com.picky.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class IOUtil {

    public static InputStream getClassFile(Class<?> clazz) {
        String name = clazz.getSimpleName();
        InputStream file = clazz.getResourceAsStream(name + ".class");

        return file;
    }

}
