package com.picky.core.agent;

import com.picky.core.Config;

import java.io.File;
import java.lang.instrument.Instrumentation;

public class PickyAgent {

    public static void premain(String rootDir, Instrumentation instrumentation) {
        Config.loadConfig(new File(rootDir));
        instrumentation.addTransformer(new PickyASMTransformer(), true);
    }

}
