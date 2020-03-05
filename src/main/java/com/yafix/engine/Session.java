package com.yafix.engine;

public class Session {

    private SessionStatus status;

    private String compId;
    private String compSubId;
    private String targetCompId;
    private String targetSubId;

    public void init() {

    }

    public boolean isActive() {
        return status != SessionStatus.LOGGED_OUT;
    }
}
