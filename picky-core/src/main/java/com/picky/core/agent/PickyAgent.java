package com.picky.core.agent;

import java.lang.instrument.Instrumentation;

public class PickyAgent {

    public static void agentmain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new PickyASMTransformer());
    }
}
