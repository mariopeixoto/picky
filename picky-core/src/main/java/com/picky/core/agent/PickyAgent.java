package com.picky.core.agent;

import com.picky.core.Config;
import com.picky.core.GeneralException;
import com.picky.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.lang.instrument.Instrumentation;
import java.util.Properties;

public class PickyAgent {

    public static void premain(String rootDir, Instrumentation instrumentation) {
        Config.loadConfig(new File(rootDir));

        Properties dependencies = loadDependencies();

        instrumentation.addTransformer(new PickyASMTransformer(dependencies), true);
    }

    private static Properties loadDependencies() {
        try {
            File dependencies = new File(Config.dependenciesFilePath);
            Properties props = new Properties();
            props.load(new FileInputStream(dependencies));

            return props;
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

}
