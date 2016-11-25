package com.picky.core;

import java.io.File;

public class Config {

    public static String pickyDirPath;

    public static String BLACKLISTED_REGEX = "(java.*)|(sun.*)|(org.junit.*)|(org.apache.maven.*)|(junit.*)|(.*\\$Proxy.*)";

    public static String dependenciesFilePath;
    public static String hashesFilePath;

    public static void loadConfig(File rootDir) {
        pickyDirPath = rootDir.getAbsolutePath() + File.separator + ".picky";
        dependenciesFilePath = pickyDirPath + File.separator + "computed.dep";
        hashesFilePath = pickyDirPath + File.separator + "computed.hs";
    }


}
