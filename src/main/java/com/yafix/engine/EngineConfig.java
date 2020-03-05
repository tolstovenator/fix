package com.yafix.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.*;

public class EngineConfig {

    private final static Logger log = LoggerFactory.getLogger(EngineConfig.class);

    private final Properties config;

    private boolean acceptor;
    private int port;
    private String serverHost;
    private final Map<String, SessionConfig> sessions;


    public EngineConfig(InputStream configuration) throws IOException {
        config = new Properties();
        sessions = new HashMap<>();
        config.load(configuration);
        readConfiguration();
    }

    private void readConfiguration() {
        acceptor = config.getProperty("acceptor", "false").equalsIgnoreCase("true");
        if (acceptor) {
            port = Integer.valueOf(config.getProperty("port"));
            serverHost = config.getProperty("server.host", "localhost");
        } else {
            throw new UnsupportedOperationException("Initiator mode is not supported");
        }
        readSessions();
    }

    private void readSessions() {

        for (Object key : config.keySet()) {
            if (((String)(key)).startsWith("session")) {
                String[] tokens = ((String)key).split("\\.");
                if (tokens.length > 1) {
                    String sessionId = tokens[1];
                    if (!sessions.keySet().contains(sessionId)) {
                        SessionConfig session = new SessionConfig();
                        session.compId = config.getProperty("session." + sessionId + ".CompId");
                        session.compSubId = config.getProperty("session." + sessionId + ".CompSubId");
                        session.targetCompId = config.getProperty("session." + sessionId + ".TargetCompId");
                        session.targetSubId = config.getProperty("session." + sessionId + ".TargetSubId");
                        session.ip = config.getProperty("session." + sessionId + ".ip");
                        if (!acceptor) {
                            session.port = Integer.parseInt(config.getProperty("session." + sessionId + ".port"));
                        }
                        sessions.put(sessionId, session);
                        log.info("Configured session [{}]: {}", sessionId, session);
                    }
                }
            }
        }
    }

    public boolean isAcceptor() {
        return acceptor;
    }

    public int getPort() {
        return port;
    }

    public String getServerHost() {
        return serverHost;
    }

    public boolean acceptConnection(InetSocketAddress remoteAddress) {
        if (sessions.isEmpty()) return false;
        for (SessionConfig value : sessions.values()) {
            if (value.ip == null || value.ip.equals(remoteAddress.getAddress().getCanonicalHostName())) {
                return true;
            }
        }
        return false;
    }

    static class SessionConfig {
        String ip;
        int port;
        String compId;
        String compSubId;
        String targetCompId;
        String targetSubId;

        @Override
        public String toString() {
            return "SessionConfig{" +
                    "ip='" + ip + '\'' +
                    ", port=" + port +
                    ", compId='" + compId + '\'' +
                    ", compSubId='" + compSubId + '\'' +
                    ", targetCompId='" + targetCompId + '\'' +
                    ", targetSubId='" + targetSubId + '\'' +
                    '}';
        }
    }
}
