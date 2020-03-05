package com.yafix.engine;

import java.io.*;

public class Engine {

    private NioLoop nioLoop;
    private final EngineConfig engineConfig;

    public Engine(InputStream configuration) throws IOException {
        engineConfig = new EngineConfig(configuration);

    }



    public void start() {
        if (engineConfig.isAcceptor()) {
            nioLoop = new NioLoop(engineConfig);
            nioLoop.start();
        } else {

        }
    }

    public Session createSession() {
        return null;
    }
}
