package com.yafix.engine;

import java.io.IOException;

public class TestServer {
    public static void main(String[] args) throws IOException {
        Engine engine = new Engine(TestServer.class.getResourceAsStream("/engine.properties"));
        engine.start();
    }
}
