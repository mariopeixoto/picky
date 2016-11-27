package com.picky.core;

import java.io.File;
import java.io.IOException;

public class Config {

    public static String pickyDirPath;

    public static String BLACKLISTED_REGEX = "(jdk.*)|(java.*)|(sun.*)|(org.junit.*)|(org.apache.maven.*)|(junit.*)|(.*\\$Proxy.*)";

    public static String dependenciesFilePath;
    public static String hashesFilePath;

    public static void loadConfig(File rootDir) {
        try {
            pickyDirPath = rootDir.getAbsolutePath() + File.separator + ".picky";
            createIfNonExistent(new File(pickyDirPath), true);

            dependenciesFilePath = pickyDirPath + File.separator + "computed.dep";
            createIfNonExistent(new File(dependenciesFilePath), false);

            hashesFilePath = pickyDirPath + File.separator + "computed.hs";
            createIfNonExistent(new File(hashesFilePath), false);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    private static void createIfNonExistent(File file, boolean folder) throws IOException {
        if (!file.exists()) {
            if (folder) {
                file.mkdir();
            } else {
                file.createNewFile();
            }
        }
    }


}
